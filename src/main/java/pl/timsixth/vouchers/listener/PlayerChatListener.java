package pl.timsixth.vouchers.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.PrepareToProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.custom.impl.ChooseEnchantAction;
import pl.timsixth.vouchers.model.process.CreationProcess;
import pl.timsixth.vouchers.model.process.EditProcess;
import pl.timsixth.vouchers.model.process.IProcess;
import pl.timsixth.vouchers.util.ChatUtil;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PlayerChatListener implements Listener {

    private final IProcessManager<CreationProcess> createProcessManager;

    private final IProcessManager<EditProcess> editProcessManager;
    private final MenuManager menuManager;

    private final VoucherManager voucherManager;

    private final VouchersPlugin vouchersPlugin;

    private final PrepareToProcessManager prepareToProcessManager;

    private final Messages messages;

    private final Pattern voucherNamePattern = Pattern.compile("[a-zA-Z\\d]{2,30}");

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (createProcessManager.isProcessedByUser(createProcessManager.getProcessByUser(player.getUniqueId()), player)) {
            player.sendMessage(messages.getCancelProcess());

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                cancelProcess(createProcessManager, player, event);
                return;
            }

            CreationProcess creationProcess = createProcessManager.getProcessByUser(player.getUniqueId());
            if (creationProcess.getCurrentVoucher() == null || creationProcess.getCurrentVoucher().getName() == null) {
                if (voucherManager.voucherExists(voucherManager.getVoucher(event.getMessage()))) {
                    player.sendMessage(messages.getVoucherAlreadyExists());
                    cancelProcess(createProcessManager, player, event);
                    return;
                }

                Matcher matcher = voucherNamePattern.matcher(event.getMessage());

                if (!matcher.matches()) {
                    createProcessManager.cancelProcess(creationProcess);
                    player.sendMessage(messages.getInvalidFormatOfName());
                    event.setCancelled(true);
                    return;
                }

                Voucher voucher = new Voucher();
                voucher.setName(ChatColor.stripColor(event.getMessage()));
                creationProcess.setCurrentVoucher(voucher);
                event.setCancelled(true);
                player.sendMessage(messages.getTypeVoucherDisplayName());
            } else if (creationProcess.getCurrentVoucher().getDisplayName() == null) {
                Voucher currentVoucher = creationProcess.getCurrentVoucher();
                currentVoucher.setDisplayName(event.getMessage());
                event.setCancelled(true);
                player.sendMessage(messages.getTypeVoucherMaterial());
            } else if (creationProcess.getCurrentVoucher().getMaterial() == null) {
                Voucher currentVoucher = creationProcess.getCurrentVoucher();
                if (event.getMessage().equalsIgnoreCase("none")) {
                    setDefaultMaterial(event, player, currentVoucher);
                    return;
                }

                if (Material.matchMaterial(event.getMessage().toUpperCase()) == null) {
                    setDefaultMaterial(event, player, currentVoucher);
                    return;
                }

                currentVoucher.setMaterial(Material.matchMaterial(event.getMessage().toUpperCase()));
                event.setCancelled(true);
                player.sendMessage(messages.getTypeVoucherCommand());
            } else if (creationProcess.getCurrentVoucher().getCommand() == null) {
                Voucher currentVoucher = creationProcess.getCurrentVoucher();
                setCommand(event, player, currentVoucher);
            } else if (creationProcess.getCurrentVoucher().getLore() == null) {
                Voucher currentVoucher = creationProcess.getCurrentVoucher();
                setLore(event, player, currentVoucher);
            }
        } else if (editProcessManager.isProcessedByUser(editProcessManager.getProcessByUser(player.getUniqueId()), player)) {
            player.sendMessage(messages.getCancelProcess());
            EditProcess editProcess = editProcessManager.getProcessByUser(player.getUniqueId());
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                editProcessManager.cancelProcess(editProcess);
                prepareToProcessManager.removeLocalizedName(prepareToProcessManager.getPrepareToProcess(player.getUniqueId()));
                event.setCancelled(true);
                return;
            }

            if (editProcess.getCurrentVoucher() == null) {
                event.setCancelled(true);
                return;
            }
            if (editProcess.getCurrentVoucher().getDisplayName() == null) {
                Voucher voucher = new Voucher();
                voucher.setDisplayName(event.getMessage());
                voucher.setName(editProcess.getCurrentVoucher().getName());
                editProcess.setCurrentVoucher(voucher);
                event.setCancelled(true);
                player.sendMessage(messages.getTypeVoucherMaterial());
            } else if (editProcess.getCurrentVoucher().getMaterial() == null) {
                Voucher currentVoucher = editProcess.getCurrentVoucher();
                if (event.getMessage().equalsIgnoreCase("none")) {
                    setDefaultMaterial(event, player, currentVoucher);
                    return;
                }

                if (Material.matchMaterial(event.getMessage().toUpperCase()) == null) {
                    setDefaultMaterial(event, player, currentVoucher);
                    return;
                }

                currentVoucher.setMaterial(Material.matchMaterial(event.getMessage().toUpperCase()));
                event.setCancelled(true);
                player.sendMessage(messages.getTypeVoucherCommand());
            } else if (editProcess.getCurrentVoucher().getCommand() == null) {
                Voucher currentVoucher = editProcess.getCurrentVoucher();
                setCommand(event, player, currentVoucher);
            } else if (editProcess.getCurrentVoucher().getLore() == null) {
                Voucher currentVoucher = editProcess.getCurrentVoucher();
                setLore(event, player, currentVoucher);
            }
        }
    }

    private void setDefaultMaterial(AsyncPlayerChatEvent event, Player player, Voucher currentVoucher) {
        currentVoucher.setMaterial(Material.PAPER);
        player.sendMessage(messages.getTypeVoucherCommand());
        event.setCancelled(true);
    }

    private void setLore(AsyncPlayerChatEvent event, Player player, Voucher currentVoucher) {
        String[] lore = event.getMessage().split("\\|");
        currentVoucher.setLore(Arrays.asList(lore));
        event.setCancelled(true);
        player.sendMessage(messages.getSetVoucherEnchants());
        Bukkit.getScheduler().runTask(vouchersPlugin, () -> {
            List<Enchantment> allEnchantments = ItemUtil.getAllEnchantments();
            Optional<Menu> menuOptional = menuManager.getMenuByName("listOfAllEnchants");
            if (!menuOptional.isPresent()) return;
            Menu enchantsMenu = menuOptional.get();
            Set<MenuItem> menuItems = enchantsMenu.getItems();
            for (int i = menuItems.size(); i < allEnchantments.size(); i++) {
                MenuItem menuItem = new MenuItem(i, Material.ENCHANTED_BOOK, ChatUtil.chatColor("&a" + allEnchantments.get(i).getName()), new ArrayList<>());
                menuItem.setAction(new ChooseEnchantAction());
                menuItem.setEnchantments(new HashMap<>());
                menuItems.add(menuItem);
            }
            enchantsMenu.setItems(menuItems);
            player.openInventory(menuManager.createMenu(enchantsMenu));
        });
    }

    private void setCommand(AsyncPlayerChatEvent event, Player player, Voucher currentVoucher) {
        currentVoucher.setCommand(event.getMessage());
        event.setCancelled(true);
        player.sendMessage(messages.getTypeVoucherLore());
    }

    private <T extends IProcess> void cancelProcess(IProcessManager<T> createProcessManager, Player player, AsyncPlayerChatEvent event) {
        createProcessManager.cancelProcess(createProcessManager.getProcessByUser(player.getUniqueId()));
        event.setCancelled(true);
    }
}
