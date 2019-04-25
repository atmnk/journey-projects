package com.atmaram.mocker;

import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.matching.UrlParser;
import com.atmaram.mocker.server.Http;
import com.atmaram.mocker.server.HttpServerContext;
import com.atmaram.mocker.data.Tag;

import java.util.List;

import static com.atmaram.mocker.server.RequestTemplateBuilder.get;
import static com.atmaram.mocker.server.RequestTemplateBuilder.post;
import static com.atmaram.mocker.server.ResponseTemplateBuilder.okJson;
//import static com.atmaram.mocker.server.ResponseTemplateBuilder.okHtml;
import static com.atmaram.mocker.body.json.ObjectBuilder.*;

public class Test {
//    @org.junit.Test
//    public void test1(){
//        Http http=new Http(new HttpServerContext(8080));
//        http.given(get("/foo/create\\?country=${country}")).respondWith(okJson(
//                object(pair("id",e("").save("id"))).save("foo")
//        ));
//        http.given(get("/baar/create\\?country=${country}")).respondWith(okJson(
//                object(pair("id",e("").save("id"))).save("foo")
//        ));
//        http.given(get("/foo/all\\?country=${country}")).respondWith(okJson(
//                all(object(pair("id",e("$id"))),"foo")
//        ));
//
////        http.given(get("/world")).respondWith(okHtml("<b>World</b>"));
////        http.given(post("/hello").withJsonBody("{\"status\":1}")).respondWith(okJson("Hello"));
//        http.join();
//    }
//
//    @org.junit.Test
//    public void text2() throws Exception{
//        List<Tag> data= UrlParser.extract(new DataPool(),"/api/home/${home.id}/${place.id}","/api/home/2/3");
//    }
//    @org.junit.Test
//    public void text3() throws Exception{
//        UrlParser.extractJSONBody("(?<country>\\{\"name\":(?<name>(\\{\"palce\":(.+?)\\}))\\})","{\"name\":{\"palce\":\"pune\"}},{\"name\":{\"palce\":\"pune\"}},{\"name\":{\"palce\":\"pune\"}}");
//    }
//    @org.junit.Test
//    public void text4() throws Exception{
//        Http http=new Http(new HttpServerContext(8080));
//        http.given(post("/country/create").withBody(object(pair("name",p("name").save("name"))).save("country"))).respondWith(okJson(
//                object(pair("id",e("").save("id")))
//        ));
//        http.given(post("/foo/create\\?country=${country.id}").withBody(object(pair("name",p("name").save("name"))).save("foo"))).respondWith(okJson(
//                all(object(pair("id",e("").save("id")),
//                        pair("name",e("$name"))),"foo")
//        ));
//        http.given(get("/foo/all\\?country=${country.id}")).respondWith(okJson(
//                all(object(pair("id",e("$id")),
//                        pair("name",e("$name"))),"foo")
//        ));
//        http.join();
//    }
}
