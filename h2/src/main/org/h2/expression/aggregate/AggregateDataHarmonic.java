/**
 * Class to compute Harmonic Mean
 * CSCI- 621
 * 
 * @author Aditya Shankaran (as4081)
 * 
 */

package org.h2.expression.aggregate;

import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.value.Value;
import org.h2.value.ValueDouble;
import org.h2.value.ValueNull;

/**
 * Compute harmonic mean
 */
public class AggregateDataHarmonic extends AggregateData{
    private double sumReciprocals = 0.0;
    private int count = 0;

    @Override
    public void add(SessionLocal session, Value v) {
        if (v == ValueNull.INSTANCE) {
            return;
        }
        count++;
        double value = v.getDouble();
        if (value == 0) {
            throw DbException.getInvalidValueException("HARMONIC_MEAN input", "0");
        }

        sumReciprocals += 1 / value;
    }

    @Override
    public Value getValue(SessionLocal session) {
        if (count == 0 || sumReciprocals == 0) {
            return ValueNull.INSTANCE;
        }
        return ValueDouble.get(count / sumReciprocals);
    }
}
