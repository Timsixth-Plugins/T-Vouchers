package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ItemUtil;

public class CreateVoucherProcessManager extends ProcessManager {

    public CreateVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
    }

    @Override
    public void saveProcess(Process process) {
        if (process.getCurrentVoucher() == null) return;

        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();

        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) != null) return;

        ConfigurationSection newVoucherSection = vouchersSection.createSection(currentVoucher.getName());
        newVoucherSection.set("commands", currentVoucher.getCommands());
        newVoucherSection.set("displayname", currentVoucher.getDisplayName());
        newVoucherSection.set("material", currentVoucher.getMaterial().name());
        newVoucherSection.set("lore", currentVoucher.getLore());
        newVoucherSection.set("enchants", ItemUtil.getEnchantments(currentVoucher.getEnchantments()));

        saveVouchersFile();
        getVoucherManager().addVoucher(currentVoucher);

        process.setProcessContinue(false);
        cancelProcess(process);

        getLogsManager().log(process);
    }
}