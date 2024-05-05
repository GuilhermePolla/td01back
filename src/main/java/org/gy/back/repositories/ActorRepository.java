package org.gy.back.repositories;

import org.gy.back.models.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ActorRepository extends MongoRepository<Actor, String> {

    @Query("{id: ?0}")
    Actor getById(String id);

    @Query("{name: ?0}")
    Actor getByName(String name);

}
