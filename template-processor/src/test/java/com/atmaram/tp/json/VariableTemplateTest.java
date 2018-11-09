package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableTemplateTest {
    //Get Variables
    @Test
    public void should_get_variables_from_expression(){
        VariableTemplate variableTemplate=new VariableTemplate("_this");
        List<Variable> vars=variableTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    //Get Template Variables
    @Test
    public void should_get_template_variables_as_empty_list(){
        VariableTemplate variableTemplate=new VariableTemplate("_this");
        List<Variable> vars=variableTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(0);
    }

    //Fill Template Variables
    @Test
    public void should_fill_template_variables_and_return_same_template(){
        VariableTemplate variableTemplate=new VariableTemplate("_this");
        JSONTemplate filled=variableTemplate.fillTemplateVariables(new HashMap<>());
        assertThat(filled).isEqualTo(variableTemplate);
    }
    //Fill
    @Test
    public void should_fill_plain_expression(){
        VariableTemplate variableTemplate=new VariableTemplate("_format(_now,'dd-mm')");
        JSONTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled).isInstanceOf(FilledVariableTemplate.class);
    }
    @Test
    public void should_return_same_template_if_cant_fill_plain_variable(){
        VariableTemplate variableTemplate=new VariableTemplate("Hello");
        JSONTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }
    @Test
    public void should_return_same_template_if_cant_fill_variable_inside_function(){
        VariableTemplate variableTemplate=new VariableTemplate("_mid(Hello,1,1)");
        JSONTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }
    //Extract

    @Test
    public void should_return_empty_hashmap_if_not_plain_variable(){
        VariableTemplate variableTemplate=new VariableTemplate("_mid(Hello,1,1)");
        HashMap data=variableTemplate.extract("Name");
        assertThat(data.values().size()).isEqualTo(0);
    }

    @Test
    public void should_return_one_variable_hashmap_if_plain_variable(){
        VariableTemplate variableTemplate=new VariableTemplate("Hello");
        HashMap data=variableTemplate.extract("Name");
        assertThat(data.containsKey("Hello")).isTrue();
        assertThat(data.get("Hello")).isEqualTo("Name");
    }

    //toXMLCompatibleObject
    @Test
    public void should_return_string_template_from_varible(){
        VariableTemplate variableTemplate=new VariableTemplate("Hello");
        assertThat(variableTemplate.toJSONCompatibleObject()).isEqualTo("${Hello}");
    }
}
