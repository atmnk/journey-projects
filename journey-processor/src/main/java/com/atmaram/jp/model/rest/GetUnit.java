package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.model.Unit;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;
public class GetUnit extends RestUnit {
    public GetUnit(RestClient restClient) {
        super(restClient);
    }

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
        GetUnit getUnit=new GetUnit(restClient);
        return fillObject(getUnit,valueStore);
    }
}
