package com.atm.ss.controllers;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(path = "/test")
public class TestController {
    @GetMapping(path = "/one")
    public JSONObject getResponse() throws ParseException {
        JSONParser parser=new JSONParser();
        JSONObject obj=(JSONObject) parser.parse("{\n" +
                "\t\"results\": {\n" +
                "\t\t\"ABC\": {\n" +
                "\t\t\t\"01\": {\n" +
                "\t\t\t\t\"01\": {\n" +
                "\t\t\t\t\t\"name\": \"Atmaram\",\n" +
                "\t\t\t\t\t\"palce\": \"Pune\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"02\": {\n" +
                "\t\t\t\t\"01\": {\n" +
                "\t\t\t\t\t\"name\": \"Yogesh\",\n" +
                "\t\t\t\t\t\"palce\": \"Mumbai\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"PQR\": {\n" +
                "\t\t\t\"01\": {\n" +
                "\t\t\t\t\"01\": {\n" +
                "\t\t\t\t\t\"name\": \"PQR Atmaram\",\n" +
                "\t\t\t\t\t\"palce\": \"PQR Pune\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"02\": {\n" +
                "\t\t\t\t\"01\": {\n" +
                "\t\t\t\t\t\"name\": \"PQR Yogesh\",\n" +
                "\t\t\t\t\t\"palce\": \"PQR Mumbai\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\n" +
                "}");
        return obj;
    }
}
