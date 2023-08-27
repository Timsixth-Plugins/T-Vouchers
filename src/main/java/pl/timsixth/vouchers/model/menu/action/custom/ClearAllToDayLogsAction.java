package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;

import java.text.ParseException;

public class ClearAllToDayLogsAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    public ClearAllToDayLogsAction() {
        super("CLEAR_ALL_TODAY_LOGS", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        try {
            vouchersPlugin.getLogsManager().clearAllLogsByCurrentDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(vouchersPlugin.getMessages().getClearAllTodayLogs());
        event.setCancelled(true);
    }
}
