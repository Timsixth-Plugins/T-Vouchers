package pl.timsixth.vouchers.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.guilibrary.core.util.UniversalItemMeta;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VoucherManager implements Reloadable{

    private final ConfigFile configFile;
    @Getter
    private final List<Voucher> vouchers = new ArrayList<>();

    public void loadVouchers() {
        ConfigurationSection vouchersSection = configFile.getYmlVouchers().getConfigurationSection("vouchers");

        for (String voucherName : vouchersSection.getKeys(false)) {
            ConfigurationSection section = vouchersSection.getConfigurationSection(voucherName);
            List<String> commands = new ArrayList<>();

            String command = section.getString("command");

            if (command != null) commands.add(command);

            if (!section.getStringList("commands").isEmpty())
                commands = section.getStringList("commands");

            Voucher voucher = new Voucher(
                    voucherName,
                    commands,
                    section.getStringList("lore"),
                    section.getString("displayname"),
                    Material.matchMaterial(section.getString("material"))
            );
            if (!section.getStringList("enchants").isEmpty()) {
                List<String> enchantsString = section.getStringList("enchants");
                voucher.setEnchantments(ItemUtil.getEnchantments(enchantsString));
            }

            if (section.getString("textures") != null) voucher.setTextures(section.getString("textures"));
            if (section.getString("permission") != null) voucher.setPermission(section.getString("permission"));
            if (section.getBoolean("discord_notification")) voucher.setDiscordNotification(true);

            List<String> itemFlagsAsStrings = section.getStringList("item_flags");
            if (!itemFlagsAsStrings.isEmpty()) {

                List<ItemFlag> itemFlags = itemFlagsAsStrings.stream()
                        .map(ItemFlag::valueOf)
                        .collect(Collectors.toList());

                voucher.setItemFlags(itemFlags);
            }

            vouchers.add(voucher);
        }
    }

    public Optional<Voucher> getVoucher(String name) {
        return vouchers.stream()
                .filter(voucher -> voucher.getName().equalsIgnoreCase(name))
                .findAny();
    }

    @Override
    public void reload() {
        vouchers.clear();
        loadVouchers();
    }

    public void addVoucher(Voucher voucher) {
        vouchers.add(voucher);
    }

    public void removeVoucher(Voucher voucher) {
        vouchers.remove(voucher);
    }

    public boolean isVoucher(ItemStack item) {
        for (Voucher voucher : vouchers) {
            if (!item.hasItemMeta()) return false;

            UniversalItemMeta itemMeta = new UniversalItemMeta(item.getItemMeta());

            if (!itemMeta.hasLocalizedName()) return false;
            if (itemMeta.getLocalizedName().equalsIgnoreCase(voucher.getName())) return true;
        }

        return false;
    }
}
