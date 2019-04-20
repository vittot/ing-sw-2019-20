package game.model;

import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public interface Target {

    public void addDamage(Player shooter, int damage);
    public void addThisTurnMarks(Player shooter, int marks);
    public void move(int numSquare, Direction dir) throws MapOutOfLimitException;
}
