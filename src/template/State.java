package template;

import logist.plan.Plan;
import logist.task.TaskSet;
import logist.topology.Topology.City;

public class State {

    public final City currentLocation;

    public final TaskSet carriedTasks;

    public final TaskSet tasks;

    public final int emptySpace;

    private final int costPerKm;

    private Plan correspPlan;

    public State (City currentCity, TaskSet deliveries, TaskSet available, int remainingCapacity, int costPerKm) {
        this.currentLocation = currentCity;
        this.carriedTasks = deliveries;
        this.tasks = available;
        this.emptySpace = remainingCapacity;
        this.costPerKm = costPerKm;
    }

    public void setPlan(Plan plan) {
        this.correspPlan = plan;
    }

    public Plan getPlan() {
        return this.correspPlan;
    }

    @Override
    public int hashCode() {
        final int prime = 11;
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

}
