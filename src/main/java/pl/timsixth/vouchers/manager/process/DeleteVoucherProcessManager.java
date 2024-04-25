package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;

public class DeleteVoucherProcessManager extends ProcessManager {

    private final VouchersPlugin vouchersPlugin;

    public DeleteVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, VouchersPlugin vouchersPlugin, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
        this.vouchersPlugin = vouchersPlugin;
    }

    @Override
    public void saveProcess(Process process) {
        if (process.getCurrentVoucher() == null) return;

        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");

        Voucher currentVoucher = process.getCurrentVoucher();

        getVoucherManager().removeVoucher(currentVoucher);

        new BukkitRunnable() {
            @Override
            public void run() {
                vouchersSection.set(currentVoucher.getName(), null);
                saveVouchersFile();
            }
        }.runTaskLater(vouchersPlugin, 2 * 20L);

        process.setProcessContinue(false);
        cancelProcess(process);

        getLogsManager().log(process);
    }
}
