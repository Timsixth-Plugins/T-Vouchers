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
import pl.timsixth.vouchers.gui.processes.VoucherEditionProcess;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.PrepareProcessUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EditVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);
    private final EditVoucherProcessManager editVoucherProcessManager;

    public EditVoucherAction() {
        super("EDIT_VOUCHER");
        this.editVoucherProcessManager = vouchersPlugin.getEditVoucherManager();
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (editVoucherProcessManager.getProcess(player.getUniqueId()).isPresent()) {
            event.setCancelled(true);
            return;
        }

        String voucherName = PrepareProcessUtil.getVoucherLocalizeName(player);

        Optional<Voucher> optionalVoucher = vouchersPlugin.getVoucherManager().getVoucher(voucherName);
        if (!optionalVoucher.isPresent()) return;

        Voucher voucher = optionalVoucher.get();

        Process editProcess = ProcessFactory.createEditionProcess(player.getUniqueId());

        Voucher currentVoucher = new Voucher(voucher.getName(), new ArrayList<>(), new ArrayList<>(), "", null);
        currentVoucher.setEnchantments(new HashMap<>());
        editProcess.setCurrentVoucher(currentVoucher);

        player.sendMessage(vouchersPlugin.getMessages().getTypeVoucherDisplayName());
        vouchersPlugin.getEditVoucherManager().startProcess(editProcess);

        player.closeInventory();
        event.setCancelled(true);

        MainGuiProcess mainGuiProcess = new VoucherEditionProcess(
                vouchersPlugin,
                vouchersPlugin.getMessages(),
                vouchersPlugin.getEditVoucherManager()
        );

        ProcessRunner.runProcess(player, mainGuiProcess);
    }
}
