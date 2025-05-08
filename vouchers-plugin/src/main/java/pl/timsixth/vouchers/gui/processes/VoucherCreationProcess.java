package pl.timsixth.vouchers.gui.processes;

import pl.timsixth.guilibrary.processes.model.SubGuiProcess;
import pl.timsixth.guilibrary.processes.model.impl.AbstractMainGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherNameSubProcess;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherDisplayNameSubProcess;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherMaterialSubProcess;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.manager.process.ProcessManager;

import java.util.List;

public class VoucherCreationProcess extends AbstractMainGuiProcess {

    public VoucherCreationProcess(VouchersPlugin vouchersPlugin, Messages messages, ProcessManager processManager, VoucherManager voucherManager) {
        super("CREATE_VOUCHER_PROCESS");
        List<SubGuiProcess> guiProcesses = this.getGuiProcesses();

        guiProcesses.add(new GiveVoucherNameSubProcess(vouchersPlugin, voucherManager, messages));
        guiProcesses.add(new GiveVoucherDisplayNameSubProcess(vouchersPlugin));
        guiProcesses.add(new GiveVoucherMaterialSubProcess(vouchersPlugin, messages, processManager));
    }
}
