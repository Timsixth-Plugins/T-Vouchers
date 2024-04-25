package pl.timsixth.vouchers.gui.processes;

import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.guilibrary.processes.model.impl.AbstractMainGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherDisplayNameSubProcess;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherMaterialSubProcess;
import pl.timsixth.vouchers.manager.process.ProcessManager;

import java.util.List;

public class VoucherEditionProcess extends AbstractMainGuiProcess {
    public VoucherEditionProcess(VouchersPlugin vouchersPlugin, Messages messages, ProcessManager processManager) {
        super("EDIT_VOUCHER_PROCESS");
        List<SubGuiProcess> guiProcesses = this.getGuiProcesses();

        guiProcesses.add(new GiveVoucherDisplayNameSubProcess(vouchersPlugin));
        guiProcesses.add(new GiveVoucherMaterialSubProcess(vouchersPlugin, messages, processManager));
    }
}
