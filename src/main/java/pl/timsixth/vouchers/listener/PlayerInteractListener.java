package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final VoucherManager voucherManager;
    private final Messages messages;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();

        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;

        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;

        Optional<Voucher> optionalVoucher = voucherManager.getVoucher(player.getInventory().getItemInMainHand().getItemMeta().getLocalizedName());

        if (!optionalVoucher.isPresent()) return;

        Voucher voucher = optionalVoucher.get();


        if (player.getInventory().getItemInMainHand().getType() != voucher.getMaterial()) {
            if (player.getInventory().getItemInMainHand().getType() != Material.PLAYER_HEAD) return;
        }

        replacePlaceholderInCommand(player, voucher);

        int amount = player.getInventory().getItemInMainHand().getAmount() - 1;
        player.getInventory().getItemInMainHand().setAmount(amount);

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null)
            player.sendMessage(messages.getUsedVoucher().replace("{VOUCHER_DISPLAY_NAME}", ChatUtil.hexColor(voucher.getDisplayName())));
        else
            player.sendMessage(PlaceholderAPI.setPlaceholders(player, messages.getUsedVoucher().replace("{VOUCHER_DISPLAY_NAME}", ChatUtil.hexColor(voucher.getDisplayName()))));
    }

    private void replacePlaceholderInCommand(Player player, Voucher voucher) {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            voucher.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command.replace("{NICK}", player.getName()))));

            return;
        }
        voucher.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{NICK}", player.getName())));
    }
}
