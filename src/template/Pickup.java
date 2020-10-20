package template;

import logist.task.Task;
import logist.task.TaskSet;


public final class Pickup extends BetterAction {

    public final Task task;

    public Pickup(Task task) {
        this.task = task;
    }

    @Override
    public logist.plan.Action getLogistAction() {
        return new logist.plan.Action.Pickup(task);
    }

    @Override
    public State transformState(State initialState) {
        assert initialState.emptySpace >= task.weight;

        // Move the task from the set of available tasks on the topology to the set of carried tasks.
        TaskSet newTasks = initialState.tasks.clone();
        newTasks.remove(task);

        TaskSet newCarriedTasks = initialState.carriedTasks.clone();
        newCarriedTasks.add(task);

        int newRemainingCapacity = initialState.emptySpace - task.weight;

        return new State(initialState.currentLocation, newCarriedTasks,
                newTasks, newRemainingCapacity, initialState.costPerKm);
    }

    @Override
    public double cost(State initialState) {
        // There is no cost or reward for picking up.
        return 0;
    }

    @Override
    public String toString() {
        return "Picked up  "+ task.id+ " from " + task.pickupCity.id +  " to deliver to " + task.deliveryCity.id ;
    }

}
