package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.IProcess;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChooseEnchantAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ChooseEnchantAction() {
        super("CHOOSE_ENCHANT", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        if (vouchersPlugin.getCreateVoucherProcessManager().isProcessedByUser(vouchersPlugin.getCreateVoucherProcessManager().getProcessByUser(player.getUniqueId()), player)) {
            chooseEnchant(player, menuItem, vouchersPlugin.getCreateVoucherProcessManager());
        } else if (vouchersPlugin.getEditVoucherManager().isProcessedByUser(vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            chooseEnchant(player, menuItem, vouchersPlugin.getEditVoucherManager());
        }
        event.setCancelled(true);
    }

    private <T extends IProcess> void chooseEnchant(Player player, MenuItem menuItem, IProcessManager<T> iProcessManager) {
        T process = iProcessManager.getProcessByUser(player.getUniqueId());
        Voucher currentVoucher = process.getCurrentVoucher();
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if (currentVoucher.getEnchantments() == null) {
            enchantments.put(Enchantment.getByName(ChatColor.stripColor(menuItem.getDisplayName())), 0);
            currentVoucher.setEnchantments(enchantments);
        }
        currentVoucher.getEnchantments().put(Enchantment.getByName(ChatColor.stripColor(menuItem.getDisplayName())), 0);
        Optional<Menu> chooseEnchantLevelOptional = vouchersPlugin.getMenuManager().getMenuByName("chooseEnchantLevel");
        if (!chooseEnchantLevelOptional.isPresent()) return;

        Menu chooseLevelMenu = chooseEnchantLevelOptional.get();
        player.openInventory(vouchersPlugin.getMenuManager().createMenu(chooseLevelMenu));
    }
}
