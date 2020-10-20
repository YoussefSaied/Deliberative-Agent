package template;

import logist.task.Task;
import logist.task.TaskSet;


public final class Deliver extends BetterAction {

    public final Task task;

    public Deliver(Task task) {
        this.task = task;
    }

    @Override
    public logist.plan.Action getLogistAction() {
        return new logist.plan.Action.Delivery(task);
    }

    @Override
    public State transformState(State initialState) {
        // Drop the task and free the truck
        TaskSet newCarriedTasks = initialState.carriedTasks.clone();
        newCarriedTasks.remove(task);

        int newEmptySpace = initialState.emptySpace + task.weight;

        return new State(initialState.currentLocation, newCarriedTasks, initialState.tasks, newEmptySpace, initialState.costPerKm);
    }

    @Override
    public double cost(State initialState) {
        // In this implementation there are no rewards, only costs as all tasks are eventually delivered.
        return 0.0;
    }

    @Override
    public String toString() {
        return "Delivered  "+ task.id+ " from " + task.pickupCity.id +  " to " + task.deliveryCity.id ;
    }

}
