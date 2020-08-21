package ru.goncharov.dto.counter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.goncharov.dto.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounterResult {
    private Filter filter;
    private long count;
}
