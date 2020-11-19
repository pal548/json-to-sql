package ru.pavel.testjsontosql;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavel.testjsontosql.dto.MainElement;
import ru.pavel.testjsontosql.repos.FeaturesRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class JsonLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonLoaderTest.class);

    private JsonLoader jsonLoader;

    @BeforeClass
    public static void beforeClass() throws IOException {

    }

    @Test
    public void go() throws IOException, URISyntaxException {
        LocalDateTime start = LocalDateTime.now();
        Jdbi jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.useHandle(handle -> handle.execute("truncate table test.features"));
        jdbi.useHandle(handle -> handle.execute("truncate table test.properties"));
        Path testFile = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("citylots-small.json")).toURI());
        try (JsonParser jParser = new JsonFactory().createParser(testFile.toFile())) {
            MainElement mainElement = new MainElement();
            FeaturesRepository featuresRepository = new FeaturesRepository(jdbi, mainElement);
            new JsonLoader(jParser, mainElement, featuresRepository).go();
        }
        Duration dur = Duration.between(start, LocalDateTime.now());
        logger.info("Duration: " + dur);
    }
}