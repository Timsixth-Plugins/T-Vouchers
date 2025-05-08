package pl.timsixth.vouchers.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ParseResult<T> {
        private final boolean successful;
        private T number;
    }
}
