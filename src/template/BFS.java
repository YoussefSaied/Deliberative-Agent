package template;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class BFS {

    private City initialCity;
    private State intialState;
    // A map from the set of states already visited to their estimated cost
    private final Map<State, Double> stateCostMap = new HashMap<>();

    public BFS(Vehicle vehicle, TaskSet tasks) {
        this.initialCity = vehicle.getCurrentCity();
        this.intialState = State.recreateInitialState(vehicle, tasks);

    }

    /**
     *
     * @return Optimal Plan using the BFS algorithm
     */

    public Plan build(boolean optimziations) {
        // A list of vertices to visits and the actions leading up to them:
        LinkedList<Planoid> queue = new LinkedList<>(); // To Iuliana: Why use this type?
        ArrayList<State> alreadyVisitedStates = new ArrayList<>(); // To Iuliana: Why use this type?
        queue.addLast(new Planoid(new ArrayList<>(), intialState));
        Double optimalCost =Double.POSITIVE_INFINITY;
        Planoid optimalPlanoid =null;
        // Debugging:
        int i=0;

        while (!queue.isEmpty()) {
            Planoid vertex = queue.poll();
            State state = vertex.lastState;
            if (state.isGoal()) {
                //Debugging:empty

                if (!optimziations) {
                    System.out.println("# of actions " + vertex.actions.size());
                    System.out.println("# of node visited: " + alreadyVisitedStates.size());
                    System.out.println(HelperClass.convertBetterActionsToString(vertex.actions));
                    return new Plan(initialCity, HelperClass.convertToLogistActions(vertex.actions));

                }
                else {
                    if (vertex.getCost() < optimalCost) {
                        optimalPlanoid = vertex;
                        optimalCost = vertex.getCost();
                    }
                }
            } else if (!alreadyVisitedStates.contains(state)) {
                alreadyVisitedStates.add(state);
                for (BetterAction action : state.getPossibleActions()) {
                    State successorState = state.transformState(action);
                    ArrayList<BetterAction> newPlanoidActions = new ArrayList<BetterAction>(vertex.actions);
                    newPlanoidActions.add(action);
                    Planoid potentialPlanoid =new Planoid(newPlanoidActions, successorState,
                            0.0,vertex.getCost()+ action.cost(state) );
                    Double previousCost = stateCostMap.getOrDefault(successorState, Double.POSITIVE_INFINITY);
                    Double currentCost = potentialPlanoid.getCost();


                    if (currentCost < previousCost && currentCost < optimalCost ) {
                        stateCostMap.put(vertex.lastState, currentCost);
                        queue.add(potentialPlanoid);

                        //Debugging:
//                        System.out.println("# of actions " + potentialPlanoid.actions.size());
//                        System.out.println("# of tasks " + potentialPlanoid.lastState.tasks.size());
//                        i++;
                    }

                }
            }
        }
        if (optimalPlanoid != null) {
            if (optimalPlanoid.lastState.isGoal()){
                return new Plan(initialCity, HelperClass.convertToLogistActions(optimalPlanoid.actions));}
        }
        throw new AssertionError("Goal state never reached!");
    }
}
