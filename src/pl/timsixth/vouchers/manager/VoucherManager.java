package pl.timsixth.vouchers.manager;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class VoucherManager {

    private final YamlConfiguration yamlVouchers;

    private final List<Voucher> voucherList = new ArrayList<>();

    public VoucherManager(ConfigFile configFile){
        yamlVouchers = YamlConfiguration.loadConfiguration(configFile.vouchersFile);
    }

    public void loadVouchers() {
        ConfigurationSection vouchers = yamlVouchers.getConfigurationSection("vouchers");

        for (String voucherName : vouchers.getKeys(false)) {
            ConfigurationSection section = vouchers.getConfigurationSection(voucherName);
            Voucher voucher = new Voucher(
                    voucherName,
                    section.getString("command"),
                    section.getStringList("lore"),
                    section.getString("displayname")
            );
            voucherList.add(voucher);
        }
    }

    public ItemStack getItemVoucher(Voucher voucher) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatUtil.chatColor(voucher.getDisplayName()));
        meta.setLore(ChatUtil.chatColor(voucher.getLore()));
        meta.addEnchant(Enchantment.getByName(ConfigFile.ENCHANT_NAME), ConfigFile.LEVEL, true);
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
}
