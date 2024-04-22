package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.gui.actions.ChooseEnchantAction;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.process.ProcessManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;

import java.util.*;

@RequiredArgsConstructor
public class AsyncPlayerChatListener implements Listener {

    protected final ProcessManager processManager;
    private final VouchersPlugin vouchersPlugin;
    private final Messages messages;
    private final MenuManager menuManager;

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Optional<Process> processOptional = processManager.getProcessByUser(player.getUniqueId());

        if (!processOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        Process process = processOptional.get();
        Voucher voucher = process.getCurrentVoucher();

        if (voucher.getCommands().isEmpty()) {
            setCommand(event, player, voucher);
        } else if (voucher.getLore().isEmpty()) {
            setLore(event, player, voucher);
            openEnchantsMenu(player);
        }
    }

    private void setLore(AsyncPlayerChatEvent event, Player player, Voucher currentVoucher) {
        String[] lore = event.getMessage().split("\\|");
        currentVoucher.setLore(Arrays.asList(lore));
        event.setCancelled(true);
        player.sendMessage(messages.getSetVoucherEnchants());
    }

    private void setCommand(AsyncPlayerChatEvent event, Player player, Voucher currentVoucher) {
        String[] commands = event.getMessage().split("\\|");

        currentVoucher.setCommands(Arrays.asList(commands));
        event.setCancelled(true);
        player.sendMessage(messages.getTypeVoucherLore());
    }

    private void openEnchantsMenu(Player player) {
        Bukkit.getScheduler().runTask(vouchersPlugin, () -> {
            List<Enchantment> allEnchantments = Arrays.asList(Enchantment.values());

            Optional<Menu> menuOptional = menuManager.getMenuByName("listOfAllEnchants");
            if (!menuOptional.isPresent()) return;

            Menu enchantsMenu = menuOptional.get();
            Set<MenuItem> menuItems = enchantsMenu.getItems();

            for (int i = menuItems.size(); i < allEnchantments.size(); i++) {
                MenuItem menuItem = new MenuItem(i, Material.ENCHANTED_BOOK, ChatUtil.chatColor("&a" + allEnchantments.get(i).getName()), new ArrayList<>());
                menuItem.setAction(new ChooseEnchantAction());
                menuItem.setEnchantments(new HashMap<>());
                menuItems.add(menuItem);
            }

            enchantsMenu.setItems(menuItems);
            player.openInventory(menuManager.createMenu(enchantsMenu));
        });
    }
}
