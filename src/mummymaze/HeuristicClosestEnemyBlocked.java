package mummymaze;

import agent.Heuristic;

public class HeuristicClosestEnemyBlocked extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeClosestEnemyBlocked();
    }
    
    @Override
    public String toString(){
        return "Closest enemy blocked";
    }
}