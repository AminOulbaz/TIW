package dao;

import model.Verbal;

public interface VerbalDao {
    public void createVerbal(Verbal verbal);
    public Verbal getVerbal(int verbalId);
}
