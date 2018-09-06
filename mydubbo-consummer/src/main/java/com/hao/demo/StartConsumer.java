package com.hao.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


/**
 * Hello world!
 *
 */
@ImportResource({"classpath*:*.xml"})
@SpringBootApplication
public class StartConsumer {

    public static void main( String[] args ) {
        SpringApplication.run(StartConsumer.class, args);
    }
}
