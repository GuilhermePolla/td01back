package org.gy.back.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonService {
    public JsonNode fileToJsonNode() throws IOException {
//        ClassPathResource resource = new ClassPathResource("latest_movies.json");
        ClassPathResource resource = new ClassPathResource("latest_movies.json");
        File file = resource.getFile();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(file);
    }
}
