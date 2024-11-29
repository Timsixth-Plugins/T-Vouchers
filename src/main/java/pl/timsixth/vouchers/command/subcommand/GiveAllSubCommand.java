package pl.timsixth.vouchers.command.subcommand;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import pl.timsixth.vouchers.command.api.SubCommand;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.NumberUtil;

import java.util.Optional;

@RequiredArgsConstructor
public class GiveAllSubCommand implements SubCommand {

    private final VoucherManager voucherManager;
    private final Messages messages;

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return giveVoucher(sender, args[1], 1);
        } else if (args.length == 3) {

            NumberUtil.ParseResult<Integer> result = NumberUtil.isInt(args[2]);

            if (!result.isSuccessful()) {
                sender.sendMessage(messages.getNotNumber());
                return true;
            }


            return giveVoucher(sender, args[1], result.getNumber());
        }

        return false;
    }

    private boolean giveVoucher(CommandSender sender, String voucherName, int amount) {
        Optional<Voucher> optionalVoucher = voucherManager.getVoucher(voucherName);

        if (!optionalVoucher.isPresent()) {
            sender.sendMessage(messages.getVoucherDoesntExists());
            return true;
        }

        Voucher voucher = optionalVoucher.get();

        Bukkit.getOnlinePlayers().forEach(player -> voucher.give(player, amount));

        sender.sendMessage(messages.getAddedVoucherEveryone());

        return false;
    }

    @Override
    public String getName() {
        return "giveall";
    }
}
