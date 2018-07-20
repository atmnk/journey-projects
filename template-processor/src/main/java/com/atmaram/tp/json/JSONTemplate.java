package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.VariableValueProcessor;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.util.JSONTemplateParsingUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONTemplate {
    private JSONAware jsonTemplate;

    public JSONTemplate(JSONAware jsonTemplate){
        this.jsonTemplate=jsonTemplate;
    }
    private JSONTemplate(){

    }

    public static JSONTemplate parse(String template) throws TemplateParseException{
        JSONParser jsonParser=new JSONParser();
        template= JSONTemplateParsingUtil.replaceVariablesWithQuotedVariables(template);
        template= JSONTemplateParsingUtil.replaceLoopsWithTransformedJSON(template);
        try {
            JSONAware jsonTemplate=(JSONAware) jsonParser.parse(template);
            JSONAware transformed_object=transform(jsonTemplate);
            return new JSONTemplate(transformed_object);
        } catch (ParseException ex){
            throw new TemplateParseException("Provided template is not valid json: "+template);
        }
    }
    public JSONAware fill(HashMap<String,Object> data) {
        if(jsonTemplate instanceof JSONLoop){
            return fillJSONLoop(data);
        } else if(jsonTemplate instanceof JSONObject){
            return fillJSONObject(data);
        } else if(jsonTemplate instanceof JSONArray){
            return fillJSONArray(data);
        }
        return null;
    }
    private static JSONObject transformJSONObject(JSONObject object){
        JSONObject ojoObject = new JSONObject();
        for (Object key :
                object.keySet()) {
            Object value = object.get(key);
            if (value instanceof JSONAware) {
                ojoObject.put(key,transform((JSONAware)value));

            } else {
                ojoObject.put(key,value);
            }
        }
        return ojoObject;
    }
    private static JSONAware transformJSONArray(JSONArray array){
        Object inner_object=array.get(0);
        if(inner_object instanceof JSONObject){
            JSONObject inner_joobject_template = (JSONObject) inner_object;
            if (inner_joobject_template.containsKey("variable") && inner_joobject_template.containsKey("template")) {
                JSONLoop loopObject = new JSONLoop();
                loopObject.variable = (String) inner_joobject_template.get("variable");
                loopObject.inner_object = transform((JSONAware) inner_joobject_template.get("template"));
                return loopObject;
            }

        }
        JSONArray transformed_array=new JSONArray();
        for (Object value :
                array) {
            if (value instanceof JSONAware) {
                transformed_array.add(transform((JSONAware)value));
            } else {
                transformed_array.add(value);
            }
        }
        return transformed_array;
    }
    private static JSONAware transform(JSONAware object) {
        if(object instanceof JSONObject){
            return transformJSONObject((JSONObject)object);
        } else if(object instanceof JSONArray){
            return transformJSONArray((JSONArray) object);
        }
        return null;
    }
    private HashMap<String,Object> extractJSONArray(JSONArray jsonResult,HashMap<String,Object> retData){
        JSONArray templateArray=(JSONArray) jsonTemplate;
        for(int i=0;i<templateArray.size();i++){
            Object oValue=templateArray.get(i);
            if(oValue instanceof JSONAware){
                JSONAware arrayElementJSONResult=(JSONAware)jsonResult.get(i);
                JSONTemplate jsonTemplate =new JSONTemplate();
                jsonTemplate.jsonTemplate=(JSONAware) oValue;
                retData.putAll(jsonTemplate.extract(arrayElementJSONResult));
            } else if(oValue instanceof String){
                if(isVariable((String)oValue)){
                    String newVariableName=getVariableName((String)oValue);
                    retData.put(newVariableName,jsonResult.get(i));
                }
            }
        }
        return retData;
    }
    private HashMap<String,Object> extractJSONLoop(JSONArray jsonResult,HashMap<String,Object> retData){
        JSONLoop jlValue=(JSONLoop)jsonTemplate;
        String variableName=jlValue.variable;
        List lst=new ArrayList();
        JSONArray resultArray=jsonResult;
        for (Object oValue:
                resultArray) {
            if(oValue instanceof JSONAware){
                JSONObject arrayElementJSONResult=(JSONObject)oValue;
                JSONTemplate jsonTemplate =new JSONTemplate();
                jsonTemplate.jsonTemplate=jlValue.inner_object;
                lst.add(jsonTemplate.extract(arrayElementJSONResult));
            } else if(oValue instanceof String){
                if(isVariable((String)oValue)){
                    String newVariableName=getVariableName((String)oValue);
                    retData.put(newVariableName,jsonResult);
                }
            }
        }
        retData.put(variableName,lst);
        return retData;
    }
    private HashMap<String,Object> extractJSONObject(JSONObject jsonResult,HashMap<String,Object> retData){

        for (Object key:
                ((JSONObject)jsonTemplate).keySet()) {
            Object oValue=((JSONObject)jsonTemplate).get(key);

            if(oValue instanceof String){
                if(isVariable((String)oValue)){
                    String variableName=getVariableName((String)oValue);
                    retData.put(variableName,jsonResult.get(key));
                }
            } else if(oValue instanceof JSONAware){
                JSONTemplate jsonTemplate=new JSONTemplate((JSONAware)oValue);
                if(oValue instanceof JSONLoop){
                    retData.putAll(jsonTemplate.extract((JSONAware)jsonResult.get(key)));
                } else if(oValue instanceof JSONObject){
                    retData.putAll(jsonTemplate.extract((JSONObject)jsonResult.get(key)));
                } else if(oValue instanceof JSONArray){
                    retData.putAll(jsonTemplate.extract((JSONArray)jsonResult.get(key)));
                }
            }
        }
        return retData;
    }
    public HashMap<String,Object> extract(JSONAware jsonResult){
        HashMap<String,Object> retData=new HashMap<>();
        if(jsonTemplate instanceof JSONObject){
            extractJSONObject((JSONObject) jsonResult,retData);
        } else if(jsonTemplate instanceof JSONLoop){
            extractJSONLoop((JSONArray) jsonResult,retData);
        } else if(jsonTemplate instanceof JSONArray){
            extractJSONArray((JSONArray)jsonResult,retData);
        }
        return retData;
    }
    private boolean isVariable(String strValue){
        return (strValue.startsWith("${") && strValue.endsWith("}"));
    }
    private String getVariableName(String strValue){
        return strValue.substring(2,strValue.length()-1);
    }
    public List<Variable> getVariables(){
        Object oValue=jsonTemplate;
        List<Variable> returnValue=new ArrayList<>();
        if(oValue instanceof String){
            if(isVariable((String)oValue)){
                String variableName=getVariableName((String)oValue);
                if(variableName.startsWith("_") && !variableName.equals("_this")){
                    //do nothing;
                } else{
                    Variable variable=new Variable();
                    variable.setName(variableName);
                    variable.setType("String");
                    returnValue.add(variable);
                }
            }
        } else if(oValue instanceof JSONAware){
            JSONTemplate inner_jt=new JSONTemplate((JSONAware) oValue);
            if(oValue instanceof JSONObject){
                returnValue.addAll(inner_jt.getVariablesFromJSONObject());
            } else if(oValue instanceof JSONArray){
                returnValue.addAll(inner_jt.getVariablesFromJSONArray());
            } else if(oValue instanceof JSONLoop){
                returnValue.addAll(inner_jt.getVariablesFromJSONLoop());
            }
        }
        return returnValue;
    }

    private List<Variable> getVariablesFromJSONLoop(){
        List<Variable> returnValue=new ArrayList<>();
        JSONLoop jlValue=(JSONLoop)jsonTemplate;
        Variable variable=new Variable();
        variable.setName(jlValue.variable);
        variable.setType("List");
        JSONTemplate inner_jt=new JSONTemplate(jlValue.inner_object);
        List<Variable> inner_variables=inner_jt.getVariables();
        List<Variable> inner_variables_excluding_this=new ArrayList<>();
        boolean found_this_variable=false;
        for (Variable inner_variable:inner_variables
                ) {
            if(inner_variable.getName().equals("_this")){
                found_this_variable=true;
            } else {
                inner_variables_excluding_this.add(inner_variable);
            }
        }
        if(found_this_variable){
            returnValue.add(variable);
            returnValue.addAll(inner_variables_excluding_this);
        } else {
            variable.setInner_variables(inner_variables_excluding_this);
            returnValue.add(variable);
        }
        return returnValue;

    }
    private List<Variable> getVariablesFromJSONObject(){
        List<Variable> returnValue=new ArrayList<>();
        for (Object key:
                ((JSONObject)jsonTemplate).keySet()) {
            Object oValue=((JSONObject)jsonTemplate).get(key);

            if(oValue instanceof String){
                if(isVariable((String)oValue)){
                    String variableName=getVariableName((String)oValue);
                    if(variableName.startsWith("_") && !variableName.equals("_this")){
                        //do nothing;
                    } else{
                        Variable variable=new Variable();
                        variable.setName(variableName);
                        variable.setType("String");
                        returnValue.add(variable);
                    }
                }
            } else if(oValue instanceof JSONAware){
                JSONTemplate inner_jt=new JSONTemplate((JSONAware) oValue);
                returnValue.addAll(inner_jt.getVariables());
            }
        }
        return returnValue;
    }
    private List<Variable> getVariablesFromJSONArray(){
        JSONArray array=(JSONArray)jsonTemplate;
        List<Variable> returnValue=new ArrayList<>();
        for(int i=0;i<array.size();i++){
            Object oValue=array.get(i);
            if(oValue instanceof String){
                if(isVariable((String)oValue)){
                    String variableName=getVariableName((String)oValue);
                    if(variableName.startsWith("_") && !variableName.equals("_this")){
                        //do nothing;
                    } else{
                        Variable variable=new Variable();
                        variable.setName(variableName);
                        variable.setType("String");
                        returnValue.add(variable);
                    }
                }
            } else if(oValue instanceof JSONAware){
                JSONTemplate inner_jt=new JSONTemplate((JSONAware) oValue);
                returnValue.addAll(inner_jt.getVariables());
            }
        }
        return returnValue;
    }
    private JSONArray fillJSONArray(HashMap<String,Object> data) {
        JSONArray jaValue=(JSONArray)jsonTemplate;
        JSONArray filled_array=new JSONArray();
        for(int i=0;i<jaValue.size();i++){
            Object arrayObject=jaValue.get(i);
            if(arrayObject instanceof JSONAware){
                JSONTemplate jsonTemplate =new JSONTemplate((JSONAware) arrayObject);
                filled_array.add(jsonTemplate.fill(data));
            } else if(arrayObject instanceof String){
                if(((String)arrayObject).startsWith("${") && ((String)arrayObject).endsWith("}")){
                    String variableName=((String)arrayObject).substring(2,((String)arrayObject).length()-1);
                    filled_array.add(VariableValueProcessor.getValue(variableName,data));
                } else {
                    filled_array.add(arrayObject);
                }
            } else {
                filled_array.add(arrayObject);
            }

        }
        return filled_array;
    }
    private JSONObject fillJSONObject(HashMap<String,Object> data) {
        JSONObject ojoObject=new JSONObject();
        JSONObject joObject=(JSONObject) this.jsonTemplate;

        for (Object key:
                joObject.keySet()) {
            Object value=joObject.get(key);
            if(value instanceof String){
                if(((String)value).startsWith("${") && ((String)value).endsWith("}")){
                    String variableName=((String)value).substring(2,((String)value).length()-1);
                    ojoObject.put(key, VariableValueProcessor.getValue(variableName,data));
                } else {
                    ojoObject.put(key,value);
                }
            } else if(value instanceof JSONLoop){
                JSONTemplate loopJSONTemplate=new JSONTemplate((JSONLoop)value);
                JSONAware arrayOutput=loopJSONTemplate.fillJSONLoop(data);
                ojoObject.put(key,arrayOutput);
            }
            else if (value instanceof JSONObject){
                JSONTemplate jsonTemplate =new JSONTemplate((JSONObject) value);
                ojoObject.put(key, jsonTemplate.fill(data));
            } else if (value instanceof JSONArray){
                JSONTemplate jsonTemplate =new JSONTemplate((JSONArray) value);
                ojoObject.put(key, jsonTemplate.fill(data));
            } else {
                ojoObject.put(key,value);
            }
        }

        return ojoObject;
    }
    private JSONAware fillJSONLoop(HashMap<String,Object> data) {
        JSONLoop value=(JSONLoop) jsonTemplate;
        String loopVariable=value.variable;
        Object varVal=VariableValueProcessor.getValue(loopVariable,data);
        if(varVal instanceof List){
            List<Object> dataArray=(List<Object>)VariableValueProcessor.getValue(loopVariable,data);
            JSONArray outputArray=new JSONArray();
            JSONTemplate jsonTemplate =new JSONTemplate(value.inner_object);
            for(int i=0;i<dataArray.size();i++){
                Object dataObject=dataArray.get(i);
                JSONAware outputObject;
                if(dataObject instanceof HashMap){
                    outputObject= jsonTemplate.fill((HashMap<String,Object>)dataObject);
                } else {
                    HashMap<String,Object> innerData=new HashMap<>();
                    innerData.put("_this",dataObject);
                    outputObject= jsonTemplate.fill(innerData);
                }
                JSONTemplate newJSONTemplate =new JSONTemplate(outputObject);
                JSONAware newOutputObject= newJSONTemplate.fill(data);
                outputArray.add(newOutputObject);
            }
            return outputArray;
        } else {
            JSONTemplate jsonTemplate =new JSONTemplate(value.inner_object);
            JSONAware newInnerTemplate= jsonTemplate.fill(data);
            JSONLoop jsonLoop=new JSONLoop();
            jsonLoop.inner_object=newInnerTemplate;
            jsonLoop.variable=value.variable;
            return jsonLoop;
        }

    }
}
