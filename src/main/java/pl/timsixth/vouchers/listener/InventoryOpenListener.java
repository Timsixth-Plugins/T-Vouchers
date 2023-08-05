package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.Optional;

import static pl.timsixth.vouchers.util.PlaceholderUtil.replacePlaceholders;

@RequiredArgsConstructor
public class InventoryOpenListener implements Listener {

    private final MenuManager menuManager;

    @EventHandler
    private void onOpen(InventoryOpenEvent event) {
        Optional<Menu> menuOptional = menuManager.getMenuByName("listOfAllVouchers");

        if (!menuOptional.isPresent()) return;

        Menu menu = menuOptional.get();

        if (!event.getView().getTitle().equalsIgnoreCase(ChatUtil.hexColor(menu.getDisplayName()))) return;

        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        for (int i = 0; i < inventory.getContents().length; i++) {
            ItemStack item = inventory.getItem(i);

            inventory.setItem(i, replacePlaceholders(player, item));
        }
    }
}
