package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.model.Process;

import java.util.Optional;

public class ReplaceVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final EditVoucherProcessManager editVoucherProcessManager;

    public ReplaceVoucherAction() {
        super("REPLACE_VOUCHER");
        this.editVoucherProcessManager = vouchersPlugin.getEditVoucherManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        Optional<Process> processOptional = editVoucherProcessManager.getProcessByUser(player.getUniqueId());
        if (!processOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        Process editProcess = processOptional.get();
        editVoucherProcessManager.saveProcess(editProcess);

        player.sendMessage(vouchersPlugin.getMessages().getUpdatedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
