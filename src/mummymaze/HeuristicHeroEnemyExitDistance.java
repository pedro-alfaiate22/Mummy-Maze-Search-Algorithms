package mummymaze;

import agent.Heuristic;

public class HeuristicHeroEnemyExitDistance extends Heuristic<MummyMazeProblem, MummyMazeState> {

    @Override
    public double compute(MummyMazeState state) {
        return state.computeHeroEnemyExitDistance();
    }
    
    @Override
    public String toString(){
        return "Hero distance to exit & enemy";
    }    
}