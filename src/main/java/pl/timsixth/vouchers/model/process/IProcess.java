package pl.timsixth.vouchers.model.process;

import pl.timsixth.vouchers.model.Voucher;

import java.util.UUID;

public interface IProcess {

    UUID getUserUuid();
    Voucher getCurrentVoucher();
    void setCurrentVoucher(Voucher currentVoucher);

    boolean isContinue();

    void setContinue(boolean arg);
}
