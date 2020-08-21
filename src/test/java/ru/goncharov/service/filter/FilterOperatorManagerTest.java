package ru.goncharov.service.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.goncharov.dto.filter.operator.FilterOperator;

import java.util.List;

class FilterOperatorManagerTest {

    private FilterOperatorManager filterOperatorManager;

    @BeforeEach
    void setUp() {
        this.filterOperatorManager = new FilterOperatorManager();
    }

    @Test
    void applyOperator_EqTrue1() {
        Assertions.assertTrue(filterOperatorManager.applyOperator("QWERTY", List.of("QWERTY"), FilterOperator.EQ));
    }

    @Test
    void applyOperator_EqTrue2() {
        Assertions.assertTrue(filterOperatorManager.applyOperator("QWERTY", List.of("ASDFGH", "QWERTY"), FilterOperator.EQ));
    }

    @Test
    void applyOperator_NeTrue1() {
        Assertions.assertTrue(filterOperatorManager.applyOperator("ASDFG", List.of("QWERTY"), FilterOperator.NE));
    }

    @Test
    void applyOperator_NeTrue2() {
        Assertions.assertTrue(filterOperatorManager.applyOperator("ASDFG", List.of("DQWFQF", "QWERTY"), FilterOperator.NE));
    }


}