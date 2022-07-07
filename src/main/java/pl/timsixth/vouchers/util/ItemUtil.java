package pl.timsixth.vouchers.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemUtil {

    public static Map<Enchantment, Integer> getEnchantments(List<String> enchants) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String enchant : enchants) {
            enchantments.put(Enchantment.getByName(enchant.split(";")[0]), Integer.parseInt(enchant.split(";")[1]));
        }
        return enchantments;
    }
    public static List<Enchantment> getAllEnchantments(){
        return Arrays.asList(Enchantment.values());
    }
}
