package ru.pavel.testjsontosql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Property {
    private Long featureId;
    private String key;
    private String value;
}
