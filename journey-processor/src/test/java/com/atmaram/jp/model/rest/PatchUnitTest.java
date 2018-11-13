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

public class PatchUnitTest {
    RestClient restClient= mock(RestClient.class);
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);

    @Test
    public void should_fill_object(){
        PatchUnit patchUnit=new PatchUnit(restClient);
        patchUnit.urlTemplate="ABC${Var1}";
        patchUnit.requestHeaders= Arrays.asList();
        patchUnit.responseHeaders=Arrays.asList();
        patchUnit.name="Unit 1";
        patchUnit.responseTemplate="<a>${Name}</a>";
        patchUnit.requestTemplate="<a>${Var2}</a>";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        PatchUnit filled=(PatchUnit) patchUnit.fill(valueStore);
        assertThat(filled.urlTemplate).isEqualTo("ABCHello");
        assertThat(filled.requestTemplate).isEqualTo("<a>World</a>");
    }
    @Test
    public void should_fire_get_request_on_execute() throws UnirestException {
        doReturn(200).when(mockResponse).getStatus();
        doReturn("<a>Atmaram</a>").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).patch(any(),anyList(),anyString());
        PatchUnit patchUnit=new PatchUnit(restClient);
        patchUnit.urlTemplate="ABC";
        patchUnit.requestHeaders= Arrays.asList();
        patchUnit.responseHeaders=Arrays.asList();
        patchUnit.name="Unit 1";
        patchUnit.responseTemplate="<a>${Name}</a>";
        patchUnit.requestTemplate="<a>Hello</a>";
        patchUnit.execute(new ValueStore(),0);

        verify(restClient,times(1)).patch("ABC",Arrays.asList(),"<a>Hello</a>");
    }

    @Test
    public void should_throw_null_pointer_exception_when_unirest_exception() throws UnirestException {
        UnirestException ex=mock(UnirestException.class);
        doThrow(ex).when(restClient).patch(any(),anyList(),anyString());
        PatchUnit patchUnit=new PatchUnit(restClient);
        patchUnit.urlTemplate="ABC";
        patchUnit.requestHeaders= Arrays.asList();
        patchUnit.responseHeaders=Arrays.asList();
        patchUnit.name="Unit 1";
        patchUnit.responseTemplate="<a>${Name}</a>";
        patchUnit.requestTemplate="<a>Hello</a>";
        expectedException.expect(NullPointerException.class);
        patchUnit.execute(new ValueStore(),0);

    }
}
