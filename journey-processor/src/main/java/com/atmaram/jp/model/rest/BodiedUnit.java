package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.ExtractableTemplate;
import com.atmaram.tp.Template;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import lombok.Data;
import org.json.simple.JSONAware;

import java.util.List;

@Data
public abstract class BodiedUnit extends RestUnit {
    String requestTemplate;

    public BodiedUnit(RestClient restClient) {
        super(restClient);
    }

    @Override
    public void printException(int index){
        super.printException(index);
        this.print(index,"Body: "+this.requestTemplate);
    }

    public Unit fillObject(BodiedUnit bodiedUnit, ValueStore valueStore){
        fillObject((RestUnit) bodiedUnit,valueStore);
        String body = requestTemplate;
        try {
            Template rTemplate=ExtractableTemplate.parse(body).fill(valueStore.getValues());
            bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
        } catch (TemplateParseException e) {
            e.printStackTrace();
            System.out.println(body);
        }
        return bodiedUnit;
    }

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        super.eval(variableStore);
        List<Variable> bodyVariables = null;
        try {
            bodyVariables = ExtractableTemplate.parse(requestTemplate).getVariables();
        } catch (TemplateParseException e) {
            throw new UnitConfigurationException("Invalid Template in request body: "+this.getName(),this.name,e);
        }
        variableStore.add(bodyVariables);
    }
}
