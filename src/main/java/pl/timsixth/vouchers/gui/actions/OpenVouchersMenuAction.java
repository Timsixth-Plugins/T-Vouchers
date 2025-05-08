package pl.timsixth.vouchers.gui.actions;

import pl.timsixth.guilibrary.core.model.Generable;

import java.util.List;

public class OpenVouchersMenuAction extends OpenPaginatedMenuAction {
    public OpenVouchersMenuAction() {
        super("OPEN_VOUCHERS_MENU");
    }

    @Override
    protected List<? extends Generable> getData() {
        return vouchersPlugin.getVoucherManager().getVouchers();
    }

    @Override
    protected String getMenuName() {
        return "vouchersList";
    }
}
