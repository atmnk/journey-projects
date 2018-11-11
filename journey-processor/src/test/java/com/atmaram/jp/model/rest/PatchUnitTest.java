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

public class PutUnitTest {
    RestClient restClient= mock(RestClient.class);
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);

    @Test
    public void should_fill_object(){
        PutUnit putUnit=new PutUnit(restClient);
        putUnit.urlTemplate="ABC${Var1}";
        putUnit.requestHeaders= Arrays.asList();
        putUnit.responseHeaders=Arrays.asList();
        putUnit.name="Unit 1";
        putUnit.responseTemplate="<a>${Name}</a>";
        putUnit.requestTemplate="<a>${Var2}</a>";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        PutUnit filled=(PutUnit) putUnit.fill(valueStore);
        assertThat(filled.urlTemplate).isEqualTo("ABCHello");
        assertThat(filled.requestTemplate).isEqualTo("<a>World</a>");
    }
    @Test
    public void should_fire_get_request_on_execute() throws UnirestException {
        doReturn(200).when(mockResponse).getStatus();
        doReturn("<a>Atmaram</a>").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).put(any(),anyList(),anyString());
        PutUnit putUnit=new PutUnit(restClient);
        putUnit.urlTemplate="ABC";
        putUnit.requestHeaders= Arrays.asList();
        putUnit.responseHeaders=Arrays.asList();
        putUnit.name="Unit 1";
        putUnit.responseTemplate="<a>${Name}</a>";
        putUnit.requestTemplate="<a>Hello</a>";
        putUnit.execute(new ValueStore(),0);

        verify(restClient,times(1)).put("ABC",Arrays.asList(),"<a>Hello</a>");
    }

    @Test
    public void should_throw_null_pointer_exception_when_unirest_exception() throws UnirestException {
        UnirestException ex=mock(UnirestException.class);
        doThrow(ex).when(restClient).put(any(),anyList(),anyString());
        PutUnit putUnit=new PutUnit(restClient);
        putUnit.urlTemplate="ABC";
        putUnit.requestHeaders= Arrays.asList();
        putUnit.responseHeaders=Arrays.asList();
        putUnit.name="Unit 1";
        putUnit.responseTemplate="<a>${Name}</a>";
        putUnit.requestTemplate="<a>Hello</a>";
        expectedException.expect(NullPointerException.class);
        putUnit.execute(new ValueStore(),0);

    }
}
