package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.DeleteProcess;

import java.io.IOException;

public class DeleteVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public DeleteVoucherAction() {
        super("DELETE_VOUCHER", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (vouchersPlugin.getDeleteVoucherManager().isProcessedByUser(vouchersPlugin.getDeleteVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            event.setCancelled(true);
            return;
        }
        DeleteProcess deleteProcess = new DeleteProcess(player.getUniqueId());

        String voucherName = vouchersPlugin.getPrepareToProcessManager().getPrepareProcess(player.getUniqueId()).getLocalizeName();

        Voucher currentVoucher = vouchersPlugin.getVoucherManager().getVoucher(voucherName)
                .orElse(null);

        deleteProcess.setCurrentVoucher(currentVoucher);
        vouchersPlugin.getDeleteVoucherManager().startProcess(deleteProcess);

        try {
            vouchersPlugin.getDeleteVoucherManager().saveProcess(deleteProcess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(vouchersPlugin.getMessages().getDeletedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
