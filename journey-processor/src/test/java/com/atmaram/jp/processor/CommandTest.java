package com.atmaram.jp.processor;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.CommandConfigurationException;
import com.atmaram.jp.model.*;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CommandTest {
    @Test
    public void should_eval_simple_units_without_variables() throws CommandConfigurationException {
        Command command=new Command();
        command.setName("Journey 1");
        List<Unit> units=new ArrayList<>();
        GetUnit unit1=new GetUnit();
        unit1.setUrlTemplate("http://localhost");
        unit1.setResponseTemplate("{\"name\":\"World\"}");
        GetUnit unit2=new GetUnit();
        unit2.setUrlTemplate("http://localhost");
        unit2.setResponseTemplate("{\"name\":\"World\"}");
        units.add(unit1);
        units.add(unit2);
        command.setUnits(units);

        VariableStore result=command.eval(new VariableStore(),new ArrayList<>());
        assertThat(result.getVariables().size()).isEqualTo(0);
    }

    @Test
    public void should_eval_simple_get_without_variables() throws CommandConfigurationException {
        Command command=new Command();
        command.setName("Journey 1");
        List<Unit> steps=new ArrayList<>();
        GetUnit step1=new GetUnit();
        step1.setUrlTemplate("http://localhost");
        step1.setResponseTemplate("{\"name\":\"World\"}");
        steps.add(step1);
        command.setUnits(steps);

        VariableStore result=command.eval(new VariableStore(),new ArrayList<>());
        assertThat(result.getVariables().size()).isEqualTo(0);
    }
    @Test
    public void should_eval_unresolved_url_variable() throws CommandConfigurationException {
        Command journey=new Command();
        journey.setName("Journey 1");
        List<Unit> steps=new ArrayList<>();
        GetUnit step1=new GetUnit();
        step1.setUrlTemplate("http://localhost:${Port}");
        step1.setResponseTemplate("{\"name\":\"World\"}");
        GetUnit step2=new GetUnit();
        step2.setUrlTemplate("http://localhost:${Port}");
        step2.setResponseTemplate("{\"name\":\"World\"}");
        steps.add(step1);
        steps.add(step2);
        journey.setUnits(steps);

        VariableStore result=journey.eval(new VariableStore(),new ArrayList<>());
        assertThat(result.getVariables().size()).isEqualTo(1);
    }

    @Test
    public void should_resolve_environment_variable() throws CommandConfigurationException {
        Command command=new Command();
        Environment environment=new Environment();
        environment.setVariables(Arrays.asList(new EnvironmentVariable("BaseURL","http://localhost:${PORT}")));
        GetUnit getUnit=new GetUnit();
        getUnit.setUrlTemplate("${BaseURL}/call1");
        getUnit.setResponseTemplate("{\"name\":${name}}");
        command.setUnits(Arrays.asList(getUnit));
        VariableStore variableStore=command.eval(new VariableStore(),Arrays.asList(environment));
        assertThat(variableStore.getVariables().size()).isEqualTo(1);
        assertThat(variableStore.getVariables().get(0).getName()).isEqualTo("PORT");

    }
    //Execute Test
    @Test
    public void should_execute_simple_steps_without_variables() throws CommandConfigurationException, ParseException, UnirestException, TemplateParseException {
        RestClient restClient=mock(RestClient.class);
        Command journey=new Command(restClient);
        journey.setName("Journey 1");
        List<Unit> steps=new ArrayList<>();
        GetUnit step1=new GetUnit();
        step1.setUrlTemplate("http://localhost");
        step1.setResponseTemplate("{\"name\":\"World\"}");
        BodiedUnit step2=new BodiedUnit();
        step2.setUrlTemplate("http://localhost");
        step2.setResponseTemplate("{\"name\":\"World\"}");
        step2.setRequestTemplate("{\"place\": \"Mumbai\"}");
        steps.add(step1);
        steps.add(step2);
        journey.setUnits(steps);
        HttpResponse<String> mockResponse=mock(HttpResponse.class);

        doReturn("{\"name\":\"World\"}").when(mockResponse).getBody();
        doReturn(mockResponse).when(restClient).get(anyString(),any(List.class));
        doReturn(mockResponse).when(restClient).post(anyString(),any(List.class),anyString());
        ValueStore valueStore=journey.execute(new ArrayList<>(),new ValueStore());
        verify(restClient,times(1)).get("http://localhost",new ArrayList<>());
        verify(restClient,times(1)).post("http://localhost",new ArrayList<>(),"{\"place\":\"Mumbai\"}");
        assertThat(valueStore.getValues().size()).isEqualTo(0);
    }


}
