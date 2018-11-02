package com.atmaram.jp;

import com.atmaram.tp.Variable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableStoreTest {
    @Test
    public void should_add_simple_string_variable(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("String");
        variableStore.add(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isInstanceOf(Variable.class);
        assertThat(result.get(0).getName()).isEqualTo("test");
        assertThat(result.get(0).getType()).isEqualTo("String");

    }
    @Test
    public void should_add_simple_List_variable(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("List");
        Variable inner=new Variable();
        inner.setName("inner");
        inner.setType("String");
        variable.setInner_variables(Arrays.asList(inner));
        variableStore.add(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isInstanceOf(Variable.class);
        assertThat(result.get(0).getName()).isEqualTo("test");
        assertThat(result.get(0).getType()).isEqualTo("List");
        assertThat(result.get(0).getInner_variables().size()).isEqualTo(1);
        assertThat(result.get(0).getInner_variables().get(0).getName()).isEqualTo("inner");
        assertThat(result.get(0).getInner_variables().get(0).getType()).isEqualTo("String");

    }
    @Test
    public void should_resolve_simple_string_variable_first_added_then_resolved(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("String");
        variableStore.add(Arrays.asList(variable));
        variableStore.resolve(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isInstanceOf(Variable.class);
        assertThat(result.get(0).getName()).isEqualTo("test");
        assertThat(result.get(0).getType()).isEqualTo("String");

    }
    @Test
    public void should_resolve_simple_string_variable_first_resolved_then_added(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("String");
        variableStore.resolve(Arrays.asList(variable));
        variableStore.add(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(0);

    }
    @Test
    public void should_resolve_simple_List_variable_first_added_then_resolved(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("List");
        Variable inner=new Variable();
        inner.setName("inner");
        inner.setType("String");
        variable.setInner_variables(Arrays.asList(inner));
        variableStore.add(Arrays.asList(variable));
        variableStore.resolve(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isInstanceOf(Variable.class);
        assertThat(result.get(0).getName()).isEqualTo("test");
        assertThat(result.get(0).getType()).isEqualTo("List");
        assertThat(result.get(0).getInner_variables().size()).isEqualTo(1);
        assertThat(result.get(0).getInner_variables().get(0).getName()).isEqualTo("inner");
        assertThat(result.get(0).getInner_variables().get(0).getType()).isEqualTo("String");

    }
    @Test
    public void should_resolve_simple_List_variable_first_resolved_then_added(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("List");
        Variable inner=new Variable();
        inner.setName("inner");
        inner.setType("String");
        variable.setInner_variables(Arrays.asList(inner));
        variableStore.resolve(Arrays.asList(variable));
        variableStore.add(Arrays.asList(variable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(0);

    }
    @Test
    public void should_stay_simple_List_variable_resolved_without_inner_variables(){
        VariableStore variableStore=new VariableStore();
        Variable variable=new Variable();
        variable.setName("test");
        variable.setType("List");
        Variable inner=new Variable();
        inner.setName("inner");
        inner.setType("String");
        Variable resolveVariable=new Variable();
        resolveVariable.setName("test");
        resolveVariable.setType("List");
        variable.setInner_variables(Arrays.asList(inner));
        variableStore.add(Arrays.asList(variable));
        variableStore.resolve(Arrays.asList(resolveVariable));
        List<Variable> result=variableStore.getVariables();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isInstanceOf(Variable.class);
        assertThat(result.get(0).getName()).isEqualTo("test");
        assertThat(result.get(0).getType()).isEqualTo("List");
        assertThat(result.get(0).getInner_variables().size()).isEqualTo(1);
        assertThat(result.get(0).getInner_variables().get(0).getName()).isEqualTo("inner");
        assertThat(result.get(0).getInner_variables().get(0).getType()).isEqualTo("String");

    }
}
