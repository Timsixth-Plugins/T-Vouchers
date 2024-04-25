package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.List;
import java.util.Optional;

public class EditVoucherProcessManager extends ProcessManager {


    public EditVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
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

        int index = getVoucherIndex(vouchers, currentVoucher);

        if (index == -1) return;

        vouchers.set(index, currentVoucher);

        process.setProcessContinue(false);
        cancelProcess(process);

        getLogsManager().log(process);
    }

    private int getVoucherIndex(List<Voucher> vouchers, Voucher currentVoucher) {
        int index = -1;

        for (int i = 0; i < vouchers.size(); i++) {
            Optional<Voucher> optionalVoucher = getVoucherManager().getVoucher(currentVoucher.getName());
            if (!optionalVoucher.isPresent()) return index;

            if (vouchers.get(i).equals(optionalVoucher.get())) {
                index = i;
            }
        }

        return index;
    }
}
