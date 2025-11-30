package dao;

import java.sql.Connection;

public class ProfessorDaoImpl implements ProfessorDao {
    private Connection connection;

    public ProfessorDaoImpl(Connection connection) {
        this.connection = connection;
    }



}
