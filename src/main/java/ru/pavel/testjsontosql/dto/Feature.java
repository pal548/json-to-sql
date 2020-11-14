package ru.pavel.testjsontosql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Feature {
    private String type;
    private Map<String, String> properties;
    private Geometry geometry;
}
