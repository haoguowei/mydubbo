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
public class StartMid {
    public static void main( String[] args ) {
        SpringApplication application = new SpringApplication(StartMid.class);
        application.run(args);
    }
}
