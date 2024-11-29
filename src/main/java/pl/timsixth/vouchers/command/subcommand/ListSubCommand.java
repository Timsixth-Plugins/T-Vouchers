package pl.timsixth.vouchers.command.subcommand;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.command.api.SubCommand;
import pl.timsixth.vouchers.manager.VoucherManager;

@RequiredArgsConstructor
public class ListSubCommand implements SubCommand {

    private final VoucherManager voucherManager;

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(ChatUtil.chatColor("&eVouchers:&7"));
            voucherManager.getVouchers().forEach(voucher -> sender.sendMessage(ChatUtil.chatColor("&7 " + voucher.getName())));
        }

        return false;
    }

    @Override
    public String getName() {
        return "list";
    }
}
