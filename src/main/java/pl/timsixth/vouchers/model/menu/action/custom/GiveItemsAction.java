package pl.timsixth.vouchers.model.menu.action.custom;

import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.model.menu.action.SectionAction;

import java.util.List;

public interface GiveItemsAction extends SectionAction {

    void setItems(List<ItemStack> items);
    List<ItemStack> getItems();
}
