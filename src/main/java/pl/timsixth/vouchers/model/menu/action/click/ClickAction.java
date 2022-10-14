package pl.timsixth.vouchers.model.menu.action.click;

import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.Action;

public interface ClickAction extends Action {

    void handleClickEvent(InventoryClickEvent event, MenuItem menuItem);
}
