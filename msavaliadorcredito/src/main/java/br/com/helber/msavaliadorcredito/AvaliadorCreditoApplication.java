package br.com.helber.msavaliadorcredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AvaliadorCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvaliadorCreditoApplication.class, args);
    }

}
