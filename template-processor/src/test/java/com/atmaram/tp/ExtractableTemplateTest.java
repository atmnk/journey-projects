package com.atmaram.tp;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import com.atmaram.tp.xml.XMLTemplate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractableTemplateTest {
    @Test
    public void should_parse_xml_as_xml_template() throws TemplateParseException {
        ExtractableTemplate result=ExtractableTemplate.parse("<a>Hello</a>");
        assertThat(result).isInstanceOf(XMLTemplate.class);

    }
    @Test
    public void should_parse_json_as_json_template() throws TemplateParseException {
        ExtractableTemplate result=ExtractableTemplate.parse("{\"name\":\"Atmaram\"}");
        assertThat(result).isInstanceOf(JSONTemplate.class);

    }
    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Test
    public void should_throw_template_parse_exception_for_xml_template() throws TemplateParseException {
        expectedException.expect(TemplateParseException.class);
        expectedException.expectMessage("Provided template is not valid xml: <a>Hello<a>");
        ExtractableTemplate.parse("<a>Hello<a>");
    }
    @Test
    public void should_throw_template_parse_exception_for_json_template() throws TemplateParseException {
        expectedException.expect(TemplateParseException.class);
        expectedException.expectMessage("Provided template is not valid json: {\"aaa:\"Hello\"}");
        ExtractableTemplate.parse("{\"aaa:\"Hello\"}");
    }
}
