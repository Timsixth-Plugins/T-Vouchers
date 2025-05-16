package pl.timsixth.vouchers.command.subcommand;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.timsixth.vouchers.command.api.SubCommand;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.NumberUtil;
import pl.timsixth.vouchers.util.ParseResult;

import java.util.Optional;

@RequiredArgsConstructor
public class GiveSubCommand implements SubCommand {

    private final VoucherManager voucherManager;
    private final Messages messages;

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 2) {
            if (isConsole(sender)) return true;
            Player player = (Player) sender;

            Optional<Voucher> optionalVoucher = getVoucher(sender, args);
            if (!optionalVoucher.isPresent()) return true;

            giveVoucher(player, optionalVoucher.get());
        } else if (args.length == 3) {

            Optional<Voucher> optionalVoucher = getVoucher(sender, args);
            if (!optionalVoucher.isPresent()) return true;

            Player other = Bukkit.getPlayer(args[2]);

            if (other == null) {
                Integer amount = getAmount(sender, args, 2);

                if (amount == null) return true;

                if (isConsole(sender)) return true;

                Player player = (Player) sender;

                giveVoucher(player, optionalVoucher.get());

                return true;
            }

            giveVoucher(other, optionalVoucher.get());

            sender.sendMessage(messages.getAddedVoucherToOtherPlayer().replace("{PLAYER_NAME}", other.getName()));
        } else if (args.length == 4) {

            Integer amount = getAmount(sender, args, 3);
            if (amount == null) return true;

            Optional<Voucher> optionalVoucher = getVoucher(sender, args);
            if (!optionalVoucher.isPresent()) return true;

            Player other = getPlayer(sender, args);

            if (other == null) return true;

            giveVoucher(other, optionalVoucher.get(), amount);

            sender.sendMessage(messages.getAddedVoucherToOtherPlayer().replace("{PLAYER_NAME}", other.getName()));
        } else {
            messages.getCommandsList().forEach(sender::sendMessage);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "give";
    }

    private boolean isConsole(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(this.messages.getOnlyPlayersCanUseThisCommand());
            return true;
        }
        return false;
    }

    private @Nullable Integer getAmount(CommandSender sender, String[] args, int index) {
        ParseResult<Integer> result = NumberUtil.isInt(args[index]);

        if (!result.isSuccessful()) {
            sender.sendMessage(messages.getNotNumber());
            return null;
        }

        return result.getValue();
    }

    private @Nullable Player getPlayer(CommandSender sender, String[] args) {
        Player other = Bukkit.getPlayer(args[2]);

        if (other == null) {
            sender.sendMessage(messages.getOfflinePlayer());
            return null;
        }
        return other;
    }

    private @NotNull Optional<Voucher> getVoucher(CommandSender sender, String[] args) {
        Optional<Voucher> optionalVoucher = voucherManager.getVoucher(args[1]);

        if (!optionalVoucher.isPresent()) {
            sender.sendMessage(messages.getVoucherDoesntExists());
            return Optional.empty();
        }
        return optionalVoucher;
    }

    private void giveVoucher(Player player, Voucher voucher, int amount) {
        voucher.give(player, amount);

        player.sendMessage(messages.getAddedVoucher());
    }

    private void giveVoucher(Player player, Voucher voucher) {
        giveVoucher(player, voucher, 1);
    }
}