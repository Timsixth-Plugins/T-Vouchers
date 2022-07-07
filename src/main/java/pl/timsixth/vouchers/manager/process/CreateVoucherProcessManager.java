package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.CreationProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateVoucherProcessManager extends AbstractProcessManager<CreationProcess> {


    public CreateVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager) {
        super(configFile, voucherManager);
    }

    @Override
    public void saveProcess(CreationProcess process) throws IOException {
        if (process.getCurrentVoucher() == null) {
            return;
        }
        ConfigurationSection vouchersSection = getYamlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) != null) {
            return;
        }
        ConfigurationSection newVoucherSection = vouchersSection.createSection(currentVoucher.getName());
        newVoucherSection.set("command", currentVoucher.getCommand());
        newVoucherSection.set("displayname", currentVoucher.getDisplayName());
        newVoucherSection.set("lore", currentVoucher.getLore());
        if (currentVoucher.getEnchantments() != null) {
            List<String> enchants = new ArrayList<>();
            currentVoucher.getEnchantments().forEach((enchantment, integer) -> enchants.add(enchantment.getName() + ";" + integer));

            newVoucherSection.set("enchants", enchants);
        }
        getYamlVouchers().save(getConfigFile().vouchersFile);
        getVoucherManager().getVoucherList().add(currentVoucher);
        process.setContinue(false);
        cancelProcess(process);
    }
}