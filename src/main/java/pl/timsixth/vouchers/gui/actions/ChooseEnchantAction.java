package pl.timsixth.vouchers.gui.actions;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.Menu;
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

public class ChooseEnchantAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final EditVoucherProcessManager editVoucherProcessManager;
    private final CreateVoucherProcessManager createVoucherProcessManager;

    public ChooseEnchantAction() {
        super("CHOOSE_ENCHANT");
        this.editVoucherProcessManager = vouchersPlugin.getEditVoucherManager();
        this.createVoucherProcessManager = vouchersPlugin.getCreateVoucherProcessManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        if (createVoucherProcessManager.getProcessByUser(player.getUniqueId()).isPresent()) {
            chooseEnchant(player, menuItem, createVoucherProcessManager);
        } else if (editVoucherProcessManager.getProcessByUser(player.getUniqueId()).isPresent()) {
            chooseEnchant(player, menuItem, editVoucherProcessManager);
        }
        event.setCancelled(true);
    }

    private void chooseEnchant(Player player, MenuItem menuItem, ProcessManager processManager) {
        Optional<Process> processOptional = processManager.getProcessByUser(player.getUniqueId());

        if (!processOptional.isPresent()) return;
        Process process = processOptional.get();
        Voucher currentVoucher = process.getCurrentVoucher();

        currentVoucher.getEnchantments().put(Enchantment.getByName(ChatColor.stripColor(menuItem.getDisplayName())), 0);

        Optional<Menu> chooseEnchantLevelOptional = vouchersPlugin.getMenuManager().getMenuByName("chooseEnchantLevel");
        if (!chooseEnchantLevelOptional.isPresent()) return;

        Menu chooseLevelMenu = chooseEnchantLevelOptional.get();
        player.openInventory(vouchersPlugin.getMenuManager().createMenu(chooseLevelMenu));
    }
}
