package ru.pavel.testjsontosql.dto;

import lombok.Data;

import java.util.List;

@Data
public class Geometry {
    private String type;
    private List<List<List<String>>> coordinates;// = new double[2][2][4];
}
