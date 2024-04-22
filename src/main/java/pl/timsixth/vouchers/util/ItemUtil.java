package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ItemUtil {

    public static Map<Enchantment, Integer> getEnchantments(List<String> enchants) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String enchant : enchants) {
            enchantments.put(Enchantment.getByName(enchant.split(";")[0]), Integer.parseInt(enchant.split(";")[1]));
        }
        return enchantments;
    }

    public static List<String> getEnchantments(Map<Enchantment, Integer> enchants) {
        List<String> enchantmentsAsString = new ArrayList<>();

        enchants.forEach((enchantment, level) -> enchantmentsAsString.add(enchantment.getName() + ";" + level));

        return enchantmentsAsString;
    }
}
