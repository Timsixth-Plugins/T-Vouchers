package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;


public class NoneClickAction extends AbstractAction implements ClickAction {
    public NoneClickAction() {
        super("NONE", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        event.setCancelled(true);
    }
}
