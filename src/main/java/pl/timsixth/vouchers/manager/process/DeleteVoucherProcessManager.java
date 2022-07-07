package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.DeleteProcess;

import java.io.IOException;

public class DeleteVoucherProcessManager extends AbstractProcessManager<DeleteProcess> {
    public DeleteVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager) {
        super(configFile, voucherManager);
    }

    @Override
    public void saveProcess(DeleteProcess process) throws IOException {
        if (process.getCurrentVoucher() == null) return;

        ConfigurationSection vouchersSection = getYamlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) == null) {
            return;
        }
        vouchersSection.set(currentVoucher.getName(), null);
        getYamlVouchers().save(getConfigFile().vouchersFile);
        getVoucherManager().getVoucherList().remove(currentVoucher);
        process.setContinue(false);
        cancelProcess(process);
    }
}
