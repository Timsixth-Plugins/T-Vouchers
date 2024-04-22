package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.model.Process;

import java.util.Optional;

public class CreateVoucherAction extends AbstractAction implements ClickAction {
    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final CreateVoucherProcessManager createVoucherProcessManager;

    public CreateVoucherAction() {
        super("CREATE_VOUCHER");
        this.createVoucherProcessManager = vouchersPlugin.getCreateVoucherProcessManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        Optional<Process> processOptional = createVoucherProcessManager.getProcessByUser(player.getUniqueId());
        if (!processOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }
        Process process = processOptional.get();

        createVoucherProcessManager.saveProcess(process);

        player.sendMessage(vouchersPlugin.getMessages().getCreatedVoucher());
        player.closeInventory();
        event.setCancelled(true);
    }
}
