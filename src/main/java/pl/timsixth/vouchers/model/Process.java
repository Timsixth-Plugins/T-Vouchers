package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.timsixth.vouchers.enums.ProcessType;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Process {

    private final UUID userUUID;
    private final ProcessType type;
    private Voucher currentVoucher;
    private boolean processContinue;

    public Process(UUID userUUID, ProcessType type) {
        this.userUUID = userUUID;
        this.type = type;
        this.processContinue = true;
    }
}
