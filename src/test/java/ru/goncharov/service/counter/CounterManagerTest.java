package ru.goncharov.service.counter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.goncharov.dto.animal.Animal;
import ru.goncharov.dto.animal.types.AnimalHeight;
import ru.goncharov.dto.animal.types.AnimalType;
import ru.goncharov.dto.animal.types.AnimalWeight;
import ru.goncharov.dto.counter.CounterResult;
import ru.goncharov.dto.filter.Filter;
import ru.goncharov.dto.filter.FilterItem;
import ru.goncharov.dto.filter.operator.FilterOperator;
import ru.goncharov.exception.CountingException;
import ru.goncharov.service.data.DataParser;
import ru.goncharov.service.filter.FilterOperatorManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CounterManagerTest {

    private final Class<Animal> animalClass = Animal.class;
    private final Animal animal = new Animal(AnimalType.CARNIVORE, AnimalHeight.HIGH, AnimalWeight.MEDIUM);
    private final Path dataPath = Path.of("datapath.json");
    private final Path filterPath = Path.of("filterpath.json");
    private final Filter filter = new Filter(Map.of(
            "type", new FilterItem(FilterOperator.EQ, List.of("HERBIVORE")),
            "height", new FilterItem(FilterOperator.EQ, List.of("SMALL")),
            "weight", new FilterItem(FilterOperator.EQ, List.of("LIGHT"))
    ));

    private CounterManager counterManager;
    private FilterOperatorManager operatorManager = mock(FilterOperatorManager.class);
    private DataParser dataParser = mock(DataParser.class);

    @BeforeEach
    void setUp() {
        this.counterManager = new CounterManager(dataParser, operatorManager);
    }

    @Test
    void getCountEntity_IfOk1() throws CountingException, IOException {
        when(dataParser.parse(eq(filterPath), any(TypeReference.class)))
                .thenReturn(List.of(filter));

        when(dataParser.parseIterable(eq(dataPath), eq(animalClass)))
                .thenReturn(List.of(animal).iterator());

        when(operatorManager.applyOperator(any(), any(), any())).thenReturn(true);

        List<CounterResult> resultCountEntity =
                counterManager.getCountEntity(dataPath, filterPath, animalClass);

        Assertions.assertEquals(List.of(new CounterResult(filter, 1)), resultCountEntity);
    }

    @Test
    void getCountEntity_IfOk2() throws CountingException, IOException {
        when(dataParser.parse(eq(filterPath), any(TypeReference.class)))
                .thenReturn(List.of(filter));

        when(dataParser.parseIterable(eq(dataPath), eq(animalClass)))
                .thenReturn(List.of(animal, animal, animal, animal).iterator());

        when(operatorManager.applyOperator(any(), any(), any())).thenReturn(true);

        List<CounterResult> resultCountEntity =
                counterManager.getCountEntity(dataPath, filterPath, animalClass);

        Assertions.assertEquals(List.of(new CounterResult(filter, 4)), resultCountEntity);
    }

    @Test
    void getCountEntity_Exception() {
        Assertions.assertThrows(CountingException.class, () -> {
            when(dataParser.parse(eq(filterPath), any(TypeReference.class)))
                    .thenThrow(new IOException());

            List<CounterResult> resultCountEntity =
                    counterManager.getCountEntity(dataPath, filterPath, animalClass);
        });
    }

}