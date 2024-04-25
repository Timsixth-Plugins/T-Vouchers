package pl.timsixth.vouchers.gui.processes;

import pl.timsixth.guilibrary.processes.model.impl.AbstractMainGuiProcess;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.gui.processes.subproceses.GiveVoucherLocalizeNameSubProcess;

public class VoucherPrepareProcess extends AbstractMainGuiProcess {

    public VoucherPrepareProcess(VouchersPlugin vouchersPlugin) {
        super("VOUCHER_PREPARE");
        getGuiProcesses().add(new GiveVoucherLocalizeNameSubProcess(vouchersPlugin));
    }
}
