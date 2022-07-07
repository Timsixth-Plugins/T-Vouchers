package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Voucher {

    private String name;
    private String command;
    private List<String> lore;
    private String displayName;
    private Map<Enchantment, Integer> enchantments;

    public Voucher(String name, String command, List<String> lore, String displayName) {
        this.name = name;
        this.command = command;
        this.lore = lore;
        this.displayName = displayName;
    }
}
