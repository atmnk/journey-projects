package com.atmaram.jp.command;

import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.CommandConfigurationException;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Environment;
import com.atmaram.jp.model.EnvironmentVariable;
import com.atmaram.jp.model.Unit;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.List;
@Data
public class Command {
    JSONArray logObject;
    String name;
    List<Unit> units;

    public void setLogObject(JSONArray logObject) {
        this.logObject = logObject;
        for (Unit unit:
                this.units) {
            unit.parentLogObject=this.logObject;
        }
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
        for (Unit unit:
             this.units) {
            unit.parentLogObject=this.logObject;
        }
    }
    List<EnvironmentVariable> variables;
    public Command() {
        this.logObject=new JSONArray();
    }
    public Command(JSONArray logObject){
        this.logObject=logObject;
    }
    public ValueStore execute(List<Environment> environments, ValueStore valueStore, int index){
        if(variables!=null) {
            for (int i = 0; i < variables.size(); i++) {
                EnvironmentVariable environmentVariable = variables.get(i);
                TextTemplate textTemplate = null;
                try {
                    textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                } catch (TemplateParseException e) {
                    e.printStackTrace();
                    System.out.println("Template:"+environmentVariable.getValueTemplate());
                }
                String envValue = textTemplate.fill(valueStore.getValues()).toStringTemplate();
                valueStore.add(environmentVariable.getName(), envValue);
            }
        }
        for (Environment environment:
                environments) {
            if(environment!=null && environment.getVariables()!=null) {
                for (int i = 0; i < environment.getVariables().size(); i++) {
                    EnvironmentVariable environmentVariable = environment.getVariables().get(i);
                    TextTemplate textTemplate = null;
                    try {
                        textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                    } catch (TemplateParseException e) {
                        e.printStackTrace();
                        System.out.println("Template:"+environmentVariable.getValueTemplate());
                    }
                    String envValue = textTemplate.fill(valueStore.getValues()).toStringTemplate();
                    valueStore.add(environmentVariable.getName(), envValue);
                }
            }
        }
        if(units!=null) {
            for (int i = 0; i < units.size(); i++) {
                Unit currentUnit = units.get(i);
                Unit filledUnit= currentUnit.fill(valueStore,false);
                filledUnit.parentLogObject=this.logObject;
                filledUnit.execute(valueStore,index);

            }
        }
        return valueStore;
    }
    public ValueStore execute(List<Environment> environments, ValueStore valueStore){
        return execute(environments,valueStore,0);
    }

    public VariableStore eval(VariableStore variableStore,List<Environment> environments) throws CommandConfigurationException {
        for (Environment environment:
                environments) {
            if(environment!=null && environment.getVariables()!=null) {
                for (int i = 0; i < environment.getVariables().size(); i++) {
                    EnvironmentVariable environmentVariable = environment.getVariables().get(i);
                    TextTemplate textTemplate = null;
                    try {
                        textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                    } catch (TemplateParseException e) {
                        throw new CommandConfigurationException("Invalid text template in variable :"+environmentVariable.getName(),e);
                    }
                    variableStore.add(textTemplate.getVariables());
                    Variable variable = new Variable();
                    variable.setName(environmentVariable.getName());
                    variable.setType("String");
                    variableStore.resolve(Arrays.asList(variable));
                }
            }
        }
        if(variables!=null) {
            for (int i = 0; i < variables.size(); i++) {
                EnvironmentVariable environmentVariable = variables.get(i);
                TextTemplate textTemplate = null;
                try {
                    textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                } catch (TemplateParseException e) {
                    new CommandConfigurationException("Invalid Template at command variable: "+environmentVariable.getName(),e);
                }
                variableStore.add(textTemplate.getVariables());
                Variable variable = new Variable();
                variable.setName(environmentVariable.getName());
                variable.setType("String");
                variableStore.resolve(Arrays.asList(variable));
            }
        }
        if(units!=null) {
            for (int i = 0; i < units.size(); i++) {
                Unit currentUnit = units.get(i);
                try {
                    currentUnit.eval(variableStore);
                } catch (UnitConfigurationException e) {
                    throw new CommandConfigurationException("One of the unit is not properly configured:",e);
                }
            }
        }
        return variableStore;
    }
}
