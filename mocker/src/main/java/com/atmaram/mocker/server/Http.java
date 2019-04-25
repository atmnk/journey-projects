package com.atmaram.mocker.server;

import com.atmaram.mocker.data.DataPool;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import java.util.ArrayList;
import java.util.List;

public class Http {
    Server jettyServer;
    Handler handler;
    DataPool dataPool=new DataPool();
    List<MappingBuilder> mappingBuilders=new ArrayList<>();
    public Http(Server server) {
        this.jettyServer = server;
        handler=new Handler(this);
    }
    public MappingBuilder given(RequestTemplateBuilder requestTemplateBuilder){
        MappingBuilder builder=new MappingBuilder(requestTemplateBuilder.toRequestTemplate());
        mappingBuilders.add(builder);
        return builder;
    }
    public Http(HttpServerContext context) {
        this.jettyServer=new Server();
        ServerConnector connector=new ServerConnector(jettyServer);
        connector.setPort(context.port);
        jettyServer.setConnectors(new Connector[]{connector});
        handler=new Handler(this);
        jettyServer.setHandler(handler);
        this.start();
    }
    public void start(){
        try {
            jettyServer.start();
//            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void join(){
        try {
            jettyServer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class MappingBuilder{
        RequestTemplate requestTemplate;
        ResponseTemplate responseTemplate;
        public MappingBuilder(RequestTemplate requestTemplate) {
            this.requestTemplate = requestTemplate;
        }

        public Mapping toMapping(){
            return new Mapping(requestTemplate,responseTemplate);
        }
        public void respondWith(ResponseTemplateBuilder responseTemplateBuilder){
            responseTemplate=responseTemplateBuilder.toResponseTemplate();
            handler.addMapping(this.toMapping());
        }
    }

}
