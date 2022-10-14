package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.CreationProcess;
import pl.timsixth.vouchers.model.process.EditProcess;
import pl.timsixth.vouchers.model.process.IProcess;

import java.io.IOException;

public class NoneEnchantsAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    public NoneEnchantsAction() {
        super("NONE_ENCHANTS", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        if (vouchersPlugin.getCreateVoucherProcessManager().isProcessedByUser(vouchersPlugin.getCreateVoucherProcessManager().getProcessByUser(player.getUniqueId()), player)) {
            try {
                setNoneEnchants(player, vouchersPlugin.getCreateVoucherProcessManager());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (vouchersPlugin.getEditVoucherManager().isProcessedByUser(vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            try {
                setNoneEnchants(player, vouchersPlugin.getEditVoucherManager());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        event.setCancelled(true);
    }

    private <T extends IProcess> void setNoneEnchants(Player player, IProcessManager<T> iProcessManager) throws IOException {
        T process = iProcessManager.getProcessByUser(player.getUniqueId());
        process.getCurrentVoucher().setEnchantments(null);
        iProcessManager.saveProcess(process);
        if (process instanceof CreationProcess) {
            player.sendMessage(vouchersPlugin.getMessages().getCreatedVoucher());
        } else if (process instanceof EditProcess) {
            player.sendMessage(vouchersPlugin.getMessages().getUpdatedVoucher());
        }
        player.closeInventory();
    }
}
