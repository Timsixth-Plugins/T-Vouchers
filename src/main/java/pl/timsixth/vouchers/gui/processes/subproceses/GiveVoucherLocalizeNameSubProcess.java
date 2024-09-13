package pl.timsixth.vouchers.gui.processes.subproceses;

import org.bukkit.inventory.Inventory;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.processes.model.impl.AbstractSubGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.MenuManager;

public class GiveVoucherLocalizeNameSubProcess extends AbstractSubGuiProcess {

    private final MenuManager menuManager;
    private final Menu menu;

    public GiveVoucherLocalizeNameSubProcess(VouchersPlugin vouchersPlugin) {
        super("GIVE_VOUCHER_LOCALIZE_NAME");
        this.menuManager = vouchersPlugin.getMenuManager();
        this.menu = this.menuManager.getMenuByName("manageVouchers")
                .orElseThrow(() -> new IllegalStateException("Menu not found"));
    }

    @Override
    public Inventory getInventory() {
        return menuManager.createMenu(menu);
    }
}
