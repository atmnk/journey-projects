package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PostUnit extends BodiedUnit {
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

    @Override
    public Unit fill(ValueStore valueStore) {
        return fillObject(new PostUnit(),valueStore);
    }
}
