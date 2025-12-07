package dao;

import model.Professor;

public interface ProfessorDao {
    public Professor getProfessorByUserId(String username);
}
