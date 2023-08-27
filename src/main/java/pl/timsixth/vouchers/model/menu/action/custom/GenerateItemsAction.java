package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.enums.GeneratedItemsType;
import pl.timsixth.vouchers.model.IGenerable;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GenerateItemsAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public GenerateItemsAction() {
        super("OPEN_MENU_AND_GENERATED_ITEMS", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        Optional<Menu> menuOptional = vouchersPlugin.getMenuManager().getMenuByName(getArgs().get(0));
        if (!menuOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }
        Menu menuToOpen = menuOptional.get();
        GeneratedItemsType typeOfGeneratedItems = GeneratedItemsType.valueOf(getArgs().get(1));
        if (typeOfGeneratedItems == GeneratedItemsType.VOUCHERS) {
            List<Voucher> voucherList = vouchersPlugin.getVoucherManager().getVoucherList();
            openGeneratedMenu(player, menuToOpen, voucherList);
        } else if (typeOfGeneratedItems == GeneratedItemsType.LOGS) {
            openGeneratedMenu(player, menuToOpen, vouchersPlugin.getLogsManager().getLogsToShowInGui());
        }
    }

    private <T extends IGenerable> void openGeneratedMenu(Player player, Menu menuToOpen, List<T> listOfItems) {
        Set<MenuItem> menuItems = menuToOpen.getItems();
        menuItems.clear();
        for (int i = 0; i < listOfItems.size(); i++) {
            menuItems.add(listOfItems.get(i).getGeneratedItem(i));
        }
        menuToOpen.setItems(menuItems);
        player.openInventory(vouchersPlugin.getMenuManager().createMenu(menuToOpen));
    }
}
