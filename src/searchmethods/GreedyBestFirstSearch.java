package searchmethods;

import agent.State;
import java.util.List;

public class GreedyBestFirstSearch extends InformedSearch {

    //f = h
    @Override
    public void addSuccessorToFrontier(State successor, Node parent) {

        double g = parent.getG()+successor.getAction().getCost();
        double h =heuristic.compute(successor);

        if(!frontier.containsState(successor)){
            if (!explored.contains(successor)){
                frontier.add(new Node(successor, parent, g, h));
            }
        }else if (g<frontier.getNode(successor).getG()){
            frontier.removeNode(successor);
            frontier.add(new Node(successor, parent, g, h));
        }

    }

    @Override
    public String toString() {
        return "Greedy best first search";
    }
}