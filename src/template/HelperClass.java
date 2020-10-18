package template;

// Example for helper class

import logist.plan.Action;

import java.util.ArrayList;
import java.util.List;


class HelperClass {

    public static List<Action> convertToLogistActions(List<BetterAction> actions) {
        ArrayList<Action> logistActions = new ArrayList<>();
        for (BetterAction action : actions) {
            logistActions.add(action.getLogistAction());
        }
        return logistActions;
    }

    public static String convertBetterActionsToString(List<BetterAction> actions) {
        String plan= "Plan is ";
        for (BetterAction action : actions) {
            plan = plan +"\n" + action;
        }
        return plan;
    }

}
