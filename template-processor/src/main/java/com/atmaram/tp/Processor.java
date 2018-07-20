package com.atmaram.tp;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;

import java.util.HashMap;

public class Processor {
    public Object fillJSON(String template,HashMap<String,Object> data) throws TemplateParseException {
        JSONTemplate tJSONTemplate =JSONTemplate.parse(template);
        return tJSONTemplate.fill(data);
    }
}
