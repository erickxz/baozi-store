package br.com.uninter.baozistore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal: inicia a aplicação Spring Boot (servidor web + JPA + H2).
 */
@SpringBootApplication
public class BaoziStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaoziStoreApplication.class, args);
    }
}
