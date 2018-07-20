package com.atmaram.tp.text;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TextTemplateTest {
    @Test
    public void should_parse_text_template_without_variable() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello");
        assertThat(textTemplate.getBlocks().size()).isEqualTo(1);
        assertThat(textTemplate.getBlocks().get(0)).isEqualTo("Hello");

    }
    @Test
    public void should_parse_text_template_with_single_variable() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello ${to}");
        assertThat(textTemplate.getBlocks().size()).isEqualTo(2);
        assertThat(textTemplate.getBlocks().get(0)).isEqualTo("Hello ");
        assertThat(textTemplate.getBlocks().get(1)).isInstanceOf(TextVariable.class);
        TextVariable textVariable=(TextVariable)textTemplate.getBlocks().get(1);
        assertThat(textVariable.name).isEqualTo("to");
    }
    @Test
    public void should_parse_text_template_with_multiple_variable() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello ${to} from ${from}");
        assertThat(textTemplate.getBlocks().size()).isEqualTo(4);
        assertThat(textTemplate.getBlocks().get(0)).isEqualTo("Hello ");
        assertThat(textTemplate.getBlocks().get(1)).isInstanceOf(TextVariable.class);
        TextVariable textVariable1=(TextVariable)textTemplate.getBlocks().get(1);
        assertThat(textVariable1.name).isEqualTo("to");
        assertThat(textTemplate.getBlocks().get(2)).isEqualTo(" from ");
        assertThat(textTemplate.getBlocks().get(3)).isInstanceOf(TextVariable.class);
        TextVariable textVariable2=(TextVariable)textTemplate.getBlocks().get(3);
        assertThat(textVariable2.name).isEqualTo("from");
    }
    @Test
    public void should_parse_text_template_with_loop() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello {{#names}}name{{/names}}");
        assertThat(textTemplate.getBlocks().size()).isEqualTo(2);
        assertThat(textTemplate.getBlocks().get(0)).isEqualTo("Hello ");
        assertThat(textTemplate.getBlocks().get(1)).isInstanceOf(TextLoop.class);
        TextLoop textLoop=(TextLoop)textTemplate.getBlocks().get(1);
        assertThat(textLoop.variable).isEqualTo("names");
        assertThat(textLoop.inner_template.getBlocks().size()).isEqualTo(1);
        assertThat(textLoop.inner_template.getBlocks().get(0)).isEqualTo("name");

    }
    @Test
    public void should_parse_text_template_with_nested_loops() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello {{#places}}{{#names}}names{{/names}}{{/places}}");
        assertThat(textTemplate.getBlocks().size()).isEqualTo(2);
        assertThat(textTemplate.getBlocks().get(0)).isEqualTo("Hello ");
        assertThat(textTemplate.getBlocks().get(1)).isInstanceOf(TextLoop.class);

    }

    //Fill test
    @Test
    public void should_return_same_template_when_no_variables() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello");
        String result=textTemplate.fill(new HashMap<>());
        assertThat(result).isEqualTo("Hello");
    }
    @Test
    public void should_fill_single_variable() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello ${to}");
        HashMap<String,Object> data=new HashMap<>();
        data.put("to","World");
        String result=textTemplate.fill(data);
        assertThat(result).isEqualTo("Hello World");
    }
    @Test
    public void should_fill_loop_with_blank_text() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello{{#names}}{{/names}}");
        HashMap<String,Object> data=new HashMap<>();
        List<String> names=new ArrayList<>();
        names.add("Atmaram");
        names.add("Roopa");
        data.put("names",names);
        String result=textTemplate.fill(data);
        assertThat(result).isEqualTo("Hello");
    }
    @Test
    public void should_fill_loop_with_static_text() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello {{#names}}name {{/names}}");
        HashMap<String,Object> data=new HashMap<>();
        List<String> names=new ArrayList<>();
        names.add("Atmaram");
        names.add("Roopa");
        data.put("names",names);
        String result=textTemplate.fill(data);
        assertThat(result).isEqualTo("Hello name name ");
    }
    @Test
    public void should_fill_loop_with_variable_text() throws TemplateParseException {
        TextTemplate textTemplate=TextTemplate.parse("Hello {{#names}}${_this} {{/names}}");
        HashMap<String,Object> data=new HashMap<>();
        List<String> names=new ArrayList<>();
        names.add("Atmaram");
        names.add("Roopa");
        data.put("names",names);
        String result=textTemplate.fill(data);
        assertThat(result).isEqualTo("Hello Atmaram Roopa ");
    }
}
