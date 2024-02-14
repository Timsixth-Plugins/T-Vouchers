package pl.timsixth.vouchers.model.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.model.menu.action.Action;
import pl.timsixth.vouchers.util.ChatUtil;
import pl.timsixth.vouchers.util.ItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuItem {

    private final int slot;
    private Material material;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Action action;
    private int price;
    private String localizedName;
    private ItemStack itemStack;

    public MenuItem(int slot, Material material, String displayName, List<String> lore) {
        this.slot = slot;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public MenuItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public ItemStack toItemStack() {
        if (material == null) return itemStack;

        List<String> replaceLore = lore.stream().map(line -> line.replace("{PRICE}", String.valueOf(price)))
                .collect(Collectors.toList());

        return new ItemBuilder(new ItemStack(material, 1))
                .setLore(ChatUtil.hexColor(replaceLore))
                .setName(ChatUtil.hexColor(displayName))
                .addEnchantmentsByMeta(enchantments)
                .toItemStack();
    }
}
