package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectTemplateTest {
    //Fill Variables
    @Test
    public void should_fill_keys() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{${Name}:\"Hello\"}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        JSONTemplate filled=nodeTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("{\"Atmaram\":\"Hello\"}");
    }
    @Test
    public void should_fill_values() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{\"name\":${Name}}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        JSONTemplate filled=nodeTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("{\"name\":\"Atmaram\"}");
    }

    //Fill Template Variables
    @Test
    public void should_fill_keys_with_template_variable() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{#{Name}:\"Hello\"}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        JSONTemplate filled=nodeTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("{\"Atmaram\":\"Hello\"}");
    }
    @Test
    public void should_fill_values_with_template_variables() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{\"name\":#{Name}}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        JSONTemplate filled=nodeTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("{\"name\":\"Atmaram\"}");
    }

    //Extract
    @Test
    public void should_extract_simple_text_child() throws TemplateParseException, ParseException {
        JSONTemplate jsonTemplate=JSONTemplate.parse("{\"name\":${Name}}");
        HashMap<String,Object> data=jsonTemplate.extract(JSONTemplate.stringToJSON("{\"name\":\"Atmaram\"}"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
    }
   
    @Test
    public void should_extract_simple_object_child() throws TemplateParseException, ParseException {
        JSONTemplate jsonTemplate=JSONTemplate.parse("{\"name\":{\"name\":${Name}}}");
        HashMap<String,Object> data=jsonTemplate.extract(JSONTemplate.stringToJSON("{\"name\":{\"name\":\"Atmaram\"}}"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
    }
    @Test
    public void should_extract_loop_child() throws TemplateParseException, ParseException {
        JSONTemplate jsonTemplate=JSONTemplate.parse("{\"bees\":[{{#Bees}}{\"name\":${Name}}{{/Bees}}]}");
        HashMap<String,Object> data=jsonTemplate.extract(JSONTemplate.stringToJSON("{\"bees\":[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]}"));
        assertThat(data.containsKey("Bees"));
        assertThat(data.get("Bees")).isInstanceOf(List.class);
        List<HashMap> actual=(List)data.get("Bees");
        HashMap<String,Object> map1=new HashMap<>();
        map1.put("Name","Atmaram");
        HashMap<String,Object> map2=new HashMap<>();
        map2.put("Name","Roopa");
        List<HashMap> expected=Arrays.asList(map1,map2);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    public void should_extract_static_array_list_child() throws TemplateParseException, ParseException {
        JSONTemplate jsonTemplate=JSONTemplate.parse("{\"bees\":[{{#Bees}}{\"name\":${Name},\"place\":${Place}}{{/Bees}}]}");
        HashMap<String,Object> data=new HashMap<>();
        HashMap<String,Object> map1=new HashMap<>();
        map1.put("Name","Atmaram");
        HashMap<String,Object> map2=new HashMap<>();
        map2.put("Name","Roopa");
        List<HashMap> expected=Arrays.asList(map1,map2);
        data.put("Bees",expected);
        JSONTemplate filled=(JSONTemplate) jsonTemplate.fill(data);
        HashMap<String,Object> eData=filled.extract(JSONTemplate.stringToJSON("{\"bees\":[{\"name\":\"Atmaram\",\"place\":\"Pune\"},{\"name\":\"Roopa\",\"place\":\"Mumbai\"}]}"));
        assertThat(eData.containsKey("Place"));
        assertThat(eData.get("Place")).isEqualTo("Mumbai");

    }

    //Get Variables
    @Test
    public void should_get_variables_from_object_template_in_keys() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{${Name}:\"Hello\"}");
        List<Variable> vars=nodeTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    @Test
    public void should_get_variables_from_node_template_in_values() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{\"name\":${Name}}");
        List<Variable> vars=nodeTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    //Get Template Variables
    @Test
    public void should_get_template_variables_from_object_template_in_keys() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{#{Name}:\"Hello\"}");
        List<Variable> vars=nodeTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    @Test
    public void should_get_template_variables_from_node_template_in_values() throws TemplateParseException {
        ObjectTemplate nodeTemplate=(ObjectTemplate) JSONTemplate.parse("{\"name\":#{Name}}");
        List<Variable> vars=nodeTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
}
