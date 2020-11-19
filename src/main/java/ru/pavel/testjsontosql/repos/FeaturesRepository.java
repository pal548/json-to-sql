package ru.pavel.testjsontosql.repos;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavel.testjsontosql.dto.Feature;
import ru.pavel.testjsontosql.dto.Geometry;
import ru.pavel.testjsontosql.dto.MainElement;
import ru.pavel.testjsontosql.dto.Property;
import ru.pavel.testjsontosql.repos.jdbi.JdbiFeaturesDao;
import ru.pavel.testjsontosql.repos.jdbi.JdbiPropertiesDao;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeaturesRepository extends JdbiRepository {
    private static final Logger logger = LoggerFactory.getLogger(FeaturesRepository.class);

    private final MainElement mainElement;

    public FeaturesRepository(Jdbi jdbi, MainElement mainElement) {
        super(jdbi);
        this.mainElement = mainElement;
    }

    public void batchInsert(List<Feature> features) {
        features.forEach(f -> logger.trace(f.toString()));
        List<Long> featureIds = jdbi.withExtension(JdbiFeaturesDao.class, dao -> dao.insert(features));
        features.forEach(f -> logger.trace(f.toString()));

        Iterator<Long> returnedIdsIterator = featureIds.iterator();
        Iterator<Feature> featureIterator = features.iterator();
        Stream<Property> propertyStream = Stream.empty();
        Stream<Geometry> geometry = Stream.empty();
        while (returnedIdsIterator.hasNext()) {
            Long id = returnedIdsIterator.next();
            Feature feature = featureIterator.next();
            propertyStream = Stream.concat(propertyStream, feature.getProperties().entrySet().stream().map(e -> new Property(id, e.getKey(), e.getValue())));

        }
        features.forEach(f -> logger.trace(f.toString()));

        List<Property> propsToDB = propertyStream.collect(Collectors.toList());

        jdbi.useExtension(JdbiPropertiesDao.class, dao -> dao.insert(propsToDB));


    }
}