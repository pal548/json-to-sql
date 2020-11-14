package ru.pavel.testjsontosql.dto;

import lombok.Data;

import java.util.List;

@Data
public class MainElement {
    private String type;
    private List<Feature> features;
}
