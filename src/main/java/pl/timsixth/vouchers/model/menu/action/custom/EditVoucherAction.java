package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.AbstractAction;
import pl.timsixth.vouchers.model.menu.action.ActionType;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.process.EditProcess;

import java.util.HashMap;
import java.util.Optional;

public class EditVoucherAction extends AbstractAction implements ClickAction {

    private final VouchersPlugin vouchersPlugin = VouchersPlugin.getPlugin(VouchersPlugin.class);

    public EditVoucherAction() {
        super("EDIT_VOUCHER", ActionType.CLICK);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event, MenuItem menuItem) {
        Player player = (Player) event.getWhoClicked();

        if (vouchersPlugin.getEditVoucherManager().isProcessedByUser(vouchersPlugin.getEditVoucherManager().getProcessByUser(player.getUniqueId()), player)) {
            event.setCancelled(true);
            return;
        }
        String actionArgs = getArgs().get(0);
        if (actionArgs.equalsIgnoreCase("open_chat")) {

            String voucherName = vouchersPlugin.getPrepareToProcessManager().getPrepareProcess(player.getUniqueId()).getLocalizeName();

            Optional<Voucher> optionalVoucher = vouchersPlugin.getVoucherManager().getVoucher(voucherName);
            if (!optionalVoucher.isPresent()) return;

            Voucher voucher = optionalVoucher.get();

            EditProcess editProcess = new EditProcess(player.getUniqueId());
            Voucher currentVoucher = new Voucher(voucher.getName(), null, null, null,null);
            currentVoucher.setEnchantments(new HashMap<>());
            editProcess.setCurrentVoucher(currentVoucher);
            player.sendMessage(vouchersPlugin.getMessages().getTypeVoucherDisplayName());
            vouchersPlugin.getEditVoucherManager().startProcess(editProcess);
            player.closeInventory();
        }
        event.setCancelled(true);
    }
}
