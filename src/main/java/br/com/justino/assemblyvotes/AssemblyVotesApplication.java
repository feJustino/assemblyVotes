package br.com.justino.assemblyvotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssemblyVotesApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssemblyVotesApplication.class, args);
    }
}