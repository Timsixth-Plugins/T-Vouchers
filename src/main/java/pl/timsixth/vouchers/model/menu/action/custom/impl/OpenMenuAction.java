package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.Optional;

public class OpenMenuAction extends AbstractAction implements ClickAction {
    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public OpenMenuAction() {
        super("OPEN_MENU", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        Optional<Menu> menuOptional = vouchersPlugin.getMenuManager().getMenuByName(getArgs().get(0));
        if (!menuOptional.isPresent()){
            player.sendMessage(ChatUtil.chatColor("&cMenu does not exists!"));
            event.setCancelled(true);
            return;
        }

        player.openInventory(vouchersPlugin.getMenuManager().createMenu(menuOptional.get()));

        event.setCancelled(true);
    }

}
