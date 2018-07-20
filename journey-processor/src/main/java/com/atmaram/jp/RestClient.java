package com.atmaram.jp;

import com.atmaram.jp.model.RequestHeader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

public class RestClient {
    private static RestClient restClient;

    private RestClient() {
        setupUnirest();
    }
    private void setupUnirest(){
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] certificate, String authType) {
                            return true;
                        }
                    })
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static RestClient get(){
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }
    public HttpResponse<String> get(String url, List<RequestHeader> headers) throws UnirestException {
        GetRequest getRequest= Unirest.get(url);
        if(headers!=null) {
            for (RequestHeader requestHeader :
                    headers) {
                getRequest = getRequest.header(requestHeader.getName(), requestHeader.getValueTemplate());
            }
        }
        return getRequest.asString();
    }
    public HttpResponse<String> post(String url,List<RequestHeader> headers,String body) throws UnirestException {
        HttpRequestWithBody postRequest= Unirest.post(url);
        if(headers!=null) {
            for (RequestHeader requestHeader :
                    headers) {
                postRequest = postRequest.header(requestHeader.getName(), requestHeader.getValueTemplate());
            }
        }
        return postRequest.body(body)
                .asString();
    }
    public HttpResponse<String> put(String url,List<RequestHeader> headers,String body) throws UnirestException {
        HttpRequestWithBody putRequest= Unirest.put(url);
        if(headers!=null) {
            for (RequestHeader requestHeader :
                    headers) {
                putRequest = putRequest.header(requestHeader.getName(), requestHeader.getValueTemplate());
            }
        }
        return putRequest.body(body)
                .asString();
    }
    public HttpResponse<String> patch(String url,List<RequestHeader> headers,String body) throws UnirestException {
        HttpRequestWithBody patchRequest= Unirest.patch(url);
        if(headers!=null) {
            for (RequestHeader requestHeader :
                    headers) {
                patchRequest = patchRequest.header(requestHeader.getName(), requestHeader.getValueTemplate());
            }
        }
        return patchRequest.body(body)
                .asString();
    }
    public HttpResponse<String> delete(String url,List<RequestHeader> headers,String body) throws UnirestException {
        HttpRequestWithBody deleteRequest= Unirest.delete(url);
        if(headers!=null) {
            for (RequestHeader requestHeader :
                    headers) {
                deleteRequest = deleteRequest.header(requestHeader.getName(), requestHeader.getValueTemplate());
            }
        }
        return deleteRequest.body(body)
                .asString();
    }
}
