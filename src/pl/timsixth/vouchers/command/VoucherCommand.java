package pl.timsixth.vouchers.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ChatUtil;

public class VoucherCommand implements CommandExecutor {

    private final VoucherManager voucherManager;

    public VoucherCommand(VoucherManager voucherManager) {
        this.voucherManager = voucherManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(ConfigFile.PERMISSION)) {
            sender.sendMessage(ConfigFile.NO_PERMISSION);
            return true;
        }


        if (args.length == 0) {
            sender.sendMessage(ConfigFile.CORRECT_USE);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatUtil.chatColor("&eVouchers:&7"));
                voucherManager.getVoucherList().forEach(voucher -> sender.sendMessage(ChatUtil.chatColor("&7 " + voucher.getName())));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                if (sender instanceof Player) {
                    if (voucherManager.voucherExists(voucherManager.getVoucher(args[1]))) {
                        Voucher voucher = voucherManager.getVoucher(args[1]);
                        ((Player) sender).getInventory().addItem(voucherManager.getItemVoucher(voucher));
                        sender.sendMessage(ConfigFile.ADDED_VOUCHER);
                    } else {
                        sender.sendMessage(ConfigFile.VOUCHER_DOESNT_EXISTS);
                    }
                } else {
                    sender.sendMessage(ConfigFile.CORRECT_USE);
                    return true;
                }
            }else{
                System.out.println("Player only use this command");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (voucherManager.voucherExists(voucherManager.getVoucher(args[1]))) {
                    Player other = Bukkit.getPlayer(args[2]);
                    if (other != null) {
                        Voucher voucher = voucherManager.getVoucher(args[1]);
                        other.getInventory().addItem(voucherManager.getItemVoucher(voucher));
                        other.sendMessage(ConfigFile.ADDED_VOUCHER);
                        String message = ConfigFile.ADDED_VOUCHER_TO_OTHER_PLAYER;
                        message = message.replace("{PLAYER_NAME}", sender.getName());
                        sender.sendMessage(message);
                    } else {
                        sender.sendMessage(ConfigFile.OFFLINE_PLAYER);
                    }
                } else {
                    sender.sendMessage(ConfigFile.VOUCHER_DOESNT_EXISTS);
                }
            } else {
                sender.sendMessage(ConfigFile.CORRECT_USE);
                return true;
            }
        } else {
            sender.sendMessage(ConfigFile.CORRECT_USE);
            return true;
        }
        return false;
    }
}
