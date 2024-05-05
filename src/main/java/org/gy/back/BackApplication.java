package org.gy.back;

import org.gy.back.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BackApplication  implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }

    @Override
    public void run(String... args){

    }
}
