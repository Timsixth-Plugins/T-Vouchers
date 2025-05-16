package pl.timsixth.vouchers.util.placeholders;

public class DynamicPlaceholder extends Placeholder {

    public DynamicPlaceholder(String name) {
        super(name);
    }

    String replace(String text, String replacement) {
        return text.replace(getName(), replacement);
    }
}
