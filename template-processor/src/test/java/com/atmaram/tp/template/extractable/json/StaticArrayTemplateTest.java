package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;

import org.json.simple.parser.ParseException;
import org.junit.Test;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticArrayTemplateTest {

    //Get Variables
    @Test
    public void should_get_variables_from_all_members() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.parse("{\"name\":${Name}}"));
        List<Variable> vars=staticArrayTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);

    }
    @Test
    public void should_get_no_variables_if_no_members() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        List<Variable> vars=staticArrayTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(0);

    }
    //Get Template Variables
    @Test
    public void should_get_template_variables_from_all_members() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.parse("{\"name\":#{Name}}"));
        List<Variable> vars=staticArrayTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);

    }
    @Test
    public void should_get_no_template_variables_if_no_members() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        List<Variable> vars=staticArrayTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(0);

    }

    //Fill Template Variables
    @Test
    public void should_fill_template_variables_from_all_members() throws TemplateParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.parse("{\"name\":#{Name}}"));
        JSONTemplate filled=staticArrayTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("[{\"name\":\"Atmaram\"}]");
    }
    @Test
    public void should_not_throw_error_if_no_members_if_fill_template_variables() throws TemplateParseException, ParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        JSONTemplate filled=staticArrayTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("[]");
    }
    //Fill Variables
    @Test
    public void should_fill_variables_from_all_members() throws TemplateParseException, ParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.parse("{\"name\":${Name}}"));
        JSONTemplate filled=staticArrayTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("[{\"name\":\"Atmaram\"}]");

    }
    @Test
    public void should_not_throw_error_if_no_members_if_fill() throws TemplateParseException, ParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        JSONTemplate filled=staticArrayTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("[]");
    }

    //Extract

    @Test
    public void should_extract_data_for_one_node_member() throws TemplateParseException, ParseException, ParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.parse("{\"name\":${Name}}"));
        HashMap<String, Object> actual=staticArrayTemplate.extract(JSONTemplate.stringToJSON("[{\"name\":\"Atmaram\"}]"));
        HashMap<String,Object> expected=new HashMap<>();
        expected.put("Name","Atmaram");
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    public void should_extract_data_for_one_loop_member() throws TemplateParseException, ParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        LoopTemplate loopTemplate=new LoopTemplate("names",JSONTemplate.parse("{\"name\":${Name}}"));
        staticArrayTemplate.add(loopTemplate);
        HashMap<String, Object> actual=staticArrayTemplate.extract(JSONTemplate.stringToJSON("[[{\"name\":\"Atmaram\"}]]"));
        HashMap<String,Object> loopMember=new HashMap<>();
        loopMember.put("Name","Atmaram");
        List<HashMap> loopData=new ArrayList<>();
        loopData.add(loopMember);
        HashMap<String,Object> expected=new HashMap<>();
        expected.put("names",loopData);
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    public void should_not_throw_error_if_no_members() throws TemplateParseException, ParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.extract(JSONTemplate.stringToJSON("[]"));
    }

    //To JSON Compatible Object, String Template
    @Test
    public void should_add_text_json_node_if_member_is_text() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        staticArrayTemplate.add(JSONTemplate.from("Hello"));
        assertThat(staticArrayTemplate.toStringTemplate()).isEqualTo("[\"Hello\"]");
    }

    @Test
    public void should_add_text_json_node_if_member_is_static_list_element() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        StaticArrayTemplate staticArrayTemplateInner=new StaticArrayTemplate();
        staticArrayTemplateInner.add(JSONTemplate.parse("{\"name\":\"Hello\"}"));
        staticArrayTemplate.add(staticArrayTemplateInner);
        assertThat(staticArrayTemplate.toStringTemplate()).isEqualTo("[[{\"name\":\"Hello\"}]]");
    }
    @Test
    public void should_add_text_json_node_if_member_is_other_element() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        ObjectTemplate inner=(ObjectTemplate) JSONTemplate.parse("{\"name\":\"Hello\"}");
        staticArrayTemplate.add(inner);
        assertThat(staticArrayTemplate.toStringTemplate()).isEqualTo("[{\"name\":\"Hello\"}]");
    }
    @Test
    public void should_give_empty_array_if_no_members() throws TemplateParseException {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        assertThat(staticArrayTemplate.toStringTemplate()).isEqualTo("[]");
    }
}
