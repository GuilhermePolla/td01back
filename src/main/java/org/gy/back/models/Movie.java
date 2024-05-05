package org.gy.back.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("movie")
@Setter
@Getter
public class Movie extends DefaultEntity{
    private String title;
    private List<String> cast;

    public Movie(String id, String title, List<String> cast){
        super(id);
        this.title = title;
        this.cast = cast;
    }
}
