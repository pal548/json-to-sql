package ru.pavel.testjsontosql;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import ru.pavel.testjsontosql.dto.Coordinate;
import ru.pavel.testjsontosql.dto.Feature;
import ru.pavel.testjsontosql.dto.Geometry;
import ru.pavel.testjsontosql.dto.MainElement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println();

        /*ObjectMapper om = new ObjectMapper();
        MainElement mainElement = om.readValue(new File("C:/Users/1/Downloads/citylots.json"), MainElement.class);
        System.out.println(mainElement.getFeatures().get(0));*/


        JsonFactory jfactory = new JsonFactory();
        try (JsonParser jParser = jfactory.createParser(new File("C:/Users/1/Downloads/citylots-small.json"))) {
            String parsedName = null;
            Integer parsedAge = null;
            List<Feature> features = new LinkedList<>();
            MainElement mainElement = new MainElement();

            jParser.nextToken();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jParser.getText();
                if ("type".equals(fieldName)) {
                    jParser.nextToken();
                    mainElement.setType(jParser.getText());
                }

                if ("features".equals(fieldName)) {
                    jParser.nextToken();
                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
                        features.add(parseFeature(jParser));
                    }
                }
            }
            features.forEach(System.out::println);
            System.out.println("total features: " + features.size());
        }
    }

    private static Feature parseFeature(JsonParser jParser) throws IOException {
        String type = null;
        Map<String, String> propertiesMap = null;
        Geometry geometry = null;
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String parsedName = jParser.getText();
            switch (parsedName) {
                case "type":
                    jParser.nextToken();
                    type = jParser.getText();
                    break;
                case "properties":
                    propertiesMap = parseProperties(jParser);
                    //System.out.println(propertiesMap);
                    break;
                case "geometry":
                    geometry = parseGeometry(jParser);
                    break;
            }
        }
        return new Feature(type, propertiesMap, geometry);
    }

    private static Geometry parseGeometry(JsonParser jParser) throws IOException {
        String type = null;
        List<Coordinate> coordinates = null;
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String parsedName = jParser.getText();
            switch (parsedName) {
                case "type":
                    jParser.nextToken();
                    type = jParser.getText();
                    break;
                case "coordinates":
                    if ("Polygon".equals(type)) { // пока не берём другие типы Geometry
                        jParser.nextToken();
                        coordinates = parseCoordinates(jParser);
                    }
                    break;
            }
        }
        return new Geometry(type, coordinates);
    }

    private static List<Coordinate> parseCoordinates(JsonParser jParser) throws IOException {
        List<Coordinate> res = new LinkedList<>();
        while (jParser.nextToken() != JsonToken.END_ARRAY) {
            while (jParser.nextToken() != JsonToken.END_ARRAY) {
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    double x = jParser.getDoubleValue();
                    jParser.nextToken();
                    double y = jParser.getDoubleValue();
                    jParser.nextToken();
                    double z = jParser.getDoubleValue();
                    res.add(new Coordinate(x, y, z));
                }
            }
        }
        return res;
    }

    private static Map<String, String> parseProperties(JsonParser jParser) throws IOException {
        Map<String, String> res = new HashMap<>();
        jParser.nextToken();
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String key = jParser.getCurrentName();
            jParser.nextToken();
            res.put(key, jParser.getText());
        }
        return res;
    }

}




