package ru.goncharov.service.counter;
/*
  @author : Maksim Goncharov
  @since : 14.08.2020, пт
*/

import ru.goncharov.dto.DataEntity;
import ru.goncharov.dto.counter.CounterResult;
import ru.goncharov.dto.filter.Filter;
import ru.goncharov.exception.CountingException;
import ru.goncharov.service.filter.FilterOperatorManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CounterEntity {

    private final List<Method> methods;
    private final Filter filter;
    private final FilterOperatorManager operatorManager;

    private long count = 0;

    public CounterEntity(Filter filter,
                         FilterOperatorManager operatorManager,
                         Class<?> classEntity) {
        this.filter = filter;
        this.operatorManager = operatorManager;
        this.methods = Arrays.stream(classEntity.getMethods())
                .filter(method -> method.getName().matches("^get[A-Z].*"))
                .filter(method -> !method.getName().equals("getClass"))
                .collect(Collectors.toList());
    }

    public void counting(DataEntity entity) throws CountingException {
        final var filterParams = filter.getParams();

        int numberMatchesByField = 0;
        for (String fieldName : filterParams.keySet()) {
            final var filterItem = filterParams.get(fieldName);

            final var getterMethod = findMethodByName(fieldName, methods);

            try {
                final Object objectValue = getterMethod.invoke(entity);

                if (operatorManager.applyOperator(objectValue.toString(), filterItem.getValue(), filterItem.getOperator())) {
                    numberMatchesByField++;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CountingException("Failed to call the getter method. ", e);
            }
        }

        if (filterParams.size() == numberMatchesByField) {
            plusValue();
        }

    }

    private void plusValue() {
        this.count++;
    }

    private Method findMethodByName(String fieldName, List<Method> methods) {
        return methods.stream()
                .filter(method -> method.getName().toLowerCase().contains(fieldName))
                .findFirst()
                .orElseThrow();
    }

    public CounterResult getResult() {
        return new CounterResult(filter, count);
    }

}
