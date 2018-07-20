package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import com.atmaram.tp.text.TextTemplate;
import com.mashape.unirest.http.HttpResponse;
import lombok.Data;
import org.json.simple.JSONAware;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Data
public abstract class RestUnit extends Unit{
    String urlTemplate;
    String responseTemplate;
    List<RequestHeader> requestHeaders;
    List<ResponseHeader> responseHeaders;
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        if (requestHeaders != null) {
            List<Variable> requestHeaderVariables = new ArrayList<>();

            for (int j = 0; j < requestHeaders.size(); j++) {
                RequestHeader requestHeader = requestHeaders.get(j);
                TextTemplate textTemplate = null;
                try {
                    textTemplate = TextTemplate.parse(requestHeader.getValueTemplate());
                } catch (TemplateParseException e) {
                    throw new UnitConfigurationException("Invalid Template in request header: "+requestHeader.getName(),this.name,e);
                }
                requestHeaderVariables.addAll(textTemplate.getVariables());
            }

            variableStore.add(requestHeaderVariables);
        }

        List<Variable> urlVariables = null;
        try {
            urlVariables = TextTemplate.parse(urlTemplate).getVariables();
        } catch (TemplateParseException e) {
            throw new UnitConfigurationException("Invalid Template in url: "+urlTemplate,this.name,e);
        }
        variableStore.add(urlVariables);

        if (responseHeaders != null) {
            List<Variable> responseHeaderVariables = new ArrayList<>();
            for (int j = 0; j < responseHeaders.size(); j++) {
                ResponseHeader responseHeader = responseHeaders.get(j);
                Variable variable = new Variable();
                variable.setName(responseHeader.getVariable());
                variable.setType("String");
                responseHeaderVariables.add(variable);
            }
            variableStore.resolve(responseHeaderVariables);
        }
        if(!responseTemplate.trim().equals(""))
        {
            List<Variable> outputVariables = null;
            try {
                outputVariables = JSONTemplate.parse(responseTemplate).getVariables();
            } catch (TemplateParseException e) {
                throw new UnitConfigurationException("Invalid response: Template"+responseTemplate,this.name,e);
            }
            variableStore.resolve(outputVariables);
        }
    }

    protected RestUnit fillObject(RestUnit restUnit,ValueStore valueStore) {
        String url = urlTemplate;

        try {
            restUnit.urlTemplate = TextTemplate.parse(url).fill(valueStore.getValues());
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
        List<RequestHeader> filledRequestHeaders=new ArrayList<>();
        if (requestHeaders != null) {
            for (int j = 0; j < requestHeaders.size(); j++) {
                RequestHeader requestHeader = requestHeaders.get(j);
                RequestHeader filledRequestHeader=new RequestHeader();
                filledRequestHeader.setName(requestHeader.getName());
                try {
                    filledRequestHeader.setValueTemplate(TextTemplate.parse(requestHeader.getValueTemplate()).fill(valueStore.getValues()));
                } catch (TemplateParseException e) {
                    e.printStackTrace();
                }
                filledRequestHeaders.add(filledRequestHeader);
            }
        }
        restUnit.setRequestHeaders(filledRequestHeaders);
        restUnit.setResponseTemplate(responseTemplate);
        restUnit.setResponseHeaders(responseHeaders);
        return restUnit;
    }

    @Override
    public ValueStore execute(RestClient restClient, ValueStore valueStore) {
        HttpResponse<String> output=fire(restClient);
        return processOutput(valueStore,output);
    }
    public abstract HttpResponse<String>  fire(RestClient restClient);
    public ValueStore processOutput(ValueStore valueStore,HttpResponse<String> output){
        if (responseHeaders != null) {
            for (int j = 0; j < responseHeaders.size(); j++) {
                ResponseHeader responseHeader = responseHeaders.get(j);
                valueStore.add(responseHeader.getVariable(), output.getHeaders().getFirst(responseHeader.getName()));
            }
        }
        if(!responseTemplate.trim().equals("")) {
            HashMap<String, Object> extractedValues = null;
            try {
                extractedValues = JSONTemplate.parse(responseTemplate).extract((JSONAware) (new JSONParser()).parse(output.getBody()));
            } catch (TemplateParseException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            valueStore.add(extractedValues);
        }
        if(wait!=0){
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return valueStore;
    }
}
