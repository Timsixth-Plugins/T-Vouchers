package pl.timsixth.vouchers.model.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.enums.ActionClickType;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class ClickAction {

    private final ActionClickType actionClickType;
    private final List<String> calledAction;
    private List<ItemStack> itemsToGive;

}
