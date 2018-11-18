package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.template.TemplateType;
import com.atmaram.tp.template.Variable;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class BodiedUnitTest {
    HttpResponse<String> mockResponse=mock(HttpResponse.class);

    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    class WithBodyUnit extends BodiedUnit{

        public WithBodyUnit(RestClient restClient) {
            super(restClient);
        }

        @Override
        public HttpResponse<String> fire(RestClient restClient) {
            return mockResponse;
        }

        @Override
        public Unit fill(ValueStore valueStore) {
            return null;
        }
    }
    //fillObject
    @Test
    public void should_fill_withbodyunit(){
        WithBodyUnit withBodyUnit=new WithBodyUnit(null);
        withBodyUnit.requestHeaders= Arrays.asList();
        withBodyUnit.responseHeaders=Arrays.asList();
        withBodyUnit.urlTemplate="ABC";
        withBodyUnit.requestTemplate="{\"name\":${Var1}}";
        withBodyUnit.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        withBodyUnit.fillObject(withBodyUnit,valueStore);
        assertThat(withBodyUnit.requestTemplate).isEqualTo("{\"name\":\"Hello\"}");
    }
    @Test
    public void should_not_throw_exception_when_invalid_template_in_withbodyunit(){
        WithBodyUnit withBodyUnit=new WithBodyUnit(null);
        withBodyUnit.requestHeaders= Arrays.asList();
        withBodyUnit.responseHeaders=Arrays.asList();
        withBodyUnit.urlTemplate="ABC";
        withBodyUnit.requestTemplate="{\"name\":${Var1}";
        withBodyUnit.responseTemplate="[\"ABC\"]";
        ValueStore valueStore=new ValueStore();
        valueStore.add("Var1","Hello");
        withBodyUnit.fillObject(withBodyUnit,valueStore);
        assertThat(withBodyUnit.requestTemplate).isEqualTo("{\"name\":${Var1}");
    }

    //Eval
    @Test
    public void should_get_variables_withbodyunit() throws UnitConfigurationException {
        WithBodyUnit withBodyUnit=new WithBodyUnit(null);
        withBodyUnit.requestHeaders= Arrays.asList();
        withBodyUnit.responseHeaders=Arrays.asList();
        withBodyUnit.urlTemplate="ABC";
        withBodyUnit.requestTemplate="{\"name\":${Var1}}";
        withBodyUnit.responseTemplate="[\"ABC\"]";
        withBodyUnit.responseTemplateType= TemplateType.Json;
        withBodyUnit.requestTemplateType=TemplateType.Json;
        VariableStore variableStore=new VariableStore();
        withBodyUnit.eval(variableStore);
        Variable variable1=new Variable();
        variable1.setName("Var1");
        variable1.setType("String");
        assertThat(variableStore.getVariables()).isEqualTo(Arrays.asList(variable1));
    }
    @Test
    public void should_throw_exception_when_invalid_template_in_withbodyunit_when_eval() throws UnitConfigurationException {
        WithBodyUnit withBodyUnit=new WithBodyUnit(null);
        withBodyUnit.name="Unit 1";
        withBodyUnit.requestHeaders= Arrays.asList();
        withBodyUnit.responseHeaders=Arrays.asList();
        withBodyUnit.urlTemplate="ABC";
        withBodyUnit.requestTemplate="{\"name\":${Var1}";
        withBodyUnit.responseTemplateType= TemplateType.Json;
        withBodyUnit.requestTemplateType=TemplateType.Json;
        withBodyUnit.responseTemplate="[\"ABC\"]";
        expectedException.expect(UnitConfigurationException.class);
        expectedException.expectMessage("Unit Unit 1 is not properly configured Invalid Template in request body: Unit 1");
        withBodyUnit.eval(new VariableStore());
    }

    @Test
    public void should_throw_runtime_exception_with_body_on_execute_if_template_not_valid(){
        WithBodyUnit withBodyUnit=new WithBodyUnit(null);
        withBodyUnit.requestHeaders= Arrays.asList();
        withBodyUnit.urlTemplate="ABC";
        withBodyUnit.responseTemplate="";
        withBodyUnit.requestTemplate="{\"name\":}";
        ValueStore valueStore=new ValueStore();
        doReturn(RestUnitTest.get_invalid_status()).when(mockResponse).getStatus();
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(contains("Body: "+withBodyUnit.requestTemplate));
        withBodyUnit.execute(valueStore,0);
    }
}
