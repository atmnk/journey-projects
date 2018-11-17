package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONTemplateTest {
    @Test
    public void should_fill_expressions_having_uuid() throws TemplateParseException {
        JSONArray obj=(JSONArray)((JSONTemplate)JSONTemplate.parse("[${_uuid}]").fill(null)).toJSONCompatibleObject();
        assertThat(obj.size()).isEqualTo(1);
    }

    @Test
    public void should_fill_expressions_having_eval() throws TemplateParseException {
        JSONArray obj=(JSONArray)((JSONTemplate)JSONTemplate.parse("[${_eval('####')}]").fill(null)).toJSONCompatibleObject();
        assertThat(obj.size()).isEqualTo(1);
        assertThat(obj.get(0)).isInstanceOf(String.class);
    }
    @Test
    public void should_fill_expressions_having_timestamp() throws TemplateParseException {
        JSONArray obj=(JSONArray)((JSONTemplate)JSONTemplate.parse("[${_timestamp}]").fill(null)).toJSONCompatibleObject();
        assertThat(obj.size()).isEqualTo(1);
        assertThat(obj.get(0)).isInstanceOf(Long.class);
    }

    @Test
    public void should_support_static_array_at_root_level_for_parsing() throws TemplateParseException {
        JSONArray obj=(JSONArray)((JSONTemplate)JSONTemplate.parse("[\"Atmaram\"]").fill(null)).toJSONCompatibleObject();
        assertThat(obj.toJSONString()).isEqualTo("[\"Atmaram\"]");
    }

    @Test
    public void should_construct_and_fill_template_when_null_object() throws TemplateParseException {
        JSONObject obj=(JSONObject) ((JSONTemplate)JSONTemplate.parse("{\"name\":[[\"Atmaram\"]]}").fill(null)).toJSONCompatibleObject();
        assertThat(obj.toJSONString()).isEqualTo("{\"name\":[[\"Atmaram\"]]}");
    }
    @Test
    public void should_fill_template_when_nested_this() throws TemplateParseException {
        HashMap<String,Object> obj=new HashMap<>();
        List<List<List<String>>> names=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");
        List<List<String>> lst2=new ArrayList<>();
        lst2.add(lst1);
        names.add(lst2);
        obj.put("names",names);

        JSONObject objJSON=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":[{{#names}}{\"places\":[{{#_this}}{\"place\":[{{#_this}}{\"value\":${_this}}{{/_this}}]}{{/_this}}]}{{/names}}]}").fill(obj)).toJSONCompatibleObject();
        assertThat(objJSON.toJSONString()).isEqualTo("{\"name\":[{\"places\":[{\"place\":[{\"value\":\"Atmaram\"},{\"value\":\"Roopa\"}]}]}]}");
    }

    @Test
    public void should_give_same_template_when_no_variables_without_arrays() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<List<String>> lst=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");

        List<String> lst2=new ArrayList<>();
        lst2.add("Hemlata");
        lst2.add("Rohan");
        lst.add(lst1);
        lst.add(lst2);
        objData.put("names",lst);
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"name\":\"Atmaram\"}");
        JSONObject result= (JSONObject) ((JSONTemplate)jsonTemplate.fill(objData)).toJSONCompatibleObject();
        assertThat(result.toJSONString()).isEqualTo("{\"name\":\"Atmaram\"}");

    }
    @Test
    public void should_give_same_template_when_no_variables_with_arrays() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<List<String>> lst=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");

        List<String> lst2=new ArrayList<>();
        lst2.add("Hemlata");
        lst2.add("Rohan");
        lst.add(lst1);
        lst.add(lst2);
        objData.put("names",lst);
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"inner_array\":[\"Hemlata\",\"Rohan\"]}");
        JSONObject result= (JSONObject)((JSONTemplate)jsonTemplate.fill(objData)).toJSONCompatibleObject();
        assertThat(result.toJSONString()).isEqualTo("{\"inner_array\":[\"Hemlata\",\"Rohan\"]}");

    }
    @Test
    public void should_give_same_template_when_no_variables_with_array_containing_static_json_object() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<List<String>> lst=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");

        List<String> lst2=new ArrayList<>();
        lst2.add("Hemlata");
        lst2.add("Rohan");
        lst.add(lst1);
        lst.add(lst2);
        objData.put("names",lst);
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"name\":[{\"name\":\"Atmaram\"}]}");
        JSONObject result=(JSONObject) ((JSONTemplate)jsonTemplate.fill(objData)).toJSONCompatibleObject();
        assertThat(result.toJSONString()).isEqualTo("{\"name\":[{\"name\":\"Atmaram\"}]}");

    }
    @Test
    public void should_fill_single_value() throws TemplateParseException {
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"name\":${name}}");
        HashMap<String,Object> objData=new HashMap<>();
        objData.put("name","Atmaram");
        JSONObject op= (JSONObject)((JSONTemplate)jsonTemplate.fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":\"Atmaram\"}");
    }

    @Test
    public void should_fill_multiple_value() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        objData.put("name","Atmaram");
        objData.put("place","Pune");
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":${name},\"place\":${place}}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":\"Atmaram\",\"place\":\"Pune\"}");
    }

    @Test
    public void should_fill_array() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<String> lst=new ArrayList<>();
        lst.add("Atmaram");
        lst.add("Roopa");
        objData.put("names",lst);
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":${names}}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[\"Atmaram\",\"Roopa\"]}");
    }
    @Test
    public void should_fill_objects_with_array_elements() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<String> lst=new ArrayList<>();
        lst.add("Atmaram");
        lst.add("Roopa");
        objData.put("names",lst);
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":[{{#names}}{\"name\":${_this}}{{/names}}]}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]}");
    }
    @Test
    public void should_fill_nested_array_constructing_array_with_array() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<List<String>> lst=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");

        List<String> lst2=new ArrayList<>();
        lst2.add("Hemlata");
        lst2.add("Rohan");
        lst.add(lst1);
        lst.add(lst2);
        objData.put("names",lst);
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":${names}}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[[\"Atmaram\",\"Roopa\"],[\"Hemlata\",\"Rohan\"]]}");
    }
    @Test
    public void should_return_same_template_if_no_variables() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        objData.put("name","Mayur");
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":[\"Atmaram\"]}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[\"Atmaram\"]}");
    }
    @Test
    public void should_fill_nested_array_constructing_array_with_object_array() throws TemplateParseException {
        HashMap<String,Object> objData=new HashMap<>();
        List<List<String>> lst=new ArrayList<>();
        List<String> lst1=new ArrayList<>();
        lst1.add("Atmaram");
        lst1.add("Roopa");

        List<String> lst2=new ArrayList<>();
        lst2.add("Hemlata");
        lst2.add("Rohan");
        lst.add(lst1);
        lst.add(lst2);
        objData.put("names",lst);
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":[{{#names}}{\"inner_array\":${_this}}{{/names}}]}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[{\"inner_array\":[\"Atmaram\",\"Roopa\"]},{\"inner_array\":[\"Hemlata\",\"Rohan\"]}]}");
    }
    @Test
    public void should_fill_array_with_objects() throws TemplateParseException {
        HashMap<String,Object> objData=new JSONObject();
        List<HashMap<String,Object>> objArray=new ArrayList<>();
        HashMap<String,Object> obj1=new HashMap<>();
        obj1.put("name","Atmaram");
        obj1.put("place","Mumbai");
        HashMap<String,Object> obj2=new HashMap<>();
        obj2.put("name","Roopa");
        obj2.put("place","Pune");
        objArray.add(obj1);
        objArray.add(obj2);
        objData.put("items",objArray);
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{\"name\":[{{#items}}{\"name\":${name},\"place\":${place}}{{/items}}]}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"name\":[{\"name\":\"Atmaram\",\"place\":\"Mumbai\"},{\"name\":\"Roopa\",\"place\":\"Pune\"}]}");
    }
    @Test
    public void should_fill_keys_with_variables() throws TemplateParseException {
        HashMap<String,Object> objData=new JSONObject();
        objData.put("name","Hello");
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{${name}:\"World\"}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"Hello\":\"World\"}");
    }

    @Test
    public void should_fill_keys_and_values_with_variables() throws TemplateParseException {
        HashMap<String,Object> objData=new JSONObject();
        objData.put("name","Hello");
        objData.put("place","World");
        JSONObject op=(JSONObject)((JSONTemplate)JSONTemplate.parse("{${name}:${place}}").fill(objData)).toJSONCompatibleObject();
        assertThat(op.toJSONString()).isEqualTo("{\"Hello\":\"World\"}");
    }

    //Extract Tests
    @Test
    public void should_extract_single_variable_from_static_array() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("[{ \"developer\":${Developer}}]");
        JSONArray objResult=(JSONArray) parser.parse("[{ \"developer\":\"Atmaram\"}]");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("Developer")).isTrue();
        assertThat(objData.get("Developer")).isEqualTo("Atmaram");

    }

    @Test
    public void should_extract_single_variable_from_nested_objects() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("[\n" +
                "  {\n" +
                "    \"Number\": \"213231231231231\",\n" +
                "    \"Id\": {\n" +
                "      \"Id\": ${Id}\n" +
                "    }\n" +
                "  }\n" +
                "]");
        JSONArray objResult=(JSONArray) parser.parse("[\n" +
                "  {\n" +
                "    \"Number\": \"213231231231231\",\n" +
                "    \"Id\": {\n" +
                "      \"Id\": \"My-ID\",\n" +
                "      \"Number\": \"ABCD-X393700\"" +
                "    }\n" +
                "  }\n" +
                "]");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("Id")).isTrue();
        assertThat(objData.get("Id")).isEqualTo("My-ID");
    }

    @Test
    public void should_extract_single_variable() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"name\":${name}}");
        JSONObject objResult=(JSONObject) parser.parse("{\"name\":\"Atmaram\"}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("name")).isTrue();
        assertThat(objData.get("name")).isEqualTo("Atmaram");
    }
    @Test
    public void should_extract_single_variable_in_loop() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${name}}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\"names\":[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(ArrayList.class);
        assertThat(((ArrayList<HashMap<String,Object>>)objData.get("names")).size()).isEqualTo(2);
        ArrayList<HashMap<String,Object>> list=(ArrayList<HashMap<String,Object>>)objData.get("names");
        for(int i=0;i<list.size();i++){
            HashMap<String,Object> listElement=list.get(i);
            assertThat(listElement).containsOnlyKeys("name");
            assertThat(listElement.get("name")).isIn("Atmaram","Roopa");
        }
    }
    @Test
    public void should_extract_multiple_variable_in_loop() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${name},\"place\":${place}}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\"names\":[{\"name\":\"Atmaram\",\"place\":\"Pune\"},{\"name\":\"Roopa\",\"place\":\"Mumbai\"}]}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(ArrayList.class);
        assertThat(((ArrayList<HashMap<String,Object>>)objData.get("names")).size()).isEqualTo(2);
        ArrayList<HashMap<String,Object>> list=(ArrayList<HashMap<String,Object>>)objData.get("names");
        for(int i=0;i<list.size();i++){
            HashMap<String,Object> listElement=list.get(i);
            assertThat(listElement).containsKeys("name","place");
            assertThat(listElement.get("name")).isIn("Atmaram","Roopa");
            assertThat(listElement.get("place")).isIn("Pune","Mumbai");
        }
    }



    //This tests

    @Test
    public void should_extract_this_variable_in_loop_containing_just_variable_this() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}${_this}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\"names\":[\"Atmaram\",\"Roopa\"]}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(List.class);
        assertThat(((ArrayList)objData.get("names")).size()).isEqualTo(2);
        List list=(List)objData.get("names");
        for(int i=0;i<list.size();i++){
            Object listElement=list.get(i);
            assertThat(listElement).isInstanceOf(String.class);
            assertThat(listElement).isIn("Atmaram","Roopa");
        }
    }

    @Test
    public void should_extract_this_variable_in_loop() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${_this}}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\"names\":[{\"name\":\"Atmaram\"},{\"name\":\"Roopa\"}]}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(List.class);
        assertThat(((ArrayList)objData.get("names")).size()).isEqualTo(2);
        List list=(List)objData.get("names");
        for(int i=0;i<list.size();i++){
            Object listElement=list.get(i);
            assertThat(listElement).isInstanceOf(String.class);
            assertThat(listElement).isIn("Atmaram","Roopa");
        }
    }

    @Test
    public void should_extract_this_variable_in_loop_with_object() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":{\"name1\":${_this}}}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\"names\":[{\"name\":{\"name1\":\"Atmaram\"}},{\"name\":{\"name1\":\"Roopa\"}}]}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(List.class);
        assertThat(((ArrayList)objData.get("names")).size()).isEqualTo(2);
        List list=(List)objData.get("names");
        for(int i=0;i<list.size();i++){
            Object listElement=list.get(i);
            assertThat(listElement).isInstanceOf(String.class);
            assertThat(listElement).isIn("Atmaram","Roopa");
        }
    }

    @Test
    public void should_extract_this_variable_as_loop_variable() throws TemplateParseException, ParseException {
        JSONParser parser=new JSONParser();
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":[{{#_this}}{\"place\":${_this}}{{/_this}}]}{{/names}}]}");
        JSONObject objResult=(JSONObject) parser.parse("{\n" +
                "\t\"names\": [{\n" +
                "\t\t\"name\": [{\n" +
                "\t\t\t\"place\": \"Pune\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"place\": \"Mumbai\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"name\": [{\n" +
                "\t\t\t\"place\": \"London\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"place\": \"Paris\"\n" +
                "\t\t}]\n" +
                "\t}]\n" +
                "}");
        HashMap<String,Object> objData= jsonTemplate.extract(objResult);
        assertThat(objData.containsKey("names")).isTrue();
        assertThat(objData.get("names")).isInstanceOf(List.class);
        assertThat(((ArrayList)objData.get("names")).size()).isEqualTo(2);
        List list=(List)objData.get("names");
        for(int i=0;i<list.size();i++){
            Object listElement=list.get(i);
            assertThat(listElement).isInstanceOf(List.class);
            assertThat(listElement).isIn(Arrays.asList(Arrays.asList("Pune","Mumbai"),Arrays.asList("London","Paris")));        }
    }

    //getVariables Tests
    @Test
    public void should_get_single_variable() throws TemplateParseException {
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"name\":${name}}");
        List<Variable> variables=jsonTemplate.getVariables();
        assertThat(variables.size()).isEqualTo(1);
        assertThat(variables.get(0).getType()).isEqualTo("String");
        assertThat(variables.get(0).getName()).isEqualTo("name");

    }
    @Test
    public void should_get_single_variable_in_loop() throws TemplateParseException {
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${name}}{{/names}}]}");
        List<Variable> variables=jsonTemplate.getVariables();
        assertThat(variables.size()).isEqualTo(1);
        assertThat(variables.get(0).getType()).isEqualTo("List");
        assertThat(variables.get(0).getName()).isEqualTo("names");
        assertThat(variables.get(0).getInner_variables().size()).isEqualTo(1);
        assertThat(variables.get(0).getInner_variables().get(0).getType()).isEqualTo("String");
        assertThat(variables.get(0).getInner_variables().get(0).getName()).isEqualTo("name");

    }
    @Test
    public void should_get_this_variable_in_loop() throws TemplateParseException {
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${_this}}{{/names}}]}");
        List<Variable> variables=jsonTemplate.getVariables();
        assertThat(variables.size()).isEqualTo(1);
        assertThat(variables.get(0).getType()).isEqualTo("List");
        assertThat(variables.get(0).getName()).isEqualTo("names");
        assertThat(variables.get(0).getInner_variables()).isNull();

    }
    @Test
    public void should_get_this_variable_in_loop_coming_with_other_variables() throws TemplateParseException {
        JSONTemplate jsonTemplate =JSONTemplate.parse("{\"names\":[{{#names}}{\"name\":${_this},\"place\":${place}}{{/names}}]}");
        List<Variable> variables=jsonTemplate.getVariables();
        assertThat(variables.size()).isEqualTo(2);
        assertThat(variables.get(0).getType()).isEqualTo("List");
        assertThat(variables.get(0).getName()).isEqualTo("names");
        assertThat(variables.get(0).getInner_variables()).isNull();
        assertThat(variables.get(1).getType()).isEqualTo("String");
        assertThat(variables.get(1).getName()).isEqualTo("place");

    }

    //parse
    @Test
    public void should_parse_variable_as_variable_template() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("${Hello}");
        assertThat(template).isInstanceOf(VariableTemplate.class);
    }
    @Test
    public void should_parse_template_variable_as_template_variable_template() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("#{Hello}");
        assertThat(template).isInstanceOf(com.atmaram.tp.template.extractable.json.TemplateVariableTemplate.class);
    }
    // from
    @Test
    public void should_form_ObjectTemplate_From_json_Object() throws ParseException{
        JSONTemplate template=JSONTemplate.from(JSONTemplate.stringToJSON("{\"name\":\"Atmaram\"}"));
        assertThat(template).isInstanceOf(ObjectTemplate.class);
    }

    @Test
    public void should_form_FilledVariableTemplate_From_Text(){
        JSONTemplate template=JSONTemplate.from("Hello");
        assertThat(template).isInstanceOf(FilledVariableTemplate.class);
    }
    @Test
    public void should_form_FilledVariableTemplate_From_string(){
        JSONTemplate template=JSONTemplate.from("Hello");
        assertThat(template).isInstanceOf(FilledVariableTemplate.class);
    }
    @Test
    public void should_form_FilledVariableTemplate_From_Other_object(){
        JSONTemplate template=JSONTemplate.from(1);
        assertThat(template).isInstanceOf(FilledVariableTemplate.class);
    }
    @Test
    public void should_form_VariableTemplate_From_string_if_contains_variable(){
        JSONTemplate template=JSONTemplate.from("${Hello}");
        assertThat(template).isInstanceOf(VariableTemplate.class);
    }
    @Test
    public void should_form_TemplateVariableTemplate_From_string_if_contains_variable(){
        JSONTemplate template=JSONTemplate.from("#{Hello}");
        assertThat(template).isInstanceOf(com.atmaram.tp.template.extractable.json.TemplateVariableTemplate.class);
    }
    @Test
    public void should_form_VariableTemplate_From_json_Text_if_contains_variable(){
        JSONTemplate template=JSONTemplate.from("${Hello}");
        assertThat(template).isInstanceOf(VariableTemplate.class);
    }
    @Test
    public void should_form_TemplateVariableTemplate_From_json_Text_if_contains_variable(){
        JSONTemplate template=JSONTemplate.from("#{Hello}");
        assertThat(template).isInstanceOf(TemplateVariableTemplate.class);
    }

    @Test
    public void should_form_LoopTemplate_if_array_with_object_with_keys_variable_and_template() throws ParseException {

        JSONTemplate template=JSONTemplate.from(JSONTemplate.stringToJSON("[{\"variable\":\"var\",\"template\":{\"name\":\"${Name}\"}}]"));
        assertThat(template).isInstanceOf(LoopTemplate.class);
        LoopTemplate loopTemplate=(LoopTemplate)template;
        assertThat(loopTemplate.variableName).isEqualTo("var");
        assertThat(loopTemplate.innerObjectTemplate).isInstanceOf(ObjectTemplate.class);
    }
    @Test
    public void should_parse_string_as_FilledVariableTemplate() {
        JSONTemplate template=JSONTemplate.from("Hello");
        assertThat(template).isInstanceOf(FilledVariableTemplate.class);

    }
    @Test
    public void should_parse_json_as_ObjectTemplate() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("{\"name\":\"Atmaram\"}");
        assertThat(template).isInstanceOf(ObjectTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("{\"name\":\"Atmaram\"}");

    }
    @Test
    public void should_parse_json_as_Template_with_variable_in_key() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("{${Name}:\"Atmaram\"}");        assertThat(template).isInstanceOf(ObjectTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("{\"${Name}\":\"Atmaram\"}");

    }
    @Test
    public void should_parse_json_as_Template_with_variable_in_value() throws TemplateParseException {
        JSONTemplate template=JSONTemplate.parse("{\"Name\":${Name}}");        assertThat(template).isInstanceOf(ObjectTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("{\"Name\":\"${Name}\"}");

    }
}
