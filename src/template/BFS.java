package template;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * Implements the BFS algorithm.
 *
 *
 */
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
     * @return Optimal Plan using the BFS algorithm.
     */
    public Plan build(boolean optimziations) {

        // A list of vertices to visits and the actions leading up to them:
        LinkedList<SubPlan> queue = new LinkedList<>();


        ArrayList<State> alreadyVisitedStates = new ArrayList<>();
        queue.addLast(new SubPlan(new ArrayList<>(), intialState));
        Double optimalCost = Double.POSITIVE_INFINITY;
        SubPlan optimalSubPlan = null;
        // Debugging:
        int i=0;

        while (!queue.isEmpty()) {
            SubPlan vertex = queue.poll();
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
                        optimalSubPlan = vertex;
                        optimalCost = vertex.getCost();
                    }
                }
            } else if (!alreadyVisitedStates.contains(state)) {
                alreadyVisitedStates.add(state);
                for (BetterAction action : state.getPossibleActions()) {
                    State successorState = state.transformState(action);
                    ArrayList<BetterAction> newPlanoidActions = new ArrayList<BetterAction>(vertex.actions);
                    newPlanoidActions.add(action);
//                    SubPlan potentialSubPlan =new SubPlan(newSubPlanActions, successorState,
//                            0.0, vertex.getCost() + action.cost(state) );
                    SubPlan potentialSubPlan = new SubPlan(newPlanoidActions, successorState);
                    Double previousCost = stateCostMap.getOrDefault(successorState, Double.POSITIVE_INFINITY);
                    Double currentCost = potentialSubPlan.getCost();
                    queue.addLast(potentialSubPlan);

                    // Attempt to implement cost-wise optimisation on BFS. However, it is not required for basic BFS.
//                    if (currentCost < previousCost && currentCost < optimalCost ) {
//                        stateCostMap.put(vertex.lastState, currentCost);
//                        queue.addLast(potentialSubPlan);
//
//                        //Debugging:
////                        System.out.println("# of actions " + potentialSubPlan.actions.size());
////                        System.out.println("# of tasks " + potentialSubPlan.lastState.tasks.size());
////                        i++;
//                    }

                }
            }
        }
        if (optimalSubPlan != null) {
            if (optimalSubPlan.lastState.isGoal()){
                return new Plan(initialCity, HelperClass.convertToLogistActions(optimalSubPlan.actions));}
        }
        throw new AssertionError("Goal state never reached!");
    }

} /*** end of BFS Algorithm **/
