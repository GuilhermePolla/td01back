package org.gy.back.models.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gy.back.models.Actor;
import org.gy.back.models.Movie;

@Data
@AllArgsConstructor
public class ActorsMovieVo {
    private Actor from;
    private Actor to;
    private Movie movie;
}
