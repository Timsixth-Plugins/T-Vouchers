package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class PlaceholderUtil {

    public static ItemStack replacePlaceholders(Player player, ItemStack itemStack) {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) return itemStack;

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasDisplayName())
            itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName()));
        if (itemMeta.hasLore()) itemMeta.setLore(PlaceholderAPI.setPlaceholders(player, itemMeta.getLore()));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
