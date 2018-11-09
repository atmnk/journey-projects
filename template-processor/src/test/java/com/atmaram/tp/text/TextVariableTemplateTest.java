package com.atmaram.tp.text;

import com.atmaram.tp.Variable;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TextVariableTemplateTest {
    //Get Variables
    @Test
    public void should_get_variables_from_expression(){
        TextVariableTemplate variableTemplate=new TextVariableTemplate("_this");
        List<Variable> vars=variableTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }

    //Fill
    @Test
    public void should_fill_plain_expression(){
        TextVariableTemplate variableTemplate=new TextVariableTemplate("_format(_now,'dd-mm')");
        TextTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled).isInstanceOf(FilledTextTemplate.class);
    }
    @Test
    public void should_return_same_template_if_cant_fill_plain_variable(){
        TextVariableTemplate variableTemplate=new TextVariableTemplate("Hello");
        TextTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }
    @Test
    public void should_return_same_template_if_cant_fill_variable_inside_function(){
        TextVariableTemplate variableTemplate=new TextVariableTemplate("_mid(Hello,1,1)");
        TextTemplate filled=variableTemplate.fill(new HashMap<>());
        assertThat(filled.toStringTemplate()).isEqualTo(variableTemplate.toStringTemplate());
    }


//    toStringTemplate
    @Test
    public void should_return_string_template_from_varible(){
        TextVariableTemplate variableTemplate=new TextVariableTemplate("Hello");
        assertThat(variableTemplate.toStringTemplate()).isEqualTo("${Hello}");
    }
}
