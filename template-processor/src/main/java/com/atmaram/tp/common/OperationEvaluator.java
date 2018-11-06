package com.atmaram.tp.common;

import java.util.HashMap;
import java.util.List;

@FunctionalInterface
public interface OperationEvaluator {
    public Object toValue(List<Object> args);
}
