package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {

    public static ParseResult<Integer> isInt(String text) {
        try {
            int number = Integer.parseInt(text);
            return new ParseResult<>(true, number);
        } catch (NumberFormatException e) {
            return new ParseResult<>(false);
        }
    }
}
