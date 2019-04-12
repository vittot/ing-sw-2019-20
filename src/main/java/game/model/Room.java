package game.model;

import java.util.List;

public class Room implements Target {

    private MapColor color;
    private Map map;

    public Room(MapColor color, Map map) {
        this.color = color;
        this.map = map;
    }

    public List<Square> getSquares(){
        return map.getRoom(color);
    }

    @Override
    public void addDamage(Player shooter, int damage) {
        getSquares().forEach( s -> s.addDamage(shooter,damage));
    }

    @Override
    public void addThisTurnMarks(Player shooter, int marks) {
        getSquares().forEach( s -> s.addThisTurnMarks(shooter,marks));
    }

    @Override
    public void move(int numSquare, Direction dir) {
        getSquares().forEach( s -> s.move(numSquare, dir));
    }
}
