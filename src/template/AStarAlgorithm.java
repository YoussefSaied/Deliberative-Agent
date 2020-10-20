
package template;

import java.util.*;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskSet;


/**
 * Implements the A* algorithm with an optimal heuristic.
 *
 *
 */
public class AStarAlgorithm {

    // A map from the set of states already visited to their estimated cost
    private final Map<State, Double> stateCostMap = new HashMap<State, Double>();

    // A queue of the states to visit sorted by priority (cost).
    private final PriorityQueue<SubPlan> queue = new PriorityQueue<SubPlan>();

    // Initial state.
    private final State initialState;

    public AStarAlgorithm(Vehicle vehicle, TaskSet tasks) {
        // Initialize the al from the initial state

        this.initialState = State.recreateInitialState(vehicle, tasks);

        double heuristicCost = -initialState.tasks.rewardSum() - initialState.carriedTasks.rewardSum();

        SubPlan initialPlan = new SubPlan(new ArrayList<BetterAction>(), this.initialState,
                heuristicCost, 0.0);


        queue.add(initialPlan);
    }

    /**
     *
     * @return Optimal Plan using the A* algorithm.
     */
    public Plan build() {

        do {
            SubPlan vertex = dequeue();

            if (vertex.lastState.isGoal()) {
                System.out.println("# of actions " + vertex.actions.size());
                System.out.println("# of node visited: " + stateCostMap.size());
                System.out.println(HelperClass.convertBetterActionsToString(vertex.actions));
                return new Plan(initialState.currentLocation, HelperClass.convertToLogistActions( vertex.actions));
            }

            Double previousCost = stateCostMap.getOrDefault(vertex.lastState, Double.POSITIVE_INFINITY);
            Double currentCost = vertex.getCost();
            if (currentCost < previousCost) {
                stateCostMap.put(vertex.lastState, currentCost);

                // Augment the queue of partial plans of interest
                for (BetterAction action : vertex.lastState.getPossibleActions()) {
                    enqueue(vertex, action);
                }
            }

        } while (true);
    }


    private void enqueue(SubPlan parentVertex, BetterAction action) {
        // Create a new Planoid using a parent Planoid/ vertex and then enqueue it to the priority queue.
        List<BetterAction> actions = new LinkedList<BetterAction>(parentVertex.actions);
        actions.add(action);

        State newLastState = parentVertex.lastState.transformState(action);

        double knownCost = parentVertex.knownCost + action.cost(parentVertex.lastState);
        double heuristicCost = -newLastState.tasks.rewardSum() - newLastState.carriedTasks.rewardSum();

        SubPlan nextNode = new SubPlan(actions, newLastState, heuristicCost, knownCost);

        queue.add(nextNode);
    }

    private SubPlan dequeue() {
        return queue.remove();
    }

} /*** end of AStarAlgorithm **/