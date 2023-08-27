package pl.timsixth.vouchers.manager.registration;

import pl.timsixth.vouchers.model.menu.action.Action;

import java.util.*;

public class ActionRegistrationImpl implements ActionRegistration {

    private final Map<String, Action> actions = new HashMap<>();

    @Override
    public void register(Action action) {
        actions.put(action.getName(), action);
    }

    @Override
    public void unregister(Action action) {
        actions.remove(action.getName(),action);
    }

    @Override
    public void register(Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("You must add at least one action");
        }

        for (Action action : actions) {
            this.actions.put(action.getName(), action);
        }
    }

    @Override
    public List<Action> getRegistrationActions() {
        return new ArrayList<>(actions.values());
    }

    @Override
    public Optional<Action> getActionByName(String name) {
        return Optional.ofNullable(actions.get(name));
    }
}
