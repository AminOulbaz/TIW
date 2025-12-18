package dao;

import model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDaoImpl implements ProfessorDao {
    private Connection connection;

    public ProfessorDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Professor getProfessorByUserId(String username){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("""
                select *
                from professor 
                where username = ?
        """);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Professor professor = new Professor();
                professor.setId(resultSet.getString("professor_id"));
                professor.setName(resultSet.getString("first_name"));
                professor.setEmail(resultSet.getString("email"));
                professor.setSurname(resultSet.getString("last_name"));
                professor.setDepartment(resultSet.getString("department"));
                return professor;
            }
            throw new RuntimeException("doesn't exist a professor with this username:"+username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
