package ru.goncharov.service.filter;

import ru.goncharov.dto.filter.operator.FilterOperator;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class FilterOperatorManager {

    private final static Map<FilterOperator, BiFunction<String, List<String>, Boolean>> filterOperatorToPredicate = Map.of(
            FilterOperator.EQ, (obj1, obj2) -> obj2.contains(obj1),
            FilterOperator.NE, (obj1, obj2) -> !obj2.contains(obj1)
    );

    public boolean applyOperator(String entityValue, List<String> fieldsInFilter, FilterOperator filterOperator) {
        return filterOperatorToPredicate.get(filterOperator).apply(entityValue, fieldsInFilter);
    }

}
