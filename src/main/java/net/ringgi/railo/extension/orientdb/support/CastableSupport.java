package net.ringgi.railo.extension.orientdb.support;

import railo.runtime.exp.PageException;
import railo.runtime.op.Castable;
import railo.runtime.type.dt.DateTime;


public class CastableSupport extends ObjectSupport implements Castable {

    @Override
    public String castToString() throws PageException {
        throw exp.createExpressionException("Can't cast Complex Object to a String");
    }

    @Override
    public String castToString(String defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean castToBooleanValue() throws PageException {
        throw exp.createExpressionException("can't cast Complex Object Type to a boolean value");
    }

    @Override
    public Boolean castToBoolean(Boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public double castToDoubleValue() throws PageException {
        throw exp.createExpressionException("can't cast Complex Object Type to a number value");
    }

    @Override
    public double castToDoubleValue(double defaultValue) {
        return defaultValue;
    }

    @Override
    public DateTime castToDateTime() throws PageException {
        throw exp.createExpressionException("can't cast Complex Object Type to a Date");
    }

    @Override
    public DateTime castToDateTime(DateTime defaultValue) {
        return defaultValue;
    }

    @Override
    public int compareTo(String s) throws PageException {
        throw exp.createExpressionException("can't compare Complex Object Type Struct with a String");
    }

    @Override
    public int compareTo(boolean b) throws PageException {
        throw exp.createExpressionException("can't compare Complex Object Type with a boolean value");
    }

    @Override
    public int compareTo(double v) throws PageException {
        throw exp.createExpressionException("can't compare Complex Object Type with a numeric value");
    }

    @Override
    public int compareTo(DateTime dateTime) throws PageException {
        throw exp.createExpressionException("can't compare Complex Object Type with a DateTime Object");
    }
}
