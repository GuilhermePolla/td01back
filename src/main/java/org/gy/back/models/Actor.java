package org.gy.back.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
@Document("actor")
@Getter
@Setter
public class Actor extends DefaultEntity {
    private String name;
    private HashSet<String> works = new HashSet<>();

    public Actor(String name){
        this.name = name;
    }

    public void addToWorks (String movie){
        this.works.add(movie);
    }
}
