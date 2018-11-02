package com.atmaram.tp.common;

import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Date;
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
    @Test
    public void should_get_pattern_value(){
        Object value=ExpressionProcessor.process("_eval('####')",new HashMap<>());
//        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_first_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_first(data,4)",data);
        assertThat(value).isEqualTo("ABCD");
    }
    @Test
    public void should_get_mid_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_mid(data,4,4)",data);
        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_last_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_last(data,4)",data);
        assertThat(value).isEqualTo("!@#$");
    }
    @Test
    public void should_get_lenth_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_len(data)",data);
        assertThat(value).isEqualTo(Integer.valueOf(12));
    }
    @Test
    public void should_do_plus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_plus(3,4)",data);
        assertThat(value).isEqualTo(Integer.valueOf(7));
    }
    @Test
    public void should_do_minus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_minus(4,3)",data);
        assertThat(value).isEqualTo(Integer.valueOf(1));
    }
    @Test
    public void should_do_composite(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","CDE-F67690200320021");
        Object value=ExpressionProcessor.process("_first(data,_len(data)-4))",data);
        assertThat(value).isEqualTo("CDE-F6769020032");
    }
    @Test
    public void should_do_now(){
        Object value=ExpressionProcessor.process("_now",new HashMap<>());
        assertThat(value).isInstanceOf(Date.class);
    }
    @Test
    public void should_format_date(){
        Date date=new Date();
        Object value=ExpressionProcessor.process("_format(_now,'dd-MM')",new HashMap<>());
        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo(String.format("%02d",date.getDate())+"-"+String.format("%02d",date.getMonth()+1));
    }
    @Test
    public void should_add_day_to_date(){
        Date date=new Date();
        Object value=ExpressionProcessor.process("_format(_add_days(_now,1),'dd-MM')",new HashMap<>());
        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo(String.format("%02d",date.getDate()+1)+"-"+String.format("%02d",date.getMonth()+1));
    }
}
