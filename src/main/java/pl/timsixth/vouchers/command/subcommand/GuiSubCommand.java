package pl.timsixth.vouchers.command.subcommand;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.vouchers.command.api.SubCommand;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.MenuManager;

import java.util.Optional;
@RequiredArgsConstructor
public class GuiSubCommand implements SubCommand {

    private final MenuManager menuManager;
    private final Messages messages;

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                Bukkit.getLogger().info(this.messages.getOnlyPlayersCanUseThisCommand());
                return true;
            }

            Player player = (Player) sender;

            Optional<Menu> menuOptional = menuManager.getMenuByName("main");

            if (!menuOptional.isPresent()) return true;

            player.openInventory(menuManager.createMenu(menuOptional.get()));
        }

        return false;
    }

    @Override
    public String getName() {
        return "gui";
    }
}
