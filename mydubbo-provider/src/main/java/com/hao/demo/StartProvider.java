package com.hao.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@ImportResource({"classpath*:*.xml"})
@SpringBootApplication
public class StartProvider implements CommandLineRunner {

    public static void main( String[] args ) {
        SpringApplication.run(StartProvider.class, args);
        while (true){

        }
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
