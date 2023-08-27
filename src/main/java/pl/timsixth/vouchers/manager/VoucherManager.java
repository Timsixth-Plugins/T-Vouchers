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
            List<String> commands = new ArrayList<>();

            String command = section.getString("command");

            if (command != null) commands.add(command);

            if (section.getStringList("commands") != null) commands = section.getStringList("commands");

            Voucher voucher = new Voucher(
                    voucherName,
                    commands,
                    section.getStringList("lore"),
                    section.getString("displayname"),
                    Material.matchMaterial(section.getString("material"))
            );
            if (section.getStringList("enchants") != null) {
                List<String> enchantsString = section.getStringList("enchants");
                voucher.setEnchantments(ItemUtil.getEnchantments(enchantsString));
            }

            if (section.getString("textures") != null) voucher.setTextures(section.getString("textures"));

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
