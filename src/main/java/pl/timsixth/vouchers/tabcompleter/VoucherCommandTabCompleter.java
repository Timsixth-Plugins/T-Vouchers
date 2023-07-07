package pl.timsixth.vouchers.tabcompleter;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VoucherCommandTabCompleter implements TabCompleter {

    private final VoucherManager voucherManager;

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("list", "give", "gui", "reload", "giveall"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "give":
                case "giveall":
                    completions.addAll(voucherManager.getVoucherList().stream()
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
    }
}
