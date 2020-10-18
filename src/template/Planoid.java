package template;


import java.util.List;

class Planoid implements Comparable<Planoid> {
    public final List<BetterAction> actions; // Partial plan
    public final State lastState; // Resulting state from applying the plan
    public final double heuristicCost; // Estimated cost of applying the plan until goal is satisfied
    public final double knownCost; // Cost of applying the plan from the start to now

    public Planoid(List<BetterAction> actions, State lastState, double heuristicCost, double knownCost) {
        this.actions = actions;
        this.lastState = lastState;
        this.heuristicCost = heuristicCost;
        this.knownCost = knownCost;
    }


    public Planoid(List<BetterAction> actions, State lastState) {
        super();
        this.actions = actions;
        this.lastState = lastState;
        this.heuristicCost = 0;
        this.knownCost = 0;
    }

    /**
     * Returns the cost of this planoid.
     */

    public double getCost() {
        return heuristicCost + knownCost;
    }

    /**
     * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     * the specified object.
     */
    @Override
    public int compareTo(Planoid o) {
        if (getCost() < o.getCost()) return -1;
        else if (getCost() > o.getCost()) return 1;
        else return 0;
    }


}
