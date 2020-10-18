package template;

public abstract class BetterAction {
    public abstract logist.plan.Action getLogistAction();

    public abstract State transformState(State initialState); // apply the action the current state

    public abstract double cost(State initialState); // cost (or reward) of applying the action

    public abstract String toString();
}

