package com.atmaram.tp.template.extractable.xml;

import com.atmaram.tp.template.Variable;
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

public class NodeStaticListTemplateTest {

    //Get Variables
    @Test
    public void should_get_variables_from_all_members() throws TemplateParseException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>${Name}</a>"));
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>${Place}</a>"));
        List<Variable> vars=nodeStaticListTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(2);

    }
    //Get Template Variables
    @Test
    public void should_get_template_variables_from_all_members() throws TemplateParseException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>#{Name}</a>"));
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>#{Place}</a>"));
        List<Variable> vars=nodeStaticListTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(2);

    }

    //Fill Template Variables
    @Test
    public void should_fill_template_variables_from_all_members() throws TemplateParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        data.put("Place","Pune");
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        NodeTemplate nodeTemplate=new NodeTemplate("Base");
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>#{Name}</a>"));
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>#{Place}</a>"));
        nodeTemplate.setChildNodes(Arrays.asList(nodeStaticListTemplate));
        nodeTemplate.setAttributes(new HashMap<>());
        XMLTemplate filled=nodeTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<Base><a>Atmaram</a><a>Pune</a></Base>");

    }
    //Fill Variables
    @Test
    public void should_fill_variables_from_all_members() throws TemplateParseException {
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        data.put("Place","Pune");
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        NodeTemplate nodeTemplate=new NodeTemplate("Base");
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>${Name}</a>"));
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>${Place}</a>"));
        nodeTemplate.setChildNodes(Arrays.asList(nodeStaticListTemplate));
        nodeTemplate.setAttributes(new HashMap<>());
        XMLTemplate filled=nodeTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<Base><a>Atmaram</a><a>Pune</a></Base>");

    }

    //Extract

    @Test
    public void should_extract_data_for_one_node_member() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        nodeStaticListTemplate.add(XMLTemplate.parse("<a>${Name}</a>"));
        HashMap<String, Object> actual=nodeStaticListTemplate.extract(XMLTemplate.StringDocToElement("<Base><a>Atmaram</a></Base>"));
        HashMap<String,Object> expected=new HashMap<>();
        expected.put("Name","Atmaram");
        assertThat(actual).isEqualTo(expected);

    }
    @Test
    public void should_extract_data_for_one_loop_member() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        LoopTemplate loopTemplate=new LoopTemplate("names",XMLTemplate.parse("<a>${Name}</a>"));
        nodeStaticListTemplate.add(loopTemplate);
        HashMap<String, Object> actual=nodeStaticListTemplate.extract(XMLTemplate.StringDocToElement("<Base><a>Atmaram</a></Base>"));
        HashMap<String,Object> loopMember=new HashMap<>();
        loopMember.put("Name","Atmaram");
        List<HashMap> loopData=new ArrayList<>();
        loopData.add(loopMember);
        HashMap<String,Object> expected=new HashMap<>();
        expected.put("names",loopData);
        assertThat(actual).isEqualTo(expected);

    }

    //To XML Compatible Object, String Template
    @Test
    public void should_add_text_xml_node_if_member_is_text() throws TemplateParseException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        nodeStaticListTemplate.add(XMLTemplate.from("Hello"));
        assertThat(nodeStaticListTemplate.toStringTemplate()).isEqualTo("<XMLStatic>Hello</XMLStatic>");
    }

    @Test
    public void should_add_text_xml_node_if_member_is_static_list_element() throws TemplateParseException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        NodeStaticListTemplate nodeStaticListTemplateInner=new NodeStaticListTemplate();
        nodeStaticListTemplateInner.add(XMLTemplate.parse("<a>Hello</a>"));
        nodeStaticListTemplate.add(nodeStaticListTemplateInner);
        assertThat(nodeStaticListTemplate.toStringTemplate()).isEqualTo("<XMLStatic><a>Hello</a></XMLStatic>");
    }
    @Test
    public void should_add_text_xml_node_if_member_is_other_element() throws TemplateParseException {
        NodeStaticListTemplate nodeStaticListTemplate=new NodeStaticListTemplate();
        NodeTemplate inner=(NodeTemplate) XMLTemplate.parse("<a>Hello</a>");
        nodeStaticListTemplate.add(inner);
        assertThat(nodeStaticListTemplate.toStringTemplate()).isEqualTo("<XMLStatic><a>Hello</a></XMLStatic>");
    }
}
