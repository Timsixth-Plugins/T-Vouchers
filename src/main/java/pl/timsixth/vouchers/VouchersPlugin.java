package pl.timsixth.vouchers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.vouchers.command.VoucherCommand;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.listener.InventoryClickListener;
import pl.timsixth.vouchers.listener.InventoryCloseListener;
import pl.timsixth.vouchers.listener.PlayerChatListener;
import pl.timsixth.vouchers.listener.PlayerInteractListener;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.PrepareProcessManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.DeleteVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.IProcessManager;
import pl.timsixth.vouchers.manager.registration.ActionRegistration;
import pl.timsixth.vouchers.manager.registration.ActionRegistrationImpl;
import pl.timsixth.vouchers.model.menu.action.custom.impl.*;
import pl.timsixth.vouchers.model.process.CreationProcess;
import pl.timsixth.vouchers.model.process.DeleteProcess;
import pl.timsixth.vouchers.model.process.EditProcess;
import pl.timsixth.vouchers.tabcompleter.VoucherCommandTabCompleter;
import pl.timsixth.vouchers.version.VersionChecker;

@Getter
public final class VouchersPlugin extends JavaPlugin {
    private MenuManager menuManager;
    private VoucherManager voucherManager;
    private PrepareProcessManager prepareToProcessManager;
    private IProcessManager<CreationProcess> createVoucherProcessManager;
    private IProcessManager<EditProcess> editVoucherManager;
    private IProcessManager<DeleteProcess> deleteVoucherManager;
    private ActionRegistration actionRegistration;
    private LogsManager logsManager;
    private ConfigFile configFile;
    private Messages messages;

    @Override
    public void onEnable() {
        loadConfig();
        voucherManager = new VoucherManager(configFile);
        actionRegistration = new ActionRegistrationImpl();
        menuManager = new MenuManager(actionRegistration, configFile);
        prepareToProcessManager = new PrepareProcessManager();
        logsManager = new LogsManager(configFile);
        createVoucherProcessManager = new CreateVoucherProcessManager(configFile, voucherManager, logsManager);
        editVoucherManager = new EditVoucherProcessManager(configFile, voucherManager, prepareToProcessManager, logsManager);
        deleteVoucherManager = new DeleteVoucherProcessManager(configFile, voucherManager, prepareToProcessManager, this, logsManager);
        getConfig().options().copyDefaults(true);
        saveConfig();
        new VersionChecker(this).checkVersion();
        getCommand("voucher").setExecutor(new VoucherCommand(voucherManager, menuManager, configFile, messages));
        getCommand("voucher").setTabCompleter(new VoucherCommandTabCompleter(voucherManager));
        loadListeners();
        registerActions();
        menuManager.load();
        voucherManager.loadVouchers();
        logsManager.load();
    }

    private void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(voucherManager), this);
        pluginManager.registerEvents(new InventoryClickListener(menuManager), this);
        pluginManager.registerEvents(new PlayerChatListener(createVoucherProcessManager, editVoucherManager, menuManager, voucherManager, this, prepareToProcessManager, messages), this);
        pluginManager.registerEvents(new InventoryCloseListener(menuManager, createVoucherProcessManager, editVoucherManager), this);
    }

    @Override
    public void onLoad() {
        loadConfig();
    }

    private void loadConfig() {
        configFile = new ConfigFile(this);
        messages = new Messages(this);
    }

    private void registerActions() {
        actionRegistration.register(new OpenMenuAction()
                , new CloseMenuAction()
                , new NoneEnchantsAction()
                , new ChooseEnchantAction()
                , new ChooseLevelAction()
                , new CreateVoucherAction()
                , new GenerateItemsAction()
                , new ManageVoucherAction()
                , new EditVoucherAction()
                , new DeleteVoucherAction()
                , new ReplaceVoucherAction()
                , new ClearAllToDayLogsAction());
    }
}

