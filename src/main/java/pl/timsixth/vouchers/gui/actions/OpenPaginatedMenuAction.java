package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.Generable;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.core.model.pagination.PaginatedMenu;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.MenuManager;

import java.util.List;
import java.util.Optional;

public abstract class OpenPaginatedMenuAction extends AbstractAction implements ClickAction {

    protected final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final MenuManager menuManager;

    public OpenPaginatedMenuAction(String name) {
        super(name);
        menuManager = vouchersPlugin.getMenuManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Optional<PaginatedMenu> paginatedMenuOptional = menuManager.getPaginatedMenu(getMenuName());

        if (!paginatedMenuOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();

        PaginatedMenu menu = paginatedMenuOptional.get();
        menu.getPages().clear();
        menu.setData(getData());

        menuManager.createPaginatedMenu(menu).ifPresent(player::openInventory);
    }

    protected abstract List<? extends Generable> getData();

    protected abstract String getMenuName();
}
