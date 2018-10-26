package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.util.JSONTemplateParsingUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;

public interface JSONTemplate {
    public List<Variable> getVariables();
    public List<Variable> getTemplateVariables();
    public JSONTemplate fillTemplateVariables(HashMap<String,Object> data);
    public JSONTemplate fill(HashMap<String,Object> data);
    public HashMap<String,Object> extract(Object from);
    public Object toJSONCompatibleObject();
    public static JSONTemplate from(Object jsonTemplate) {
        if(jsonTemplate instanceof JSONObject){
           return new ObjectTemplate((JSONObject)jsonTemplate);
        } else if(jsonTemplate instanceof JSONArray){
            JSONArray array=(JSONArray) jsonTemplate;
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
            if(isVariable((String)jsonTemplate)){
                return new VariableTemplate(getVariableName((String)jsonTemplate));
            } else if(isTemplateVariable((String)jsonTemplate)){
                return new TemplateVariableTemplate(getVariableName((String)jsonTemplate));
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
    public static boolean isVariable(String strValue){
        return (strValue.startsWith("${") && strValue.endsWith("}"));
    }
    public static boolean isTemplateVariable(String strValue){
        return (strValue.startsWith("#{") && strValue.endsWith("}"));
    }
    public static String getVariableName(String strValue){
        return strValue.substring(2,strValue.length()-1);
    }
    public static JSONTemplate parse(String template) throws TemplateParseException {
        if(isVariable(template)){
            return new VariableTemplate(getVariableName(template));
        }
        if(isTemplateVariable(template)){
            return new TemplateVariableTemplate(getVariableName(template));
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
}
