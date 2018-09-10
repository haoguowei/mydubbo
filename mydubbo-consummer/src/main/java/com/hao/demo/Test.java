package com.hao.demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Created by haoguowei. Time 2018/9/10 14:21 Desc
 */
public class Test {

    public static void main(String[] args){

        for (int i=0;i<3;i++){
            int finalI = i;
            new Thread(() -> {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet("http://localhost:8080/");

                HttpResponse response = null;
                try {
                    response = client.execute(request);

                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(finalI + ">>>>>>"+result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        while (true){

        }
    }

}
