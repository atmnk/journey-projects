package com.atmaram.jp;

import org.json.simple.JSONObject;

import java.util.*;

public class ValueStore {
    HashMap<String,Object> values;
    HashMap<String, Stack<Object>> previousValues;

    public ValueStore() {
        values=new HashMap<>();
        previousValues=new HashMap<>();
    }
    public void add(HashMap<String,Object> additional){
        for (String key:
             additional.keySet()) {
            if(previousValues.containsKey(key)){
                previousValues.get(key).push(values.get(key));
                values.put(key,additional.get(key));
            } else {
                if(values.containsKey(key)){
                    Stack<Object> val=new Stack<>();
                    val.push(values.get(key));
                    previousValues.put(key,val);
                    values.put(key,additional.get(key));
                } else {
                    values.put(key,additional.get(key));
                }
            }
        }
    }
    public void add(String key, Object value){
        if(previousValues.containsKey(key)){
            previousValues.get(key).push(values.get(key));
            values.put(key,value);
        } else {
            if(values.containsKey(key)){
                Stack<Object> val=new Stack<>();
                val.push(values.get(key));
                previousValues.put(key,val);
                values.put(key,value);
            } else {
                values.put(key,value);
            }
        }
    }
    public void remove(String name){
        values.remove(name);
    }
    public void pop(String name){
        if(previousValues.containsKey(name)){
            values.put(name,previousValues.get(name).pop());
            if(previousValues.get(name).empty()){
                previousValues.remove(name);
            }
        } else {
            values.remove(name);
        }
    }
    public void popAll(Set<String> keys){
        for (String key:keys) {
            pop(key);
        }
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
    private List<String> addAdditionalKeepingOriginal(HashMap<String,Object> original,HashMap<String,Object> additional){
        List<String> added=new ArrayList<>();
        if(original!=additional) {
            for (String key :
                    additional.keySet()) {
                if(!original.containsKey(key)) {
                    values.put(key, additional.get(key));
                    added.add(key);
                }

            }
        }
        return added;
    }
    public List<String> addAdditionalKeepingOriginal(HashMap<String,Object> additional){
        return addAdditionalKeepingOriginal(values,additional);
    }
    public void remove(List<String> remove){
        for (String removeKey:
             remove) {
            if(!previousValues.containsKey(removeKey))
                values.remove(removeKey);
        }
    }
}
