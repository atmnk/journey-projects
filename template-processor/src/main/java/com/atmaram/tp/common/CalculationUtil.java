package com.atmaram.tp.common;

import java.math.BigDecimal;

public interface CalculationUtil {
    public static Object add(Object one,Object two){
        Class returnType=returnType(one,two);
        return add(returnType,one,two);
    }
    public static Object substract(Object one,Object two){
        Class returnType=returnType(one,two);
        return substract(returnType,one,two);
    }
    public static Object multiply(Object one,Object two){
        Class returnType=returnType(one,two);
        return multiply(returnType,one,two);
    }
    public static Object devide(Object one,Object two){
        Class returnType=returnType(one,two);
        return devide(returnType,one,two);
    }
    public static Object add(Class returnType,Object one,Object two){
        Object convertedOne=getBest(one);
        Object convertedTwo=getBest(two);
        if(convertedOne instanceof String || convertedTwo instanceof String){
            return convertedOne.toString()+convertedTwo.toString();
        } else {
            return perform(returnType,Operation.ADD,(Number)convertedOne,(Number)convertedTwo);
        }
    }
    public static Object substract(Class returnType,Object one,Object two){
        Object convertedOne=getBest(one);
        Object convertedTwo=getBest(two);
        if(convertedOne instanceof String || convertedTwo instanceof String){
            return convertedOne.toString()+convertedTwo.toString();
        } else {
            return perform(returnType,Operation.Substract,(Number)convertedOne,(Number)convertedTwo);
        }
    }
    public static Object multiply(Class returnType,Object one,Object two){
        Object convertedOne=getBest(one);
        Object convertedTwo=getBest(two);
        if(convertedOne instanceof String || convertedTwo instanceof String){
            return convertedOne.toString()+convertedTwo.toString();
        } else {
            return perform(returnType,Operation.Multiply,(Number)convertedOne,(Number)convertedTwo);
        }
    }
    public static Object devide(Class returnType,Object one,Object two){
        Object convertedOne=getBest(one);
        Object convertedTwo=getBest(two);
        if(convertedOne instanceof String || convertedTwo instanceof String){
            return convertedOne.toString()+convertedTwo.toString();
        } else {
            return perform(returnType,Operation.Devide,(Number)convertedOne,(Number)convertedTwo);
        }
    }
    public static Object calculate(Operation operation,Object one,Object two){
        switch (operation){
            case ADD:
                return add(one,two);
            case Substract:
                return substract(one,two);
            case Multiply:
                return multiply(one,two);
            case Devide:
                return devide(one,two);
        }
        return add(one,two);
    }
    public static enum Operation{
        ADD,
        Substract,
        Multiply,
        Devide
    }
    public static Class returnType(Object one,Object two){
        Object convertedOne=getBest(one);
        Object convertedTwo=getBest(two);
        if (convertedOne instanceof String || convertedTwo instanceof String){
            return String.class;
        } else {
            return getHighest(convertedOne,convertedTwo);
        }
    }
    public static Object getBest(Object value){
        if(value instanceof String){
            try{
                return tryNum((String)value);
            } catch (Exception ex){
                return value;
            }
        } else if(value instanceof Number){
            return value;
        }
        return value;
    }
    public static Number tryNum(String value){
        try {
            return Integer.parseInt(value);
        } catch (Exception ex){
            try {
                return Long.parseLong(value);
            }catch (Exception ex1){
                try {
                    return Float.parseFloat(value);
                } catch (Exception ex2){
                    try {
                        return Double.parseDouble(value);
                    }
                    catch (Exception ex3){
                        return new BigDecimal(value);
                    }
                }
            }
        }
    }
    public static Class getHighest(Object one,Object two){
        if(one instanceof BigDecimal || two instanceof BigDecimal){
            return BigDecimal.class;
        }
        if(one instanceof Double || two instanceof Double){
            return Double.class;
        }
        if(one instanceof Float || two instanceof Float){
            return Float.class;
        }
        if(one instanceof Long || two instanceof Long){
            return Long.class;
        }
        if(one instanceof Integer || two instanceof Integer){
            return Integer.class;
        }
        return BigDecimal.class;
    }
    public static Object perform(Class returnType,Operation operation,Number one,Number two){
        if(returnType.equals(Integer.class)){
            return operate(operation,one.intValue(),two.intValue());
        }
        if(returnType.equals(Long.class)){
            return operate(operation,one.longValue(),two.longValue());
        }
        if(returnType.equals(Float.class)){
            return operate(operation,one.floatValue(),two.floatValue());
        }
        if(returnType.equals(Double.class)){
            return operate(operation,one.doubleValue(),two.doubleValue());
        }
        if(returnType.equals(BigDecimal.class)){
            return operate(operation,new BigDecimal(one.toString()),new BigDecimal(two.toString()));
        }
        return operate(operation,one.toString(),two.toString());
    }
    public static String  operate(Operation operation,String one,String two){
        return one+two;
    }
    public static Integer operate(Operation operation,Integer one,Integer two){
        switch (operation){
            case ADD:
                return one+two;
            case Substract:
                return one-two;
            case Multiply:
                return one*two;
            case Devide:
                return one/two;
        }
        return one+two;
    }
    public static Long operate(Operation operation,Long one,Long two){
        switch (operation){
            case ADD:
                return one+two;
            case Substract:
                return one-two;
            case Multiply:
                return one*two;
            case Devide:
                return one/two;
        }
        return one+two;
    }
    public static Float operate(Operation operation,Float one,Float two){
        switch (operation){
            case ADD:
                return one+two;
            case Substract:
                return one-two;
            case Multiply:
                return one*two;
            case Devide:
                return one/two;
        }
        return one+two;
    }
    public static Double operate(Operation operation,Double one,Double two){
        switch (operation){
            case ADD:
                return one+two;
            case Substract:
                return one-two;
            case Multiply:
                return one*two;
            case Devide:
                return one/two;
        }
        return one+two;
    }
    public static BigDecimal operate(Operation operation,BigDecimal one,BigDecimal two){
        switch (operation){
            case ADD:
                return one.add(two);
            case Substract:
                return one.subtract(two);
            case Multiply:
                return one.multiply(two);
            case Devide:
                return one.divide(two);
        }
        return one.add(two);
    }
}
