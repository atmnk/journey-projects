package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoopTemplateTest {
    //Get Variables
    @Test
    public void should_return_single_variable_as_list_with_inner_variables() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${Name}}"));
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
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${_this}}"));
        List<Variable> vars=loopTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("test");
        assertThat(var.getType()).isEqualTo("List");
        assertThat(var.getInner_variables()).isNull();

    }
    @Test
    public void should_return_two_variables_when_inner_variable_and_this_variables() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${Name},\"place\":${_this}}"));
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
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":#{Name}}"));
        List<Variable> vars=loopTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
        Variable var=vars.get(0);
        assertThat(var.getName()).isEqualTo("Name");
        assertThat(var.getType()).isEqualTo("String");

    }

    //Fill Template Variables
    @Test
    public void should_fill_template_variables_within_pattern() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":#{Name}}"));
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        LoopTemplate filled=(LoopTemplate) loopTemplate.fillTemplateVariables(data);
        assertThat(filled.innerObjectTemplate).isInstanceOf(ObjectTemplate.class);
        ObjectTemplate pattern=(ObjectTemplate)filled.innerObjectTemplate;
        assertThat(pattern.keyValueTemplates.values().toArray()[0]).isInstanceOf(FilledVariableTemplate.class);

    }

    //Fill Variables
    @Test
    public void should_fill_single_variable_within_pattern() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("[{{#test}}{\"name\":${Name}}{{/test}}]");
        HashMap<String,Object> innerData1=new HashMap<>();
        innerData1.put("Name","Atmaram");
        HashMap<String,Object> innerData2=new HashMap<>();
        innerData2.put("Name","Roopa");
        List<HashMap> test=Arrays.asList(innerData1,innerData2);
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]");

    }
    @Test
    public void should_fill_this_variable_within_pattern() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("[{{#test}}{\"name\":${_this}}{{/test}}]");
        List<String> test=Arrays.asList("Atmaram","Roopa");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test",test);
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]");

    }

    @Test
    public void should_return_same_loop_if_loopvariable_not_in_data() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("[{{#test}}{\"name\":${Name}}{{/test}}]");
        assertThat(template.fill(new HashMap<>()).toStringTemplate()).isEqualTo("[{\"template\":{\"name\":\"${Name}\"},\"variable\":\"test\"}]");

    }
    @Test
    public void should_return_same_loop_if_loopvariable_in_data_but_not_list() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("[{{#test}}{\"name\":${Name}}{{/test}}]");
        HashMap<String,Object> data=new HashMap<>();
        data.put("test","Atmaram");
        assertThat(template.fill(data).toStringTemplate()).isEqualTo("[{\"template\":{\"name\":\"${Name}\"},\"variable\":\"test\"}]");

    }

    //Extract
    @Test
    public void should_extract_single_inner_variable_as_hashmap_in_list() throws TemplateParseException, ParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${Name}}"));
        HashMap<String,Object> data=loopTemplate.extract(JSONTemplate.stringToJSON("[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]"));
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
    public void should_extract_single_inner_this_variable_as_list() throws TemplateParseException, ParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${_this}}"));
        HashMap<String,Object> data=loopTemplate.extract(JSONTemplate.stringToJSON("[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]"));
        assertThat(data.containsKey("test"));
        assertThat(data.get("test")).isInstanceOf(List.class);
        List<String> actualData= (List<String>) data.get("test");
        List<String> expectedData=Arrays.asList("Atmaram","Roopa");
        assertThat(actualData).isEqualTo(expectedData);

    }

    //To String Template
    @Test
    public void should_covert_to_string_template() throws TemplateParseException {
        LoopTemplate loopTemplate=new LoopTemplate("test",JSONTemplate.parse("{\"name\":${Name}}"));
        assertThat(loopTemplate.toStringTemplate()).isEqualTo("[{\"template\":{\"name\":\"${Name}\"},\"variable\":\"test\"}]");
    }
}
