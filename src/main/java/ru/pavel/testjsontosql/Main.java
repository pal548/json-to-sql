package ru.pavel.testjsontosql;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavel.testjsontosql.repos.FeaturesRepository;
import ru.pavel.testjsontosql.dto.MainElement;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Started");
        LocalDateTime start = LocalDateTime.now();
        Jdbi jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.useHandle(handle -> handle.execute("truncate table test.features"));
        jdbi.useHandle(handle -> handle.execute("truncate table test.properties"));
        try (JsonParser jParser = new JsonFactory().createParser(new File("C:/Users/1/Downloads/citylots.json"))) {
            MainElement mainElement = new MainElement();
            FeaturesRepository featuresRepository = new FeaturesRepository(jdbi, mainElement);
            new JsonLoader(jParser, mainElement, featuresRepository).go();
        }
        Duration dur = Duration.between(start, LocalDateTime.now());
        logger.info("Duration: " + dur);
    }
}




