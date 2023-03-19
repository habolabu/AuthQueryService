package edu.ou.authqueryservice;

import edu.ou.coreservice.annotation.BaseQueryAnnotation;
import org.springframework.boot.SpringApplication;

@BaseQueryAnnotation
public class AuthQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthQueryServiceApplication.class, args);
    }
}
