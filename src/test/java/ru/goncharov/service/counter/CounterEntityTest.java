package ru.goncharov.service.counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.goncharov.dto.animal.Animal;
import ru.goncharov.dto.animal.types.AnimalHeight;
import ru.goncharov.dto.animal.types.AnimalType;
import ru.goncharov.dto.animal.types.AnimalWeight;
import ru.goncharov.dto.filter.Filter;
import ru.goncharov.dto.filter.FilterItem;
import ru.goncharov.dto.filter.operator.FilterOperator;
import ru.goncharov.exception.CountingException;
import ru.goncharov.service.filter.FilterOperatorManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CounterEntityTest {

    private final Filter testFilter = new Filter(Map.of(
            "type", new FilterItem(FilterOperator.EQ, List.of("OMNIVORE", "HERBIVORE")),
            "height", new FilterItem(FilterOperator.EQ, List.of("SHORT")),
            "weight", new FilterItem(FilterOperator.EQ, List.of("HEAVY"))
    ));

    private FilterOperatorManager operatorManager = mock(FilterOperatorManager.class);
    private CounterEntity counterEntityNormal;

    @BeforeEach
    void setUp() {
        this.counterEntityNormal = new CounterEntity(testFilter, operatorManager, Animal.class);
    }

    @Test
    void counting_Animal_AllCounted() throws CountingException {
        final var animal1 = Animal.builder()
                .type(AnimalType.HERBIVORE)
                .height(AnimalHeight.SHORT)
                .weight(AnimalWeight.HEAVY)
                .build();
        final var animal2 = Animal.builder()
                .type(AnimalType.OMNIVORE)
                .height(AnimalHeight.SHORT)
                .weight(AnimalWeight.HEAVY)
                .build();

        when(operatorManager.applyOperator(anyString(), any(), eq(FilterOperator.EQ)))
                .thenReturn(true);

        this.counterEntityNormal.counting(animal1);
        this.counterEntityNormal.counting(animal2);

        assertEquals(2, counterEntityNormal.getResult().getCount());
        assertEquals(testFilter, counterEntityNormal.getResult().getFilter());
    }

    @Test
    void counting_Animal_One_of_Two() throws CountingException {
        final var animal1 = Animal.builder()
                .type(AnimalType.HERBIVORE)
                .height(AnimalHeight.SHORT)
                .weight(AnimalWeight.HEAVY)
                .build();
        final var animal2 = Animal.builder()
                .type(AnimalType.OMNIVORE)
                .height(AnimalHeight.SHORT)
                .weight(AnimalWeight.HEAVY)
                .build();

        when(operatorManager.applyOperator(any(), any(), eq(FilterOperator.EQ)))
                .thenReturn(true);
        when(operatorManager.applyOperator(eq("HERBIVORE"), any(), eq(FilterOperator.EQ)))
                .thenReturn(false);


        this.counterEntityNormal.counting(animal1);
        this.counterEntityNormal.counting(animal2);

        assertEquals(1, counterEntityNormal.getResult().getCount());
        assertEquals(testFilter, counterEntityNormal.getResult().getFilter());
    }

    @Test
    void counting_Animal_Exception() {
        assertThrows(CountingException.class, () -> {
            final var animal1 = Animal.builder()
                    .type(AnimalType.OMNIVORE)
                    .height(AnimalHeight.SHORT)
                    .weight(AnimalWeight.HEAVY)
                    .build();

            when(operatorManager.applyOperator(any(), any(), any()))
                    .thenThrow(InvocationTargetException.class);

            this.counterEntityNormal.counting(animal1);
        });
    }

}