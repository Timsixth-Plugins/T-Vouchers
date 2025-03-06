package pl.timsixth.vouchers.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.timsixth.vouchers.command.api.ParentCommand;
import pl.timsixth.vouchers.command.api.tabcompleter.BaseTabCompleter;
import pl.timsixth.vouchers.command.subcommand.*;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.config.Settings;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.tabcompleter.VoucherCommandTabCompleter;

public class VoucherCommand extends ParentCommand implements CommandExecutor {

    private final Messages messages;
    private final VoucherManager voucherManager;

    public VoucherCommand(VoucherManager voucherManager, MenuManager menuManager, ConfigFile configFile, Messages messages, Settings settings) {
        super(settings.getPermission(), true, false, true, messages);
        this.messages = messages;
        this.voucherManager = voucherManager;

        addSubCommand(new ListSubCommand(voucherManager));
        addSubCommand(new GuiSubCommand(menuManager, messages));
        addSubCommand(new ReloadSubCommand(menuManager, voucherManager, configFile, messages));
        addSubCommand(new GiveSubCommand(voucherManager, messages));
        addSubCommand(new GiveAllSubCommand(voucherManager, messages));
    }

    @Override
    protected boolean executeCommand(CommandSender sender, String[] args) {
        messages.getCommandsList().forEach(sender::sendMessage);

        return false;
    }

    @Override
    public String getName() {
        return "voucher";
    }

    @Override
    public BaseTabCompleter getTabCompleter() {
        return new VoucherCommandTabCompleter(this, voucherManager);
    }
}
