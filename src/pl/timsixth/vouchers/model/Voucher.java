package pl.timsixth.vouchers.model;
import java.util.List;

public class Voucher {

    private final String name;
    private final String displayName;
    private final String command;
    private final List<String> lore;

    public Voucher(String name, String command, List<String> lore, String displayName) {
        this.name = name;
        this.command = command;
        this.lore = lore;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getCommand() {
        return command;
    }

    public String getDisplayName() {
        return displayName;
    }
}
