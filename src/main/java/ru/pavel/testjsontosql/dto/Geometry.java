package ru.pavel.testjsontosql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Geometry {
    private String type;
    private List<Coordinate> coordinates;
}
