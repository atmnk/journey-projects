package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;

@Data
public class GetUnit extends RestUnit {
    @Override
    public HttpResponse<String> fire(RestClient restClient){
        HttpResponse<String> output = null;
        try {
            output = restClient.get(urlTemplate, requestHeaders);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public Unit fill(ValueStore valueStore) {
        GetUnit getUnit=new GetUnit();
        return fillObject(getUnit,valueStore);
    }
}
