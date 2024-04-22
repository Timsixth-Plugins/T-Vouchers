package pl.timsixth.vouchers.gui.actions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.ProcessManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ChooseLevelAction extends AbstractAction implements ClickAction {

    private final EditVoucherProcessManager editVoucherProcessManager;
    private final CreateVoucherProcessManager createVoucherProcessManager;

    public ChooseLevelAction() {
        super("CHOOSE_LEVEL");
        VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
        this.editVoucherProcessManager = vouchersPlugin.getEditVoucherManager();
        this.createVoucherProcessManager = vouchersPlugin.getCreateVoucherProcessManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (createVoucherProcessManager.getProcessByUser(player.getUniqueId()).isPresent()) {
            chooseLevel(player, createVoucherProcessManager);
        } else if (editVoucherProcessManager.getProcessByUser(player.getUniqueId()).isPresent()) {
            chooseLevel(player, editVoucherProcessManager);
        }

        event.setCancelled(true);
    }

    private void chooseLevel(Player player, ProcessManager processManager) {
        Optional<Process> processOptional = processManager.getProcessByUser(player.getUniqueId());

        if (!processOptional.isPresent()) return;

        Process process = processOptional.get();
        Voucher currentVoucher = process.getCurrentVoucher();
        String actionArgs = getArgs().get(0);

        AtomicReference<Enchantment> enchantment = new AtomicReference<>();
        currentVoucher.getEnchantments().forEach((enchantment1, integer) -> {
            if (integer == 0) {
                enchantment.set(enchantment1);
            }
        });

        currentVoucher.getEnchantments().replace(enchantment.get(), 0, Integer.parseInt(actionArgs));
    }
}
