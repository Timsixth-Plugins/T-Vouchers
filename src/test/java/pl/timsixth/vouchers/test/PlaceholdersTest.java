package pl.timsixth.vouchers.test;

import org.junit.Test;
import pl.timsixth.vouchers.util.placeholders.DynamicPlaceholder;
import pl.timsixth.vouchers.util.placeholders.Placeholders;
import pl.timsixth.vouchers.util.placeholders.StaticPlaceholder;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlaceholdersTest {

    @Test
    public void shouldReplaceStaticPlaceholder() {
        String string2 = "Hi, {PLUGIN_VERSION}";

        StaticPlaceholder PLUGIN_VERSION = new StaticPlaceholder("{PLUGIN_VERSION}", "v1.0.0");

        string2 = Placeholders.applyStaticPlaceholder(string2, PLUGIN_VERSION);

        assertEquals("Hi, v1.0.0", string2);
    }

    @Test
    public void shouldReplaceDynamicPlaceholder() {
        String string2 = "Hi, {PLUGIN_VERSION}";

        DynamicPlaceholder PLUGIN_VERSION = new DynamicPlaceholder("{PLUGIN_VERSION}");

        string2 = Placeholders.applyDynamicPlaceholder(string2, PLUGIN_VERSION, "v1.0.0");

        assertEquals("Hi, v1.0.0", string2);
    }

    @Test
    public void shouldReplaceMultiDynamicPlaceholder() {
        String string2 = "Hi, {PLUGIN_VERSION} and {PLUGIN_VERSION2}";

        DynamicPlaceholder PLUGIN_VERSION = new DynamicPlaceholder("{PLUGIN_VERSION}");
        DynamicPlaceholder PLUGIN_VERSION2 = new DynamicPlaceholder("{PLUGIN_VERSION2}");

        string2 = Placeholders.applyDynamicPlaceholders(string2, () -> {
            Map<DynamicPlaceholder, String> placeholderStringMap = new LinkedHashMap<>();

            placeholderStringMap.put(PLUGIN_VERSION, "v1.0.0");
            placeholderStringMap.put(PLUGIN_VERSION2, "v1.0.1");

            return placeholderStringMap;
        });

        assertEquals("Hi, v1.0.0 and v1.0.1", string2);
    }
}
