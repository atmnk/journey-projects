package com.atmaram.jp.model;

import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.text.TextTemplate;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
@Data
public class StaticLoopUnit extends Unit{
    String counterVariable;
    int times=0;
    List<Unit> units;
    List<EnvironmentVariable> variables;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        VariableStore newVariableStore=new VariableStore();
        if(variables!=null) {
            for (int i = 0; i < variables.size(); i++) {
                EnvironmentVariable environmentVariable = variables.get(i);
                TextTemplate textTemplate = null;
                try {
                    textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                } catch (TemplateParseException e) {
                    throw new UnitConfigurationException("Invalid Template in variable: "+environmentVariable.getName(),this.name,e);
                }
                newVariableStore.add(textTemplate.getVariables());
                Variable variable = new Variable();
                variable.setName(environmentVariable.getName());
                variable.setType("String");
                newVariableStore.resolve(Arrays.asList(variable));
            }
        }
        for (Unit unit:
                units) {
            try {
                unit.eval(newVariableStore);
            }catch (UnitConfigurationException ex){
                throw new UnitConfigurationException("Unit not properly configured in block unit: "+this.getName(),this.name,ex);
            }
        }

        variableStore.add(newVariableStore.getVariables());
        Variable variableToResolve=new Variable();
        variableToResolve.setName(counterVariable);
        variableToResolve.setType("List");
        variableToResolve.setInner_variables(newVariableStore.getResolvedVariables());
        variableStore.resolve(Arrays.asList(variableToResolve));
    }
    @Override
    public Unit fill(ValueStore valueStore) {
        StaticLoopUnit staticLoopUnit=new StaticLoopUnit();
        List<Unit> newUnits=new ArrayList<>();
        for (Unit unit:
                units) {
            Unit newUnit=unit.fill(valueStore);
            newUnits.add(newUnit);
        }
        staticLoopUnit.setName(this.getName());
        staticLoopUnit.setUnits(newUnits);
        staticLoopUnit.setWait(this.wait);
        staticLoopUnit.setCounterVariable(this.counterVariable);
        staticLoopUnit.setTimes(this.times);
        staticLoopUnit.setVariables(variables);
        staticLoopUnit.setWait(this.wait);
        return staticLoopUnit;
    }

    @Override
    public ValueStore execute(ValueStore valueStore,int index){
        this.printStartExecute(index);
        List<HashMap<String,Object>> constructed=new ArrayList<>();
        for (int i=0;i<times;i++) {
            ValueStore newValueStore=new ValueStore();
            if(variables!=null) {
                for (int j = 0; j < variables.size(); i++) {
                    EnvironmentVariable environmentVariable = variables.get(j);
                    TextTemplate textTemplate = null;
                    try {
                        textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                    } catch (TemplateParseException e) {
                        e.printStackTrace();
                        System.out.println("Environment Variable Template:"+environmentVariable.getValueTemplate());
                    }
                    String envValue = null;
                    envValue = textTemplate.fill(valueStore.getValues()).fill(newValueStore.getValues()).toStringTemplate();
                    newValueStore.add(environmentVariable.getName(), envValue);
                }
            }
            this.print(index+1,"Loop "+i);
            for (Unit unit :
                    units) {
                unit.fill(newValueStore).execute(newValueStore,index+2);
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            constructed.add(newValueStore.getValues());
            this.print(index+1,"Done Loop "+i);
        }
        valueStore.add(counterVariable,constructed);
        this.printDoneExecute(index);
        return valueStore;
    }
}
