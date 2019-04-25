package com.atmaram.mocker.body;

import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.List;

public abstract class AbstractBuilder {
    protected String variable=null;
    public abstract String toBody(DataPool dataPool, List<Tag> tags);
    public abstract String toRegex();
    public String getVariable(){
        return variable;
    }
    public abstract void gerpBody(DataPool dataPool, List<Tag> tags,String body) throws Exception;
}
