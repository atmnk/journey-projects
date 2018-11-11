package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PostUnitTest {
    RestClient restClient= mock(RestClient.class);
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);
    @Test
    public void should_fire_get_request_on_execute() throws UnirestException {
        doReturn(200).when(mockResponse).getStatus();
        doReturn("<a>Atmaram</a>").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).post(any(),anyList(),anyString());
        PostUnit postUnit=new PostUnit(restClient);
        postUnit.urlTemplate="ABC";
        postUnit.requestHeaders= Arrays.asList();
        postUnit.responseHeaders=Arrays.asList();
        postUnit.name="Unit 1";
        postUnit.responseTemplate="<a>${Name}</a>";
        postUnit.requestTemplate="<a>Hello</a>";
        postUnit.execute(new ValueStore(),0);

        verify(restClient,times(1)).post("ABC",Arrays.asList(),"<a>Hello</a>");
    }

    @Test
    public void should_throw_null_pointer_exception_when_unirest_exception() throws UnirestException {
        UnirestException ex=mock(UnirestException.class);
        doThrow(ex).when(restClient).post(any(),anyList(),anyString());
        PostUnit postUnit=new PostUnit(restClient);
        postUnit.urlTemplate="ABC";
        postUnit.requestHeaders= Arrays.asList();
        postUnit.responseHeaders=Arrays.asList();
        postUnit.name="Unit 1";
        postUnit.responseTemplate="<a>${Name}</a>";
        postUnit.requestTemplate="<a>Hello</a>";
        expectedException.expect(NullPointerException.class);
        postUnit.execute(new ValueStore(),0);


    }
}
