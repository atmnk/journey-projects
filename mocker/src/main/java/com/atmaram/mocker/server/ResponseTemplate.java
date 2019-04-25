package com.atmaram.mocker.server;

import com.atmaram.mocker.body.AbstractBuilder;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.List;

public class ResponseTemplate {
    AbstractBuilder body;
    StringTemplate contentType;
    int status;
    public ResponseTemplate() {
    }
    public ResponseTemplate(AbstractBuilder body) {
        this.body = body;
    }
    public String toBody(DataPool dataPool, List<Tag> tags){
        return body.toBody(dataPool,tags);
    }
}
