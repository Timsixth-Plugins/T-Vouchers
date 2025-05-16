package pl.timsixth.vouchers.util.placeholders;

import lombok.Getter;

@Getter
public class StaticPlaceholder extends Placeholder {
    private final String replacement;

    public StaticPlaceholder(String name, String replacement) {
        super(name);
        this.replacement = replacement;
    }

    String replace(String text) {
        return text.replace(getName(), replacement);
    }
}
