package pl.timsixth.vouchers.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import pl.timsixth.vouchers.command.api.ParentCommand;
import pl.timsixth.vouchers.command.api.tabcompleter.BaseTabCompleter;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoucherCommandTabCompleter extends BaseTabCompleter {

    public VoucherCommandTabCompleter(ParentCommand parentCommand, VoucherManager voucherManager) {
        super(parentCommand);

        addConditions((sender, args) -> {
            List<String> completions = new ArrayList<>();

            if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "give":
                    case "giveall":
                        completions.addAll(voucherManager.getVouchers().stream()
                                .map(Voucher::getName)
                                .collect(Collectors.toList()));
                        break;
                    default:
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                completions.addAll(Bukkit.getServer().getOnlinePlayers()
                        .stream()
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList()));
            }

            return completions;
        });
    }
}
