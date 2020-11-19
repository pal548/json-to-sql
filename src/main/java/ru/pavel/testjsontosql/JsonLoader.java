package ru.pavel.testjsontosql;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavel.testjsontosql.dto.Coordinate;
import ru.pavel.testjsontosql.dto.Feature;
import ru.pavel.testjsontosql.dto.Geometry;
import ru.pavel.testjsontosql.dto.MainElement;
import ru.pavel.testjsontosql.repos.FeaturesRepository;

import java.io.IOException;
import java.util.*;

public class JsonLoader {
    private static final Logger logger = LoggerFactory.getLogger(JsonLoader.class);

    private static final int BATCH_SIZE_FEATURES = 1000;

    private final JsonParser jParser;
    private final MainElement mainElement;
    private final FeaturesRepository featuresRepository;

    public JsonLoader(JsonParser jParser, MainElement mainElement, FeaturesRepository featuresRepository) {
        this.jParser = jParser;
        this.mainElement = mainElement;
        this.featuresRepository = featuresRepository;
    }

    public void go() throws IOException {
        int totalFeatures = 0;
        jParser.nextToken();
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jParser.getText();
            if ("type".equals(fieldName)) {
                jParser.nextToken();
                mainElement.setType(jParser.getText());
            }

            if ("features".equals(fieldName)) {
                jParser.nextToken();
                int i = 0;
                int currBatchRows = 0;
                List<Feature> features = new ArrayList<>(BATCH_SIZE_FEATURES);
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    Feature feature = parseFeature(jParser);
                    feature.setType(/*feature.getType() + "-" + */feature.getProperties().get("TO_ST")); // TODO remove
                    features.add(feature);
                    currBatchRows++;
                    if (currBatchRows == BATCH_SIZE_FEATURES) {
                        featuresRepository.batchInsert(features);
                        currBatchRows = 0;
                        features.clear();
                    }
                    i++;
                }
                if (currBatchRows > 0) {
                    featuresRepository.batchInsert(features);
                }
                totalFeatures = i;
            }
        }
        //features.forEach(System.out::println);
        /*features.forEach(feature -> {
            if (!feature.getType().equals(feature.getProperties().get("TO_ST"))) {
                throw new RuntimeException("wrong join");
            }
        });*/
        logger.info("total features: " + totalFeatures);
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
