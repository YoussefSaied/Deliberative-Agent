package template;


import java.util.List;


class SubPlan implements Comparable<SubPlan> {

    // The actual partial plan.
    public final List<BetterAction> actions;

    // The state which resulted from applying the plan.
    public final State lastState;

    // Heuristic-based cost estimation of applying the plan.
    public final double heuristicCost;

    // Cost of applying the plan from the start to the current state
    public final double knownCost;

    public SubPlan(List<BetterAction> actions, State lastState, double heuristicCost, double knownCost) {
        this.actions = actions;
        this.lastState = lastState;
        this.heuristicCost = heuristicCost;
        this.knownCost = knownCost;
    }


    public SubPlan(List<BetterAction> actions, State lastState) {
        this.actions = actions;
        this.lastState = lastState;
        this.heuristicCost = 0;
        this.knownCost = 0;
    }


    public double getCost() {
        return heuristicCost + knownCost;
    }


    @Override
    public int compareTo(SubPlan o) {
        // Compare 2 subplans.

        if (getCost() < o.getCost()) return -1;
        else if (getCost() > o.getCost()) return 1;
        else return 0;
    }

}
