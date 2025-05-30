package pl.timsixth.vouchers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class RedeemVoucher {

    private final String voucherName;
    private int redeemTimes;

    public RedeemVoucher(String voucherName) {
        this.voucherName = voucherName;
        this.redeemTimes = 1;
    }

    public void addRedeem() {
        redeemTimes++;
    }
}
