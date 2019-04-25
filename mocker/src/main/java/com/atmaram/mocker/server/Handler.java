package com.atmaram.mocker.server;

import com.atmaram.mocker.data.Tag;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Handler extends AbstractHandler {
    List<Mapping> mappings=new ArrayList<>();
    Http parent;

    public Handler(Http parent) {
        this.parent = parent;
    }

    public void addMapping(Mapping mapping){
        mappings.add(mapping);
    }
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        for (Mapping mapping:
             mappings) {
            try {
                List<Tag> tags=mapping.requestTemplate.grepRequest(request,parent.dataPool);
                response.setStatus(mapping.responseTemplate.status);
                response.setContentType(mapping.responseTemplate.contentType.content+"; charset=utf-8");
                out.println(mapping.responseTemplate.toBody(parent.dataPool,tags));
                break;
            } catch (Exception e){

            }
        }
        baseRequest.setHandled(true);
    }
}
