package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.ProcessManager;
import pl.timsixth.vouchers.model.Process;

import java.util.HashMap;
import java.util.Optional;

public class NoneEnchantsAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final EditVoucherProcessManager editVoucherProcessManager;
    private final CreateVoucherProcessManager createVoucherProcessManager;

    public NoneEnchantsAction() {
        super("NONE_ENCHANTS");
        this.editVoucherProcessManager = vouchersPlugin.getEditVoucherManager();
        this.createVoucherProcessManager = vouchersPlugin.getCreateVoucherProcessManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (createVoucherProcessManager.getProcess(player.getUniqueId()).isPresent()) {
            setNoneEnchants(player, createVoucherProcessManager);
        } else if (editVoucherProcessManager.getProcess(player.getUniqueId()).isPresent()) {
            setNoneEnchants(player, editVoucherProcessManager);
        }

        event.setCancelled(true);
    }

    private void setNoneEnchants(Player player, ProcessManager processManager) {
        Optional<Process> processOptional = processManager.getProcess(player.getUniqueId());

        if (!processOptional.isPresent()) return;
        Process process = processOptional.get();

        process.getCurrentVoucher().setEnchantments(new HashMap<>());
        processManager.saveProcess(process);
        if (process.getType() == ProcessType.CREATE) {
            player.sendMessage(vouchersPlugin.getMessages().getCreatedVoucher());
        } else if (process.getType() == ProcessType.EDIT) {
            player.sendMessage(vouchersPlugin.getMessages().getUpdatedVoucher());
        }

        player.closeInventory();
    }
}
