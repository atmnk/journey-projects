package com.atmaram.jp;

import org.json.simple.JSONObject;

import java.util.HashMap;

public class ValueStore {
    HashMap<String,Object> values;

    public ValueStore() {
        values=new HashMap<>();
    }
    public void add(HashMap<String,Object> additional){
        values.putAll(additional);
    }
    public void add(String name,Object value){
        values.put(name,value);
    }
    public void remove(String name){
        values.remove(name);
    }
    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }
    @Override
    public String toString()
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("data",values);
        return jsonObject.toJSONString();
    }
    private void addAdditionalKeepingOriginal(HashMap<String,Object> original,HashMap<String,Object> additional){
        if(original!=additional) {
            for (String key :
                    additional.keySet()) {
                if (original.containsKey(key)) {
                    Object newOriginal = values.get(key);
                    Object newAddition = additional.get(key);
                    if (newOriginal != newAddition) {
                        if (newAddition instanceof HashMap && newAddition instanceof HashMap) {
                            addAdditionalKeepingOriginal((HashMap<String, Object>) newOriginal, (HashMap<String, Object>) newAddition);
                        }
                    }
                } else {
                    values.put(key, additional.get(key));
                }
            }
        }
    }
    public void addAdditionalKeepingOriginal(HashMap<String,Object> additional){
        addAdditionalKeepingOriginal(values,additional);
    }
}
