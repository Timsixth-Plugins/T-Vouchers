package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.EditProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditVoucherProcessManager extends AbstractProcessManager<EditProcess>{
    public EditVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager) {
        super(configFile, voucherManager);
    }

    @Override
    public void saveProcess(EditProcess process) throws IOException {
        if (process.getCurrentVoucher() == null) {
            return;
        }
        ConfigurationSection vouchersSection = getYamlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) == null) {
            return;
        }
        vouchersSection.set(currentVoucher.getName(),null);

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
        List<Voucher> vouchers = getVoucherManager().getVoucherList();
        int index = 0;
        for (int i = 0; i < vouchers.size(); i++) {
            if (vouchers.get(i).equals(getVoucherManager().getVoucher(currentVoucher.getName()))){
                index = i;
            }
        }
        vouchers.set(index,currentVoucher);
        process.setContinue(false);
        cancelProcess(process);
    }
}
