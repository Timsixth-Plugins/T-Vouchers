package pl.timsixth.vouchers.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.AbstractAction;
import pl.timsixth.guilibrary.core.model.action.click.ClickAction;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.MainGuiProcess;
import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.VoucherUtil;

public class AcceptVoucherRedeemAction extends AbstractAction implements ClickAction {
    public AcceptVoucherRedeemAction() {
        super("ACCEPT_VOUCHER_REDEEM");
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        MainGuiProcess mainGuiProcess = ProcessRunner.getCurrentUserProcess(player);
        SubGuiProcess subGuiProcess = mainGuiProcess.getSubGuiProcess("CONFIRM_VOUCHER");

        Voucher voucher = (Voucher) subGuiProcess.getDatum("voucher");

        VoucherUtil.redeemVoucher(player, voucher);

        player.closeInventory();
    }

}
