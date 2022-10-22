package pl.timsixth.vouchers.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
public class VoucherManager implements Reloadable{

    private final ConfigFile configFile;
    private final List<Voucher> voucherList = new ArrayList<>();

    public void loadVouchers() {
        ConfigurationSection vouchers = configFile.getYmlVouchers().getConfigurationSection("vouchers");

        for (String voucherName : vouchers.getKeys(false)) {
            ConfigurationSection section = vouchers.getConfigurationSection(voucherName);
            Voucher voucher = new Voucher(
                    voucherName,
                    section.getString("command"),
                    section.getStringList("lore"),
                    section.getString("displayname")
            );
            if (section.getStringList("enchants") != null) {
                voucher.setEnchantments(getEnchants(section));
            }
            voucherList.add(voucher);
        }
    }

    public ItemStack getItemVoucher(Voucher voucher) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatUtil.chatColor(voucher.getDisplayName()));
        meta.setLore(ChatUtil.chatColor(voucher.getLore()));

        if (voucher.getEnchantments() != null) {
            voucher.getEnchantments().forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
        }
        meta.setLocalizedName(voucher.getName());
        item.setItemMeta(meta);

        return item;
    }

    public boolean voucherExists(Voucher voucher) {
        return voucherList.contains(voucher);
    }

    public Voucher getVoucher(String name) {
        return voucherList.stream().filter(voucher -> voucher.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    private Map<Enchantment, Integer> getEnchants(ConfigurationSection section) {
        List<String> enchantsString = section.getStringList("enchants");
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchantsString.forEach(enchantString -> {
            String[] enchantAndLevel = enchantString.split(";");
            enchants.put(Enchantment.getByName(enchantAndLevel[0]), Integer.parseInt(enchantAndLevel[1]));
        });
        return enchants;
    }

    @Override
    public void reload() {
        voucherList.clear();
        loadVouchers();
    }
}
