package mummymaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

public class MummyMazeState extends State implements Cloneable {
    public static final int SIZE = 3;
    private final char[][] matrix;

    private int lineHero;
    private int columnHero;
    private int lineExit;
    private int columnExit;

    private Cell whiteMummies;
    private Cell redMummies;
    private Cell scorpions;

    private Cell traps;
    private int lineKey;
    private int columnKey;
    private Cell gates;

    public int getLineHero() {
        return lineHero;
    }

    public int getColumnHero() {
        return columnHero;
    }

    public int getLineExit() {
        return lineExit;
    }

    public int getColumnExit() {
        return columnExit;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public MummyMazeState(char[][] matrix, EnemyPosition hero, EnemyPosition exit, Cell whiteMummies, Cell redMummies, Cell scorpions, Cell traps, EnemyPosition key, Cell gates) {
        this.matrix = new char[matrix.length][matrix.length];

        this.whiteMummies = new Cell();
        this.redMummies = new Cell();
        this.scorpions = new Cell();
        this.traps = new Cell();
        this.gates = new Cell();

        this.lineExit = 0;
        this.columnExit = 0;
        this.lineKey = 0;
        this.columnKey = 0;
        this.lineHero = 0;
        this.columnHero = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }

        //Get hero's position
        if (hero!=null) {
            lineHero = hero.getLine();
            columnHero = hero.getColumn();
        }

        //Get exit position
        if (exit!=null) {
            lineExit = exit.getLine();
            columnExit = exit.getColumn();
        }

        //Get white mummies' positions
        if (whiteMummies!=null && whiteMummies.size()>0)
            for (EnemyPosition whiteMummy:whiteMummies)
                this.whiteMummies.add(whiteMummy.getLine(),whiteMummy.getColumn());

        //Get white mummies' positions
        if (redMummies!=null && redMummies.size()>0)
            for (EnemyPosition redMummy:redMummies)
                this.redMummies.add(redMummy.getLine(),redMummy.getColumn());

        //Get scorpions' positions
        if (scorpions!=null && scorpions.size()>0)
            for (EnemyPosition scorpion:scorpions)
                this.scorpions.add(scorpion.getLine(),scorpion.getColumn());

        //Get traps positions
        if (traps != null && traps.size()>0)
            for (EnemyPosition trap:traps)
                this.traps.add(trap.getLine(),trap.getColumn());

        //Get key position
        if (key!=null && key.getLine()>0 && key.getColumn()>0) {
            lineKey = key.getLine();
            columnKey = key.getColumn();
        } else {
            lineKey = columnKey = 0;
        }

        //Get gate positions
        if (gates!=null && gates.size()>0)
            for (EnemyPosition gate:gates)
                this.gates.add(gate.getLine(),gate.getColumn(), gate.getEnemyType());
    }

    public MummyMazeState(String matrix) {
        this.matrix = new char[13][13];

        String[] matrixLines = matrix.split("\n");

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                this.matrix[i][j] = matrixLines[i].charAt(j);
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        if (lineHero == lineExit && columnHero == columnExit) {
            fireHeroChanged(null);
        }
    }

