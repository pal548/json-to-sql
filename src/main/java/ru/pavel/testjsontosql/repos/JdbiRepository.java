package ru.pavel.testjsontosql.repos;

import org.jdbi.v3.core.Jdbi;

public abstract class JdbiRepository {
    protected final Jdbi jdbi;

    public JdbiRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }
}
