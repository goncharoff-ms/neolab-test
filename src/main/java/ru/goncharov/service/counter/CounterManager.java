package ru.goncharov.service.counter;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import ru.goncharov.dto.DataEntity;
import ru.goncharov.dto.counter.CounterResult;
import ru.goncharov.dto.filter.Filter;
import ru.goncharov.exception.CountingException;
import ru.goncharov.service.data.DataParser;
import ru.goncharov.service.filter.FilterOperatorManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CounterManager {

    private final DataParser dataParser;
    private final FilterOperatorManager operatorManager;
    private final TypeReference<List<Filter>> QUERY_LIST_TYPE = new TypeReference<>() {
    };

    public CounterManager(DataParser dataParser,
                          FilterOperatorManager operatorManager) {
        this.dataParser = dataParser;
        this.operatorManager = operatorManager;
    }

    public List<CounterResult> getCountEntity(Path dataPath, Path filterPath, Class<? extends DataEntity> tClass) throws CountingException {
        try {
            final var filters = dataParser.parse(filterPath, QUERY_LIST_TYPE);
            log.info("The following filters were read: {}", filters);

            final var root = dataParser.parseIterable(dataPath, tClass);

            final var filterToCounter = filters
                    .stream()
                    .collect(Collectors.toMap(Function.identity(), f -> new CounterEntity(f, operatorManager, tClass)));

            log.info("Starting to iterate over all entities");
            while (root.hasNext()) {
                final var dataEntity = root.next();
                log.info("Analyzing the entity: {}", dataEntity);
                for (Filter filter : filters) {
                    filterToCounter.get(filter).counting(dataEntity);
                }
            }

            log.info("Finished iterating over all entities");

            return filterToCounter.values()
                    .stream()
                    .map(CounterEntity::getResult)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error reading data");
            throw new CountingException("Error reading data. ", e);
        }
    }
}
