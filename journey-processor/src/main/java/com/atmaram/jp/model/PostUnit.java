package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;

import java.util.List;

@Data
public class PostUnit extends RestUnit {
    String requestTemplate;

    public PostUnit() {
    }

    @Override
    public Unit fill(ValueStore valueStore) {
        PostUnit postUnit=new PostUnit();
        fillObject(postUnit,valueStore);
        String body = requestTemplate;
        try {
            postUnit.requestTemplate = JSONTemplate.parse(body).fill(valueStore.getValues()).toJSONString();
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
        return postUnit;
    }

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        super.eval(variableStore);
        List<Variable> bodyVariables = null;
        try {
            bodyVariables = JSONTemplate.parse(requestTemplate).getVariables();
        } catch (TemplateParseException e) {
            throw new UnitConfigurationException("Invalid Template in request body: "+this.getName(),this.name,e);
        }
        variableStore.add(bodyVariables);
    }

    @Override
    public HttpResponse<String> fire(RestClient restClient){
        HttpResponse<String> output = null;
        try {
                output = restClient.post(urlTemplate, requestHeaders, requestTemplate);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return output;
    }
}
