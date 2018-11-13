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
    @Test
    public void should_combine_resolved_variables_in_list(){
        VariableStore variableStore=new VariableStore();

        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        Variable resolveVariable1=new Variable();
        resolveVariable1.setName("test");
        resolveVariable1.setType("List");
        resolveVariable1.setInner_variables(Arrays.asList(inner1));

        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("String");
        Variable resolveVariable2=new Variable();
        resolveVariable2.setName("test");
        resolveVariable2.setType("List");
        resolveVariable2.setInner_variables(Arrays.asList(inner2));

        variableStore.resolve(Arrays.asList(resolveVariable1));
        variableStore.resolve(Arrays.asList(resolveVariable2));

        Variable result=variableStore.getResolved("test","List");
        assertThat(result.getInner_variables().size()).isEqualTo(2);

    }
    @Test
    public void should_not_add_resolved_variable_again(){
        VariableStore variableStore=new VariableStore();

        Variable variable=new Variable();
        variable.setName("variable");
        variable.setType("String");


        variableStore.resolve(Arrays.asList(variable));
        variableStore.resolve(Arrays.asList(variable));
        List<Variable> vars=variableStore.getResolvedVariables();
        assertThat(vars.size()).isEqualTo(1);

    }
    @Test
    public void should_not_add_already_added_variable_again(){
        VariableStore variableStore=new VariableStore();

        Variable variable=new Variable();
        variable.setName("variable");
        variable.setType("String");


        variableStore.add(Arrays.asList(variable));
        variableStore.add(Arrays.asList(variable));
        List<Variable> vars=variableStore.getVariables();
        assertThat(vars.size()).isEqualTo(1);

    }
    @Test
    public void should_merge_into_already_resolved_variable(){
        VariableStore variableStore=new VariableStore();

        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        Variable variable1=new Variable();
        variable1.setName("test");
        variable1.setType("List");
        variable1.setInner_variables(Arrays.asList(inner1));

        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("List");
        Variable variable2=new Variable();
        variable2.setName("test");
        variable2.setType("List");
        variable2.setInner_variables(Arrays.asList(inner1,inner2));

        variableStore.resolve(Arrays.asList(variable1));
        variableStore.resolve(Arrays.asList(variable2));

        List<Variable> vars=variableStore.getResolved("test","List").getInner_variables();
        assertThat(vars).isEqualTo(Arrays.asList(inner1,inner2));

    }
    @Test
    public void should_add_new_inner_variables_to_already_added_variable(){
        VariableStore variableStore=new VariableStore();

        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        Variable variable1=new Variable();
        variable1.setName("test");
        variable1.setType("List");
        variable1.setInner_variables(Arrays.asList(inner1));

        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("String");
        Variable variable2=new Variable();
        variable2.setName("test");
        variable2.setType("List");
        variable2.setInner_variables(Arrays.asList(inner2));

        variableStore.add(Arrays.asList(variable1));
        variableStore.add(Arrays.asList(variable2));

        List<Variable> vars=variableStore.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable variable=variableStore.getUnResolved("test","List");
        assertThat(variable.getInner_variables().size()).isEqualTo(2);

    }
    @Test
    public void should_add_new_inner_variables_to_already_added_and_resolved_variable(){
        VariableStore variableStore=new VariableStore();

        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        Variable variable1=new Variable();
        variable1.setName("test");
        variable1.setType("List");
        variable1.setInner_variables(Arrays.asList(inner1));

        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("String");
        Variable variable2=new Variable();
        variable2.setName("test");
        variable2.setType("List");
        variable2.setInner_variables(Arrays.asList(inner2));

        variableStore.add(Arrays.asList(variable1));
        variableStore.resolve(Arrays.asList(variable1));
        variableStore.add(Arrays.asList(variable2));

        List<Variable> vars=variableStore.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable variable=variableStore.getUnResolved("test","List");
        assertThat(variable.getInner_variables().size()).isEqualTo(2);

    }
    @Test
    public void should_add_new_inner_variables_to_already_resolved_and_added_variable(){
        VariableStore variableStore=new VariableStore();

        Variable inner1=new Variable();
        inner1.setName("inner1");
        inner1.setType("String");
        Variable variable1=new Variable();
        variable1.setName("test");
        variable1.setType("List");
        variable1.setInner_variables(Arrays.asList(inner1));

        Variable inner2=new Variable();
        inner2.setName("inner2");
        inner2.setType("String");
        Variable variable2=new Variable();
        variable2.setName("test");
        variable2.setType("List");
        variable2.setInner_variables(Arrays.asList(inner2));

        variableStore.resolve(Arrays.asList(variable1));
        variableStore.add(Arrays.asList(variable1));
        variableStore.add(Arrays.asList(variable2));

        List<Variable> vars=variableStore.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable variable=variableStore.getUnResolved("test","List");
        assertThat(variable.getInner_variables().size()).isEqualTo(1);

    }
    @Test
    public void should_return_null_if_no_resolved_variable(){
        VariableStore variableStore=new VariableStore();
        assertThat(variableStore.getResolved("test","String")).isNull();

    }
    @Test
    public void should_return_null_if_no_unresolved_variable(){
        VariableStore variableStore=new VariableStore();
        assertThat(variableStore.getUnResolved("test","String")).isNull();

    }
}
