package com.atmaram.tp.common;

import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionProcessorTest {
    @Test
    public void should_return_string(){
        String op=(String)ExpressionProcessor.process("'AAAA'",new HashMap<>());
        assertThat(op).isEqualTo("AAAA");
    }
    @Test
    public void should_return_integer(){
        Integer op=(Integer)ExpressionProcessor.process("32",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(32));
    }
    @Test
    public void should_add_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 + 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(5));
    }
    @Test
    public void should_substract_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 - 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(1));
    }
    @Test
    public void should_add_multiple_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3+2+3",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(8));
    }
    @Test
    public void should_multiply_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 * 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(6));
    }
    @Test
    public void should_divide_integer(){
        Integer op=(Integer)ExpressionProcessor.process("6 / 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(3));
    }
    @Test
    public void should_get_lenth_of_string(){
        Integer op=(Integer)ExpressionProcessor.process("_len('AAAA')",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(4));
    }
    @Test
    public void should_get_timestamp(){
        Long op=(Long)ExpressionProcessor.process("_timestamp",new HashMap<>());
        assertThat(op).isInstanceOf(Long.class);
    }
    @Test
    public void should_eval_expression(){
        String op=(String)ExpressionProcessor.process("_eval('#')",new HashMap<>());
        assertThat(op).isInstanceOf(String.class);
        assertThat(op.length()).isEqualTo(1);
        assertThat(Character.isDigit(op.charAt(0))).isTrue();

    }
    @Test
    public void should_get_last_from_string(){
        String op=(String)ExpressionProcessor.process("_last('ABC',2)",new HashMap<>());
        assertThat(op).isEqualTo("BC");

    }
    @Test
    public void should_get_first_from_string(){
        String op=(String)ExpressionProcessor.process("_first('ABC',2)",new HashMap<>());
        assertThat(op).isEqualTo("AB");

    }
    @Test
    public void should_get_mid_from_string(){
        String op=(String)ExpressionProcessor.process("_mid('ABCDEF',2,2)",new HashMap<>());
        assertThat(op).isEqualTo("CD");

    }
    @Test
    public void should_process_stack(){
        String op=(String)ExpressionProcessor.process("_mid('ABCDEF',_len('ABCDEF')-4,2)",new HashMap<>());
        assertThat(op).isEqualTo("CD");

    }
    @Test
    public void should_process_concat() {
        String op = (String) ExpressionProcessor.process("_concat(3,_timestamp,_eval('1###'))", new HashMap<>());
        assertThat(op.length()).isEqualTo(Integer.valueOf(18));
    }
}
