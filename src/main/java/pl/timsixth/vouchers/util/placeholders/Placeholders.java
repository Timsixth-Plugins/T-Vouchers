package pl.timsixth.vouchers.util.placeholders;

import pl.timsixth.vouchers.VouchersPlugin;

import java.util.Map;
import java.util.function.Supplier;

public final class Placeholders {

    public static StaticPlaceholder PLUGIN_VERSION;

    public static final DynamicPlaceholder VOUCHER_DISPLAY_NAME_WITH_STRIPED_COLORS = new DynamicPlaceholder("{VOUCHER_DISPLAY_NAME_WITH_STRIPED_COLORS}");
    public static final DynamicPlaceholder PLAYER_NAME = new DynamicPlaceholder("{PLAYER_NAME}");

    static {
        //Tests could not run without this try-catch
        try {
            PLUGIN_VERSION = new StaticPlaceholder("{PLUGIN_VERSION}", VouchersPlugin.getPlugin(VouchersPlugin.class).getDescription().getVersion());
        } catch (Exception ex) {
            PLUGIN_VERSION = null; //tests failed
        }
    }

    public static String applyStaticPlaceholder(String text, StaticPlaceholder placeholder) {
        return placeholder.replace(text);
    }

    public static String applyDynamicPlaceholder(String text, DynamicPlaceholder placeholder, String replacement) {
        return placeholder.replace(text, replacement);
    }

    public static String applyDynamicPlaceholders(String text, Supplier<Map<DynamicPlaceholder, String>> placeholderSupplier) {
        String result = "";

        Map<DynamicPlaceholder, String> placeholders = placeholderSupplier.get();
        for (Map.Entry<DynamicPlaceholder, String> placeholderAndReplacement : placeholders.entrySet()) {
            String textToReplace = result.isEmpty() ? text : result;

            result = placeholderAndReplacement.getKey().replace(textToReplace, placeholderAndReplacement.getValue());
        }

        return result;
    }
}
