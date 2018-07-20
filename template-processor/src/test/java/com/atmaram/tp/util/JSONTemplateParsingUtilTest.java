package com.atmaram.tp.util;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
public class JSONTemplateParsingUtilTest {
    @Test
    public void should_work_with_quoted_variables() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceVariablesWithQuotedVariables("{\"name\":\"${name}\"}");
        assertThat(results).isEqualTo("{\"name\":\"${name}\"}");
    }
    @Test
    public void should_replace_loop_variables() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON("{\"name\":[{{#items}}{\"name\":\"Atmaram\",\"place\":\"India\"}{{/items}}]}");
        assertThat(results).isEqualTo("{\"name\":[{\"template\":{\"name\":\"Atmaram\",\"place\":\"India\"},\"variable\":\"items\"}]}");
    }
    @Test
    public void should_replace_nested_loop_variables() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON("{\"name\":[{{#items}}{\"name\":[{{#names}}{\"inner\":\"inner_value\"}{{/names}}],\"place\":\"India\"}{{/items}}]}");
        assertThat(results).isEqualTo("{\"name\":[{\"template\":{\"name\":[{\"template\":{\"inner\":\"inner_value\"},\"variable\":\"names\"}],\"place\":\"India\"},\"variable\":\"items\"}]}");
    }
    @Test
    public void should_replace_same_variable_loop_apearing_twise() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON("{\"name\":[{{#items}}{\"name\":\"Atmaram\",\"place\":\"India\"}{{/items}}],\"place\":[{{#items}}{\"name\":\"Atmaram\",\"place\":\"India\"}{{/items}}]}");
        assertThat(results).isEqualTo("{\"name\":[{\"template\":{\"name\":\"Atmaram\",\"place\":\"India\"},\"variable\":\"items\"}],\"place\":[{\"template\":{\"name\":\"Atmaram\",\"place\":\"India\"},\"variable\":\"items\"}]}");
    }
    @Test()
    public void  should_throw_exception_if_nested_same_variable_loop() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON("{\"name\":[{{#items}}{\"name\":[{{#items}}{\"inner\":\"inner_value\"}{{/items}}],\"place\":\"India\"}{{/items}}]}");
        assertThat(results).isEqualTo("{\"name\":[{\"template\":{\"name\":[{\"template\":{\"inner\":\"inner_value\"},\"variable\":\"items\"}],\"place\":\"India\"},\"variable\":\"items\"}]}");
    }
    @Test
    public void should_replace_loops_with_variables() throws TemplateParseException {
        String results=JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON("{\"name\":[{{#items}}{\"name\":\"${name}\",\"place\":\"India\"}{{/items}}]}");
        assertThat(results).isEqualTo("{\"name\":[{\"template\":{\"name\":\"${name}\",\"place\":\"India\"},\"variable\":\"items\"}]}");
    }
}
