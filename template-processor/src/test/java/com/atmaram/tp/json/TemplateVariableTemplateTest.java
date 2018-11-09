package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateVariableTemplateTest {
    //Get Variables
    @Test
    public void should_get_variables_from_expression(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("_this");
        List<Variable> vars=variableTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(0);
    }
    //Get Template Variables
    @Test
    public void should_get_template_variables_as_empty_list(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("_this");
        List<Variable> vars=variableTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
    }

    //Fill Variables Variables
    @Test
    public void should_fill_template_variables_and_return_same_template(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("_this");
        JSONTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled).isEqualTo(variableTemplate);
    }
    //Fill Template Variables
    @Test
    public void should_fill_plain_expression(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("_format(_now,'dd-mm')");
        JSONTemplate filled=variableTemplate.fillTemplateVariables(new HashMap<>());
        assertThat(filled).isInstanceOf(FilledVariableTemplate.class);
    }
    @Test
    public void should_return_same_template_if_cant_fill_plain_variable(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("Hello");
        JSONTemplate filled=variableTemplate.fillTemplateVariables(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }
    @Test
    public void should_return_same_template_if_cant_fill_variable_inside_function(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("_mid(Hello,1,1)");
        JSONTemplate filled=variableTemplate.fillTemplateVariables(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }
    //Extract

    @Test
    public void should_return_empty_hashmap_when_extract(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("Hello");
        HashMap data=variableTemplate.extract("Name");
        assertThat(data.values().size()).isEqualTo(0);
    }

    //toXMLCompatibleObject
    @Test
    public void should_return_string_template_from_varible(){
        TemplateVariableTemplate variableTemplate=new TemplateVariableTemplate("Hello");
        assertThat(variableTemplate.toJSONCompatibleObject()).isEqualTo("#{Hello}");
    }
}
