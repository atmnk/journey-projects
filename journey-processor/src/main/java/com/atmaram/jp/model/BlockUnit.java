package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
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
public class BlockUnit extends Unit {
    String counterVariable;
    List<Unit> units;
    List<EnvironmentVariable> variables;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        VariableStore newVariableStore=new VariableStore();
        newVariableStore.resolve(variableStore.getResolved(counterVariable,"List").getInner_variables());
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
    }
    @Override
    public Unit fill(ValueStore valueStore) {
        BlockUnit blockUnit=new BlockUnit();
        List<Unit> newUnits=new ArrayList<>();
        for (Unit unit:
                units) {
            Unit newUnit=unit.fill(valueStore);
            newUnits.add(newUnit);
        }
        blockUnit.setName(this.getName());
        blockUnit.setUnits(newUnits);
        blockUnit.setWait(this.wait);
        blockUnit.setCounterVariable(this.counterVariable);
        blockUnit.setVariables(variables);
        blockUnit.setWait(this.wait);
        return blockUnit;
    }

    @Override
    public ValueStore execute(ValueStore valueStore,int index){
        this.printStartExecute(index);
        if(valueStore.getValues().containsKey(counterVariable)) {
            List<HashMap<String,Object>> counterValues=(List<HashMap<String,Object>>)valueStore.getValues().get(counterVariable);
            int counter=1;
            for (HashMap<String,Object> counterValue:
                 counterValues) {
                ValueStore newValueStore=new ValueStore();
                newValueStore.setValues(counterValue);
                if(variables!=null) {
                    for (int i = 0; i < variables.size(); i++) {
                        EnvironmentVariable environmentVariable = variables.get(i);
                        TextTemplate textTemplate = null;
                        try {
                            textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
                        } catch (TemplateParseException e) {
                            e.printStackTrace();
                        }
                        String envValue = null;
                        envValue = textTemplate.fill(valueStore.getValues()).fill(newValueStore.getValues()).toValue();
                        newValueStore.add(environmentVariable.getName(), envValue);
                    }
                }
                this.print(index+1,"Loop "+counter);
                for (Unit unit :
                        units) {
                    unit.fill(newValueStore).execute(newValueStore,index+2);
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                this.print(index+1,"Done Loop "+counter++);
            }
        }
        this.printDoneExecute(index);
        return valueStore;
    }
}
