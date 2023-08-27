package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.EditProcess;

import java.io.IOException;

public class ReplaceVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ReplaceVoucherAction() {
        super("REPLACE_VOUCHER", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        if (!vouchersPlugin.getEditVoucherManager().isProcessedByUser(vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            event.setCancelled(true);
            return;
        }
        EditProcess editProcess = vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId());
        try {
            vouchersPlugin.getEditVoucherManager().saveProcess(editProcess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(vouchersPlugin.getMessages().getUpdatedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
