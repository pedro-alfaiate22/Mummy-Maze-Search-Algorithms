package mummymaze;

/**
 * This class is meant to save the position of an enemy/object.
 *
 * It's used in the Cell class in an ArrayList<EnemyPosition>(),
 *  to store the position of more than 1 enemy of the same type,
 *  but can also be used to store the position of a single enemy/object.
 */
public class EnemyPosition {
    private int line;
    private int column;
    private char enemyType;

    public EnemyPosition() {

    }

    public EnemyPosition(int line, int column, char enemyType) {
        this.line = line;
        this.column = column;
        this.enemyType = enemyType;
    }

    public EnemyPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public char getEnemyType() {
        return enemyType;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setEnemyType(char enemyType) {
        this.enemyType = enemyType;
    }

    public void setPosition(int line, int column, char enemyType) {
        this.line = line;
        this.column = column;
        this.enemyType = enemyType;
    }

    public void setPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }
}