package pl.timsixth.vouchers.manager.process;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.PrepareProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.DeleteProcess;

import java.io.IOException;

public class DeleteVoucherProcessManager extends AbstractProcessManager<DeleteProcess> {

    private final PrepareProcessManager prepareToProcessManager;

    private final VouchersPlugin vouchersPlugin;

    public DeleteVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, PrepareProcessManager prepareToProcessManager, VouchersPlugin vouchersPlugin, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
        this.prepareToProcessManager = prepareToProcessManager;
        this.vouchersPlugin = vouchersPlugin;
    }

    @Override
    public void saveProcess(DeleteProcess process) {
        if (process.getCurrentVoucher() == null) return;
        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        prepareToProcessManager.removeLocalizedName(prepareToProcessManager.getPrepareProcess(process.getUserUuid()));
        getVoucherManager().getVoucherList().remove(currentVoucher);
        new BukkitRunnable() {
            @Override
            public void run() {
                vouchersSection.set(currentVoucher.getName(), null);
                try {
                    getConfigFile().getYmlVouchers().save(getConfigFile().getVouchersFile());
                } catch (IOException e) {
                    Bukkit.getLogger().severe(e.getMessage());
                }
            }
        }.runTaskLater(vouchersPlugin, 2 * 20L);
        process.setContinue(false);
        cancelProcess(process);
        getLogsManager().log(process, ProcessType.DELETE);
    }
}
