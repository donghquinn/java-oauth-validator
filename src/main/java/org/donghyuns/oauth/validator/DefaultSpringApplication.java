package org.donghyuns.oauth.validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DefaultSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefaultSpringApplication.class, args);
    }

}
