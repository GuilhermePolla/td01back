package org.gy.back.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gy.back.models.Actor;
import org.gy.back.models.Movie;
import org.gy.back.models.vos.ActorsMovieVo;
import org.gy.back.repositories.ActorRepository;
import org.gy.back.repositories.MovieRepository;
import org.gy.back.services.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private JsonService jsonService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @PostMapping("/load-from-json")
    public ResponseEntity<String> loadFromJson() {
        try {
            JsonNode jsonNode = jsonService.fileToJsonNode();
            List<Movie> movies = new ArrayList<>();
            HashMap<String, Actor> actors = new HashMap<>();

            for (JsonNode node : jsonNode) {
                Movie movie = objectMapper.treeToValue(node, Movie.class);
                movies.add(movie);
                for (String castMember : movie.getCast()) {
                    Actor actor;
                    if (actors.containsKey(castMember)) {
                        actor = actors.get(castMember);
                    } else {
                        actor = new Actor(castMember);
                    }
                    actor.addToColegas(movie.getCast());
                    actors.put(castMember, actor);
                }
            }
            movieRepository.saveAll(movies);
            actorRepository.saveAll(actors.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Sucesso");
    }

    @GetMapping("/movie/getall")
    public ResponseEntity<List<Movie>> getAllMovies() throws Exception {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new Exception("Empty list");
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/actor/getall")
    public ResponseEntity<List<Actor>> getAllActors() throws Exception {
        List<Actor> actors = actorRepository.findAll();
        if (actors.isEmpty()) {
            throw new Exception("Empty list");
        }
        return ResponseEntity.ok(actors);
    }

    @GetMapping("/movie/getbyid/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) throws Exception {
        Movie movie = movieRepository.getById(id);
        if (movie == null) {
            throw new Exception("Movie not found");
        }
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/movie/getbytitle")
    public ResponseEntity<Movie> getMovieByTitle(@RequestBody Map<String, Object> requestBody) throws Exception {
        String title = (String) requestBody.get("title");

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("O título do filme não foi fornecido");
        }

        Movie movie = movieRepository.getByTitle(title);
        if (movie == null) {
            throw new Exception("Filme não encontrado");
        }
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/actor/getbyid/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable String id) throws Exception {
        Actor actor = actorRepository.getById(id);
        if (actor == null) {
            throw new Exception("Actor not found");
        }
        return ResponseEntity.ok(actor);
    }

    @GetMapping("/actor/getbyname")
    public ResponseEntity<Actor> getActorByName(@RequestBody Map<String, Object> requestBody) throws Exception {
        String name = (String) requestBody.get("name");

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("O nome do ator não foi fornecido");
        }

        Actor actor = actorRepository.getByName(name);
        if (actor == null) {
            throw new Exception("Ator não encontrado");
        }
        return ResponseEntity.ok(actor);
    }


    @GetMapping("/getrelations")
    public ResponseEntity<List<ActorsMovieVo>> getRelation(@RequestBody Map<String, Object> requestBody){
        String starting = (String) requestBody.get("starting");
        String target = (String) requestBody.get("target");

        if (starting == null || starting.isEmpty() || target == null || target.isEmpty()) {
            throw new IllegalArgumentException("Nomes dos atores de início e destino não foram fornecidos");
        }

        Actor startingActor = actorRepository.getByName(starting);
        Actor targetActor = actorRepository.getByName(target);

        List<ActorsMovieVo> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorsMovieVo acv = new ActorsMovieVo(
                    startingActor, targetActor, new Movie("xxx", "A volta dos que não foram", new ArrayList<>()));
            result.add(acv);
        }
        return ResponseEntity.ok(result);
    }

}
