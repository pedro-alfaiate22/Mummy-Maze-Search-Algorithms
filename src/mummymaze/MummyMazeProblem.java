package mummymaze;

import agent.Action;
import agent.Problem;

import java.util.LinkedList;
import java.util.List;

public class MummyMazeProblem extends Problem<MummyMazeState> {

    protected List<Action> actions;

    public MummyMazeProblem(MummyMazeState initialState) {
        super(initialState);
        actions = new LinkedList<Action>() {{
            add(new ActionDown());
            add(new ActionUp());
            add(new ActionRight());
            add(new ActionLeft());
            add(new ActionStay());
        }};
    }

    @Override
    public List<Action<MummyMazeState>> getActions(MummyMazeState state) {
        List<Action<MummyMazeState>> possibleActions = new LinkedList<>();
        if(state.getLineHero()==0 && state.getColumnHero()==0)
            return possibleActions;
        for (Action action : actions) {
            if (action.isValid(state)) {
                possibleActions.add(action);
            }
        }
        return possibleActions;
    }

    @Override
    public MummyMazeState getSuccessor(MummyMazeState state, Action action){
        MummyMazeState successor = state.clone();
        action.execute(successor);
        return successor;
    }


    @Override
    public boolean isGoal(MummyMazeState state) {
        int lineHero = state.getLineHero();
        int lineExit = state.getLineExit();
        int columnHero = state.getColumnHero();
        int columnExit = state.getColumnExit();

        if (lineHero == lineExit && columnHero == columnExit) {
            return true;
        }
        return false;
    }


    @Override
    public double computePathCost(List<Action> path) {
        return path.size();
    }


}


