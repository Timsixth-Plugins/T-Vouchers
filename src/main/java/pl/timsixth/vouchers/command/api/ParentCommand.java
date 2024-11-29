package pl.timsixth.vouchers.command.api;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.timsixth.vouchers.command.api.tabcompleter.BaseTabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents parent command.<br>
 * For example /(parent_command) (sub_command) (sub_command_args).
 */
public abstract class ParentCommand implements CommandExecutor {

    private List<SubCommand> subCommands;
    private final String permission;
    private final boolean hasArguments;
    private final boolean onlyPlayers;
    private final boolean usePermission;


    /**
     * All args constructor
     *
     * @param permission           command permission
     * @param hasArguments         true if command has sub commands otherwise false
     * @param onlyPlayers          true if command can be used by only players otherwise false
     * @param usePermission        true if command has permission otherwise false
     */
    public ParentCommand(String permission, boolean hasArguments, boolean onlyPlayers, boolean usePermission) {
        this.permission = permission;
        this.hasArguments = hasArguments;
        this.onlyPlayers = onlyPlayers;
        this.usePermission = usePermission;
        this.subCommands = new ArrayList<>();
    }

    /**
     * Constructor to command which doesn't have sub commands
     *
     * @param permission           command permission
     * @param onlyPlayers          true if command can be used by only players otherwise false
     * @param usePermission        true if command has permission otherwise false
     */
    public ParentCommand(String permission, boolean onlyPlayers, boolean usePermission) {
        this.permission = permission;
        this.onlyPlayers = onlyPlayers;
        this.usePermission = usePermission;
        this.hasArguments = false;
    }

    /**
     * Constructor without params, everything is set to default value like false, empty string and for configuration null
     */
    public ParentCommand() {
        this("", false, false, false);
    }

    /**
     * Method override from {@link CommandExecutor}
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (onlyPlayers) {
            if (!(sender instanceof Player)) {
                Bukkit.getLogger().info("");
                return true;
            }
        }

        if (usePermission) {
            if (!sender.hasPermission(permission)) {
                sender.sendMessage("");
                return true;
            }
        }

        if (hasArguments) {
            for (SubCommand subCommand : subCommands) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase(subCommand.getName())) {
                        return subCommand.executeCommand(sender, args);
                    }
                }
            }
        }

        return executeCommand(sender, args);
    }

    /**
     * Executes command
     *
     * @param commandSender Source of the command
     * @param args          Passed command arguments
     * @return true if a valid command, otherwise false
     */
    protected abstract boolean executeCommand(CommandSender commandSender, String[] args);

    /**
     * Gets sub commands
     *
     * @return list of sub commands
     * @throws IllegalStateException when subCommands is null
     */
    public List<SubCommand> getSubCommands() {
        if (subCommands == null)
            throw new IllegalStateException("Can not get sub commands from ParentCommand, because field hasArguments is false");

        return subCommands;
    }

    /**
     * Adds new sub command
     *
     * @param subCommand subcommand to add
     */
    public void addSubCommand(SubCommand subCommand) {
        if (subCommands == null) return;
        if (subCommands.contains(subCommand)) return;

        subCommands.add(subCommand);
    }

    /**
     * Gets command name, this is so important for addons system
     *
     * @return command name
     */
    public abstract String getName();

    /**
     * Gets tab completer for command
     *
     * @return tab completer
     */
    public BaseTabCompleter getTabCompleter() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
