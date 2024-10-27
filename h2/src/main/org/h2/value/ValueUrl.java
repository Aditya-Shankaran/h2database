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

/**
 * Implementation of the URL data type.
 */
public final class ValueUrl extends ValueStringBase {

    public static final ValueUrl EMPTY = new ValueUrl("");

    private ValueUrl(String value) {
        super(value);
    }

    @Override
    public int getValueType() {
        return URL;
    }

    @Override
    public int compareTypeSafe(Value v, CompareMode mode, CastDataProvider provider) {
        return mode.compareString(value, ((ValueStringBase) v).value, true);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValueUrl
                && value.equals(((ValueUrl) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public StringBuilder getSQL(StringBuilder builder, int sqlFlags) {
        return StringUtils.quoteStringSQL(builder, value);
    }

    /**
     * Get or create a URL value for the given string.
     *
     * @param s the string
     * @return the value
     */
    public static Value get(String s) {
        return get(s, null);
    }

    /**
     * Get or create a URL value for the given string.
     *
     * @param s 
            the url string
     * @return 
            the url value
     */
    public static Value get(String s, CastDataProvider provider) {
        if (s.isEmpty()) {
            return provider != null && provider.getMode().treatEmptyStringsAsNull ? ValueNull.INSTANCE : EMPTY;
        }

        ValueUrl obj = new ValueUrl(StringUtils.cache(s));
        if (s.length() > SysProperties.OBJECT_CACHE_MAX_PER_ELEMENT_SIZE) {
            return obj;
        }
        return Value.cache(obj);
    }
}