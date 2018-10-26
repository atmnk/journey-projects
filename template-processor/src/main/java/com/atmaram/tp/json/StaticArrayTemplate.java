package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class StaticArrayTemplate implements JSONTemplate {
    List<JSONTemplate> memberTemplates=new ArrayList<>();

    public StaticArrayTemplate() {
    }

    public void add(JSONTemplate memberTemplate){
        memberTemplates.add(memberTemplate);
    }
    @Override
    public List<Variable> getVariables() {
        List<Variable> variables=new ArrayList<>();
        for (JSONTemplate memberTemplate:
             memberTemplates) {
            variables.addAll(memberTemplate.getVariables());
        }
        return variables;
    }

    @Override
    public List<Variable> getTemplateVariables() {
        List<Variable> variables=new ArrayList<>();
        for (JSONTemplate memberTemplate:
                memberTemplates) {
            variables.addAll(memberTemplate.getTemplateVariables());
        }
        return variables;
    }

    @Override
    public JSONTemplate fillTemplateVariables(HashMap<String, Object> data) {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        for (JSONTemplate memberTemplate:
                memberTemplates) {
            staticArrayTemplate.add(memberTemplate.fillTemplateVariables(data));
        }
        return staticArrayTemplate;
    }

    @Override
    public JSONTemplate fill(HashMap<String, Object> data) {
        StaticArrayTemplate staticArrayTemplate=new StaticArrayTemplate();
        for (JSONTemplate memberTemplate:
                memberTemplates) {
            staticArrayTemplate.add(memberTemplate.fill(data));
        }
        return staticArrayTemplate;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        HashMap<String,Object> retData=new HashMap<>();
        JSONArray jsonResult=(JSONArray)from;
        for(int i=0;i<memberTemplates.size();i++){
            JSONTemplate oValue=memberTemplates.get(i);
            retData.putAll(oValue.extract(jsonResult.get(i)));
        }
        return retData;
    }

    @Override
    public Object toJSONCompatibleObject() {
        JSONArray jsonArray=new JSONArray();
        for (JSONTemplate memberTemplate:
             memberTemplates) {
            jsonArray.add(memberTemplate.toJSONCompatibleObject());
        }
        return jsonArray;
    }
}
