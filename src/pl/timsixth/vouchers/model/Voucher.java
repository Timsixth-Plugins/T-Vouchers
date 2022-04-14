package pl.timsixth.vouchers.model;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public class Voucher {

    private final String name;
    private final String displayName;
    private final String command;
    private final List<String> lore;
    private Map<Enchantment,Integer> enchantments;

    public Voucher(String name, String command, List<String> lore, String displayName) {
        this.name = name;
        this.command = command;
        this.lore = lore;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getCommand() {
        return command;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }
}
