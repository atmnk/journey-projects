package com.atmaram.jp.exceptions;

public class UnitConfigurationException extends Exception {
    String unitName;
    public UnitConfigurationException(String message, String unitName,Throwable cause) {
        super("Unit " + unitName+ " is not properly configured "+message, cause);
        this.unitName=unitName;
    }
}
