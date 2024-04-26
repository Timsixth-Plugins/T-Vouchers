package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.manager.VoucherManager;

@RequiredArgsConstructor
public class BlockPlaceListener implements Listener {

    private final VoucherManager voucherManager;

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();

        if (voucherManager.isVoucher(itemInHand)) {
            event.setCancelled(true);
        }
    }
}
