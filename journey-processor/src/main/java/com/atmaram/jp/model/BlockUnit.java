package com.atmaram.jp.model;
import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

@Data
public class BlockUnit extends Unit {
    JSONArray stepLogObject=new JSONArray();
    String counterVariable;
    String filter="[]";
    JSONArray sort=new JSONArray();
    List<Unit> units;
    List<EnvironmentVariable> variables;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        VariableStore newVariableStore=new VariableStore();
        Variable resolvedLoopVariable=variableStore.getResolved(counterVariable,"List");
        if(resolvedLoopVariable!=null) {
            newVariableStore.resolve(resolvedLoopVariable.getInner_variables());
            JSONTemplate filterTemplate = null;
            try {
                filterTemplate = JSONTemplate.parse(filter);
                newVariableStore.add(filterTemplate.getVariables());
            } catch (TemplateParseException e) {
                throw new UnitConfigurationException("Invalid Template in filter",this.name,e);
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
            blockUnit.setFilter(((JSONTemplate)filterTemplate.fill(valueStore.getValues(),true)).toJSONCompatibleObject().toString());
        } catch (TemplateParseException e) {
            e.printStackTrace();
            System.out.println("Filter Template:"+filter);
        }
        blockUnit.setSort(this.sort);
        blockUnit.parentLogObject=this.parentLogObject;
        return blockUnit;
    }
    public void sortList(List<HashMap<String,Object>> list,JSONArray sort){
        Collections.sort(list,(o1,o2)-> compare(o1,o2,sort));
    }
    public int compare(HashMap<String,Object> o1,HashMap<String,Object> o2,JSONArray sort){
        int ret=0;
        for (Object oCriteria:
             sort) {
            JSONObject joCriteria=(JSONObject)oCriteria;
            String field=(String)joCriteria.get("field");
            String order=(String)joCriteria.get("order");
            if(o1.containsKey(field) && o2.containsKey(field)){
                Object val1=o1.get(field);
                Object val2=o2.get(field);
                if(val1 instanceof Number && val2 instanceof Number){
                    double dV1=((Number)val1).doubleValue();
                    double dV2=((Number)val2).doubleValue();
                    if(order.toLowerCase().equals("asc")){
                        ret = Double.compare(dV1,dV2);
                    } else {
                        ret = Double.compare(dV2,dV1);
                    }

                }
                if((val1 instanceof Number && !(val2 instanceof Number))||(val2 instanceof Number && !(val1 instanceof Number))){
                    continue;
                }
                if(val1 instanceof String && val2 instanceof String){
                    String sV1=(String)val1;
                    String sV2=(String)val2;
                    if(order.toLowerCase().equals("asc")){
                        ret = sV1.compareTo(sV2);
                    } else {
                        ret = sV2.compareTo(sV1);
                    }
                }
                if(ret!=0)
                    return ret;
            }
        }
        return ret;
    }
    @Override
    public ValueStore execute(ValueStore valueStore,int index){
        logObject.put("iterations",stepLogObject);
        logObject.put("type","block");
        this.printStartExecute(index);
        if(valueStore.getValues().containsKey(counterVariable)) {
            List<HashMap<String,Object>> counterValuesOriginal=(List<HashMap<String,Object>>)valueStore.getValues().get(counterVariable);
            List<HashMap<String,Object>> counterValues=new ArrayList<>();
            counterValues.addAll(counterValuesOriginal);
            sortList(counterValues,sort);
            int counter=1;
            JSONArray jaFilter=new JSONArray();
            ValueStore newValueStore=new ValueStore();
            newValueStore.add(valueStore.getValues());
            for (HashMap<String,Object> counterValue:
                 counterValues) {
                boolean continueLoop=false;
                try {
                    JSONTemplate filterTemplate=JSONTemplate.parse(filter);
                    jaFilter=(JSONArray) filterTemplate.fill(counterValue).toJSONCompatibleObject();
                } catch (TemplateParseException e) {
                    e.printStackTrace();
                }
                for(Object filterObject:jaFilter){
                    JSONObject joFilterObject=(JSONObject)filterObject;
                    String match=(String)joFilterObject.get("match");
                    if(match.equalsIgnoreCase("equals")){
                        if(!joFilterObject.get("one").equals(joFilterObject.get("two"))){
                            continueLoop=true;
                            break;
                        }
                    } else if(match.equalsIgnoreCase("doesNotEqual")){
                        if(joFilterObject.get("one").equals(joFilterObject.get("two"))){
                            continueLoop=true;
                            break;
                        }
                    }
                }
                if(continueLoop)
                    continue;
                newValueStore.add(counterValue);
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
                    Unit filledUnit=
                    unit.fill(newValueStore,false);
                    filledUnit.parentLogObject=loopLogObject;
                    filledUnit.execute(newValueStore,index+2);
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                this.print(index+1,"Done Loop "+counter++);
            }
            valueStore.addAdditionalKeepingOriginal(newValueStore.getValues());
        }
        this.printDoneExecute(index);
        return valueStore;
    }
}
