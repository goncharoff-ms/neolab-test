package ru.goncharov;

import lombok.extern.slf4j.Slf4j;
import ru.goncharov.dto.animal.Animal;
import ru.goncharov.dto.counter.CounterResult;
import ru.goncharov.exception.CountingException;
import ru.goncharov.service.counter.CounterManager;
import ru.goncharov.service.data.DataParser;
import ru.goncharov.service.filter.FilterOperatorManager;

import java.nio.file.Path;

@Slf4j
public class App {

    private static final FilterOperatorManager filterOperatorManager = new FilterOperatorManager();
    private static final DataParser dataParser = new DataParser();

    public static void main(String[] args) throws CountingException {

        final var counterManager = new CounterManager(dataParser, filterOperatorManager);

        final var results = counterManager.getCountEntity(Path.of("data_animal_100.json"),
                Path.of("filter_params_animal.json"), Animal.class);

        for (CounterResult result : results) {
            log.info("Filter: {}", result.getFilter().getParams());
            log.info("Number of entities found: {}", result.getCount());
        }
    }
}

