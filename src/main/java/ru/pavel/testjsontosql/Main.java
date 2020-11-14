package ru.pavel.testjsontosql;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.sun.xml.internal.ws.addressing.WsaActionUtil;
import ru.pavel.testjsontosql.dto.Feature;
import ru.pavel.testjsontosql.dto.Geometry;
import ru.pavel.testjsontosql.dto.MainElement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println();

        /*ObjectMapper om = new ObjectMapper();
        MainElement mainElement = om.readValue(new File("C:/Users/1/Downloads/citylots.json"), MainElement.class);
        System.out.println(mainElement.getFeatures().get(0));*/


        JsonFactory jfactory = new JsonFactory();
        try (JsonParser jParser = jfactory.createParser(new File("C:/Users/1/Downloads/citylots.json"))) {
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
            System.out.println("total features: " + features.size());
        }
    }

    private static Feature parseFeature(JsonParser jParser) throws IOException {
        String type = null;
        Map<String, String> propertiesMap = null;
        Geometry geometry = null;
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            //System.out.println(jParser.getCurrentToken() + " " + jParser.getText());
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
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String parsedName = jParser.getText();
            switch (parsedName) {
                case "type":
            }
        }
        return null;
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




