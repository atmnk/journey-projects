package com.atmaram.jp.model;

import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Data
public class StaticLoopUnit extends Unit{
    JSONArray stepLogObject=new JSONArray();
    String counterVariable;
    String times="0";
    List<Unit> units;
    List<EnvironmentVariable> variables;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        VariableStore newVariableStore=new VariableStore();
        newVariableStore.resolve(variableStore.getResolvedVariables());
        Variable variableCounter=new Variable();
        variableCounter.setName(counterVariable);
        variableCounter.setType("String");
        newVariableStore.resolve(Arrays.asList(variableCounter));
        try {
            TextTemplate textTemplate=TextTemplate.parse(times);
            newVariableStore.add(textTemplate.getVariables());
        } catch (TemplateParseException e) {
            throw new UnitConfigurationException("Unit not properly configured in static loop unit for times: "+this.getName(),this.name,e);
        }
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
                throw new UnitConfigurationException("Unit not properly configured in static loop unit: "+this.getName(),this.name,ex);
            }
        }

        variableStore.add(newVariableStore.getVariables());
        variableStore.resolve(newVariableStore.getResolvedVariables());
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
        String newTimes=times;
        try {
            TextTemplate textTemplate=TextTemplate.parse(times);
            newTimes=textTemplate.fill(valueStore.getValues()).toStringTemplate();

        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
        staticLoopUnit.setTimes(newTimes);
        staticLoopUnit.setVariables(variables);
        staticLoopUnit.setWait(this.wait);
        return staticLoopUnit;
    }

    @Override
    public ValueStore execute(ValueStore valueStore,int index){
        JSONArray prevLogObject=Runtime.currentLogObject;
        prevLogObject.add(logObject);
        logObject.put("iterations",stepLogObject);
        logObject.put("type","loop");
        Runtime.currentLogObject=stepLogObject;
        this.printStartExecute(index);
//        List<HashMap<String,Object>> constructed=new ArrayList<>();
        int iTimes=Integer.parseInt(times);
        for (int i=0;i<iTimes;i++) {
//            ValueStore newValueStore=new ValueStore();
            valueStore.add(counterVariable,i+1);
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
                    envValue = textTemplate.fill(valueStore.getValues()).fill(valueStore.getValues()).toStringTemplate();
                    valueStore.add(environmentVariable.getName(), envValue);
                }
            }
            JSONArray loopLogObject=new JSONArray();
            stepLogObject.add(loopLogObject);
            this.print(index+1,"Loop "+i);
            for (Unit unit :
                    units) {
                Runtime.currentLogObject=loopLogObject;
                unit.fill(valueStore).execute(valueStore,index+2);
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
//            constructed.add(newValueStore.getValues());
            this.print(index+1,"Done Loop "+i);
        }
//        valueStore.add(counterVariable,constructed);
        valueStore.remove(counterVariable);
        this.printDoneExecute(index);
        Runtime.currentLogObject=prevLogObject;
        return valueStore;
    }
}
