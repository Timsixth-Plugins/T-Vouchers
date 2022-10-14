package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.util.ChatUtil;

@RequiredArgsConstructor
public class InventoryClickListener implements Listener {

    private final MenuManager menuManager;

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        for (Menu menu : menuManager.getMenus()) {
            if (event.getView().getTitle().equalsIgnoreCase(ChatUtil.chatColor(menu.getDisplayName()))) {
                for (MenuItem menuItem : menu.getItems()) {
                    if (event.getSlot() == menuItem.getSlot()) {
                        if (menuItem.getAction() instanceof ClickAction) {
                            ClickAction clickAction = (ClickAction) menuItem.getAction();
                            clickAction.handleClickEvent(event,menuItem);
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
