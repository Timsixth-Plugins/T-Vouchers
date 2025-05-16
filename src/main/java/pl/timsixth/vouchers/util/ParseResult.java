package pl.timsixth.vouchers.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ParseResult<T> {
    private final boolean successful;
    private T value;

    public static <T> ParseResult<T> success(T obj) {
        return new ParseResult<>(true, obj);
    }

    public static <T> ParseResult<T> failed() {
        return new ParseResult<>(false);
    }
}
