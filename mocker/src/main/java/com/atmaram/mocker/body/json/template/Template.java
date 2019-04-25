package com.atmaram.mocker.body.json.template;

import com.atmaram.mocker.data.Data;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.List;

public abstract class Template {
    String template;
    String variable=null;

    public String getVariable() {
        return variable;
    }

    protected Template(String template) {
        this.template = template;
    }
    public abstract String toRegex();
    public abstract Object toValue(DataPool dataPool);
    public Object toValue(DataPool dataPool, List<Tag> tagList) {
        if(variable!=null){
            Data data=new Data(variable,toValue(dataPool),tagList);
            dataPool.add(data,true);
            return data.getValue();
        } else {
            return toValue(dataPool);
        }

    }
}
