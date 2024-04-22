package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.PrepareProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.List;
import java.util.Optional;

public class EditVoucherProcessManager extends ProcessManager {

    private final PrepareProcessManager prepareToProcessManager;

    public EditVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, PrepareProcessManager prepareToProcessManager, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
        this.prepareToProcessManager = prepareToProcessManager;
    }

    @Override
    public void saveProcess(Process process) {
        if (process.getCurrentVoucher() == null) return;

        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");

        Voucher currentVoucher = process.getCurrentVoucher();

        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) == null) return;

        ConfigurationSection newVoucherSection = vouchersSection.getConfigurationSection(currentVoucher.getName());
        newVoucherSection.set("commands", currentVoucher.getCommands());
        newVoucherSection.set("displayname", currentVoucher.getDisplayName());
        newVoucherSection.set("lore", currentVoucher.getLore());
        newVoucherSection.set("material", currentVoucher.getMaterial().name());
        newVoucherSection.set("enchants", ItemUtil.getEnchantments(currentVoucher.getEnchantments()));
        saveVouchersFile();

        List<Voucher> vouchers = getVoucherManager().getVouchers();

        prepareToProcessManager.removeLocalizedName(prepareToProcessManager.getPrepareProcess(process.getUserUUID()));

        int index = 0;
        for (int i = 0; i < vouchers.size(); i++) {
            Optional<Voucher> optionalVoucher = getVoucherManager().getVoucher(currentVoucher.getName());
            if (!optionalVoucher.isPresent()) return;

            if (vouchers.get(i).getName().equalsIgnoreCase(optionalVoucher.get().getName())) {
                index = i;
            }
        }

        vouchers.set(index, currentVoucher);

        process.setProcessContinue(false);
        cancelProcess(process);

        getLogsManager().log(process);
    }
}
