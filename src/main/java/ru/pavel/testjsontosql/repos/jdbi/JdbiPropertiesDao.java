package ru.pavel.testjsontosql.repos.jdbi;

import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import ru.pavel.testjsontosql.dto.Property;

import java.util.List;

public interface JdbiPropertiesDao {
    @SqlBatch("insert into test.properties (feature_id, key, value) values (:featureId, :key, :value)")
    void insert(@BindBean List<Property> properties);
}
