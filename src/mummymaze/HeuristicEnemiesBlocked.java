package mummymaze;

import agent.Heuristic;

public class HeuristicEnemiesBlocked extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeEnemiesBlocked();
    }
    
    @Override
    public String toString(){
        return "Amount of enemies blocked";
    }
}