package com.atmaram.tp.json;

import com.atmaram.tp.Variable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ObjectTemplate implements JSONTemplate {
    HashMap<JSONTemplate,JSONTemplate> keyValueTemplates=new HashMap<>();
    public void put(JSONTemplate keyTemplate,JSONTemplate valueTemplate){
        keyValueTemplates.put(keyTemplate,valueTemplate);
    }

    protected ObjectTemplate() {
        keyValueTemplates=new HashMap<>();
    }

    public ObjectTemplate(JSONObject jsonTemplate) {
        for (Object key:
             jsonTemplate.keySet()) {
            keyValueTemplates.put(JSONTemplate.from(key),JSONTemplate.from(jsonTemplate.get(key)));
        }
    }

    @Override
    public List<Variable> getVariables() {
        List<Variable> variables=new ArrayList<>();
        for (JSONTemplate keyTemplate:
             keyValueTemplates.keySet()) {
            variables.addAll(keyTemplate.getVariables());
            variables.addAll(keyValueTemplates.get(keyTemplate).getVariables());
        }
        return variables;
    }

    @Override
    public JSONTemplate fill(HashMap<String, Object> data) {
        ObjectTemplate objectTemplate=new ObjectTemplate();
        for (JSONTemplate keyTemplate:
                keyValueTemplates.keySet()) {
            objectTemplate.put(keyTemplate.fill(data),keyValueTemplates.get(keyTemplate).fill(data));
        }
        return objectTemplate;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        HashMap<String,Object> retData=new HashMap<>();
        JSONObject fromJson=(JSONObject) from;
        for (JSONTemplate key:
                keyValueTemplates.keySet()) {
            JSONTemplate oValue=keyValueTemplates.get(key);
            if(fromJson.containsKey(key.toJSONCompatibleObject())) {
                retData.putAll(oValue.extract(fromJson.get(key.toJSONCompatibleObject())));
            }
        }
        return retData;
    }

    @Override
    public Object toJSONCompatibleObject() {
        JSONObject jsonObject=new JSONObject();
        for (JSONTemplate keyTemplate:
                keyValueTemplates.keySet()) {
            jsonObject.put(keyTemplate.toJSONCompatibleObject(),keyValueTemplates.get(keyTemplate).toJSONCompatibleObject());
        }
        return jsonObject;
    }
}
