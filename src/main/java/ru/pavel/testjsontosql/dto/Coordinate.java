package ru.pavel.testjsontosql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private double x;
    private double y;
    private double z;
}
