package pl.timsixth.vouchers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.vouchers.command.VoucherCommand;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.listener.InventoryClickListener;
import pl.timsixth.vouchers.listener.InventoryCloseListener;
import pl.timsixth.vouchers.listener.PlayerChatListener;
import pl.timsixth.vouchers.listener.PlayerInteractListener;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.PrepareToProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.DeleteVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.model.process.CreationProcess;
import pl.timsixth.vouchers.model.process.DeleteProcess;
import pl.timsixth.vouchers.model.process.EditProcess;

public final class VouchersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigFile configFile = new ConfigFile();
        VoucherManager voucherManager = new VoucherManager(configFile);
        MenuManager menuManager = new MenuManager(configFile);
        PrepareToProcessManager prepareToProcessManager = new PrepareToProcessManager();
        IProcessManager<CreationProcess> createVoucherProcessManager = new CreateVoucherProcessManager(configFile, voucherManager);
        IProcessManager<EditProcess> editVoucherManager = new EditVoucherProcessManager(configFile, voucherManager, prepareToProcessManager);
        IProcessManager<DeleteProcess> deleteVoucherManager = new DeleteVoucherProcessManager(configFile, voucherManager, prepareToProcessManager, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getCommand("voucher").setExecutor(new VoucherCommand(voucherManager, menuManager));
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(voucherManager), this);
        pluginManager.registerEvents(new InventoryClickListener(menuManager, createVoucherProcessManager, deleteVoucherManager, editVoucherManager, voucherManager, prepareToProcessManager), this);
        pluginManager.registerEvents(new PlayerChatListener(createVoucherProcessManager, editVoucherManager, menuManager, voucherManager, this, prepareToProcessManager), this);
        pluginManager.registerEvents(new InventoryCloseListener(menuManager, createVoucherProcessManager, editVoucherManager), this);
        menuManager.load();
        voucherManager.loadVouchers();
    }
}

