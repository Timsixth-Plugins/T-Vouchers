package pl.timsixth.vouchers.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;

public class PlayerInteractListener implements Listener {

    private final VoucherManager voucherManager;

    public PlayerInteractListener(VoucherManager voucherManager) {
        this.voucherManager = voucherManager;
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();

        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;

        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;

        Voucher voucher = voucherManager.getVoucher(player.getInventory().getItemInMainHand().getItemMeta().getLocalizedName());

        if (player.getInventory().getItemInMainHand().getType() != voucher.getMaterial()) return;

        String command = voucher.getCommand();
        command = command.replace("{NICK}", player.getName());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        int amount = player.getInventory().getItemInMainHand().getAmount() - 1;
        player.getInventory().getItemInMainHand().setAmount(amount);
    }
}
