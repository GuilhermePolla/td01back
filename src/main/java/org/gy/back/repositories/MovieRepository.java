package org.gy.back.repositories;

import org.gy.back.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MovieRepository extends MongoRepository<Movie, String> {

    @Query("{id: ?0}")
    Movie getById(String id);

    @Query("{title: ?0}")
    Movie getByTitle(String title);
}
