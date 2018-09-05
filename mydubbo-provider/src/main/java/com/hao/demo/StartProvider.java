package com.hao.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@ImportResource({"classpath*:*.xml"})
@SpringBootApplication
public class StartProvider {

    public static void main( String[] args ) {
        SpringApplication application = new SpringApplication(StartProvider.class);
        application.run(args);
    }
}
