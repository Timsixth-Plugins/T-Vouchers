package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.model.Voucher;

@UtilityClass
public class VoucherUtil {

    public static void redeemVoucher(Player player, Voucher voucher) {
        Messages messages = VouchersPlugin.getPlugin(VouchersPlugin.class).getMessages();

        replacePlaceholderInCommand(player, voucher);

        int amount = player.getInventory().getItemInMainHand().getAmount() - 1;
        player.getInventory().getItemInMainHand().setAmount(amount);

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null)
            player.sendMessage(messages.getUsedVoucher().replace("{VOUCHER_DISPLAY_NAME}", ChatUtil.hexColor(voucher.getDisplayName())));
        else
            player.sendMessage(PlaceholderAPI.setPlaceholders(player, messages.getUsedVoucher().replace("{VOUCHER_DISPLAY_NAME}", ChatUtil.hexColor(voucher.getDisplayName()))));
    }

    private static void replacePlaceholderInCommand(Player player, Voucher voucher) {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            voucher.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command.replace("{NICK}", player.getName()))));

            return;
        }
        voucher.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{NICK}", player.getName())));
    }
}
