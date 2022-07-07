package pl.timsixth.vouchers.model.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@Getter
@Setter
public class MenuItem {

    private final int slot;
    private final Material material;
    private final String displayName;
    private final List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private ClickAction clickAction;
    private int materialDataId;
    private String localizedName;

}
