package mummymaze;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is meant to save the position of multiple enemies
 *  or in-game objects of the same type.
 *
 * It uses the EnemyPosition class which contains line and column
 *  position, setters and getters to retrieve/set its values
 *
 * This class implements the Iterable class so its possible to use
 *  fori and foreach loop iterations easily
 */
public class Cell implements Iterable<EnemyPosition> {
    private ArrayList<EnemyPosition> enemy = new ArrayList<EnemyPosition>();

    public Cell() {

    }

    public Cell(int line, int column, char enemyType) {
        enemy.add(new EnemyPosition(line, column, enemyType));
    }

    public Cell(int line, int column) {
        enemy.add(new EnemyPosition(line, column));
    }

    public EnemyPosition get() {
        return enemy.get(0);
    }

    public int getLine() {
        return enemy.get(0).getLine();
    }

    public int getColumn() {
        return enemy.get(0).getColumn();
    }

    public void add(int line, int column, char enemyType) {
        enemy.add(new EnemyPosition(line, column, enemyType));
    }

    public void add(int line, int column) {
        enemy.add(new EnemyPosition(line, column));
    }

    public void addAll(Cell enemy) {
        for (EnemyPosition enemyPosition:enemy) {
            this.enemy.add(enemyPosition);
        }
    }

    public void remove(int i) {
        enemy.remove(i);
    }

    public int size() {
        return enemy.size();
    }

    public EnemyPosition get(int i) {
        return enemy.get(i);
    }

    public void disable (int i) {
        enemy.get(i).setLine(0);
        enemy.get(i).setColumn(0);
        enemy.get(i).setEnemyType(' ');
    }

    @Override
    public Iterator<EnemyPosition> iterator() {
        return new Iterator<EnemyPosition> () {
            private final Iterator<EnemyPosition> iter = enemy.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public EnemyPosition next() {
                return iter.next();
            }
        };
    }
}


