package com.atmaram.jp.processor;

public class Processor {
//    RestClient restClient;
//    public Processor(){
//        restClient=RestClient.get();
//    }
//    public Processor(RestClient restClient){
//        this.restClient=restClient;
//    }
//    public  VariableStore eval(Command command,List<Environment> environments) throws TemplateParseException {
//        VariableStore variableStore=new VariableStore();
//        for (Environment environment:
//             environments) {
//            if(environment!=null && environment.getVariables()!=null) {
//                for (int i = 0; i < environment.getVariables().size(); i++) {
//                    EnvironmentVariable environmentVariable = environment.getVariables().get(i);
//                    TextTemplate textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
//                    variableStore.add(textTemplate.getVariables());
//                    Variable variable = new Variable();
//                    variable.setName(environmentVariable.getName());
//                    variable.setType("String");
//                    variableStore.resolve(Arrays.asList(variable));
//                }
//            }
//        }
//        if(command.getVariables()!=null) {
//            for (int i = 0; i < command.getVariables().size(); i++) {
//                EnvironmentVariable environmentVariable = command.getVariables().get(i);
//                TextTemplate textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
//                variableStore.add(textTemplate.getVariables());
//                Variable variable = new Variable();
//                variable.setName(environmentVariable.getName());
//                variable.setType("String");
//                variableStore.resolve(Arrays.asList(variable));
//            }
//        }
//        if(command.getUnits()!=null) {
//            for (int i = 0; i < command.getUnits().size(); i++) {
//                Unit currentUnit = command.getUnits().get(i);
//                if (currentUnit.getRequestHeaders() != null) {
//                    List<Variable> requestHeaderVariables = new ArrayList<>();
//
//                    for (int j = 0; j < currentUnit.getRequestHeaders().size(); j++) {
//                        RequestHeader requestHeader = currentUnit.getRequestHeaders().get(j);
//                        TextTemplate textTemplate = TextTemplate.parse(requestHeader.getValueTemplate());
//                        requestHeaderVariables.addAll(textTemplate.getVariables());
//                    }
//
//                    variableStore.add(requestHeaderVariables);
//                }
//
//                List<Variable> urlVariables = TextTemplate.parse(currentUnit.getUrlTemplate()).getVariables();
//                variableStore.add(urlVariables);
//
//                if (currentUnit instanceof PostUnit) {
//                    List<Variable> bodyVariables = JSONTemplate.parse(((PostUnit) currentUnit).getRequestTemplate()).getVariables();
//                    variableStore.add(bodyVariables);
//                }
//                if (currentUnit.getResponseHeaders() != null) {
//                    List<ResponseHeader> responseHeaders = currentUnit.getResponseHeaders();
//                    List<Variable> responseHeaderVariables = new ArrayList<>();
//                    for (int j = 0; j < responseHeaders.size(); j++) {
//                        ResponseHeader responseHeader = responseHeaders.get(j);
//                        Variable variable = new Variable();
//                        variable.setName(responseHeader.getVariable());
//                        variable.setType("String");
//                        responseHeaderVariables.add(variable);
//                    }
//                    variableStore.resolve(responseHeaderVariables);
//                }
//                if(!currentUnit.getResponseTemplate().trim().equals(""))
//                {
//                    List<Variable> outputVariables = JSONTemplate.parse(currentUnit.getResponseTemplate()).getVariables();
//                    variableStore.resolve(outputVariables);
//                }
//            }
//        }
//        return variableStore;
//    }
//    public  ValueStore execute(Command command, List<Environment> environments,ValueStore valueStore) throws TemplateParseException, ValueNotFoundException, ParseException {
//        if(command.getVariables()!=null) {
//            for (int i = 0; i < command.getVariables().size(); i++) {
//                EnvironmentVariable environmentVariable = command.getVariables().get(i);
//                TextTemplate textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
//                String envValue = textTemplate.fill(valueStore.getValues());
//                valueStore.add(environmentVariable.getName(), envValue);
//            }
//        }
//        for (Environment environment:
//             environments) {
//            if(environment!=null && environment.getVariables()!=null) {
//                for (int i = 0; i < environment.getVariables().size(); i++) {
//                    EnvironmentVariable environmentVariable = environment.getVariables().get(i);
//                    TextTemplate textTemplate = TextTemplate.parse(environmentVariable.getValueTemplate());
//                    String envValue = textTemplate.fill(valueStore.getValues());
//                    valueStore.add(environmentVariable.getName(), envValue);
//                }
//            }
//        }
//        if(command.getUnits()!=null) {
//            for (int i = 0; i < command.getUnits().size(); i++) {
//                Unit currentUnit = command.getUnits().get(i);
//                String url = currentUnit.getUrlTemplate();
//                String outputTemplate = currentUnit.getResponseTemplate();
//
//                String filledURL = TextTemplate.parse(url).fill(valueStore.getValues());
//                JSONAware filledBody = null;
//                if (currentUnit instanceof PostUnit) {
//                    String body = ((PostUnit) currentUnit).getRequestTemplate();
//                    filledBody = JSONTemplate.parse(body).fill(valueStore.getValues());
//                }
//                HashMap<String, String> headers = new HashMap<>();
//                if (currentUnit.getRequestHeaders() != null) {
//                    for (int j = 0; j < currentUnit.getRequestHeaders().size(); j++) {
//                        RequestHeader requestHeader = currentUnit.getRequestHeaders().get(j);
//                        headers.put(requestHeader.getName(), TextTemplate.parse(requestHeader.getValueTemplate()).fill(valueStore.getValues()));
//                    }
//                }
//                HttpResponse<String> output = null;
//                try {
//                    if (currentUnit instanceof PostUnit) {
//                        output = restClient.post(filledURL, headers, filledBody.toJSONString());
//                    } else {
//                        output = restClient.get(filledURL, headers);
//                    }
//                } catch (UnirestException e) {
//                    e.printStackTrace();
//                }
//                if (currentUnit.getResponseHeaders() != null) {
//                    List<ResponseHeader> responseHeaders = currentUnit.getResponseHeaders();
//                    for (int j = 0; j < responseHeaders.size(); j++) {
//                        ResponseHeader responseHeader = responseHeaders.get(j);
//                        valueStore.add(responseHeader.getVariable(), output.getHeaders().getFirst(responseHeader.getName()));
//                    }
//                }
//                if(!outputTemplate.trim().equals("")) {
//                    HashMap<String, Object> extractedValues = JSONTemplate.parse(outputTemplate).extract((JSONAware) (new JSONParser()).parse(output.getBody()));
//                    valueStore.add(extractedValues);
//                }
//                if(currentUnit.getWait()!=0){
//                    try {
//                        Thread.sleep(currentUnit.getWait());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return valueStore;
//    }
//
//    public  VariableStore eval(Command command) throws TemplateParseException {
//        return eval(command,new ArrayList<>());
//    }
//    public  ValueStore execute(Command command,ValueStore valueStore) throws TemplateParseException, ValueNotFoundException, ParseException {
//        return execute(command,new ArrayList<>(),valueStore);
//    }
}
