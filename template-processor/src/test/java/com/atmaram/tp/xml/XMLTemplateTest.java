package com.atmaram.tp.xml;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class XMLTemplateTest {
    @Test
    public void should_parse_string_as_FilledVariableTemplate() {
        XMLTemplate template=XMLTemplate.from("Hello");
        assertThat(template).isInstanceOf(FilledVariableTemplate.class);

    }
    @Test
    public void should_parse_xml_as_Template() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a>Hello</a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);

    }
    @Test
    public void should_parse_xml_as_Template_with_attribute() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a name=\"ATM\">Hello</a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a name=\"ATM\">Hello</a>");

    }
    @Test
    public void should_parse_xml_as_Template_with_child_nodes() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a><b>Hello</b></a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a><b>Hello</b></a>");

    }
    @Test
    public void should_parse_xml_as_Template_with_multiple_attributes() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a name=\"ATM\" place=\"Pune\">Hello</a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a name=\"ATM\" place=\"Pune\">Hello</a>");

    }
    @Test
    public void should_parse_xml_as_Template_with_multiple_child_nodes() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a><b>Hello</b><c>World</c></a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a><b>Hello</b><c>World</c></a>");

    }
    @Test
    public void should_parse_xml_as_Template_with_variable_in_contents() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a>${Content}</a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a>${Content}</a>");

    }
    @Test
    public void should_parse_xml_as_Template_with_variable_in_attribute_values() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a name=${Name}>Hello</a>");
        assertThat(template).isInstanceOf(NodeTemplate.class);
        assertThat(template.toStringTemplate()).isEqualTo("<a name=\"${Name}\">Hello</a>");

    }

    //Fill
    @Test
    public void should_fill_single_variable_in_contents() throws TemplateParseException {
       XMLTemplate template=XMLTemplate.parse("<a>${Content}</a>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Content","Hello");
        XMLTemplate filled=(XMLTemplate)template.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<a>Hello</a>");


    }
    @Test
    public void should_fill_single_variable_in_attribute() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a name=${Name}>Hello</a>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","ATM");
        XMLTemplate filled=(XMLTemplate)template.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<a name=\"ATM\">Hello</a>");


    }
    @Test
    public void should_fill_loop_with_single_variable_in_contents() throws TemplateParseException {
        XMLTemplate template=XMLTemplate.parse("<a>{{#names}}<b>${Name}</b>{{/names}}</a>");
        HashMap<String,Object> data=new HashMap<>();
        ArrayList names=new ArrayList();
        HashMap<String,Object> name1=new HashMap<>();
        name1.put("Name","ATM");
        names.add(name1);
        HashMap<String,Object> name2=new HashMap<>();
        name2.put("Name","ANU");
        names.add(name2);
        data.put("names",names);
        XMLTemplate filled=(XMLTemplate)template.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<a><b>ATM</b><b>ANU</b></a>");


    }
}
