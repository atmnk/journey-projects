package com.atmaram.tp;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableTest {
    //merge
    @Test
    public void should_return_source_if_name_differs(){
        Variable variable1=new Variable();
        variable1.setName("Test1");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("String");
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test1");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner1));
    }
    @Test
    public void should_return_source_if_type_differs(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("String");
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("String");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner1));
    }

    @Test
    public void should_merge_Lists_without_inner_lists(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("String");
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner1,inner2));
    }
    @Test
    public void should_merge_Lists_without_inner_lists_with_target_empty_list(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("String");
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner1));
    }
    @Test
    public void should_merge_Lists_without_inner_lists_with_source_empty_list_and_target_with_string(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner2));
    }
    @Test
    public void should_merge_Lists_without_inner_lists_with_source_empty_list_and_target_with_List(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("List");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner2));
    }
    @Test
    public void should_merge_Lists_with_source_with_inner_list(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("List");
        Variable inner_inner1=new Variable();
        inner_inner1.setName("Var1");
        inner_inner1.setType("String");
        inner1.setInner_variables(Arrays.asList(inner_inner1));
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("String");
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner2,inner1));
    }
    @Test
    public void should_merge_Lists_with_target_with_inner_list(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var1");
        inner1.setType("List");
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var2");
        inner2.setType("List");
        Variable inner2_inner1=new Variable();
        inner2_inner1.setName("Var1");
        inner2_inner1.setType("String");
        inner2.setInner_variables(Arrays.asList(inner2_inner1));
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(inner1,inner2));
    }
    @Test
    public void should_merge_Lists_with_both_source_and_target_with_inner_list_same_variable_name_taget_without_members(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var");
        inner1.setType("List");
        Variable inner1_inner1=new Variable();
        inner1_inner1.setName("Var1");
        inner1_inner1.setType("String");
        inner1.setInner_variables(Arrays.asList(inner1_inner1));
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        Variable assertVar=new Variable();
        assertVar.setName("Var");
        assertVar.setType("List");
        assertVar.setInner_variables(Arrays.asList(inner1_inner1));
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(assertVar));
    }
    @Test
    public void should_merge_Lists_with_both_source_and_target_with_inner_list_same_variable_name(){
        Variable variable1=new Variable();
        variable1.setName("Test");
        variable1.setType("List");
        Variable inner1=new Variable();
        inner1.setName("Var");
        inner1.setType("List");
        Variable inner1_inner1=new Variable();
        inner1_inner1.setName("Var1");
        inner1_inner1.setType("String");
        inner1.setInner_variables(Arrays.asList(inner1_inner1));
        variable1.setInner_variables(Arrays.asList(inner1));
        Variable variable2=new Variable();
        variable2.setName("Test");
        variable2.setType("List");
        Variable inner2=new Variable();
        inner2.setName("Var");
        inner2.setType("List");
        Variable inner2_inner1=new Variable();
        inner2_inner1.setName("Var2");
        inner2_inner1.setType("String");
        inner2.setInner_variables(Arrays.asList(inner2_inner1));
        variable2.setInner_variables(Arrays.asList(inner2));
        Variable merged=variable1.mergeWith(variable2);

        assertThat(merged.getName()).isEqualTo("Test");
        assertThat(merged.getType()).isEqualTo("List");
        Variable assertVar=new Variable();
        assertVar.setName("Var");
        assertVar.setType("List");
        assertVar.setInner_variables(Arrays.asList(inner1_inner1,inner2_inner1));
        assertThat(merged.getInner_variables()).isEqualTo(Arrays.asList(assertVar));
    }
}
