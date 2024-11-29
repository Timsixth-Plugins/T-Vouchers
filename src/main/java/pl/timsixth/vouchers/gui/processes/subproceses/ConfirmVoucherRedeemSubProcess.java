package pl.timsixth.vouchers.gui.processes.subproceses;

import org.bukkit.inventory.Inventory;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.processes.model.impl.AbstractSubGuiProcess;
import pl.timsixth.vouchers.manager.MenuManager;

public class ConfirmVoucherRedeemSubProcess extends AbstractSubGuiProcess {

    private final MenuManager menuManager;
    private final Menu menu;

    public ConfirmVoucherRedeemSubProcess(MenuManager menuManager) {
        super("CONFIRM_VOUCHER");
        this.menuManager = menuManager;
        this.menu = this.menuManager.getMenuByName("voucherConfirmation")
                .orElseThrow(() -> new IllegalStateException("Menu not found"));
    }

    @Override
    public Inventory getInventory() {
        return menuManager.createMenu(menu);
    }
}
