package mummymaze;

import agent.Agent;
import java.io.File;
import java.io.IOException;

public class  MummyMazeAgent extends Agent<MummyMazeState>{
    
    protected MummyMazeState initialEnvironment;
    private EnemyPosition key;
    private EnemyPosition exit;
    private EnemyPosition hero;

    private Cell whiteMummies;
    private Cell redMummies;
    private Cell scorpions;
    private Cell traps;
    private Cell gates;

    public MummyMazeAgent(MummyMazeState environemt) {
        super(environemt);
        initialEnvironment = (MummyMazeState) environemt.clone();
        heuristics.add(new HeuristicHeroExitDistance());
        heuristics.add(new HeuristicHeroEnemyExitDistance());
        heuristics.add(new HeuristicClosestEnemyBlocked());
        heuristics.add(new HeuristicEnemiesBlocked());
        heuristic = heuristics.get(0);
    }
            
    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();
        return environment;
    }
                 
    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        whiteMummies = new Cell();
        redMummies = new Cell();
        scorpions = new Cell();
        traps = new Cell();
        gates = new Cell();

        exit = new EnemyPosition();
        key = new EnemyPosition();
        hero = new EnemyPosition();

        char[][] matrix = new char [13][13];
        for (int i = 0; i < 13; i++) {
            String s = scanner.nextLine();
            matrix[i] = s.toCharArray();

            for (int j = 0; j < 13; j++) {
                switch (matrix[i][j]) {
                    //Hero position
                    case 'H':
                        hero.setLine(i);
                        hero.setColumn(j);
                        break;

                    //Exit stairs position
                    case 'S':
                        exit.setLine(i);
                        exit.setColumn(j);
                        break;

                    //White mummy positions
                    case 'M':
                        whiteMummies.add(i,j);
                        break;

                    //Red mummy positions
                    case 'V':
                        redMummies.add(i,j);
                        break;

                    //Scorpion positions
                    case 'E':
                        scorpions.add(i,j);
                        break;

                    //Traps positions
                    case'A':
                        traps.add(i,j);
                        break;

                    //Key position
                    case'C':
                        key.setLine(i);
                        key.setColumn(j);
                        break;

                    //Closed gates positions
                    case '=':
                    case '"':
                        gates.add(i,j);
                        break;
                }
            }
        }

        initialEnvironment = new MummyMazeState(matrix,hero,exit,whiteMummies,redMummies,scorpions,traps,key,gates);
        resetEnvironment();
        return environment;
    }
}