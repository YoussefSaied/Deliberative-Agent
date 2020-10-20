package template;

import logist.topology.Topology;


public final class Move extends BetterAction {

    public final Topology.City destination;

    public Move(Topology.City destination) {
        this.destination = destination;
    }

    @Override
    public logist.plan.Action getLogistAction() {
        return new logist.plan.Action.Move(destination);
    }

    @Override
    public State transformState(State initialState) {
        // Simply move to the destination
        return new State(destination, initialState.carriedTasks,
                initialState.tasks, initialState.emptySpace, initialState.costPerKm);
    }

    @Override
    public double cost(State initialState) {
        // Calculate travelling cost
        double distance = initialState.currentLocation.distanceTo(destination);
        return distance * initialState.costPerKm;
    }

    @Override
    public String toString() {
        return "Move to "+ destination.id;
    }

}
