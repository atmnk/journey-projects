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
                {12, "34", CalculationUtil.Operation.ADD,Integer.class,46},
                {12, "34.2", CalculationUtil.Operation.ADD,Float.class,46.2f},
                {12, 34, CalculationUtil.Operation.ADD,Integer.class,46},
                {12, 34l, CalculationUtil.Operation.ADD,Long.class,46l},
                {12, 12.2F, CalculationUtil.Operation.ADD,Float.class,24.2F},
                {12, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
                {12l, 12, CalculationUtil.Operation.ADD,Long.class,24l},
                {12l,12l, CalculationUtil.Operation.ADD,Long.class,24l},
                {12l, 12.2f, CalculationUtil.Operation.ADD,Float.class,24.2f},
                {12l, 12.2d, CalculationUtil.Operation.ADD,Double.class,24.2d},
        });
    }
}
