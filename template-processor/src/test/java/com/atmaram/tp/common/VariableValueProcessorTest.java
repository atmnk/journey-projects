package com.atmaram.tp.common;

import org.junit.Test;

import java.util.HashMap;
import static org.assertj.core.api.Assertions.*;

public class VariableValueProcessorTest {
    @Test
    public void should_get_pattern_value(){
        Object value=VariableValueProcessor.getValue("_eval('####')",new HashMap<>());
//        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_first_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_first(data,4)",data);
        assertThat(value).isEqualTo("ABCD");
    }
    @Test
    public void should_get_mid_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_mid(data,4,4)",data);
        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_last_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_last(data,4)",data);
        assertThat(value).isEqualTo("!@#$");
    }
    @Test
    public void should_get_lenth_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_len(data)",data);
        assertThat(value).isEqualTo(Integer.valueOf(12));
    }
    @Test
    public void should_do_plus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_plus(3,4)",data);
        assertThat(value).isEqualTo(Integer.valueOf(7));
    }
    @Test
    public void should_do_minus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=VariableValueProcessor.getValue("_minus(4,3)",data);
        assertThat(value).isEqualTo(Integer.valueOf(1));
    }
    @Test
    public void should_do_composite(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("bbid","BTY-X67690200320021");
        Object value=VariableValueProcessor.getValue("_first(bbid,_len(bbid)-4))",data);
        assertThat(value).isEqualTo("BTY-X6769020032");
    }

}
