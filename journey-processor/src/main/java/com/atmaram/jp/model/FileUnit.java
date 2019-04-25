package com.atmaram.jp.model;

import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import com.atmaram.tp.template.text.TextTemplate;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

@Data
public class FileUnit extends Unit {
    JSONArray stepLogObject=new JSONArray();
    String counterVariable;
    String filter="[]";
    JSONArray sort=new JSONArray();
    String filename="out.csv";
    String lineTemplate="";
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
            try {
                TextTemplate textTemplate = TextTemplate.parse(lineTemplate);
                newVariableStore.add(textTemplate.getVariables());
            } catch (TemplateParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Unit fill(ValueStore valueStore, boolean lazy) {
        FileUnit fileUnit=new FileUnit();
        JSONTemplate filterTemplate = null;
        try {
            filterTemplate = JSONTemplate.parse(filter);
            fileUnit.setFilter(((JSONTemplate)filterTemplate.fill(valueStore.getValues(),true)).toJSONCompatibleObject().toString());
        } catch (TemplateParseException e) {
            e.printStackTrace();
            System.out.println("Filter Template:"+filter);
        }
        fileUnit.setSort(this.sort);
        fileUnit.parentLogObject=this.parentLogObject;
        return fileUnit;
    }
    public void sortList(List<HashMap<String,Object>> list, JSONArray sort){
        Collections.sort(list,(o1, o2)-> compare(o1,o2,sort));
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
    public ValueStore execute(ValueStore valueStore, int index){
        PrintWriter out;
        try {
            out=new PrintWriter("log/"+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return valueStore;
        }
        logObject.put("iterations",stepLogObject);
        logObject.put("type","file");
        this.printStartExecute(index);
        if(valueStore.getValues().containsKey(counterVariable)) {
            List<HashMap<String,Object>> counterValuesOriginal=(List<HashMap<String,Object>>)valueStore.getValues().get(counterVariable);
            List<HashMap<String,Object>> counterValues=new ArrayList<>();
            counterValues.addAll(counterValuesOriginal);
            sortList(counterValues,sort);
            int counter=1;
            JSONArray jaFilter=new JSONArray();

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
                ValueStore newValueStore=new ValueStore();
                newValueStore.setValues(counterValue);
                if(continueLoop)
                    continue;
                List<String> added=newValueStore.addAdditionalKeepingOriginal(valueStore.getValues());
                this.print(index+1,"Loop "+counter);
                JSONObject lineObject=new JSONObject();
                stepLogObject.add(lineObject);
                try {
                    TextTemplate textTemplate = TextTemplate.parse(lineTemplate);
                    TextTemplate filled=textTemplate.fill(newValueStore.getValues(),false);
                    lineObject.put("type","line");
                    String toPrint=filled.toValue();
                    lineObject.put("value",toPrint);
                    out.println(toPrint);
                } catch (TemplateParseException e) {
                    e.printStackTrace();
                }
                newValueStore.remove(added);
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
