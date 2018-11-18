package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.tp.template.TemplateType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DeleteUnitTest {
    RestClient restClient= mock(RestClient.class);
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);
    @Test
    public void should_fire_get_request_on_execute() throws UnirestException {
        doReturn(200).when(mockResponse).getStatus();
        doReturn("<a>Atmaram</a>").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).delete(any(),anyList(),anyString());
        DeleteUnit deleteUnit=new DeleteUnit(restClient);
        deleteUnit.urlTemplate="ABC";
        deleteUnit.requestHeaders= Arrays.asList();
        deleteUnit.responseHeaders=Arrays.asList();
        deleteUnit.name="Unit 1";
        deleteUnit.responseTemplate="<a>${Name}</a>";
        deleteUnit.requestTemplate="<a>Hello</a>";
        deleteUnit.execute(new ValueStore(),0);

        verify(restClient,times(1)).delete("ABC",Arrays.asList(),"<a>Hello</a>");
    }

    @Test
    public void should_throw_null_pointer_exception_when_unirest_exception() throws UnirestException {
        UnirestException ex=mock(UnirestException.class);
        doThrow(ex).when(restClient).delete(any(),anyList(),anyString());
        DeleteUnit deleteUnit=new DeleteUnit(restClient);
        deleteUnit.urlTemplate="ABC";
        deleteUnit.requestHeaders= Arrays.asList();
        deleteUnit.responseHeaders=Arrays.asList();
        deleteUnit.name="Unit 1";
        deleteUnit.responseTemplate="<a>${Name}</a>";
        deleteUnit.requestTemplate="<a>Hello</a>";
        expectedException.expect(NullPointerException.class);
        deleteUnit.execute(new ValueStore(),0);


    }

    @Test
    public void should_fill_object(){
        DeleteUnit deleteUnit=new DeleteUnit(restClient);
        deleteUnit.urlTemplate="ABC${Var1}";
        deleteUnit.requestHeaders= Arrays.asList();
        deleteUnit.responseHeaders=Arrays.asList();
        deleteUnit.name="Unit 1";
        deleteUnit.responseTemplate="<a>${Name}</a>";
        deleteUnit.requestTemplate="<a>${Var2}</a>";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        DeleteUnit filled=(DeleteUnit) deleteUnit.fill(valueStore);
        assertThat(filled.urlTemplate).isEqualTo("ABCHello");
        assertThat(filled.requestTemplate).isEqualTo("<a>World</a>");
    }
}
