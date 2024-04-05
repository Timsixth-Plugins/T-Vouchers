package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.CreationProcess;

public class CloseMenuAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public CloseMenuAction() {
        super("CLOSE_MENU");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        String args = getArgs().get(0);
        if (args.equalsIgnoreCase("open_chat")) {
            CreationProcess creationProcess = new CreationProcess(player.getUniqueId());
            vouchersPlugin.getCreateVoucherProcessManager().startProcess(creationProcess);
            player.sendMessage(vouchersPlugin.getMessages().getTypeVoucherName());
        }
        player.closeInventory();
    }
}
