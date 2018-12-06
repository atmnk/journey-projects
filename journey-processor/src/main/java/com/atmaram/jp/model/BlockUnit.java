package com.atmaram.jp.model;
import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
@Data
public class BlockUnit extends Unit {
    JSONArray stepLogObject=new JSONArray();
    String counterVariable;
    String filter="{}";
    List<Unit> units;
    List<EnvironmentVariable> variables;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        VariableStore newVariableStore=new VariableStore();
        Variable resolvedLoopVariable=variableStore.getResolved(counterVariable,"List");
        if(resolvedLoopVariable!=null) {
            newVariableStore.resolve(resolvedLoopVariable.getInner_variables());

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
                    variableStore.add(newVariableStore.getVariables());
                    Variable variable=new Variable();
                    variable.setName(counterVariable);
                    variable.setType("List");
                    variable.setInner_variables(newVariableStore.getResolvedVariables());
                    variableStore.resolve(Arrays.asList(variable));

                }catch (UnitConfigurationException ex){
                    throw new UnitConfigurationException("Unit not properly configured in block unit: "+this.getName(),this.name,ex);
                }
            }
            JSONTemplate filterTemplate = null;
            try {
                filterTemplate = JSONTemplate.parse(filter);
                variableStore.add(filterTemplate.getVariables());
            } catch (TemplateParseException e) {
                throw new UnitConfigurationException("Invalid Template in filter",this.name,e);
            }
        }
    }
    @Override
    public Unit fill(ValueStore valueStore,boolean lazy) {
        BlockUnit blockUnit=new BlockUnit();
        List<Unit> newUnits=new ArrayList<>();
        for (Unit unit:
                units) {
            Unit newUnit=unit.fill(valueStore,true);
            newUnits.add(newUnit);
        }
        blockUnit.setName(this.getName());
        blockUnit.setUnits(newUnits);
        blockUnit.setWait(this.wait);
        blockUnit.setCounterVariable(this.counterVariable);
        blockUnit.setVariables(variables);
        blockUnit.setWait(this.wait);
        JSONTemplate filterTemplate = null;
        try {
            filterTemplate = JSONTemplate.parse(filter);
            blockUnit.setFilter(((JSONTemplate)filterTemplate.fill(valueStore.getValues())).toJSONCompatibleObject().toString());
        } catch (TemplateParseException e) {
            e.printStackTrace();
            System.out.println("Filter Template:"+filter);
        }
        return blockUnit;
    }

    @Override
    public ValueStore execute(ValueStore valueStore,int index){
        JSONArray prevLogObject=Runtime.currentLogObject;
        prevLogObject.add(logObject);
        logObject.put("iterations",stepLogObject);
        logObject.put("type","block");
        Runtime.currentLogObject=stepLogObject;
        this.printStartExecute(index);
        if(valueStore.getValues().containsKey(counterVariable)) {
            List<HashMap<String,Object>> counterValues=(List<HashMap<String,Object>>)valueStore.getValues().get(counterVariable);
            JSONObject jofilter=new JSONObject();
            try {
                jofilter= (JSONObject) new JSONParser().parse(filter);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Template:"+filter);
            }
            int counter=1;
            for (HashMap<String,Object> counterValue:
                 counterValues) {
                boolean continueLoop=false;
                for(Object key:jofilter.keySet()){
                    if(!counterValue.get(key).equals(jofilter.get(key))){
                        continueLoop=true;
                        break;
                    }
                }
                if(continueLoop)
                    continue;
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
                            System.out.println("Environment Variable Template:"+environmentVariable.getValueTemplate());
                        }
                        String envValue = null;
                        envValue = textTemplate.fill(valueStore.getValues()).fill(newValueStore.getValues()).toStringTemplate();
                        newValueStore.add(environmentVariable.getName(), envValue);
                    }
                }
                this.print(index+1,"Loop "+counter);
                JSONArray loopLogObject=new JSONArray();
                stepLogObject.add(loopLogObject);
                for (Unit unit :
                        units) {
                    Runtime.currentLogObject=loopLogObject;
                    unit.fill(newValueStore,false).execute(newValueStore,index+2);
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
        Runtime.currentLogObject=prevLogObject;
        return valueStore;
    }
}
