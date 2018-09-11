package com.hao.demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet("http://localhost:8080/");
                //                                HttpGet request = new HttpGet("http://localhost:28080/");
                //                HttpGet request = new HttpGet("http://localhost:38080/");

                HttpResponse response = null;
                try {
                    response = client.execute(request);

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(finalI + ">>>>>>" + result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        while (true) {

        }
    }
}
