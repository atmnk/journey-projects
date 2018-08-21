package com.atmaram.tp.common;

import java.util.HashMap;

@FunctionalInterface
public interface Executable {
    public Object execute(String expression, HashMap<String,Object> data);
}
