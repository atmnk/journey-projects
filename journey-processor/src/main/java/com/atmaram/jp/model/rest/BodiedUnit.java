package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.template.TemplateType;
import com.atmaram.tp.template.extractable.ExtractableTemplate;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.text.SingleVariableStringTemplate;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.extractable.text.SingleVariableTemplate;
import com.atmaram.tp.template.extractable.xml.XMLTemplate;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;

import java.util.List;

@Data
public abstract class BodiedUnit extends RestUnit {
    String requestTemplate="";
    TemplateType requestTemplateType=TemplateType.Extractable;

    public BodiedUnit(RestClient restClient) {
        super(restClient);
    }

    @Override
    public void printException(int index){
        super.printException(index);
        this.print(index,"Body: "+this.requestTemplate);
    }

    public Unit fillObject(BodiedUnit bodiedUnit, ValueStore valueStore) {
        fillObject((RestUnit) bodiedUnit, valueStore);
        String body = requestTemplate;
        try {
            if (requestTemplateType.equals(TemplateType.Extractable)){
                ExtractableTemplate rTemplate = (ExtractableTemplate) ExtractableTemplate.parse(body).fill(valueStore.getValues());
                bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
            } else if(requestTemplateType.equals(TemplateType.Json)){
                JSONTemplate rTemplate = (JSONTemplate) JSONTemplate.parse(body).fill(valueStore.getValues());
                bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
            } else if(requestTemplateType.equals(TemplateType.XML)){
                XMLTemplate rTemplate = (XMLTemplate) XMLTemplate.parse(body).fill(valueStore.getValues());
                bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
            } else if(requestTemplateType.equals(TemplateType.SingleVariableText)){
                SingleVariableTemplate rTemplate = (SingleVariableTemplate) SingleVariableTemplate.parse(body).fill(valueStore.getValues());
                bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
            } else if(requestTemplateType.equals(TemplateType.Text)){
                TextTemplate rTemplate = (TextTemplate) TextTemplate.parse(body).fill(valueStore.getValues());
                bodiedUnit.requestTemplate = rTemplate.toStringTemplate();
            }
            bodiedUnit.requestTemplateType=requestTemplateType;
        } catch (TemplateParseException ex){
            ex.printStackTrace();
            System.out.println(body);
        }

        return bodiedUnit;
    }

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        super.eval(variableStore);
        List<Variable> bodyVariables = null;
        try {
            if (requestTemplateType.equals(TemplateType.Extractable)){
                ExtractableTemplate template=ExtractableTemplate.parse(requestTemplate);
                bodyVariables = template.getVariables();
                bodyVariables.addAll(template.getTemplateVariables());
            } else if(requestTemplateType.equals(TemplateType.Json)){
                JSONTemplate template=JSONTemplate.parse(requestTemplate);
                bodyVariables = template.getVariables();
                bodyVariables.addAll(template.getTemplateVariables());
            } else if(requestTemplateType.equals(TemplateType.XML)){
                XMLTemplate template=XMLTemplate.parse(requestTemplate);
                bodyVariables = template.getVariables();
                bodyVariables.addAll(template.getTemplateVariables());
            } else if(requestTemplateType.equals(TemplateType.SingleVariableText)){
                SingleVariableTemplate template=SingleVariableTemplate.parse(requestTemplate);
                bodyVariables = template.getVariables();
            } else if(requestTemplateType.equals(TemplateType.Text)){
                TextTemplate template=TextTemplate.parse(requestTemplate);
                bodyVariables = template.getVariables();
            }
        } catch (TemplateParseException ex){
            throw new UnitConfigurationException("Invalid Template in request body: "+this.getName(),this.name,ex);
        }
        variableStore.add(bodyVariables);
    }
}
