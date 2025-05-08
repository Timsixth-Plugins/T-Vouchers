package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.MainGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.factory.ProcessFactory;
import pl.timsixth.vouchers.gui.processes.VoucherCreationProcess;
import pl.timsixth.vouchers.model.Process;

public class CloseMenuAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public CloseMenuAction() {
        super("CLOSE_MENU");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();
        String args = getArgs().get(0);
        if (args.equalsIgnoreCase("start_creation_process")) {
            Process creationProcess = ProcessFactory.createCreationProcess(player.getUniqueId());
            vouchersPlugin.getCreateVoucherProcessManager().startProcess(creationProcess);

            player.closeInventory();
            player.sendMessage(vouchersPlugin.getMessages().getTypeVoucherName());

            MainGuiProcess mainGuiProcess = new VoucherCreationProcess(
                    vouchersPlugin,
                    vouchersPlugin.getMessages(),
                    vouchersPlugin.getCreateVoucherProcessManager(),
                    vouchersPlugin.getVoucherManager()
            );

            ProcessRunner.runProcess(player, mainGuiProcess);
        }

    }
}
