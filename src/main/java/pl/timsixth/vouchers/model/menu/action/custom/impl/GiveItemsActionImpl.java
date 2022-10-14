package pl.timsixth.vouchers.model.menu.action.custom.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.custom.GiveItemsAction;

import java.util.List;

public class GiveItemsActionImpl extends AbstractAction implements GiveItemsAction {

    private List<ItemStack> items;

    public GiveItemsActionImpl() {
        super("GIVE_ITEMS", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {

        addItems((Player) event.getWhoClicked());
        event.setCancelled(true);
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    private void addItems(Player player) {
        for (ItemStack item : getItems()) {
            player.getInventory().addItem(item);
        }
    }
}
