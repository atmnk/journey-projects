package com.atmaram.tp.xml;

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

public class NodeTemplateTest {
    //Fill Variables
    @Test
    public void should_fill_attribute() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A name=${Name}>Hello</A>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        XMLTemplate filled=nodeTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<A name=\"Atmaram\">Hello</A>");
    }
    @Test
    public void should_fill_childNodes() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A>${Name}</A>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        XMLTemplate filled=nodeTemplate.fill(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<A>Atmaram</A>");
    }

    //Fill Template Variables
    @Test
    public void should_fill_attribute_with_template_variable() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A name=#{Name}>Hello</A>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        XMLTemplate filled=nodeTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<A name=\"Atmaram\">Hello</A>");
    }
    @Test
    public void should_fill_childNodes_with_template_variables() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A>#{Name}</A>");
        HashMap<String,Object> data=new HashMap<>();
        data.put("Name","Atmaram");
        XMLTemplate filled=nodeTemplate.fillTemplateVariables(data);
        assertThat(filled.toStringTemplate()).isEqualTo("<A>Atmaram</A>");
    }

    //Extract
    @Test
    public void should_extract_simple_attribute() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A name=${Name}>Hello</A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A name=\"Atmaram\">Hello</A>"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
    }

    @Test
    public void should_extract_multiple_attributes() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A name=${Name} place=${Place}>Hello</A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A name=\"Atmaram\" place=\"Pune\">Hello</A>"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
        assertThat(data.containsKey("Place"));
        assertThat(data.get("Place")).isEqualTo("Pune");
    }
    @Test
    public void should_extract_simple_text_child() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A>${Name}</A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A>Atmaram</A>"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
    }
    @Test
    public void should_extract_attribute_with_simple_text_child() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A name=${Name}>${Place}</A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A name=\"Atmaram\">Pune</A>"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
        assertThat(data.containsKey("Place"));
        assertThat(data.get("Place")).isEqualTo("Pune");
    }
    @Test
    public void should_extract_simple_node_child() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A><B>${Name}</B></A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A><B>Atmaram</B></A>"));
        assertThat(data.containsKey("Name"));
        assertThat(data.get("Name")).isEqualTo("Atmaram");
    }
    @Test
    public void should_extract_loop_child() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A>{{#Bees}}<B>${Name}</B>{{/Bees}}</A>");
        HashMap<String,Object> data=xmlTemplate.extract(XMLTemplate.StringDocToElement("<A><B>Atmaram</B><B>Roopa</B></A>"));
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
    public void should_extract_static_node_list_child() throws TemplateParseException, IOException, SAXException, ParserConfigurationException {
        XMLTemplate xmlTemplate=XMLTemplate.parse("<A>{{#Bees}}<B place=${Place}>${Name}</B>{{/Bees}}</A>");
        HashMap<String,Object> data=new HashMap<>();
        HashMap<String,Object> map1=new HashMap<>();
        map1.put("Name","Atmaram");
        HashMap<String,Object> map2=new HashMap<>();
        map2.put("Name","Roopa");
        List<HashMap> expected=Arrays.asList(map1,map2);
        data.put("Bees",expected);
        XMLTemplate filled=(XMLTemplate) xmlTemplate.fill(data);
        HashMap<String,Object> eData=filled.extract(XMLTemplate.StringDocToElement("<A><B place=\"Pune\">Atmaram</B><B place=\"Mumbai\">Roopa</B></A>"));
        assertThat(eData.containsKey("Place"));
        assertThat(eData.get("Place")).isEqualTo("Mumbai");

    }

    //Get Variables
    @Test
    public void should_get_variables_from_node_template_in_attributes() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A name=${Name}>Hello</A>");
        List<Variable> vars=nodeTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    @Test
    public void should_get_variables_from_node_template_in_members() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A>${Name}</A>");
        List<Variable> vars=nodeTemplate.getVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    //Get Template Variables
    @Test
    public void should_get_tempate_variables_from_node_template_in_attributes() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A name=#{Name}>Hello</A>");
        List<Variable> vars=nodeTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
    @Test
    public void should_get_template_variables_from_node_template_in_members() throws TemplateParseException {
        NodeTemplate nodeTemplate=(NodeTemplate) XMLTemplate.parse("<A>#{Name}</A>");
        List<Variable> vars=nodeTemplate.getTemplateVariables();
        assertThat(vars.size()).isEqualTo(1);
    }
}
