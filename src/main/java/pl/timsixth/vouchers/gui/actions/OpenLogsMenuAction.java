package pl.timsixth.vouchers.gui.actions;

import pl.timsixth.guilibrary.core.model.Generable;

import java.util.List;

public class OpenLogsMenuAction extends OpenPaginatedMenuAction {
    public OpenLogsMenuAction() {
        super("OPEN_LOGS_MENU");
    }

    @Override
    protected List<? extends Generable> getData() {
        return vouchersPlugin.getLogsManager().getLogs();
    }

    @Override
    protected String getMenuName() {
        return "logsList";
    }
}
