package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.IProcess;

import java.util.concurrent.atomic.AtomicReference;

public class ChooseLevelAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ChooseLevelAction() {
        super("CHOOSE_LEVEL", ActionType.CLICK);

    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        if (vouchersPlugin.getCreateVoucherProcessManager().isProcessedByUser(vouchersPlugin.getCreateVoucherProcessManager().getProcessByUser(player.getUniqueId()), player)) {
            chooseLevel(player, vouchersPlugin.getCreateVoucherProcessManager());
        } else if (vouchersPlugin.getEditVoucherManager().isProcessedByUser(vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            chooseLevel(player,vouchersPlugin.getEditVoucherManager());
        }
        event.setCancelled(true);
    }
        private <T extends IProcess> void chooseLevel(Player player,IProcessManager<T> iProcessManager) {
        T process = iProcessManager.getProcessByUser(player.getUniqueId());
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
