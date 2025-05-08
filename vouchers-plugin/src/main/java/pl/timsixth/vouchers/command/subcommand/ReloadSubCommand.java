package pl.timsixth.vouchers.command.subcommand;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import pl.timsixth.vouchers.command.api.SubCommand;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.VoucherManager;

import java.util.Arrays;

@RequiredArgsConstructor
public class ReloadSubCommand implements SubCommand {

    private final MenuManager menuManager;
    private final VoucherManager voucherManager;
    private final ConfigFile configFile;
    private final Messages messages;


    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            configFile.reloadFiles(Arrays.asList(menuManager, voucherManager));
            sender.sendMessage(messages.getFilesReloaded());
        }

        return false;
    }

    @Override
    public String getName() {
        return "reload";
    }
}
