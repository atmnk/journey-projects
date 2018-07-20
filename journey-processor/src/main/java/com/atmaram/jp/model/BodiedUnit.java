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
public abstract class BodiedUnit extends RestUnit {
    String requestTemplate;

    public BodiedUnit() {
    }

    public Unit fillObject(BodiedUnit bodiedUnit,ValueStore valueStore){
        fillObject(bodiedUnit,valueStore);
        String body = requestTemplate;
        try {
            bodiedUnit.requestTemplate = JSONTemplate.parse(body).fill(valueStore.getValues()).toJSONString();
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
        return bodiedUnit;
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
}
