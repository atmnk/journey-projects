package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.extractable.ExtractableTemplate;
import com.atmaram.tp.template.Template;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.xml.XMLTemplate;
import com.atmaram.tp.util.JSONTemplateParsingUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public interface JSONTemplate extends ExtractableTemplate {
    public Object toJSONCompatibleObject();
    public static JSONTemplate from(Object jsonTemplate) {
        if(jsonTemplate instanceof JSONObject){
           return new ObjectTemplate((JSONObject)jsonTemplate);
        } else if(jsonTemplate instanceof JSONArray){
            JSONArray array=(JSONArray) jsonTemplate;
            if(array.size()==0){
                return new StaticArrayTemplate();
            }
            Object inner_object=array.get(0);
            if(inner_object instanceof JSONObject){
                JSONObject inner_joobject_template = (JSONObject) inner_object;
                if (inner_joobject_template.containsKey("variable") && inner_joobject_template.containsKey("template")) {
                    return new LoopTemplate((String) inner_joobject_template.get("variable"),JSONTemplate.from(inner_joobject_template.get("template")));
                }else{
                    StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
                    for (Object inner_json_object:
                         array) {
                        staticArrayTemplate.add(JSONTemplate.from(inner_json_object));
                    }
                    return staticArrayTemplate;
                }
            } else {
                StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
                for (Object inner_json_object:
                        array) {
                    staticArrayTemplate.add(JSONTemplate.from(inner_json_object));
                }
                return staticArrayTemplate;
            }
        } else if(jsonTemplate instanceof String){
            if(Template.isVariable((String)jsonTemplate)){
                return new VariableTemplate(Template.getVariableName((String)jsonTemplate));
            } else if(Template.isTemplateVariable((String)jsonTemplate)){
                return new TemplateVariableTemplate(Template.getVariableName((String)jsonTemplate));
            }
            else {
                try {
                    Object parsed=new JSONParser().parse((String)jsonTemplate);
                    if(parsed instanceof JSONAware){
                        JSONTemplate.from(parsed);
                    } else {
                        return new FilledVariableTemplate(jsonTemplate);
                    }
                } catch (ParseException e) {
                    return new FilledVariableTemplate(jsonTemplate);
                }
            }
        }
        return new FilledVariableTemplate(jsonTemplate);
    }
    @Override
    public JSONTemplate fill(HashMap<String, Object> data, boolean lazy);
    public default JSONTemplate fill(HashMap<String, Object> data){
        return fill(data,false);
    }
    public static JSONTemplate parse(String template) throws TemplateParseException {
        if(Template.isVariable(template)){
            return new VariableTemplate(Template.getVariableName(template));
        }
        if(Template.isTemplateVariable(template)){
            return new TemplateVariableTemplate(Template.getVariableName(template));
        }
        JSONParser jsonParser=new JSONParser();
        template= JSONTemplateParsingUtil.replaceVariablesWithQuotedVariables(template);
        template= JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON(template);
        try {
            JSONAware jsonTemplate=(JSONAware) jsonParser.parse(template);
            return JSONTemplate.from(jsonTemplate);
        } catch (ParseException ex){
            throw new TemplateParseException("Provided template is not valid json: "+template);
        }
    }
    public static Object stringToJSON(String string) throws ParseException {
        JSONParser parser=new JSONParser();
        return parser.parse(string);
    }
}
