package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.timsixth.guilibrary.core.util.UniversalItemMeta;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.config.Settings;
import pl.timsixth.vouchers.gui.processes.VoucherConfirmationProcess;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.VoucherUtil;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final VoucherManager voucherManager;
    private final MenuManager menuManager;
    private final Messages messages;
    private final Settings settings;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();

        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;

        UniversalItemMeta universalItemMeta = new UniversalItemMeta(player.getInventory().getItemInMainHand().getItemMeta());
        if (!universalItemMeta.hasLocalizedName()) return;

        Optional<Voucher> optionalVoucher = voucherManager.getVoucher(universalItemMeta.getLocalizedName());

        if (!optionalVoucher.isPresent()) return;

        Voucher voucher = optionalVoucher.get();

        if (player.getInventory().getItemInMainHand().getType() != voucher.getMaterial()) {
            if (player.getInventory().getItemInMainHand().getType() != Material.PLAYER_HEAD) return;
        }

        if (voucher.hasPermission()) {
            if (!player.hasPermission(voucher.getPermission())) {
                player.sendMessage(messages.getNoPermission());
                event.setCancelled(true);
                return;
            }
        }

        if (settings.isUseConfirmationMenu()) {
            VoucherConfirmationProcess process = new VoucherConfirmationProcess(menuManager);
            ProcessRunner.runProcess(player, process);

            Optional<SubGuiProcess> subGuiProcessOptional = process.currentProcess();

            if (!subGuiProcessOptional.isPresent()) return;

            SubGuiProcess subGuiProcess = subGuiProcessOptional.get();

            subGuiProcess.transformedData().put("voucher", voucher);

            return;
        }

        VoucherUtil.redeemVoucher(player, voucher);
    }
}
