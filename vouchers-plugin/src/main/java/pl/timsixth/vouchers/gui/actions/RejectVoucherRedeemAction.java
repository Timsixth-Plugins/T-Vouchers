package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.MainGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;

public class RejectVoucherRedeemAction extends AbstractAction implements ClickAction {

    private final Messages messages;

    public RejectVoucherRedeemAction() {
        super("REJECT_VOUCHER_REDEEM");

        messages = VouchersPlugin.getPlugin(VouchersPlugin.class).getMessages();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        MainGuiProcess mainGuiProcess = ProcessRunner.getCurrentUserProcess(player);
        mainGuiProcess.getGuiProcesses().forEach(subGuiProcess -> subGuiProcess.setEnded(true));

        mainGuiProcess.setEnded(true);

        player.closeInventory();

        player.sendMessage(messages.getVoucherRedeemRejected());
    }
}
