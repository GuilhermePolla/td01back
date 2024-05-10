package org.gy.back.services;

import org.gy.back.models.Actor;
import org.gy.back.models.Movie;
import org.gy.back.models.vos.ActorsMovieVo;
import org.gy.back.repositories.ActorRepository;
import org.gy.back.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FindRelationsService {


    private MovieRepository movieRepository;
    private ActorRepository actorRepository;
    private HashMap<String, Actor> actorsHashMap = new HashMap<>();
    private HashSet<String> seenActors = new HashSet<>();
    private HashMap<String, Movie> moviesHashMap = new HashMap<>();
    private HashSet<String> seenMovies = new HashSet<>();

    private List<List<ActorsMovieVo>> result = new ArrayList<>();

    @Autowired
    public FindRelationsService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;

        List<Actor> actorList = actorRepository.findAll();
        List<Movie> movieList = movieRepository.findAll();
        actorList.forEach(actor -> {
            actorsHashMap.put(actor.getName(), actor);
        });
        movieList.forEach(movie -> {
            moviesHashMap.put(movie.getTitle(), movie);
        });
    }

    public List<List<ActorsMovieVo>> getRelations(String start, String target, Boolean getAll) {
        Actor starting = this.actorsHashMap.get(start);
        if (starting == null) {
            throw new RuntimeException("You've done goofed");
        }
        if (!actorsHashMap.containsKey(target)) {
            throw new RuntimeException("You've done goofed 2");
        }
        search(starting, target, getAll);
        return result;
    }


    private void search(Actor starting, String target, Boolean getAll) {
        LinkedList<List<ActorsMovieVo>> queue = new LinkedList<>();
        seenActors.add(starting.getName());

        for (String firstLayerMovieString : starting.getWorks()) {
            Movie firstLayerMovie = moviesHashMap.get(firstLayerMovieString);
            seenMovies.add(firstLayerMovieString);

            for (String firstLayerActorString : firstLayerMovie.getCast()) {
                if (!seenActors.contains(firstLayerActorString)) {
                    Actor firstLayerActor = actorsHashMap.get(firstLayerActorString);
                    ActorsMovieVo actorsMovieVoFirstLayer = new ActorsMovieVo(starting, firstLayerActor, firstLayerMovie);
                    List<ActorsMovieVo> firstLayerList = new ArrayList<>();
                    firstLayerList.add(actorsMovieVoFirstLayer);

                    if (firstLayerActorString.equals(target)) {
                        result.add(firstLayerList);
                        if(!getAll){
                            return;
                        }
                    } else {
                        queue.addLast(firstLayerList);
                        seenActors.add(firstLayerActorString);
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            List<ActorsMovieVo> currentRoute = queue.removeFirst();
            ActorsMovieVo amv = currentRoute.get(currentRoute.size() - 1);
            Actor fromActor = amv.getTo();

            for (String movieString : fromActor.getWorks()) {
                if (!seenMovies.contains(movieString)) {
                    seenMovies.add(movieString);
                    Movie movie = moviesHashMap.get(movieString);

                    for (String toActorString : movie.getCast()) {
                        if (!seenActors.contains(toActorString)) {
                            Actor toActor = actorsHashMap.get(toActorString);
                            ActorsMovieVo actorsMovieVo = new ActorsMovieVo(fromActor, toActor, movie);
                            List<ActorsMovieVo> newRoute = new ArrayList<>(currentRoute);
                            newRoute.add(actorsMovieVo);

                            if (toActorString.equals(target)) {
                                result.add(newRoute);
                                if(!getAll){
                                    return;
                                }
                            } else {
                                if (newRoute.size() < 5) {
                                    queue.addLast(newRoute);
                                }
                                seenActors.add(toActorString);
                            }
                        }
                    }
                }
            }
        }
    }

}

//    private void recursiveSearch(Actor starting, String target) {
//        for (String movieString : starting.getWorks()) {
//            if (!seenMovies.contains(movieString)) {
//                seenMovies.add(movieString);
//                Movie movie = moviesHashMap.get(movieString);
//                for (String actorString : movie.getCast()) {
//                    if(actorString.equals(target)){
//                        Actor actor = actorsHashMap.get(actorString);
//                        tempList.add(new ActorsMovieVo(starting, actor, movie));
//                        result.add(tempList);
//                        tempList = new ArrayList<>();
//                        continue;
//                    }
//                    if (!seenActors.contains(actorString)) {
//                        seenActors.add(actorString);
//                        Actor actor = actorsHashMap.get(actorString);
//                        tempList.add(new ActorsMovieVo(starting, actor, movie));
//                        if (!Objects.equals(actorString, target)) {
//                            recursiveSearch(actor, target);
//                        }
//                        if (!tempList.isEmpty()) {
//                            tempList.removeLast();
//                        }
//                    }
//                }
//                return;
//
//            }
//        }
//    }
