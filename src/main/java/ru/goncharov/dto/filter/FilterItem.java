package ru.goncharov.dto.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.goncharov.dto.filter.operator.FilterOperator;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterItem {
    private FilterOperator operator;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> value;
}
