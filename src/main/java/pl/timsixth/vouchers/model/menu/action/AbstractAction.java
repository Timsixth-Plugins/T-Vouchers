package pl.timsixth.vouchers.model.menu.action;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@RequiredArgsConstructor
public abstract class AbstractAction implements Action {

    private final String name;
    private List<String> args;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getArgs() {
        return args;
    }

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public boolean hasArgs() {
        if (args == null) args = new ArrayList<>();

        return !args.isEmpty();
    }
}
