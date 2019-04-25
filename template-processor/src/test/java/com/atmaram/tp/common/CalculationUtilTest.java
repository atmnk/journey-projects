package com.atmaram.tp.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(Parameterized.class)
public class CalculationUtilTest {
    Object one;
    Object two;
    CalculationUtil.Operation operation;
    Class returnType;
    Object value;

    public CalculationUtilTest(Object one, Object two, CalculationUtil.Operation operation, Class returnType, Object value) {
        this.one = one;
        this.two = two;
        this.operation = operation;
        this.returnType = returnType;
        this.value = value;
    }

    @Test
    public void testMyCalulations(){
        Object ret=CalculationUtil.calculate(operation,one,two);
        assertThat(ret.getClass()).isEqualTo(returnType);
        assertThat(ret).isEqualTo(value);
    }
    @Parameterized.Parameters(name = "{index}: Test with X={0}, Y={1}, type: {2},result: {3}")
    public static Iterable<Object[]> data(){
        return Arrays.asList(new Object[][] {
                {"12", "34", CalculationUtil.Operation.ADD,Integer.class,46},
                {"12", "92233720368547758", CalculationUtil.Operation.ADD,Long.class,92233720368547770l},
                {"12", "34.2", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {"12", 34, CalculationUtil.Operation.ADD,Integer.class,46},
                {"12", 34l, CalculationUtil.Operation.ADD,Long.class,46l},
                {"12", 12.2F, CalculationUtil.Operation.ADD,Float.class,24.2F},
                {"12", 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {"12", "ABC", CalculationUtil.Operation.ADD,String.class,"12ABC"},
                {"12.0", "34", CalculationUtil.Operation.ADD,Float.class,46f},
                {"12.0", "92233720368547758", CalculationUtil.Operation.ADD,Float.class,92233720368547770f},
                {"12.0", "34.2", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {"12.0", 34, CalculationUtil.Operation.ADD,Float.class,46f},
                {"12.0", 34l, CalculationUtil.Operation.ADD,Float.class,46f},
                {"12.0", 12.2F, CalculationUtil.Operation.ADD,Float.class,24.2F},
                {"12.0", 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {"12.0", "ABC", CalculationUtil.Operation.ADD,String.class,"12.0ABC"},
                {12, "34", CalculationUtil.Operation.ADD,Integer.class,46},
                {12, "92233720368547758", CalculationUtil.Operation.ADD,Long.class,92233720368547770l},
                {12, "34.2", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {12, 34, CalculationUtil.Operation.ADD,Integer.class,46},
                {12, 34l, CalculationUtil.Operation.ADD,Long.class,46l},
                {12, 12.2F, CalculationUtil.Operation.ADD,Float.class,24.2F},
                {12, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {12, "ABC", CalculationUtil.Operation.ADD,String.class,"12ABC"},
                {12l, "34", CalculationUtil.Operation.ADD,Long.class,46l},
                {12l, "92233720368547758", CalculationUtil.Operation.ADD,Long.class,92233720368547770l},
                {12l, "34.2", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {12l, 12, CalculationUtil.Operation.ADD,Long.class,24l},
                {12l,12l, CalculationUtil.Operation.ADD,Long.class,24l},
                {12l, 12.2f, CalculationUtil.Operation.ADD,Float.class,24.2f},
                {12l, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {12l, "ABC", CalculationUtil.Operation.ADD,String.class,"12ABC"},
                {12.2f, "34", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {12.2f, "92233720368547758", CalculationUtil.Operation.ADD,Float.class,92233720368547770.2f},
                {12.2f, "34.2", CalculationUtil.Operation.ADD,Float.class,46.4f},
                {12.2f, 12, CalculationUtil.Operation.ADD,Float.class,24.2f},
                {12.2f,12l, CalculationUtil.Operation.ADD,Float.class,24.2f},
                {12.2f, 12.2f, CalculationUtil.Operation.ADD,Float.class,24.4f},
                {12.2f, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.399999809265136d},
                {12f, "ABC", CalculationUtil.Operation.ADD,String.class,"12.0ABC"},
                {12.2d, "34", CalculationUtil.Operation.ADD,Double.class,46.2d},
                {12.2d, "92233720368547758", CalculationUtil.Operation.ADD,Double.class,92233720368547770.2d},
                {12.2d, "34.2", CalculationUtil.Operation.ADD,Double.class,46.400000762939456d},
                {12.2d, 12, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {12.2d,12l, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {12.2d, 12.2f, CalculationUtil.Operation.ADD,Double.class,24.399999809265136d},
                {12.2d, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.4d},
                {12.2d, "ABC", CalculationUtil.Operation.ADD,String.class,"12.2ABC"},
                {"ABC", "34", CalculationUtil.Operation.ADD,String.class,"ABC34"},
                {"ABC", "92233720368547758", CalculationUtil.Operation.ADD,String.class,"ABC92233720368547758"},
                {"ABC", "34.2", CalculationUtil.Operation.ADD,String.class,"ABC34.2"},
                {"ABC", 12, CalculationUtil.Operation.ADD,String.class,"ABC12"},
                {"ABC",12l, CalculationUtil.Operation.ADD,String.class,"ABC12"},
                {"ABC", 12.2f, CalculationUtil.Operation.ADD,String.class,"ABC12.2"},
                {"ABC", 12.2d, CalculationUtil.Operation.ADD,String.class,"ABC12.2"},
                {"ABC", "ABC", CalculationUtil.Operation.ADD,String.class,"ABCABC"},
        });
    }
}
