package pl.timsixth.vouchers.model.process;

import lombok.ToString;
import pl.timsixth.vouchers.model.Voucher;

import java.util.UUID;
@ToString
public abstract class AbstractProcess implements IProcess{

    private final UUID userUUID;
    private Voucher currentVoucher;
    private boolean processContinue;

    public AbstractProcess(UUID userUUID) {
        this.userUUID = userUUID;
        this.processContinue = true;
    }

    @Override
    public UUID getUserUuid() {
        return userUUID;
    }
    @Override
    public Voucher getCurrentVoucher() {
        return currentVoucher;
    }
    @Override
    public void setCurrentVoucher(Voucher currentVoucher) {
        this.currentVoucher = currentVoucher;
    }

    @Override
    public boolean isContinue() {
        return processContinue;
    }

    @Override
    public void setContinue(boolean arg) {
        this.processContinue = arg;
    }
}
