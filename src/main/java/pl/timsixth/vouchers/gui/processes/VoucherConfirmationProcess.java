package pl.timsixth.vouchers.gui.processes;

import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.guilibrary.processes.model.impl.AbstractMainGuiProcess;
import pl.timsixth.vouchers.gui.processes.subproceses.ConfirmVoucherRedeemSubProcess;
import pl.timsixth.vouchers.manager.MenuManager;

import java.util.List;

public class VoucherConfirmationProcess extends AbstractMainGuiProcess {
    public VoucherConfirmationProcess(MenuManager menuManager) {
        super("CONFIRM_VOUCHER_PROCESS");

        List<SubGuiProcess> guiProcesses = this.getGuiProcesses();
        guiProcesses.add(new ConfirmVoucherRedeemSubProcess(menuManager));
    }
}
