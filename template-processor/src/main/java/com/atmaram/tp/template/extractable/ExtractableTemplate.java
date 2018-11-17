package com.atmaram.tp.template.extractable;

import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.extractable.xml.XMLTemplate;

import java.util.HashMap;
import java.util.List;

public interface ExtractableTemplate extends Template {
    public HashMap<String,Object> extract(Object from);
    public List<Variable> getTemplateVariables();
    public static ExtractableTemplate parse(String template) throws TemplateParseException {
        String trimmed=template.trim();
        if(trimmed.startsWith("<")){
            XMLTemplate xmlTemplate=XMLTemplate.parse(trimmed);
            return xmlTemplate;
        } else {
            JSONTemplate jsonTemplate = JSONTemplate.parse(trimmed);
            return jsonTemplate;
        }
    }
    public ExtractableTemplate fillTemplateVariables(HashMap<String, Object> data);
}
