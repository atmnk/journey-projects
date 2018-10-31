package com.atmaram.tp;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import com.atmaram.tp.xml.XMLTemplate;

import java.util.HashMap;
import java.util.List;

public interface ExtractableTemplate extends Template {
    public HashMap<String,Object> extract(Object from);
    public List<Variable> getTemplateVariables();
    public static ExtractableTemplate parse(String template) throws TemplateParseException {
        try{
            XMLTemplate xmlTemplate=XMLTemplate.parse(template);
            return xmlTemplate;
        } catch (TemplateParseException ex){
            try {
                JSONTemplate jsonTemplate = JSONTemplate.parse(template);
                return jsonTemplate;
            } catch (TemplateParseException ex1){
                throw new TemplateParseException("Provided template is neither valid xml or json template:"+template);
            }
        }
    }
    public ExtractableTemplate fillTemplateVariables(HashMap<String, Object> data);
}
