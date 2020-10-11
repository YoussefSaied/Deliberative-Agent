package template;

// Example for helper class
import java.lang.*;
import java.util.*;

import logist.plan.Action;
import logist.plan.Action.Delivery;
import logist.plan.Action.Move;
import logist.plan.Action.Pickup;

class HelperClass {

    public static Double getCost (State state, Action action, Task task) {
        if (action instanceof Move) {
            double distance = state.getCurrentCity().distanceTo(state.getDestinationCity());
            return distance * state.getCostPerKm();
        } else if (action instanceof Delivery) {
            return -task.reward;
        } else if (action instanceof Pickup) {
            return 0.0;
        }
    }
} /*** end of helperClass **/
