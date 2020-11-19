package ru.pavel.testjsontosql.repos.jdbi;

import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import ru.pavel.testjsontosql.dto.Feature;

import java.util.List;

public interface JdbiFeaturesDao {
    @SqlBatch("insert into test.features (type) values (:feature.type)")
    @GetGeneratedKeys("id")
    List<Long> insert(@BindBean("feature") List<Feature> features);
}
