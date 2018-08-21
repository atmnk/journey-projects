package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class LoopTemplate implements JSONTemplate {
    String variableName;
    JSONTemplate innerObjectTemplate;

    public LoopTemplate(String variableName, JSONTemplate innerObjectTemplate) {
        this.variableName = variableName;
        this.innerObjectTemplate = innerObjectTemplate;
    }

    @Override
    public List<Variable> getVariables() {
        List<Variable> returnValue=new ArrayList<>();
        Variable variable=new Variable();
        variable.setName(variableName);
        variable.setType("List");
        List<Variable> inner_variables=innerObjectTemplate.getVariables();
        Variable this_variable=new Variable();
        this_variable.setName("_this");
        this_variable.setType("String");
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

    @Override
    public JSONTemplate fill(HashMap<String, Object> data) {
        if(data.containsKey(variableName)){
            StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
            HashMap<String,Object> localVariables;
            if(data.get(variableName) instanceof List){
                List<Object> filling_list=(List<Object>)data.get(variableName);
                for (Object list_object:
                 filling_list) {
                    if(list_object instanceof HashMap) {
                        localVariables = (HashMap<String, Object>) list_object;
                    }else{
                        localVariables=new HashMap<>();
                        localVariables.put("_this",list_object);
                    }
                    staticArrayTemplate.add(innerObjectTemplate.fill(localVariables).fill(data));
                }
                return staticArrayTemplate;
            } else {
                return new LoopTemplate(variableName,innerObjectTemplate.fill(data));
            }
        } else {
            return new LoopTemplate(variableName,innerObjectTemplate.fill(data));
        }
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        HashMap<String,Object> retData=new HashMap<>();
        List lst=new ArrayList();
        JSONArray resultArray=(JSONArray)from;
        for (Object oValue:
                resultArray) {
            HashMap<String,Object> memberExtractedData=innerObjectTemplate.extract(oValue);
            if(memberExtractedData.containsKey("_this") && memberExtractedData.keySet().size()==1){
                lst.add(memberExtractedData.get("_this"));
            } else {
                lst.add(memberExtractedData);
            }
        }
        retData.put(variableName,lst);
        return retData;
    }

    @Override
    public Object toJSONCompatibleObject() {
        JSONArray array=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("variable",variableName);
        jsonObject.put("template",innerObjectTemplate.toJSONCompatibleObject());
        array.add(jsonObject);
        return array;
    }
}
