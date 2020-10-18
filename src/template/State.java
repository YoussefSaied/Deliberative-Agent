package template;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class State {

    public final City currentLocation;

    public final TaskSet carriedTasks;

    public final TaskSet tasks;

    public final int emptySpace;

    public final int costPerKm;

    private Plan correspPlan;

    public State (City currentCity, TaskSet carriedTasks, TaskSet tasks, int remainingCapacity, int costPerKm) {
        this.currentLocation = currentCity;
        this.carriedTasks = carriedTasks;
        this.tasks = tasks;
        this.emptySpace = remainingCapacity;
        this.costPerKm = costPerKm;
    }

    public void setPlan(Plan plan) {
        this.correspPlan = plan;
    }

    public Plan getPlan() {
        return this.correspPlan;
    }

    /**
     * Create the initial state for the given vehicle and the task list
     */
    public static State recreateInitialState(Vehicle vehicle, TaskSet tasks) {
        TaskSet currentTasks = vehicle.getCurrentTasks();
        int remainingCapacity = vehicle.capacity() - currentTasks.weightSum();
        return new State(vehicle.getCurrentCity(), currentTasks, tasks, remainingCapacity, vehicle.costPerKm());
    }

    /**
     *
     * @return list of all possible actions given the current state
     */
    public List<BetterAction> getPossibleActions(){
        List<BetterAction> possibleActions = new ArrayList<BetterAction>(); // A list of possible actions
        Set<City> destinations = new HashSet<City>(); // The set of logical destinations for pickup or delivery. A set is used to avoid duplicates.

        // Drop actions
        for (Task taskToDrop : carriedTasks) {
            if (taskToDrop.deliveryCity.equals(currentLocation)) {
                // We are in the same city so we can deliver deliver the task now.
                possibleActions.add(new Deliver(taskToDrop));
            } else {
                // We need to go to the task's destination city.
                List<City> path = currentLocation.pathTo(taskToDrop.deliveryCity);
                City nextStep = path.get(0);
                destinations.add(nextStep);
            }
        }

        // Pickup actions
        for (Task taskToPick : tasks) {
            // If the task is too heavy, it is not considered
            if (taskToPick.weight > emptySpace)
                continue;

            if (taskToPick.pickupCity.equals(currentLocation)) {
                // We are in the same city so we can pick it up now.
                possibleActions.add(new Pickup(taskToPick));
            } else {
                // We need to go to this task's pickup site and similarly to the drop case
                // we add the next city toward it.
                List<City> path = currentLocation.pathTo(taskToPick.pickupCity);
                City nextStep = path.get(0);
                destinations.add(nextStep);
            }

        }

        // Move actions
        for (City destination : destinations) {
            assert currentLocation.neighbors().contains(destination); // additional check (normally redundant)
            possibleActions.add(new Move(destination));
        }


        return possibleActions;

    }


    @Override
    public int hashCode() {
        final int prime = 13;
        int result = 1;
        result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
        result = prime * result + ((currentLocation == null) ? 0 : currentLocation.hashCode());
        result = prime * result + ((carriedTasks == null) ? 0 : carriedTasks.hashCode());
        result = prime * result + emptySpace;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        State other = (State) obj;
        if (tasks == null) {
            if (other.tasks != null)
                return false;
        } else if (!tasks.equals(other.tasks))
            return false;

        if (currentLocation == null) {
            if (other.currentLocation != null)
                return false;
        } else if (!currentLocation.equals(other.currentLocation))
            return false;

        if (carriedTasks == null) {
            if (other.carriedTasks != null)
                return false;
        } else if (!carriedTasks.equals(other.carriedTasks))
            return false;

        if (emptySpace != other.emptySpace)
            return false;

        return true;
    }


    public State transformState (BetterAction action) {
        return action.transformState(this);
    }


    public boolean isGoal() {
        return carriedTasks.isEmpty() && tasks.isEmpty();
    }

}
