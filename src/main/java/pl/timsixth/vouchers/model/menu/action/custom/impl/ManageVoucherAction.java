package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.PrepareProcess;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;

import java.util.Optional;

public class ManageVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ManageVoucherAction() {
        super("MANAGE_VOUCHER", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        String voucherName = menuItem.getLocalizedName();
        PrepareProcess prepareToProcess = new PrepareProcess(player.getUniqueId(), voucherName);
        vouchersPlugin.getPrepareToProcessManager().addNewLocalizedName(prepareToProcess);
        Optional<Menu> menuOptional = vouchersPlugin.getMenuManager().getMenuByName("manageVouchers");
        if (!menuOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }
        Menu menu = menuOptional.get();
        player.openInventory(vouchersPlugin.getMenuManager().createMenu(menu));
        event.setCancelled(true);
    }
}
