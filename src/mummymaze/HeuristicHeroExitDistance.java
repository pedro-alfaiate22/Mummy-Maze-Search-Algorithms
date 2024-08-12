package mummymaze;

import agent.Heuristic;

public class HeuristicHeroExitDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeHeroExitDistance();
    }
    
    @Override
    public String toString(){
        return "Hero distance to exit";
    }
}