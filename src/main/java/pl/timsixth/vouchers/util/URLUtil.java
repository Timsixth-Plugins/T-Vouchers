package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
public class URLUtil {

    public static ParseResult<URL> parseURL(String text) {
        try {
            return ParseResult.success(new URL(text));
        } catch (MalformedURLException exception) {
            return ParseResult.failed();
        }
    }
}
