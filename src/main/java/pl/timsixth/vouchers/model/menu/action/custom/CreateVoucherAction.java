package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.CreationProcess;

import java.io.IOException;

public class CreateVoucherAction extends AbstractAction implements ClickAction {
    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public CreateVoucherAction() {
        super("CREATE_VOUCHER");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (!vouchersPlugin.getCreateVoucherProcessManager().isProcessedByUser(vouchersPlugin.getCreateVoucherProcessManager().getProcessByUser(player.getUniqueId()), player)) {
            event.setCancelled(true);
            return;
        }
        CreationProcess creationProcess = vouchersPlugin.getCreateVoucherProcessManager().getProcessByUser(player.getUniqueId());
        try {
            vouchersPlugin.getCreateVoucherProcessManager().saveProcess(creationProcess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(vouchersPlugin.getMessages().getCreatedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
