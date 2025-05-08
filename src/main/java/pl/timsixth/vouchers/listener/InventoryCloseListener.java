package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.timsixth.guilibrary.core.model.Menu;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.ProcessManager;
import pl.timsixth.vouchers.model.Process;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InventoryCloseListener implements Listener {

    private final MenuManager menuManager;
    private final CreateVoucherProcessManager createProcessManager;
    private final EditVoucherProcessManager editProcessManager;

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Optional<Menu> chooseEnchantLevelMenuOptional = menuManager.getMenuByName("chooseEnchantLevel");
        Optional<Menu> listOfAllEnchantsMenuOptional = menuManager.getMenuByName("listOfAllEnchants");

        if (!chooseEnchantLevelMenuOptional.isPresent()) return;
        if (!listOfAllEnchantsMenuOptional.isPresent()) return;

        List<Menu> menusWhichCancelProcess = Arrays.asList(chooseEnchantLevelMenuOptional.get(), listOfAllEnchantsMenuOptional.get());
        menusWhichCancelProcess.forEach(menu -> {
            if (!event.getView().getTitle().equalsIgnoreCase(ChatUtil.chatColor(menu.getDisplayName()))) return;

            if (createProcessManager.getProcess(player.getUniqueId()).isPresent()) {
                cancelProcess(player, createProcessManager);
            } else if (editProcessManager.getProcess(player.getUniqueId()).isPresent()) {
                cancelProcess(player, editProcessManager);
            }
        });
    }

    private void cancelProcess(Player player, ProcessManager processManager) {
        Optional<Process> processOptional = processManager.getProcess(player.getUniqueId());

        if (!processOptional.isPresent()) return;

        Process process = processOptional.get();

        if (process.isProcessContinue()) return;

        processManager.cancelProcess(process);
    }
}
