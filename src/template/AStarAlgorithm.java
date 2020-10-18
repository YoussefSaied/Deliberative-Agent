
package template;

import java.util.*;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskSet;

/**
 * Implements the A* algorithm with the following heuristics:
 *
 *
 */
public class AStarAlgorithm {

    // A map from the set of states already visited to their estimated cost
    private final Map<State, Double> stateCostMap = new HashMap<State, Double>();

    // A queue of the states to visit sorted by priority (cost).
    private final PriorityQueue<Planoid> queue = new PriorityQueue<Planoid>();

    // Initial state.
    private final State initialState;

    private final Heuristic heuristic;

    /* see computeHeuristic for details */
    public enum Heuristic {
        DELIVERY, OPTIMISTIC, CONSTANT
    }

    public AStarAlgorithm(Vehicle vehicle, TaskSet tasks, Heuristic heuristic) {
        this.heuristic = heuristic;
        // Initialize the al from the initial state

        this.initialState = State.recreateInitialState(vehicle, tasks);

        Planoid initialPlan = new Planoid(new ArrayList<BetterAction>(), this.initialState,
                computeHeuristic(initialState), 0.0);

        queue.add(initialPlan);
    }

    public Plan build() {

        do {
            Planoid vertex = dequeue();

            if (vertex.lastState.isGoal()) {
                System.out.println("# of node visited: " + stateCostMap.size());
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


    private double computeHeuristic(State state) {
        switch (heuristic) {
            case CONSTANT:
                // Simplest heuristic
                // -> fast, but sub-optimal for small enough problem. Doesn't converge for higher number of tasks.
                return 0;

            case OPTIMISTIC:
                // Naive heuristic
                // -> MUCH slower but much more cost-efficient
                return -state.tasks.rewardSum() - state.carriedTasks.rewardSum();

            case DELIVERY:
                // Slightly less naive heuristic: use only what's on the lorry to predict cost
                // -> Very fast, a bit sub-optimal
                return -state.carriedTasks.rewardSum();

            default:
                throw new AssertionError("Should not happen.");
        }
    }

    private void enqueue(Planoid parentVertex, BetterAction action) {
        // Create a new Planoid using a parent Planoid/ vertex and then enqueue it to the priority queue.
        List<BetterAction> actions = new LinkedList<BetterAction>(parentVertex.actions);
        actions.add(action);

        State newLastState = parentVertex.lastState.transformState(action);

        double knownCost = parentVertex.knownCost + action.cost(parentVertex.lastState);
        double heuristic = computeHeuristic(newLastState);

        Planoid nextNode = new Planoid(actions, newLastState, heuristic, knownCost);

        queue.add(nextNode);
    }

    private Planoid dequeue() {
        return queue.remove();
    }

} /*** end of AStarAlgorithm **/