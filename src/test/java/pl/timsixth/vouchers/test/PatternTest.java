package pl.timsixth.vouchers.test;

import org.junit.Test;
import pl.timsixth.vouchers.model.Voucher;

import java.util.regex.Matcher;

import static org.junit.Assert.assertFalse;

public class PatternTest {

    @Test
    public void shouldCompareStringToPattern() {
        Matcher matcher = Voucher.VOUCHER_NAME_PATTERN.matcher("a");

        assertFalse(matcher.matches());
    }
}
