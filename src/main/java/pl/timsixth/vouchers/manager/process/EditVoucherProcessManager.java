package pl.timsixth.vouchers.manager.process;

import org.bukkit.configuration.ConfigurationSection;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.PrepareToProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Log;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.process.EditProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditVoucherProcessManager extends AbstractProcessManager<EditProcess> {

    private final PrepareToProcessManager prepareToProcessManager;

    public EditVoucherProcessManager(ConfigFile configFile, VoucherManager voucherManager, PrepareToProcessManager prepareToProcessManager, LogsManager logsManager) {
        super(configFile, voucherManager, logsManager);
        this.prepareToProcessManager = prepareToProcessManager;
    }

    @Override
    public void saveProcess(EditProcess process) throws IOException {
        if (process.getCurrentVoucher() == null) {
            return;
        }
        ConfigurationSection vouchersSection = getConfigFile().getYmlVouchers().getConfigurationSection("vouchers");
        Voucher currentVoucher = process.getCurrentVoucher();
        if (vouchersSection.getConfigurationSection(currentVoucher.getName()) == null) {
            return;
        }

        ConfigurationSection newVoucherSection = vouchersSection.getConfigurationSection(currentVoucher.getName());
        newVoucherSection.set("command", currentVoucher.getCommand());
        newVoucherSection.set("displayname", currentVoucher.getDisplayName());
        newVoucherSection.set("lore", currentVoucher.getLore());
        if (currentVoucher.getEnchantments() != null) {
            List<String> enchants = new ArrayList<>();
            currentVoucher.getEnchantments().forEach((enchantment, integer) -> enchants.add(enchantment.getName() + ";" + integer));

            newVoucherSection.set("enchants", enchants);
        } else {
            newVoucherSection.set("enchants", null);
        }
        getConfigFile().getYmlVouchers().save(getConfigFile().getVouchersFile());
        List<Voucher> vouchers = getVoucherManager().getVoucherList();
        prepareToProcessManager.removeLocalizedName(prepareToProcessManager.getPrepareToProcess(process.getUserUuid()));
        int index = 0;
        for (int i = 0; i < vouchers.size(); i++) {
            if (vouchers.get(i).getName().equalsIgnoreCase(getVoucherManager().getVoucher(currentVoucher.getName()).getName())) {
                index = i;
            }
        }

        vouchers.set(index, currentVoucher);
        process.setContinue(false);
        cancelProcess(process);
        getLogsManager().addLog(new Log(process.getUserUuid(),"Voucher of name "+ process.getCurrentVoucher().getName()+" has been updated",new Date(), ProcessType.EDIT));
    }
}
