package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GetUnitTest {
    RestClient restClient= mock(RestClient.class);
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);
    @Test
    public void should_fire_get_request_on_execute() throws UnirestException {
        doReturn(200).when(mockResponse).getStatus();
        doReturn("<a>Atmaram</a>").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).get(any(),anyList());
        GetUnit getUnit=new GetUnit(restClient);
        getUnit.urlTemplate="ABC";
        getUnit.requestHeaders= Arrays.asList();
        getUnit.responseHeaders=Arrays.asList();
        getUnit.name="Unit 1";
        getUnit.responseTemplate="<a>${Name}</a>";
        getUnit.execute(new ValueStore(),0);

        verify(restClient,times(1)).get("ABC",Arrays.asList());
    }

    @Test
    public void should_not_throw_exception_when_unirest_exception() throws UnirestException {
        UnirestException ex=mock(UnirestException.class);
        doThrow(ex).when(restClient).get(any(),anyList());
        GetUnit getUnit=new GetUnit(restClient);
        getUnit.urlTemplate="ABC";
        getUnit.requestHeaders= Arrays.asList();
        getUnit.responseHeaders=Arrays.asList();
        getUnit.name="Unit 1";
        getUnit.responseTemplate="<a>${Name}</a>";
        expectedException.expect(NullPointerException.class);
        getUnit.execute(new ValueStore(),0);


    }
}