    //#region HeroMovement
    public boolean canMoveUp() {
        //Game boundaries, internal movement, key presence, gates and hero close to exit validation
        if (lineHero > 1 || (columnExit == columnHero && lineExit == lineHero - 1)) {
            if ((matrix[lineHero - 1][columnHero] == ' ' || matrix[lineHero - 1][columnHero] == '_' || matrix[lineHero - 1][columnHero] == 'S')) {
                if (matrix[lineHero - 1][columnHero] != 'S' && (matrix[lineHero - 2][columnHero] == '.' || matrix[lineHero - 2][columnHero] == 'C')) {
                    return true;
                } else if (matrix[lineHero - 1][columnHero] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveRight() {
        //Game boundaries, internal movement, key presence, gates and hero close to exit validation
        if (columnHero < 11 || (columnExit == columnHero + 1 && lineExit == lineHero)) {
            if ((matrix[lineHero][columnHero + 1] == ' ' || matrix[lineHero][columnHero + 1] == ')' || matrix[lineHero][columnHero + 1] == 'S')) {
                if (matrix[lineHero][columnHero + 1] != 'S' && (matrix[lineHero][columnHero + 2] == '.' || matrix[lineHero][columnHero + 2] == 'C')) {
                    return true;
                } else if (matrix[lineHero][columnHero + 1] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveDown() {
        //Game boundaries, internal movement, key presence, gates and hero close to exit validation
        if (lineHero < 11 || (columnExit == columnHero && lineExit == lineHero + 1)) {
            if ((matrix[lineHero + 1][columnHero] == ' ' || matrix[lineHero + 1][columnHero] == '_' || matrix[lineHero + 1][columnHero] == 'S')) {
                if (matrix[lineHero + 1][columnHero] != 'S' && (matrix[lineHero + 2][columnHero] == '.' || matrix[lineHero + 2][columnHero] == 'C')) {
                    return true;
                } else if (matrix[lineHero + 1][columnHero] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveLeft() {
        //Game boundaries, internal movement, key presence, gates and hero close to exit validation
        if (columnHero > 1 || (columnExit == columnHero - 1 && lineExit == lineHero)) {
            if ((matrix[lineHero][columnHero - 1] == ' ' || matrix[lineHero][columnHero - 1] == ')' || matrix[lineHero][columnHero - 1] == 'S')) {
                if (matrix[lineHero][columnHero - 1] != 'S' && (matrix[lineHero][columnHero - 2] == '.' || matrix[lineHero][columnHero - 2] == 'C')) {
                    return true;
                } else if (matrix[lineHero][columnHero - 1] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }


    public void moveUp() {
        //Saves the hero's line position before he moves
        int initialLineHero = lineHero;

        //Move hero
        matrix[lineHero][columnHero] = '.';
        if (lineHero==1) { //if hero is about to walk to the exit
            lineHero--;
            matrix[lineHero][columnHero] = 'F';
            return;
        } else {
            lineHero-=2;
            matrix[lineHero][columnHero] = 'H';
        }

        //If game level contains keys
        if (lineKey>0 && columnKey>0) {
            //If the hero's next position != key position, key will be restored in matrix
            if (initialLineHero==lineKey && columnHero==columnKey)
                matrix[lineKey][columnKey] = 'C';

            //If hero steps on key gates will either open or close
            if (lineHero==lineKey && columnHero==columnKey)
                gateOpenClose();
        }

        fireHeroChanged(null);

        //Move enemies after hero is done moving
        moveEnemies();
    }

    public void moveRight() {
        //Saves the hero's column position before he moves
        int initialColumnHero = columnHero;

        //Move hero
        matrix[lineHero][columnHero]='.';
        if (columnHero == 11) { //if hero is about to walk to the exit
            columnHero++;
            matrix[lineHero][columnHero] = 'F';
            return;
        } else {
            columnHero += 2;
            matrix[lineHero][columnHero] = 'H';
        }

        //If game level contains keys
        if (lineKey > 0 && columnKey > 0) {
            //If the hero's next position != key position, key will be restored in matrix
            if (lineHero==lineKey && initialColumnHero==columnKey)
                matrix[lineKey][columnKey] = 'C';

            //If hero steps on key gates will either open or close
            if (lineHero==lineKey && columnHero==columnKey)
                gateOpenClose();
        }

        fireHeroChanged(null);

        //Move enemies after hero is done moving
        moveEnemies();
    }

    public void moveDown() {
        //Saves the hero's line position before he moves
        int initialLineHero = lineHero;

        //Move hero
        matrix[lineHero][columnHero] = '.';
        if (lineHero == 11) { //if hero is about to walk to the exit
            lineHero++;
            matrix[lineHero][columnHero] = 'F';
            return;
        } else {
            lineHero += 2;
            matrix[lineHero][columnHero] = 'H';
        }

        //If game level contains keys
        if (lineKey > 0 && columnKey > 0) {
            //If the hero's next position != key position, key will be restored in matrix
            if (initialLineHero == lineKey && columnHero == columnKey)
                matrix[lineKey][columnKey] = 'C';

            //If hero steps on key gates will either open or close
            if (lineHero == lineKey && columnHero == columnKey)
                gateOpenClose();
        }

        fireHeroChanged(null);

        //Move enemies after hero is done moving
        moveEnemies();
    }

    public void moveLeft() {
        //Saves the hero's column position before he moves
        int initialColumnHero = columnHero;

        //Move hero
        matrix[lineHero][columnHero] = '.';
        if (columnHero == 1) { //if hero is about to walk to the exit
            columnHero--;
            matrix[lineHero][columnHero] = 'F';
            return;
        } else {
            columnHero -= 2;
            matrix[lineHero][columnHero] = 'H';
        }

        //If game level contains keys
        if (lineKey>0 && columnKey>0) {
            //If the hero's next position != key position, key will be restored in matrix
            if (lineHero==lineKey && initialColumnHero==columnKey)
                matrix[lineKey][columnKey] = 'C';

            //If hero steps on key gates will either open or close
            if (lineHero==lineKey && columnHero==columnKey)
                gateOpenClose();
        }

        fireHeroChanged(null);

        //Move enemies after hero is done moving
        moveEnemies();
    }

    public void moveStay() {
        fireHeroChanged(null);
        //Move enemies even though hero doesn't move
        moveEnemies();
    }
    //#endregion HeroMovement

    /**
     * This method is called whenever the hero or an enemy's position is same as the key position
     * It changes the gates' status, all closed gates become open gates and vice-versa
     * _ : Open horizontal gate
     * = : Closed horizontal gate
     * ) : Open vertical gate
     * " : Closed vertical gate
     */
    public void gateOpenClose() {
        for (EnemyPosition gate : gates) {
            if (matrix[gate.getLine()][gate.getColumn()] == '_') {
                matrix[gate.getLine()][gate.getColumn()] = '=';
                continue;
            }
            if (matrix[gate.getLine()][gate.getColumn()] == ')') {
                matrix[gate.getLine()][gate.getColumn()] = '"';
                continue;
            }
            if (matrix[gate.getLine()][gate.getColumn()] == '=') {
                matrix[gate.getLine()][gate.getColumn()] = '_';
                continue;
            }
            if (matrix[gate.getLine()][gate.getColumn()] == '"') {
                matrix[gate.getLine()][gate.getColumn()] = ')';
                continue;
            }
        }
    }

    //#region EnemyMovement
    /**
     * This method is called in the end of each hero movement, it calls all enemies' movements,
     * depending on their existence in the game, by a specific order
     */
    public void moveEnemies() {
        if (lineHero!=0 && columnHero!=0)
            if (scorpions.size()>0)
                moveScorpion();

        if (lineHero!=0 && columnHero!=0)
            if (whiteMummies.size()>0)
                moveWhiteMummy();

        if (lineHero!=0 && columnHero!=0)
            if (redMummies.size()>0)
                moveRedMummy();
    }

    /**
     * This method is *only* called after an enemy has completed his movement
     * It regulates/chooses which enemy dies in case of a collision
     * @param currentEnemyType   type of the current enemy which has changed position, expected to be either of: {'M','V','E'}.
     * @param currentEnemy       current enemy which has already moved (changed position).
     * @param collisionEnemy     type of the enemy who had his position taken over, expected to be either of: {'M','V','E'}.
     * @param collisionEnemyType enemy who had his position taken over.
     * Note: currenEnemy points to an actual place in memory,
     *  while collisionEnemy is a newly created Cell variable
     */
    public void collisionEnemy(char currentEnemyType, EnemyPosition currentEnemy, char collisionEnemyType, EnemyPosition collisionEnemy) {
        //If two enemies of the same type collide, the first enemy (the moving enemy) one dies
        // currentAnyEnemy vs collisionAnyEnemy -> currentAnyEnemy dies
        if (currentEnemyType == collisionEnemyType) {
            currentEnemy.setLine(0);
            currentEnemy.setColumn(0);
            matrix[collisionEnemy.getLine()][collisionEnemy.getColumn()] = collisionEnemyType;
            return;
        }

        // whiteMummy always stays alive after a collision with any kind of enemy
        // redMummy vs whiteMummy -> redMummy dies
        // scorpion vs whiteMummy -> scorpion dies
        if ((currentEnemyType == 'V' || currentEnemyType == 'E') && collisionEnemyType == 'M') {
            currentEnemy.setLine(0);
            currentEnemy.setColumn(0);
            matrix[collisionEnemy.getLine()][collisionEnemy.getColumn()] = collisionEnemyType;
            return;
        }

        // scorpion vs redMummy -> scorpion dies
        if (currentEnemyType == 'E' && collisionEnemyType == 'V') {
            currentEnemy.setLine(0);
            currentEnemy.setColumn(0);
            matrix[collisionEnemy.getLine()][collisionEnemy.getColumn()] = collisionEnemyType;
            return;
        }

        // whiteMummy vs redMummy -> redMummy dies
        if (currentEnemyType == 'M' && collisionEnemyType == 'V') {
            for (int i = 0; i < redMummies.size(); i++) {
                if (redMummies.get(i).getLine() == collisionEnemy.getLine() && redMummies.get(i).getColumn() == collisionEnemy.getColumn()) {
                    redMummies.get(i).setLine(0);
                    redMummies.get(i).setColumn(0);
                    //redMummySetPosition(0, 0,i);
                    matrix[collisionEnemy.getLine()][collisionEnemy.getColumn()] = currentEnemyType;
                    break;
                }
            }
            return;
        }

        // whiteMummy vs scorpion -> scorpion dies
        // redMummy vs scorpion -> scorpion dies
        if (collisionEnemyType == 'E') {
            for (int i = 0; i < scorpions.size(); i++) {
                if (scorpions.get(i).getLine() == collisionEnemy.getLine() && scorpions.get(i).getColumn() == collisionEnemy.getColumn()) {
                    scorpions.get(i).setLine(0);
                    scorpions.get(i).setColumn(0);
                    //scorpionSetPosition(0, 0,i);
                    matrix[collisionEnemy.getLine()][collisionEnemy.getColumn()] = currentEnemyType;
                    break;
                }
            }
            return;
        }
    }

    public void moveEnemyUp(EnemyPosition currentEnemy) {
        //Current moving enemy vars
        int lineEnemy = currentEnemy.getLine();
        int columnEnemy = currentEnemy.getColumn();
        char currentEnemyType = matrix[lineEnemy][columnEnemy];

        //Enemy in future cell vars
        char collisionEnemyType = matrix[lineEnemy - 2][columnEnemy];
        EnemyPosition collisionEnemy = new EnemyPosition(lineEnemy - 2, columnEnemy);

        //Move current enemy
        matrix[lineEnemy][columnEnemy] = '.';
        currentEnemy.setLine(lineEnemy - 2);
        matrix[lineEnemy - 2][columnEnemy] = currentEnemyType;

        //Restore trap after enemy moves away
        for (EnemyPosition trap : traps) {
            if (trap.getLine()>0 && trap.getColumn()>0) {
                if (trap.getLine() == lineEnemy && trap.getColumn() == columnEnemy) {
                    matrix[lineEnemy][columnEnemy] = 'A';
                    break;
                }
            }
        }

        //Operate gates in case enemy steps on key
        if (lineKey > 0 && columnKey > 0) {
            //After enemy has moved away from key, the key is restored
            if (lineEnemy == lineKey && columnEnemy == columnKey) {
                matrix[lineKey][columnKey] = 'C';
            }
            //Open or close gates (depending on gatesOpen variable and if the hero/enemy has stepped on the key
            if (lineEnemy - 2 == lineKey && columnEnemy == columnKey) {
                gateOpenClose();
            }
        }

        //After movement, verify if current enemy has collided with already exiting enemy in that place
        if (collisionEnemyType == 'M' || collisionEnemyType == 'V' || collisionEnemyType == 'E') {
            collisionEnemy(currentEnemyType, currentEnemy, collisionEnemyType, collisionEnemy);
        }
        fireEnemyChanged(null);
    }

    public void moveEnemyRight(EnemyPosition currentEnemy) {
        //Current moving enemy vars
        int lineEnemy = currentEnemy.getLine();
        int columnEnemy = currentEnemy.getColumn();
        char currentEnemyType = matrix[lineEnemy][columnEnemy];

        //Enemy in future cell vars
        char collisionEnemyType = matrix[lineEnemy][columnEnemy + 2];
        EnemyPosition collisionEnemy = new EnemyPosition(lineEnemy, columnEnemy + 2);

        //Move current enemy
        matrix[lineEnemy][columnEnemy] = '.';
        currentEnemy.setColumn(columnEnemy + 2);
        matrix[lineEnemy][columnEnemy + 2] = currentEnemyType;

        //Restore trap after enemy moves away
        for (EnemyPosition trap : traps) {
            if (trap.getLine()>0 && trap.getColumn()>0) {
                if (trap.getLine() == lineEnemy && trap.getColumn() == columnEnemy) {
                    matrix[lineEnemy][columnEnemy] = 'A';
                    break;
                }
            }
        }

        //Operate gates in case enemy steps on key
        if (lineKey > 0 && columnKey > 0) {
            //After enemy has moved away from key, the key is restored
            if (lineEnemy == lineKey && columnEnemy == columnKey) {
                matrix[lineKey][columnKey] = 'C';
            }
            //Open or close gates (depending on gatesOpen variable and if the hero/enemy has stepped on the key
            if (lineEnemy == lineKey && columnEnemy + 2 == columnKey) {
                gateOpenClose();
            }
        }

        //After movement, verify if current enemy has collided with already exiting enemy in that place
        if (collisionEnemyType == 'M' || collisionEnemyType == 'V' || collisionEnemyType == 'E') {
            collisionEnemy(currentEnemyType, currentEnemy, collisionEnemyType, collisionEnemy);
        }
        fireEnemyChanged(null);
    }

    public void moveEnemyDown(EnemyPosition currentEnemy) {
        //Current moving enemy vars
        int lineEnemy = currentEnemy.getLine();
        int columnEnemy = currentEnemy.getColumn();
        char currentEnemyType = matrix[lineEnemy][columnEnemy];

        //Enemy in future cell vars
        char collisionEnemyType = matrix[lineEnemy + 2][columnEnemy];
        EnemyPosition collisionEnemy = new EnemyPosition(lineEnemy + 2, columnEnemy);

        //Move current enemy
        matrix[lineEnemy][columnEnemy] = '.';
        currentEnemy.setLine(lineEnemy + 2);
        matrix[lineEnemy + 2][columnEnemy] = currentEnemyType;

        //Restore trap after enemy moves away
        for (EnemyPosition trap : traps) {
            if (trap.getLine()>0 && trap.getColumn()>0) {
                if (trap.getLine() == lineEnemy && trap.getColumn() == columnEnemy) {
                    matrix[lineEnemy][columnEnemy] = 'A';
                    break;
                }
            }
        }
        //Operate gates in case enemy steps on key
        if (lineKey > 0 && columnKey > 0) {
            //After enemy has moved away from key, the key is restored
            if (lineEnemy == lineKey && columnEnemy == columnKey) {
                matrix[lineKey][columnKey] = 'C';
            }

            //Open or close gates (depending on gatesOpen variable and if the hero/enemy has stepped on the key
            if (lineEnemy + 2 == lineKey && columnEnemy == columnKey) {
                gateOpenClose();
            }
        }

        //After movement, verify if current enemy has collided with already exiting enemy in that place
        if (collisionEnemyType == 'M' || collisionEnemyType == 'V' || collisionEnemyType == 'E') {
            collisionEnemy(currentEnemyType, currentEnemy, collisionEnemyType, collisionEnemy);
        }
        fireEnemyChanged(null);
    }

    public void moveEnemyLeft(EnemyPosition currentEnemy) {
        //Current moving enemy vars
        int lineEnemy = currentEnemy.getLine();
        int columnEnemy = currentEnemy.getColumn();
        char currentEnemyType = matrix[lineEnemy][columnEnemy];

        //Enemy in future cell vars+
        char collisionEnemyType = matrix[lineEnemy][columnEnemy - 2];
        EnemyPosition collisionEnemy = new EnemyPosition(lineEnemy, columnEnemy - 2);

        //Move current enemy
        matrix[lineEnemy][columnEnemy] = '.';
        currentEnemy.setColumn(columnEnemy - 2);
        matrix[lineEnemy][columnEnemy - 2] = currentEnemyType;

        //Restore trap after enemy moves away
        for (EnemyPosition trap : traps) {
            if (trap.getLine()>0 && trap.getColumn()>0) {
                if (trap.getLine() == lineEnemy && trap.getColumn() == columnEnemy) {
                    matrix[lineEnemy][columnEnemy] = 'A';
                    break;
                }
            }
        }

        //Operate gates in case enemy steps on key
        if (lineKey > 0 && columnKey > 0) {
            //After enemy has moved away from key, the key is restored
            if (lineEnemy == lineKey && columnEnemy == columnKey) {
                matrix[lineKey][columnKey] = 'C';
            }
            //Open or close gates (depending on gatesOpen variable and if the hero/enemy has stepped on the key
            if (lineEnemy == lineKey && columnEnemy - 2 == columnKey) {
                gateOpenClose();
            }
        }

        //After movement, verify if current enemy has collided with already exiting enemy in that place
        if (collisionEnemyType == 'M' || collisionEnemyType == 'V' || collisionEnemyType == 'E') {
            collisionEnemy(currentEnemyType, currentEnemy, collisionEnemyType, collisionEnemy);
        }
        fireEnemyChanged(null);
    }

    public void moveWhiteMummy() {
        for (int m = 0; m < whiteMummies.size(); m++) {
            EnemyPosition currentMummy = whiteMummies.get(m);
            int lineMummy = currentMummy.getLine();
            int columnMummy = currentMummy.getColumn();

            if (lineMummy == 0 || columnMummy == 0) {
                continue;
            }

            for (int i = 0; i < 2; i++) {
                if (lineMummy == 0 || columnMummy == 0) {
                    break;
                }

                if (columnHero < columnMummy) {
                    if (matrix[lineMummy][columnMummy - 1] == ' ' || matrix[lineMummy][columnMummy - 1] == ')') {
                        moveEnemyLeft(currentMummy);
                    } else {
                        if (lineHero < lineMummy) {
                            if (matrix[lineMummy - 1][columnMummy] == ' ' || matrix[lineMummy - 1][columnMummy] == '_') {
                                moveEnemyUp(currentMummy);
                            }
                        } else {
                            if (lineHero > lineMummy) {
                                if (matrix[lineMummy + 1][columnMummy] == ' ' || matrix[lineMummy + 1][columnMummy] == '_') {
                                    moveEnemyDown(currentMummy);
                                }
                            }
                        }
                    }
                } else if (columnHero > columnMummy) {
                    if (matrix[lineMummy][columnMummy + 1] == ' ' || matrix[lineMummy][columnMummy + 1] == ')') {
                        moveEnemyRight(currentMummy);
                    } else {
                        if (lineHero < lineMummy) {
                            if (matrix[lineMummy - 1][columnMummy] == ' ' || matrix[lineMummy - 1][columnMummy] == '_') {
                                moveEnemyUp(currentMummy);
                            }
                        } else {
                            if (lineHero > lineMummy) {
                                if (matrix[lineMummy + 1][columnMummy] == ' ' || matrix[lineMummy + 1][columnMummy] == '_') {
                                    moveEnemyDown(currentMummy);
                                }
                            }
                        }
                    }
                } else if (columnHero == columnMummy) {
                    if (lineHero < lineMummy) {
                        if (matrix[lineMummy - 1][columnMummy] == ' ' || matrix[lineMummy - 1][columnMummy] == '_') {
                            moveEnemyUp(currentMummy);
                        }
                    } else {
                        if (lineHero > lineMummy) {
                            if (matrix[lineMummy + 1][columnMummy] == ' ' || matrix[lineMummy + 1][columnMummy] == '_') {
                                moveEnemyDown(currentMummy);
                            }
                        }
                    }
                }

                lineMummy = currentMummy.getLine();
                columnMummy = currentMummy.getColumn();

                if (lineMummy == lineHero && columnMummy == columnHero) {
                    lineHero = columnHero = 0;
                    return;
                }
            }
        }
    }

    public void moveRedMummy() {
        for (int m = 0; m < redMummies.size(); m++) {
            EnemyPosition currentMummy = redMummies.get(m);
            int lineRedMummy = currentMummy.getLine();
            int columnRedMummy = currentMummy.getColumn();

            if (lineRedMummy == 0 || columnRedMummy == 0) {
                continue;
            }

            for (int i = 0; i < 2; i++) {
                if (lineRedMummy == 0 || columnRedMummy == 0) {
                    break;
                }

                if (lineHero < lineRedMummy) {
                    if (matrix[lineRedMummy - 1][columnRedMummy] == ' ' || matrix[lineRedMummy - 1][columnRedMummy] == '_') {
                        moveEnemyUp(currentMummy);
                    } else {
                        if (columnHero < columnRedMummy) {
                            if (matrix[lineRedMummy][columnRedMummy - 1] == ' ' || matrix[lineRedMummy][columnRedMummy - 1] == ')') {
                                moveEnemyLeft(currentMummy);
                            }
                        } else {
                            if (columnHero > columnRedMummy) {
                                if (matrix[lineRedMummy][columnRedMummy + 1] == ' ' || matrix[lineRedMummy][columnRedMummy + 1] == ')') {
                                    moveEnemyRight(currentMummy);
                                }
                            }
                        }
                    }
                } else if (lineHero > lineRedMummy) {
                    if (matrix[lineRedMummy + 1][columnRedMummy] == ' ' || matrix[lineRedMummy + 1][columnRedMummy] == '_') {
                        moveEnemyDown(currentMummy);
                    } else {
                        if (columnHero < columnRedMummy) {
                            if (matrix[lineRedMummy][columnRedMummy - 1] == ' ' || matrix[lineRedMummy][columnRedMummy - 1] == ')') {
                                moveEnemyLeft(currentMummy);
                            }
                        } else {
                            if (columnHero > columnRedMummy) {
                                if (matrix[lineRedMummy][columnRedMummy + 1] == ' ' || matrix[lineRedMummy][columnRedMummy + 1] == ')') {
                                    moveEnemyRight(currentMummy);
                                }
                            }
                        }
                    }
                } else if (lineHero == lineRedMummy) {
                    if (columnHero < columnRedMummy) {
                        if (matrix[lineRedMummy][columnRedMummy - 1] == ' ' || matrix[lineRedMummy][columnRedMummy - 1] == ')') {
                            moveEnemyLeft(currentMummy);
                        }
                    } else {
                        if (columnHero > columnRedMummy) {
                            if (matrix[lineRedMummy][columnRedMummy + 1] == ' ' || matrix[lineRedMummy][columnRedMummy + 1] == ')') {
                                moveEnemyRight(currentMummy);
                            }
                        }
                    }
                }

                lineRedMummy = currentMummy.getLine();
                columnRedMummy = currentMummy.getColumn();

                if (lineRedMummy == lineHero && columnRedMummy == columnHero) {
                    lineHero = columnHero = 0;
                    return;
                }
            }
        }
    }

    public void moveScorpion() {
        for (int m = 0; m < scorpions.size(); m++) {
            EnemyPosition currentScorpion = scorpions.get(m);
            int lineScorpion = currentScorpion.getLine();
            int columnScorpion = currentScorpion.getColumn();

            if (lineScorpion == 0 || columnScorpion == 0) {
                continue;
            }

            if (columnHero < columnScorpion) {
                if (matrix[lineScorpion][columnScorpion - 1] == ' ' || matrix[lineScorpion][columnScorpion - 1] == ')') {
                    moveEnemyLeft(currentScorpion);
                } else {
                    if (lineHero < lineScorpion) {
                        if (matrix[lineScorpion - 1][columnScorpion] == ' ' || matrix[lineScorpion - 1][columnScorpion] == '_') {
                            moveEnemyUp(currentScorpion);
                        }
                    } else {
                        if (lineHero > lineScorpion) {
                            if (matrix[lineScorpion + 1][columnScorpion] == ' ' || matrix[lineScorpion + 1][columnScorpion] == '_') {
                                moveEnemyDown(currentScorpion);
                            }
                        }
                    }
                }
            } else if (columnHero > columnScorpion) {
                if (matrix[lineScorpion][columnScorpion + 1] == ' ' || matrix[lineScorpion][columnScorpion + 1] == ')') {
                    moveEnemyRight(currentScorpion);
                } else {
                    if (lineHero < lineScorpion) {
                        if (matrix[lineScorpion - 1][columnScorpion] == ' ' || matrix[lineScorpion - 1][columnScorpion] == '_') {
                            moveEnemyUp(currentScorpion);
                        }
                    } else {
                        if (lineHero > lineScorpion) {
                            if (matrix[lineScorpion + 1][columnScorpion] == ' ' || matrix[lineScorpion + 1][columnScorpion] == '_') {
                                moveEnemyDown(currentScorpion);
                            }
                        }
                    }
                }
            } else if (columnHero == columnScorpion) {
                if (lineHero < lineScorpion) {
                    if (matrix[lineScorpion - 1][columnScorpion] == ' ' || matrix[lineScorpion - 1][columnScorpion] == '_') {
                        moveEnemyUp(currentScorpion);
                    }
                } else {
                    if (lineHero > lineScorpion) {
                        if (matrix[lineScorpion + 1][columnScorpion] == ' ' || matrix[lineScorpion + 1][columnScorpion] == '_') {
                            moveEnemyDown(currentScorpion);
                        }
                    }
                }
            }

            lineScorpion = currentScorpion.getLine();
            columnScorpion = currentScorpion.getColumn();


            if (lineScorpion == lineHero && columnScorpion == columnHero) {
                lineHero = columnHero = 0;
                return;
            }
        }
    }
    //#endregion EnemyMovement

    //#region Heuristics
     /**
     * This heuristic is determined with the calculation of the distance from the hero to the exit.
     *
     * HeroExitDistance = (lineHero-lineExit)+(columnHero-columnExit)
     * (The furthest away from the exit the bigger distance, meaning higher value for heuristic, therefore)
     *
     * @return distanceToExit       : distanceToExit value based on calculations of heuristic
     * @return 0                    : if hero has reached exit stairs
     * @return Double.MAX_VALUE     : if hero has died due to collision with enemies
     */
    public double computeHeroExitDistance() {
        //Evaluate if hero has died
        if (lineHero==0 && columnHero==0) {
            return Double.MAX_VALUE;
        }

        //Evaluate if hero has reached the exit stairs
        if (lineHero == lineExit && columnHero == columnExit) {
            return 0;
        }

        //Calculate distance from hero to exit
        return (Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit));
    }


    /**
     * This heuristic is determined with the calculation of the distance from the hero to the exit,
     *  along with the calculation of the distance from the hero to the closest enemy (of any kind).
     *
     * heroEnemyExitDistance = distanceHeroExit / distanceEnemyHero
     * The calculation of both distance components are similar to @computeHeroExitDistance heuristic distance
     *  calculation, however, in the distanceEnemyHero component *only* the closest enemy is taken into account.
     *
     * @return distanceHeroExit/distanceEnemyHero   : value based on calculations of heuristic
     * @return 0                                    : if hero has reached exit stairs
     * @return Double.MAX_VALUE                     : if hero has died due to collision with enemies
     */
    public double computeHeroEnemyExitDistance() {
        //Evaluate if hero has died
        if (lineHero==0 && columnHero==0) {
            return Double.MAX_VALUE;
        }

        //Evaluate if hero has reached the exit stairs
        if (lineHero == lineExit && columnHero == columnExit) {
            return 0;
        }

        //Calculate distance from the hero to the exit
        double distanceHeroExit = (Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit));

        //Calculate distance from the closest enemy to the hero
        double distanceEnemyHero = Double.MAX_VALUE;
        double auxDistanceEnemyHero=0;

        //Evaluate existence of enemies, if there are no enemies in-game then return the distance from hero to exit stairs
        if (whiteMummies.size()==0 && redMummies.size()==0 && scorpions.size()==0) {
            return distanceHeroExit;
        }

        if (whiteMummies.size() > 0) {
            for (EnemyPosition whiteMummy:whiteMummies) {
                //Calculate distance from hero to whiteMummy, the distance is divided by 2 because the enemy moves 2 tiles at the time
                auxDistanceEnemyHero = ((Math.abs(lineHero + whiteMummy.getLine()) + Math.abs(columnHero + whiteMummy.getColumn())))/2;

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                }
            }
        }

        if (redMummies.size() > 0) {
            for (EnemyPosition redMummy:redMummies) {
                //Calculate distance from hero to redMummy, the distance is divided by 2 because the enemy moves 2 tiles at the time
                auxDistanceEnemyHero = ((Math.abs(lineHero + redMummy.getLine()) + Math.abs(columnHero + redMummy.getColumn())))/2;

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                }
            }
        }

        if (scorpions.size() > 0) {
            for (EnemyPosition scorpion:scorpions) {
                //Calculate distance from hero to scorpion
                auxDistanceEnemyHero = ((Math.abs(lineHero + scorpion.getLine()) + Math.abs(columnHero + scorpion.getColumn())));

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                }
            }
        }

        return distanceHeroExit / distanceEnemyHero;
    }


    /**
     * This heuristic determines whether the closest enemy is blocked from moving (by walls or gates).
     *
     * @return 1                        : if closest enemy is blocked from moving or if there are no enemies present
     * @return 2                        : if closest enemy is not blocked from moving
     * @return 0                        : if hero has reached exit stairs
     * @return Double.MAX_VALUE         : if hero has died due to collision with enemies
     */
    public double computeClosestEnemyBlocked() {
        //Evaluate if hero has died
        if (lineHero==0 && columnHero==0) {
            return Double.MAX_VALUE;
        }

        //Evaluate if hero has reached the exit stairs
        if (lineHero == lineExit && columnHero == columnExit) {
            return 0;
        }

        double distanceEnemyHero = Double.MAX_VALUE;
        double auxDistanceEnemyHero;
        int lineEnemy = 0;
        int columnEnemy = 0;
        char enemyType = ' ';

        //Calculate distance from the hero to the exit
        double distanceHeroExit = (Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit));

        //Evaluate existence of enemies, if there are no enemies in-game then return 1
        if (whiteMummies.size()==0 && redMummies.size()==0 && scorpions.size()==0) {
            return 1;
        }

        //Next 3 if statements determine the closest enemy to the hero
        if (whiteMummies.size() > 0) {
            for (EnemyPosition whiteMummy:whiteMummies) {
                //Calculate distance from hero to whiteMummy, the distance is divided by 2 because the enemy moves 2 tiles at the time
                auxDistanceEnemyHero = ((Math.abs(lineHero + whiteMummy.getLine()) + Math.abs(columnHero + whiteMummy.getColumn())))/2;

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                    lineEnemy = whiteMummy.getLine();
                    columnEnemy = whiteMummy.getColumn();
                    enemyType = 'M';
                }
            }
        }
        if (redMummies.size() > 0) {
            for (EnemyPosition redMummy:redMummies) {
                //Calculate distance from hero to redMummy, the distance is divided by 2 because the enemy moves 2 tiles at the time
                auxDistanceEnemyHero = ((Math.abs(lineHero + redMummy.getLine()) + Math.abs(columnHero + redMummy.getColumn())))/2;

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                    lineEnemy = redMummy.getLine();
                    columnEnemy = redMummy.getColumn();
                    enemyType = 'V';
                }
            }
        }
        if (scorpions.size() > 0) {
            for (EnemyPosition scorpion:scorpions) {
                //Calculate distance from hero to scorpion
                auxDistanceEnemyHero = ((Math.abs(lineHero + scorpion.getLine()) + Math.abs(columnHero + scorpion.getColumn())));

                //Evaluate if the current enemy is the closest one to the hero
                if (auxDistanceEnemyHero < distanceEnemyHero) {
                    distanceEnemyHero = auxDistanceEnemyHero;
                    lineEnemy = scorpion.getLine();
                    columnEnemy = scorpion.getColumn();
                    enemyType = 'E';
                }
            }
        }

        //Determine if the closest enemy (being a whiteMummy or a scorpion) is blocked by walls or closed gates
        if(enemyType == 'M' || enemyType == 'E') {
            if (columnHero < columnEnemy && lineHero < lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero < columnEnemy && lineHero > lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero > columnEnemy && lineHero < lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero > columnEnemy && lineHero > lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero == columnEnemy && lineHero < lineEnemy) {
                if (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"') {
                    return 1;
                }
            } else if (columnHero == columnEnemy && lineHero > lineEnemy) {
                if (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"') {
                    return 1;
                }
            }
        } else if (enemyType == 'V') {
            //Determine if the closest enemy (redMummy) is blocked by walls or closed gates
            //The statement is separated from the previous statement because redMummies have a different movement order
            // than other enemies
            if (lineHero < lineEnemy && columnHero < columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero < lineEnemy && columnHero > columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero > lineEnemy && columnHero < columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero > lineEnemy && columnHero > columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero == lineEnemy && columnHero < columnEnemy) {
                if (matrix[lineEnemy][columnEnemy - 1] == '-' || matrix[lineEnemy][columnEnemy - 1] == '"') {
                    return 1;
                }
            } else if (lineHero == lineEnemy && columnHero > columnEnemy) {
                if (matrix[lineEnemy][columnEnemy + 1] == '-' || matrix[lineEnemy][columnEnemy + 1] == '"') {
                    return 1;
                }
            }
        }

        //Value returned in case the closest enemy isn't blocked from moving
        return 2;
    }


    /**
     * This heuristic is determined with the calculation of the amount of enemies which aren't able to move, which are
     *  stuck from moving by either a wall or closed gate.
     *
     *
     *
     * In first place, the closest enemy is determined, afterwards its evaluated wheter that enemy is blocked and finna
     *
     * @return amountBlockedEnemies  : value based on calculations of heuristic for amount of blocked enemies
     * @return amountOfEnemies       : in case none of the enemies are blocked
     * @return 1                     : if there are no enemies present
     * @return 0                     : if hero has reached exit stairs
     * @return Double.MAX_VALUE      : if hero has died due to collision with enemies
     */
    public double computeEnemiesBlocked() {
        //Evaluate if hero has died
        if (lineHero==0 && columnHero==0) {
            return Double.MAX_VALUE;
        }

        //Evaluate if hero has reached the exit stairs
        if (lineHero == lineExit && columnHero == columnExit) {
            return 0;
        }

        if (whiteMummies.size()==0 && redMummies.size()==0 && scorpions.size()==0) {
            return 1;
        }

        int amountEnemiesBlocked = 0;

        if (whiteMummies.size() > 0) {
            for (EnemyPosition whiteMummy:whiteMummies) {
                amountEnemiesBlocked+=enemiesBlocked(whiteMummy.getLine(),whiteMummy.getColumn(),'M');
            }
        }

        if (redMummies.size() > 0) {
            for (EnemyPosition redMummy:redMummies) {
                amountEnemiesBlocked+=enemiesBlocked(redMummy.getLine(), redMummy.getColumn(),'V');
            }
        }

        if (scorpions.size() > 0) {
            for (EnemyPosition scorpion:scorpions) {
                amountEnemiesBlocked+=enemiesBlocked(scorpion.getLine(),scorpion.getColumn(),'E');
            }
        }

        if (amountEnemiesBlocked==0) {
            return whiteMummies.size() + redMummies.size() + scorpions.size();
        }

        return 1/amountEnemiesBlocked;
    }
    //Auxiliary function to determine if given enemy(line, column, type) is blocked
    public double enemiesBlocked(int lineEnemy, int columnEnemy, char enemyType) {
        if(enemyType == 'M' || enemyType == 'E') {
            if (columnHero < columnEnemy && lineHero < lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero < columnEnemy && lineHero > lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero > columnEnemy && lineHero < lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero > columnEnemy && lineHero > lineEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (columnHero == columnEnemy && lineHero < lineEnemy) {
                if (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"') {
                    return 1;
                }
            } else if (columnHero == columnEnemy && lineHero > lineEnemy) {
                if (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"') {
                    return 1;
                }
            }
        }
        else if (enemyType == 'V'){
            if (lineHero < lineEnemy && columnHero < columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero < lineEnemy && columnHero > columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy - 1] == '=') && (matrix[lineEnemy - 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero > lineEnemy && columnHero < columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy - 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy - 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero > lineEnemy && columnHero > columnEnemy) {
                if ((matrix[lineEnemy][columnEnemy + 1] == '|' || matrix[lineEnemy][columnEnemy + 1] == '=') && (matrix[lineEnemy + 1][columnEnemy] == '-' || matrix[lineEnemy + 1][columnEnemy] == '"')) {
                    return 1;
                }
            } else if (lineHero == lineEnemy && columnHero < columnEnemy) {
                if (matrix[lineEnemy][columnEnemy - 1] == '-' || matrix[lineEnemy][columnEnemy - 1] == '"') {
                    return 1;
                }
            } else if (lineHero == lineEnemy && columnHero > columnEnemy) {
                if (matrix[lineEnemy][columnEnemy + 1] == '-' || matrix[lineEnemy][columnEnemy + 1] == '"') {
                    return 1;
                }
            }
        }
        return 0;
    }
    //#endregion Heuristics

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MummyMazeState)) {
            return false;
        }

        MummyMazeState o = (MummyMazeState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public MummyMazeState clone() {
        return new MummyMazeState(matrix,new EnemyPosition(lineHero,columnHero),new EnemyPosition(lineExit,columnExit),whiteMummies,redMummies,scorpions,traps,new EnemyPosition(lineKey,columnKey),gates);
    }

    //Listeners
    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void fireHeroChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.heroChanged(null);
        }
    }

    public void fireEnemyChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.enemyChanged(null);
        }
    }
}
