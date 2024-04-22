package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.vouchers.VouchersPlugin;

import java.text.ParseException;

public class ClearAllToDayLogsAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public ClearAllToDayLogsAction() {
        super("CLEAR_ALL_TODAY_LOGS");
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
