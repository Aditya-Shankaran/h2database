/*
 * Copyright 2004-2024 H2 Group. 
 * Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (https://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import org.h2.api.ErrorCode;
import org.h2.engine.CastDataProvider;
import org.h2.engine.SysProperties;
import org.h2.message.DbException;
import org.h2.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Implementation of the URL data type.
 */
public final class ValueUrl extends ValueStringBase {

    public static final ValueUrl EMPTY = new ValueUrl("");

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:\\d+)?(/.*)?$", 
        Pattern.CASE_INSENSITIVE
    );

    private ValueUrl(String value) {
        super(value);
    }

    @Override
    public StringBuilder getSQL(StringBuilder builder, int sqlFlags) {
        return StringUtils.quoteStringSQL(builder, value);
    }

    @Override
    public int getValueType() {
        return URL;
    }

    /**
     * Get or create an URL value for the given string.
     *
     * @param s 
            the url string
     * @return 
            the url value
     */
    public static Value get(String s) {
        return get(s, null);
    }

    /**
     * Get or create a URL value for the given string with a CastDataProvider.
     *
     * @param s        
            the url string
     * @param provider 
            the cast information provider, or {@code null}
     * @return 
            the URL value
     */
    public static Value get(String s, CastDataProvider provider) {
        if (s.isEmpty()) {
            return provider != null && provider.getMode().treatEmptyStringsAsNull ? ValueNull.INSTANCE : EMPTY;
        }
        
        if (!URL_PATTERN.matcher(s).matches()) {
            throw DbException.get(ErrorCode.INVALID_VALUE_2, "Value", s);
        }

        // Everything in url should be lowercase
        String normalized = s.toLowerCase();
        ValueUrl obj = new ValueUrl(StringUtils.cache(normalized));
        if (s.length() > SysProperties.OBJECT_CACHE_MAX_PER_ELEMENT_SIZE) {
            return obj;
        }
        return Value.cache(obj);
    }

    // @Override
    // public String getString() {
    //     return value;
    // }

    // @Override
    // public Object getObject() {
    //     return value;
    // }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ValueUrl)) {
            return false;
        }
        ValueUrl o = (ValueUrl) other;
        return value.equals(o.value);
    }

}