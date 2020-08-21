package ru.goncharov.dto.animal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.goncharov.dto.DataEntity;
import ru.goncharov.dto.animal.types.AnimalHeight;
import ru.goncharov.dto.animal.types.AnimalType;
import ru.goncharov.dto.animal.types.AnimalWeight;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Animal extends DataEntity {
    private AnimalType type;
    private AnimalHeight height;
    private AnimalWeight weight;
}
