package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.process.CreationProcess;
import pl.timsixth.vouchers.model.process.EditProcess;
import pl.timsixth.vouchers.model.process.IProcess;
import pl.timsixth.vouchers.util.ChatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InventoryCloseListener implements Listener {

    private final MenuManager menuManager;
    private final IProcessManager<CreationProcess> createProcessManager;

    private final IProcessManager<EditProcess> editProcessManager;

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Optional<Menu> menuOptional = menuManager.getMenuByName("chooseEnchantLevel");
        Optional<Menu> menuOptional1 = menuManager.getMenuByName("listOfAllEnchants");

        if (!menuOptional.isPresent()) {
            return;
        }
        if (!menuOptional1.isPresent()) {
            return;
        }
        List<Menu> menusWhichCancelProcess = Arrays.asList(menuOptional.get(), menuOptional1.get());
        menusWhichCancelProcess.forEach(menu -> {
            if (!event.getView().getTitle().equalsIgnoreCase(ChatUtil.chatColor(menu.getDisplayName()))) {
                return;
            }

            if (createProcessManager.isProcessedByUser(createProcessManager.getProcessByUser(player.getUniqueId()), player)) {
                cancelProcess(player, createProcessManager);
            } else if (editProcessManager.isProcessedByUser(editProcessManager.getProcessByUser(player.getUniqueId()), player)) {
                cancelProcess(player, editProcessManager);
            }
        });
    }

    private <T extends IProcess> void cancelProcess(Player player, IProcessManager<T> processManager) {
        T process = processManager.getProcessByUser(player.getUniqueId());

        if (process.isContinue()) return;

        processManager.cancelProcess(process);
    }
}
