package org.gy.back.repositories;

import org.gy.back.models.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ActorRepository extends MongoRepository<Actor, String> {

    @Query("{id: ?0}")
    Actor getById(String id);

    @Query("{name: ?0}")
    Actor getByName(String name);

    @Query("{ 'name' : { $regex: ?0, $options: i } }")
    List<Actor> getByString(String regex);
}
