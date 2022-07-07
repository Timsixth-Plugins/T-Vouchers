package pl.timsixth.vouchers.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;


public class TestPattern {

    private final Pattern voucherNamePattern = Pattern.compile("[a-zA-Z\\d]{2,30}");

    @Test
    public void shouldCompareStringToPattern(){
        Matcher matcher = voucherNamePattern.matcher("a");
        assertFalse(matcher.matches());
    }
}
