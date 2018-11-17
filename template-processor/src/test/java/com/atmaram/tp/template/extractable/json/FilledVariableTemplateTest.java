package com.atmaram.tp.template.extractable.json;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class FilledVariableTemplateTest {
    //Get Variables
    @Test
    public void should_return_empty_list_for_get_variables(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.getVariables()).isEqualTo(Arrays.asList());
    }
    //Get Template Variables
    @Test
    public void should_return_empty_list_for_get_template_variables(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.getTemplateVariables()).isEqualTo(Arrays.asList());
    }

    //Fill Template Variables
    @Test
    public void should_return_same_template_for_fill_template(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.fillTemplateVariables(new HashMap<>())).isEqualTo(filledVariableTemplate);
    }
    //Fill
    @Test
    public void should_return_same_template_for_fill(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.fill(new HashMap<>())).isEqualTo(filledVariableTemplate);
    }

    //Extract
    @Test
    public void should_return_empty_hashmap_for_extract(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.extract(1)).isEqualTo(new HashMap<>());
    }

    //toStringTemplate
    @Test
    public void should_convert_to_string(){
        FilledVariableTemplate filledVariableTemplate=new FilledVariableTemplate(1);
        assertThat(filledVariableTemplate.toStringTemplate()).isEqualTo("1");
    }
}
