package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.CreationProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateVoucherProcessManager extends AbstractProcessManager<CreationProcess> {


    public CreateVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
    }

    @Override
    public void saveProcess(CreationProcess process) throws IOException {
        if (process.getCurrentVoucher() == null) {
            return;
        }
        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) != null) {
            return;
        }
        ConfigurationSection newVoucherSection = vouchersSection.createSection(currentVoucher.getName());
        newVoucherSection.set("commands", currentVoucher.getCommands());
        newVoucherSection.set("displayname", currentVoucher.getDisplayName());
        newVoucherSection.set("material", currentVoucher.getMaterial().name());
        newVoucherSection.set("lore", currentVoucher.getLore());
        if (currentVoucher.getEnchantments() != null) {
            List<String> enchants = new ArrayList<>();
            currentVoucher.getEnchantments().forEach((enchantment, integer) -> enchants.add(enchantment.getName() + ";" + integer));

            newVoucherSection.set("enchants", enchants);
        }
        getConfigFile().getYmlVouchers().save(getConfigFile().getVouchersFile());
        getVoucherManager().getVoucherList().add(currentVoucher);
        process.setContinue(false);
        cancelProcess(process);
        getLogsManager().log(process, ProcessType.CREATE);
    }
}