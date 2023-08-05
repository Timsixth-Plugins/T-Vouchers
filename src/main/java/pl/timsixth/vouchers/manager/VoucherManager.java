package pl.timsixth.vouchers.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VoucherManager implements Reloadable {

    private final ConfigFile configFile;
    @Getter
    private final List<Voucher> voucherList = new ArrayList<>();

    public void loadVouchers() {
        ConfigurationSection vouchers = configFile.getYmlVouchers().getConfigurationSection("vouchers");

        for (String voucherName : vouchers.getKeys(false)) {
            ConfigurationSection section = vouchers.getConfigurationSection(voucherName);
            Voucher voucher = new Voucher(
                    voucherName,
                    section.getString("command"),
                    section.getStringList("lore"),
                    section.getString("displayname"),
                    Material.matchMaterial(section.getString("material"))
            );
            if (section.getStringList("enchants") != null) {
                List<String> enchantsString = section.getStringList("enchants");
                voucher.setEnchantments(ItemUtil.getEnchantments(enchantsString));
            }
            voucherList.add(voucher);
        }
    }

    public Optional<Voucher> getVoucher(String name) {
        return voucherList.stream()
                .filter(voucher -> voucher.getName().equalsIgnoreCase(name))
                .findAny();
    }

    @Override
    public void reload() {
        voucherList.clear();
        loadVouchers();
    }
}
