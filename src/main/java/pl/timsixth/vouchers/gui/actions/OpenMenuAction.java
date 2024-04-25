package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;

import java.util.Optional;

public class OpenMenuAction extends AbstractAction implements ClickAction {
    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public OpenMenuAction() {
        super("OPEN_MENU");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        Optional<Menu> menuOptional = vouchersPlugin.getMenuManager().getMenuByName(getArgs().get(0));
        if (!menuOptional.isPresent()) {
            player.sendMessage(ChatUtil.chatColor("&cMenu does not exists!"));
            event.setCancelled(true);
            return;
        }

        player.openInventory(vouchersPlugin.getMenuManager().createMenu(menuOptional.get()));

        event.setCancelled(true);
    }

}
