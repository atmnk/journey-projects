package com.atmaram.tp.xml;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class LoopTemplateTest {
    //Get Variables
    @Test
    public void should_return_single_variable_as_list_with_inner_variables() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>${Name}</a>"));
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
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>${_this}</a>"));
        List<Variable> vars=loopTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("test");
        assertThat(var.getType()).isEqualTo("List");
        assertThat(var.getInner_variables()).isNull();

    }
    @Test
    public void should_return_two_variables_when_inner_variable_and_this_variables() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a name=${Name}>${_this}</a>"));
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

    //Get Template Variables
    @Test
    public void should_return_template_variables_within_pattern_as_template_variable() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>#{Name}</a>"));
        List<Variable> vars=loopTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("Name");
        assertThat(var.getType()).isEqualTo("String");

    }

    //Fill Template Variables
    @Test
    public void should_fill_template_variables_within_pattern() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>#{Name}</a>"));
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        LoopTemplate filled=(LoopTemplate) loopTemplate.fillTemplateVariables(data);
        assertThat(filled.pattern).isInstanceOf(NodeTemplate.class);
        NodeTemplate pattern=(NodeTemplate)filled.pattern;
        assertThat(pattern.childNodes.get(0)).isInstanceOf(FilledVariableTemplate.class);

    }

    //Fill Variables
    @Test
    public void should_fill_single_variable_within_pattern() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<Base>{{#test}}<a>${Name}</a>{{/test}}</Base>");
        HashMap<String,Object> innerData1=new HashMap<>();
        innerData1.put("Name","Atmaram");
        HashMap<String,Object> innerData2=new HashMap<>();
        innerData2.put("Name","Roopa");
        List<HashMap> test=Arrays.asList(innerData1,innerData2);
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("<Base><a>Atmaram</a><a>Roopa</a></Base>");

    }
    @Test
    public void should_fill_this_variable_within_pattern() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<Base>{{#test}}<a>${_this}</a>{{/test}}</Base>");
        List<String> test=Arrays.asList("Atmaram","Roopa");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("<Base><a>Atmaram</a><a>Roopa</a></Base>");

    }

    @Test
    public void should_return_same_loop_if_loopvariable_not_in_data() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<Base>{{#test}}<a>${Name}</a>{{/test}}</Base>");
        assertThat(template.fill(new HashMap<>()).toStringTemplate()).isEqualTo("<Base><XMLLoop variable=\"test\"><a>${Name}</a></XMLLoop></Base>");

    }
    @Test
    public void should_return_same_loop_if_loopvariable_in_data_but_not_list() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<Base>{{#test}}<a>${Name}</a>{{/test}}</Base>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test","Atmaram");
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("<Base><XMLLoop variable=\"test\"><a>${Name}</a></XMLLoop></Base>");

    }

    //Extract
    @Test
    public void should_extract_single_inner_variable_as_hashmap_in_list() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>${Name}</a>"));
        HashMap<String,Object> data=loopTemplate.extract(XMLTemplate.StringDocToElement("<Base><a>Atmaram</a><a>Roopa</a></Base>"));
        assertThat(data.containsKey("test"));
        assertThat(data.get("test")).isInstanceOf(List.class);
        List<HashMap> actualData= (List<HashMap>) data.get("test");
        HashMap map1=new HashMap();
        map1.put("Name","Atmaram");
        HashMap map2=new HashMap();
        map2.put("Name","Roopa");
        List<HashMap> expectedData=Arrays.asList(map1,map2);
        assertThat(actualData).isEqualTo(expectedData);
    }
    @Test
    public void should_extract_single_inner_this_variable_as_list() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>${_this}</a>"));
        HashMap<String,Object> data=loopTemplate.extract(XMLTemplate.StringDocToElement("<Base><a>Atmaram</a><a>Roopa</a></Base>"));
        assertThat(data.containsKey("test"));
        assertThat(data.get("test")).isInstanceOf(List.class);
        List<String> actualData= (List<String>) data.get("test");
        List<String> expectedData=Arrays.asList("Atmaram","Roopa");
        assertThat(actualData).isEqualTo(expectedData);

    }

    //To String Template
    @Test
    public void should_covert_to_string_template() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a>Hello</a>"));
        assertThat(loopTemplate.toStringTemplate()).isEqualTo("<XMLLoop variable=\"test\"><a>Hello</a></XMLLoop>");
    }
//    @Test
//    public void should_return_two_variables_when_inner_variable_and_this_variables() throws TemplateParseException {
//        LoopTemplate loopTemplate=new LoopTemplate("test",XMLTemplate.parse("<a name=${Name}>${_this}</a>"));
//        List<Variable> vars=loopTemplate.getVariables();
//        assertThat(vars.size()).isEqualTo(2);
//        Variable var1=vars.get(0);
//        Variable var2=vars.get(1);
//        assertThat(var1.getName()).isEqualTo("test");
//        assertThat(var1.getType()).isEqualTo("List");
//        assertThat(var1.getInner_variables()).isNull();
//        assertThat(var2.getName()).isEqualTo("Name");
//        assertThat(var2.getType()).isEqualTo("String");
//
//    }
}
