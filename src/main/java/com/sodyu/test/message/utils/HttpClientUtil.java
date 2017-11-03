package com.sodyu.test.message.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpMethod;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by yuhp on 2017/10/27.
 */
public class HttpClientUtil {

    public static HttpResponse execute(HttpMethod method, String url, Map<String,String> params,Map<String,String> headers) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClient client = null;
        HttpRequestBase requestBase = null;
        HttpResponse response = null;
        if (method == HttpMethod.GET) {
            requestBase = new HttpGet(url);
        } else if (method == HttpMethod.POST) {
            requestBase = new HttpPost(url);
        } else if(method == HttpMethod.PUT){
            requestBase = new HttpPut(url);
        }else{
            return null;
        }
        //设置参数
        if (params != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if (MapUtils.isNotEmpty(params)) {
                Set<Map.Entry<String, String>> set = params.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext(); ) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                        list.add(new BasicNameValuePair(header.getKey(), header.getValue()));
                    }
                }
            }
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            StringEntity requestEntity = new StringEntity(list.toString(), ContentType.APPLICATION_JSON);
            ((HttpEntityEnclosingRequestBase) requestBase).setEntity(requestEntity);
        }

        //setHeader
        if (MapUtils.isNotEmpty(headers)) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext(); ) {
                Map.Entry<String, String> header = it.next();
                if (header != null) {
                    requestBase.setHeader(header.getKey(), header.getValue());
                }
            }
        }
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier());
        client = httpClientBuilder.build();
        response = client.execute(requestBase);
        return response;
    }
}
