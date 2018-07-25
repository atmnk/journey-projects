package com.atmaram.tp.json.old;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class JSONLoop implements JSONAware {
    public JSONAware inner_object;
    public String variable;

    @Override
    public String toJSONString() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("variable",variable);
        jsonObject.put("template",inner_object);
        return jsonObject.toJSONString();
    }
}
