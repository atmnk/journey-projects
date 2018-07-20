package com.atmaram.tp.common;

import org.junit.Test;

import java.util.HashMap;
import static org.assertj.core.api.Assertions.*;

public class VariableValueProcessorTest {
    @Test
    public void should_get_pattern_value(){
        Object value=VariableValueProcessor.getValue("_eval(####)",new HashMap<>());
//        assertThat(value).isEqualTo("1234");
    }
}
