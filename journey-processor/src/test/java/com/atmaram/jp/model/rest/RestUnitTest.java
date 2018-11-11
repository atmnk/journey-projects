package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.RequestHeader;
import com.atmaram.jp.model.ResponseHeader;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RestUnitTest{
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    HttpResponse<String> mockResponse=mock(HttpResponse.class);
    class RestUnitSub extends RestUnit{

        public RestUnitSub(RestClient restClient) {
            super(restClient);
        }

        @Override
        public HttpResponse<String> fire(RestClient restClient)
        {

            return mockResponse;
        }

        @Override
        public Unit fill(ValueStore valueStore) {
            return null;
        }
    }
    //eval

    //Request Headers
    @Test
    public void should_get_variables_from_request_headers() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        RequestHeader requestHeader1=new RequestHeader();
        requestHeader1.setName("Header1");
        requestHeader1.setValueTemplate("ABC${Var1}");
        RequestHeader requestHeader2=new RequestHeader();
        requestHeader2.setName("Header2");
        requestHeader2.setValueTemplate("ABC${Var2}");
        restUnitSub.requestHeaders= Arrays.asList(requestHeader1,requestHeader2);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        VariableStore variableStore=new VariableStore();
        restUnitSub.eval(variableStore);
        Variable variable1=new Variable();
        Variable variable2=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        variable2.setName("Var2");
        variable2.setType("String");
        List<Variable> actual=variableStore.getVariables();
        List<Variable> expected=Arrays.asList(variable1,variable2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_throw_unit_confguration_exception_when_request_header_template_not_valid() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        RequestHeader requestHeader1=new RequestHeader();
        requestHeader1.setName("Header1");
        requestHeader1.setValueTemplate("ABC${Var1");
        restUnitSub.name="Unit 1";
        restUnitSub.requestHeaders= Arrays.asList(requestHeader1);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        VariableStore variableStore=new VariableStore();
        expectedException.expect(UnitConfigurationException.class);
        expectedException.expectMessage("Unit Unit 1 is not properly configured Invalid Template in request header: Header1");
        restUnitSub.eval(variableStore);
    }
    //URLTemplate
    @Test
    public void should_get_variables_from_url_template() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC${Var1}PQR${Var2}";
        restUnitSub.responseTemplate="[\"ABC\"]";
        VariableStore variableStore=new VariableStore();
        restUnitSub.eval(variableStore);
        Variable variable1=new Variable();
        Variable variable2=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        variable2.setName("Var2");
        variable2.setType("String");
        List<Variable> actual=variableStore.getVariables();
        List<Variable> expected=Arrays.asList(variable1,variable2);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void should_throw_unit_confguration_exception_when_url_template_not_valid() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.name="Unit 1";
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC${Var1";
        restUnitSub.responseTemplate="[\"ABC\"]";
        VariableStore variableStore=new VariableStore();
        expectedException.expect(UnitConfigurationException.class);
        expectedException.expectMessage("Unit Unit 1 is not properly configured Invalid Template in url: ABC${Var1");
        restUnitSub.eval(variableStore);
    }

    //Response Headers
    @Test
    public void should_resolve_variables_from_response_headers() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        ResponseHeader responseHeader1=new ResponseHeader();
        responseHeader1.setName("Header1");
        responseHeader1.setVariable("Var1");
        ResponseHeader responseHeader2=new ResponseHeader();
        responseHeader2.setName("Header2");
        responseHeader2.setVariable("Var2");
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList(responseHeader1,responseHeader2);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        VariableStore variableStore=new VariableStore();
        restUnitSub.eval(variableStore);
        Variable variable1=new Variable();
        Variable variable2=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        variable2.setName("Var2");
        variable2.setType("String");
        variableStore.add(Arrays.asList(variable1,variable2));
        List<Variable> actual=variableStore.getVariables();
        List<Variable> expected=Arrays.asList();
        assertThat(actual).isEqualTo(expected);
    }

    //Response Template

    @Test
    public void should_get_variables_from_response_template_if_template_variables() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="{#{Var1}:${Var2}}";
        VariableStore variableStore=new VariableStore();
        restUnitSub.eval(variableStore);
        Variable variable1=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        List<Variable> actual=variableStore.getVariables();
        List<Variable> expected=Arrays.asList(variable1);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void should_resolve_variables_from_response_template() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC$";
        restUnitSub.responseTemplate="{\"name\":${Var1},\"place\":${Var2}}";
        VariableStore variableStore=new VariableStore();
        restUnitSub.eval(variableStore);
        Variable variable1=new Variable();
        Variable variable2=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        variable2.setName("Var2");
        variable2.setType("String");
        variableStore.add(Arrays.asList(variable1,variable2));
        List<Variable> actual=variableStore.getVariables();
        List<Variable> expected=Arrays.asList();
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void should_throw_unit_confguration_exception_when_response_template_not_valid() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.name="Unit 1";
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="{\"name\":\"${ATM\"}";
        VariableStore variableStore=new VariableStore();
        expectedException.expect(UnitConfigurationException.class);
        expectedException.expectMessage("Unit Unit 1 is not properly configured Invalid response: Template{\"name\":\"${ATM\"}");
        restUnitSub.eval(variableStore);
    }

    //fillObject
    @Test
    public void should_fill_url_template() {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC${Var1}";
        restUnitSub.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        RestUnit filled=restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(filled.urlTemplate).isEqualTo("ABCHello");
    }
    @Test
    public void should_not_throw_exception_when_inavlid_url_template() {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC${Var1";
        restUnitSub.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        RestUnit filled=restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(filled.urlTemplate).isEqualTo("ABC${Var1");
    }

    @Test
    public void should_fill_request_headers() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        RequestHeader requestHeader1=new RequestHeader();
        requestHeader1.setName("Header1");
        requestHeader1.setValueTemplate("ABC${Var1}");
        RequestHeader requestHeader2=new RequestHeader();
        requestHeader2.setName("Header2");
        requestHeader2.setValueTemplate("ABC${Var2}");
        restUnitSub.requestHeaders= Arrays.asList(requestHeader1,requestHeader2);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(restUnitSub.requestHeaders.get(0).getValueTemplate()).isEqualTo("ABCHello");
        assertThat(restUnitSub.requestHeaders.get(1).getValueTemplate()).isEqualTo("ABCWorld");
    }

    @Test
    public void should_not_throw_exception_when_invalid_request_header_template() {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        RequestHeader requestHeader1=new RequestHeader();
        requestHeader1.setName("Header1");
        requestHeader1.setValueTemplate("ABC${Var1");
        restUnitSub.requestHeaders= Arrays.asList(requestHeader1);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(restUnitSub.requestHeaders.get(0).getValueTemplate()).isEqualTo(null);
    }

    @Test
    public void should_fill_response_template_if_template_variables() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="{#{Var1}:${Var2}}";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(restUnitSub.responseTemplate).isEqualTo("{\"Hello\":\"${Var2}\"}");
    }
    @Test
    public void should_not_throw_exception_if_invalid_response_template() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="{#{Var1:${Var2}}";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(restUnitSub.responseTemplate).isEqualTo("{#{Var1:${Var2}}");
    }
    @Test
    public void should_return_same_response_template_if_blank() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        valueStore.add("Var2","World");
        restUnitSub.fillObject(restUnitSub,valueStore);
        assertThat(restUnitSub.responseTemplate).isEqualTo("");
    }
    //Execute - Process Output

    @Test
    public void should_execute_rest_unit(){
        doReturn("{\"name\":\"World\"}").when(mockResponse).getBody();
        doReturn(200).when(mockResponse).getStatus();
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="";
        ValueStore valueStore=new ValueStore();
        ValueStore newValueStore=restUnitSub.execute(valueStore,0);
        assertThat(newValueStore).isNotNull();
    }

    @Test
    public void should_throw_runtime_exception_when_cant_execute_rest_unit(){
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="";
        ValueStore valueStore=new ValueStore();
        expectedException.expect(RuntimeException.class);
        restUnitSub.execute(valueStore,0);
    }

    //processOutput
    @Test
    public void should_throw_exception_when_output_is_not_in_expected_status(){
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="";
        ValueStore valueStore=new ValueStore();
        doReturn(get_invalid_status()).when(mockResponse).getStatus();
        expectedException.expect(RuntimeException.class);
        restUnitSub.execute(valueStore,0);
    }

    @Test
    public void should_extract_values_from_response_headers() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        ResponseHeader responseHeader1=new ResponseHeader();
        responseHeader1.setName("Header1");
        responseHeader1.setVariable("Var1");
        ResponseHeader responseHeader2=new ResponseHeader();
        responseHeader2.setName("Header2");
        responseHeader2.setVariable("Var2");
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList(responseHeader1,responseHeader2);
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        Headers headers=new Headers();
        headers.put("Header1",Arrays.asList("Hello"));
        headers.put("Header2",Arrays.asList("World"));
        doReturn(200).when(mockResponse).getStatus();
        doReturn("[\"ABC\"]").when(mockResponse).getBody();
        doReturn(headers).when(mockResponse).getHeaders();

        restUnitSub.execute(valueStore,0);

        assertThat(valueStore.getValues()).containsKeys("Var1");
        assertThat(valueStore.getValues()).containsKeys("Var2");
        assertThat(valueStore.getValues().get("Var1")).isEqualTo("Hello");
        assertThat(valueStore.getValues().get("Var2")).isEqualTo("World");
    }

    @Test
    public void should_extract_values_from_response() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="{\"name\":${Var1},\"place\":${Var2}}";
        ValueStore valueStore=new ValueStore();
        Headers headers=new Headers();
        doReturn(200).when(mockResponse).getStatus();
        doReturn("{\"name\":\"Hello\",\"place\":\"World\"}").when(mockResponse).getBody();
        doReturn(headers).when(mockResponse).getHeaders();

        restUnitSub.execute(valueStore,0);

        assertThat(valueStore.getValues()).containsKeys("Var1");
        assertThat(valueStore.getValues()).containsKeys("Var2");
        assertThat(valueStore.getValues().get("Var1")).isEqualTo("Hello");
        assertThat(valueStore.getValues().get("Var2")).isEqualTo("World");
    }

    @Test
    public void should_extract_values_from_response_if_simple_text_template() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="${Var1}";
        ValueStore valueStore=new ValueStore();
        Headers headers=new Headers();
        doReturn(200).when(mockResponse).getStatus();
        doReturn("{Hello").when(mockResponse).getBody();
        doReturn(headers).when(mockResponse).getHeaders();

        restUnitSub.execute(valueStore,0);

        assertThat(valueStore.getValues()).containsKeys("Var1");
        assertThat(valueStore.getValues().get("Var1")).isEqualTo("{Hello");
    }

    @Test
    public void should_not_throw_runtime_exception_if_not_even_valid_text_template() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.responseTemplate="${Var1";
        ValueStore valueStore=new ValueStore();
        Headers headers=new Headers();
        doReturn(200).when(mockResponse).getStatus();
        doReturn("Hello").when(mockResponse).getBody();
        doReturn(headers).when(mockResponse).getHeaders();

        restUnitSub.execute(valueStore,0);
    }
    @Test
    public void should_not_throw_runtime_exception_if_not_valid_json() throws UnitConfigurationException {
        RestUnitSub restUnitSub=new RestUnitSub(null);
        restUnitSub.requestHeaders= Arrays.asList();
        restUnitSub.responseHeaders=Arrays.asList();
        restUnitSub.urlTemplate="ABC";
        restUnitSub.wait=1;
        restUnitSub.responseTemplate="{\"Hello\":${Var1}}";
        ValueStore valueStore=new ValueStore();
        Headers headers=new Headers();
        doReturn(200).when(mockResponse).getStatus();
        doReturn("{Hello:atmaram}").when(mockResponse).getBody();
        doReturn(headers).when(mockResponse).getHeaders();

        restUnitSub.execute(valueStore,0);

        assertThat(valueStore.getValues()).containsKeys("Var1");
        assertThat(valueStore.getValues().get("Var1")).isEqualTo("{Hello:atmaram}");
    }

    public static int get_invalid_status(){
        Random random=new Random();
        int stat=random.nextInt(999);
        List<Integer> validSat=Arrays.asList(200,201,202,203,204,205,206,207,208,226);
        if(validSat.contains(stat))
            return get_invalid_status();
        return stat;
    }


}
