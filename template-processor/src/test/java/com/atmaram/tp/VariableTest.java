package com.atmaram.tp;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableTest {
    @Test
    public void should_merge_List_with_other_list(){
        Variable variable1=new Variable();
        variable1.setName("test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        variable1.setInner_variables(Arrays.asList(inner1));

        Variable variable2=new Variable();
        variable2.setName("test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));

        Variable newVar=variable1.mergeWith(variable2);
        assertThat(newVar.getInner_variables().size()).isEqualTo(2);
    }
}
