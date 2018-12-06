package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.util.TemplateParsingUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;

import java.util.stream.Collectors;

public class DeleteUnit extends BodiedUnit {
    public DeleteUnit(RestClient restClient) {
        super(restClient);
    }

    @Override
    public HttpResponse<String> fire(RestClient restClient){
        logObject.put("type","delete");
        logObject.put("URL",urlTemplate);
        logObject.put("Request Headers",requestHeaders.stream().map((h) -> {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(h.getName(),h.getValueTemplate());
            return jsonObject;
        }).collect(Collectors.toList()));
        logObject.put("Body",TemplateParsingUtil.replaceVariablesWithNulls(requestTemplate));
        if(Runtime.verbose) {
            System.out.println("Request:URL:" + urlTemplate);
            System.out.println("Request:Headers:" + requestHeaders.stream().map((h) -> h.toString()).reduce("", (a, s) -> a + "," + s));
            System.out.println("Request:Body:" + TemplateParsingUtil.replaceVariablesWithNulls(requestTemplate));
        }
        HttpResponse<String> output = null;
        try {
            output = restClient.delete(urlTemplate, requestHeaders, TemplateParsingUtil.replaceVariablesWithNulls(requestTemplate));
            logObject.put("Response Code",output.getStatus());
            logObject.put("Response",output.getBody());
            logObject.put("Response Headers",output.getHeaders());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public Unit fill(ValueStore valueStore,boolean lazy) {
        return fillObject(new DeleteUnit(restClient),valueStore,lazy);
    }
}
