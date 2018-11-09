package com.atmaram.tp.text;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TextLoopTemplateTest {
    //Get Variables
    @Test
    public void should_return_single_variable_as_list_with_inner_variables() throws TemplateParseException {
        TextLoopTemplate loopTemplate=new TextLoopTemplate("test", TextTemplate.parse("${Name}"));
        List<Variable> vars=loopTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("test");
        assertThat(var.getType()).isEqualTo("List");
        Variable inner=new Variable();
        inner.setName("Name");
        inner.setType("String");
        assertThat(var.getInner_variables()).isEqualTo(Arrays.asList(inner));

    }
    @Test
    public void should_return_single_variable_as_list_with_inner_this_variables() throws TemplateParseException {
        TextLoopTemplate loopTemplate=new TextLoopTemplate("test",TextTemplate.parse("${_this}"));
        List<Variable> vars=loopTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("test");
        assertThat(var.getType()).isEqualTo("List");
        assertThat(var.getInner_variables()).isNull();

    }
    @Test
    public void should_return_two_variables_when_inner_variable_and_this_variables() throws TemplateParseException {
        TextLoopTemplate loopTemplate=new TextLoopTemplate("test",TextTemplate.parse("${Name}ABC${_this}"));
        List<Variable> vars=loopTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(2);
        Variable var1=vars.get(0);
        Variable var2=vars.get(1);
        assertThat(var1.getName()).isEqualTo("test");
        assertThat(var1.getType()).isEqualTo("List");
        assertThat(var1.getInner_variables()).isNull();
        assertThat(var2.getName()).isEqualTo("Name");
        assertThat(var2.getType()).isEqualTo("String");

    }

//    //Fill Variables
    @Test
    public void should_fill_single_variable_within_pattern() throws TemplateParseException {
        TextLoopTemplate template=(TextLoopTemplate) TextTemplate.parse("{{#test}}${Name}{{/test}}");
        HashMap<String,Object> innerData1=new HashMap<>();
        innerData1.put("Name","Atmaram");
        HashMap<String,Object> innerData2=new HashMap<>();
        innerData2.put("Name","Roopa");
        List<HashMap> test=Arrays.asList(innerData1,innerData2);
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("AtmaramRoopa");

    }
    @Test
    public void should_fill_this_variable_within_pattern() throws TemplateParseException {
        TextLoopTemplate template=(TextLoopTemplate)TextTemplate.parse("{{#test}}${_this}{{/test}}");
        List<String> test=Arrays.asList("Atmaram","Roopa");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("AtmaramRoopa");

    }

    @Test
    public void should_return_same_loop_if_loopvariable_not_in_data() throws TemplateParseException {
        TextLoopTemplate template=(TextLoopTemplate)TextTemplate.parse("{{#test}}${Name}{{/test}}");
        assertThat(template.fill(new HashMap<>()).toStringTemplate()).isEqualTo("{{#test}}${Name}{{/test}}");

    }
    @Test
    public void should_return_same_loop_if_loopvariable_in_data_but_not_list() throws TemplateParseException {
        TextLoopTemplate template=(TextLoopTemplate)TextTemplate.parse("{{#test}}${Name}{{/test}}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test","Atmaram");
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("{{#test}}${Name}{{/test}}");

    }

//
//    //To String Template
    @Test
    public void should_covert_to_string_template() throws TemplateParseException {
        TextLoopTemplate loopTemplate=new TextLoopTemplate("test",TextTemplate.parse("Hello"));
        assertThat(loopTemplate.toStringTemplate()).isEqualTo("{{#test}}Hello{{/test}}");
    }
}
