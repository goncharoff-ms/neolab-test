package ru.goncharov.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.goncharov.exception.DataParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.StreamSupport;


public class DataParser {

    private final ObjectMapper objectMapper;

    public DataParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DataParser() {
        this(new ObjectMapper());
    }

    public <T> T parse(Path path, TypeReference<T> type) throws IOException {
        return objectMapper.readValue(Files.newInputStream(path), type);
    }

    public <T> T parse(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    public <T> Iterator<T> parseIterable(Path dataPath, Class<T> type) throws IOException {
        final var root = objectMapper.readTree(Files.newInputStream(dataPath));
        return StreamSupport.stream(root.spliterator(), false).map(node -> {
            try {
                return objectMapper.readValue(node.toString(), type);
            } catch (JsonProcessingException e) {
                throw new DataParserException(e);
            }
        }).iterator();
    }

}
