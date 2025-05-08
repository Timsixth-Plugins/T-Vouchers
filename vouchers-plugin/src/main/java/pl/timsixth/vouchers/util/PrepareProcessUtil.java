package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.MainGuiProcess;

@UtilityClass
public class PrepareProcessUtil {

    public static String getVoucherLocalizeName(Player player) {
        MainGuiProcess currentUserProcess = ProcessRunner.getCurrentUserProcess(player);

        String voucherName = (String) currentUserProcess.getSubGuiProcess("GIVE_VOUCHER_LOCALIZE_NAME").getDatum("voucherName");

        ProcessRunner.endProcess(player, currentUserProcess);

        return voucherName;
    }
}
