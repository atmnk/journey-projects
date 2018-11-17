package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.RequestHeader;
import com.atmaram.jp.model.ResponseHeader;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.template.extractable.ExtractableTemplate;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.extractable.xml.XMLTemplate;
import com.atmaram.tp.template.text.TextTemplate;
import com.mashape.unirest.http.HttpResponse;
import lombok.Data;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
@Data
public  abstract class RestUnit extends Unit {
    String urlTemplate;
    String responseTemplate;
    List<RequestHeader> requestHeaders;
    List<ResponseHeader> responseHeaders;
    public RestClient restClient=null;

    public RestUnit(RestClient restClient) {
        this.restClient = restClient;
    }
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
            List<Variable> outputVariables = new ArrayList<>();
            List<Variable> inputVariables = new ArrayList<>();
            try {
                ExtractableTemplate xtResponseTemplate= ExtractableTemplate.parse(responseTemplate);
                inputVariables=xtResponseTemplate.getTemplateVariables();
                outputVariables = xtResponseTemplate.getVariables();
            } catch (TemplateParseException e) {
                throw new UnitConfigurationException("Invalid response: Template" + responseTemplate, this.name, e);
            }
            variableStore.add(inputVariables);
            variableStore.resolve(outputVariables);
        }
    }

    @Override
    public ValueStore execute(ValueStore valueStore, int index) {
        return execute(restClient,valueStore,index);
    }

    protected RestUnit fillObject(RestUnit restUnit,ValueStore valueStore) {
        restUnit.setName(this.getName());
        String url = urlTemplate;

        try {
            restUnit.urlTemplate = TextTemplate.parse(url).fill(valueStore.getValues()).toStringTemplate();
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
                    filledRequestHeader.setValueTemplate(TextTemplate.parse(requestHeader.getValueTemplate()).fill(valueStore.getValues()).toStringTemplate());
                } catch (TemplateParseException e) {
                    e.printStackTrace();
                }
                filledRequestHeaders.add(filledRequestHeader);
            }
        }
        restUnit.setRequestHeaders(filledRequestHeaders);
        try {
            if(responseTemplate!=null && !responseTemplate.trim().equals("")) {
                restUnit.setResponseTemplate(ExtractableTemplate.parse(responseTemplate).fillTemplateVariables(valueStore.getValues()).toStringTemplate());
            } else {
                restUnit.setResponseTemplate("");
            }
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
        restUnit.setResponseHeaders(responseHeaders);
        restUnit.setWait(this.wait);
        return restUnit;
    }
    public ValueStore execute(RestClient restClient, ValueStore valueStore,int index) {
        this.printStartExecute(index);
        HttpResponse<String> output=fire(restClient);
        try {
            ValueStore valueStore1 = processOutput(valueStore, output);
            this.printDoneExecute(index);
            return valueStore1;
        } catch (RuntimeException ex){
            printException(index);
            throw ex;
        }

    }
    public abstract HttpResponse<String>  fire(RestClient restClient);
    public void printException(int index){
        this.print(index,"URL: "+this.getUrlTemplate());
    }
    public ValueStore processOutput(ValueStore valueStore,HttpResponse<String> output){
        List<Integer> validStatus= Arrays.asList(200,201,202,203,204,205,206,207,208,226);
        if(validStatus.contains(output.getStatus())){
            if (responseHeaders != null) {
                for (int j = 0; j < responseHeaders.size(); j++) {
                    ResponseHeader responseHeader = responseHeaders.get(j);
                    valueStore.add(responseHeader.getVariable(), output.getHeaders().getFirst(responseHeader.getName()));
                }
            }
            if(!responseTemplate.trim().equals("")) {
                HashMap<String, Object> extractedValues = null;
                try {
                    ExtractableTemplate extractableTemplate=ExtractableTemplate.parse(responseTemplate);
                    if(extractableTemplate instanceof XMLTemplate){
                        extractedValues=extractableTemplate.extract(XMLTemplate.StringDocToElement(output.getBody()));
                    } else if(extractableTemplate instanceof JSONTemplate){
                        extractedValues=extractableTemplate.extract((new JSONParser()).parse(output.getBody()));
                    }
                } catch (TemplateParseException e) {
                    try {
                        TextTemplate rtTemplate=TextTemplate.parse(responseTemplate);
                        Variable variable=rtTemplate.getVariables().get(0);
                        extractedValues=new HashMap<>();
                        extractedValues.put(variable.getName(),output.getBody());
                    } catch (TemplateParseException tex){
                        e.printStackTrace();
                    }

                } catch (ParseException e) {
                    try {
                        TextTemplate rtTemplate=TextTemplate.parse(responseTemplate);
                        Variable variable=rtTemplate.getVariables().get(0);
                        extractedValues=new HashMap<>();
                        extractedValues.put(variable.getName(),output.getBody());
                    } catch (TemplateParseException tex){
                        e.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    try {
                        TextTemplate rtTemplate=TextTemplate.parse(responseTemplate);
                        Variable variable=rtTemplate.getVariables().get(0);
                        extractedValues=new HashMap<>();
                        extractedValues.put(variable.getName(),output.getBody());
                    } catch (TemplateParseException tex){
                        e.printStackTrace();
                    }
                }
                if(extractedValues!=null) {
                    valueStore.add(extractedValues);
                }
            }
            if(wait!=0){
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return valueStore;
        } else{
            throw new RuntimeException("Invalid Response from Server got : "+output.getStatus()+"-"+output.getBody());
        }
    }
}
