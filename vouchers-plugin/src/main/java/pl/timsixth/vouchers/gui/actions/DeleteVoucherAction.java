package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.factory.ProcessFactory;
import pl.timsixth.vouchers.manager.process.DeleteVoucherProcessManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.PrepareProcessUtil;

import java.util.Optional;

public class DeleteVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final DeleteVoucherProcessManager deleteVoucherProcessManager;

    public DeleteVoucherAction() {
        super("DELETE_VOUCHER");
        this.deleteVoucherProcessManager = vouchersPlugin.getDeleteVoucherManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (deleteVoucherProcessManager.getProcess(player.getUniqueId()).isPresent()) {
            event.setCancelled(true);
            return;
        }

        Process deleteProcess = ProcessFactory.createDelationProcess(player.getUniqueId());

        String voucherName = PrepareProcessUtil.getVoucherLocalizeName(player);

        Optional<Voucher> voucherOptional = vouchersPlugin.getVoucherManager().getVoucher(voucherName);

        if (!voucherOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        deleteProcess.setCurrentVoucher(voucherOptional.get());

        deleteVoucherProcessManager.startProcess(deleteProcess);

        deleteVoucherProcessManager.saveProcess(deleteProcess);

        player.sendMessage(vouchersPlugin.getMessages().getDeletedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
