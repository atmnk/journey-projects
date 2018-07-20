package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PatchUnit extends BodiedUnit {
    @Override
    public HttpResponse<String> fire(RestClient restClient){
        HttpResponse<String> output = null;
        try {
            output = restClient.patch(urlTemplate, requestHeaders, requestTemplate);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public Unit fill(ValueStore valueStore) {
        return fillObject(new PatchUnit(),valueStore);
    }
}
