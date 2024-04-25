package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.MainGuiProcess;
import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.gui.processes.VoucherPrepareProcess;

import java.util.Optional;

public class ManageVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ManageVoucherAction() {
        super("MANAGE_VOUCHER");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        String voucherName = menuItem.getLocalizedName();

        MainGuiProcess mainGuiProcess = new VoucherPrepareProcess(vouchersPlugin);
        SubGuiProcess subGuiProcess = mainGuiProcess.getSubGuiProcess("GIVE_VOUCHER_LOCALIZE_NAME");

        subGuiProcess.transformedData().put("voucherName", voucherName);

        ProcessRunner.runProcess(player, mainGuiProcess);

        Optional<Menu> menuOptional = vouchersPlugin.getMenuManager().getMenuByName("manageVouchers");
        if (!menuOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        Menu menu = menuOptional.get();
        player.openInventory(vouchersPlugin.getMenuManager().createMenu(menu));
        event.setCancelled(true);
    }
}
