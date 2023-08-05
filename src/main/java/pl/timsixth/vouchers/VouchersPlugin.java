package pl.timsixth.vouchers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.vouchers.bstats.Metrics;
import pl.timsixth.vouchers.command.VoucherCommand;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.listener.*;
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

import java.util.TimeZone;

import static pl.timsixth.vouchers.model.Log.LOG_DATE_TIME_FORMATTER;

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

    static {
        LOG_DATE_TIME_FORMATTER.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    }

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

        new Metrics(this, 19403);

        getCommand("voucher").setExecutor(new VoucherCommand(voucherManager, menuManager, configFile, messages));
        getCommand("voucher").setTabCompleter(new VoucherCommandTabCompleter(voucherManager));

        registerListeners();
        registerActions();

        if (!initPlaceHolderApi()) {
            Bukkit.getLogger().warning("[T-Vouchers] Please download PlaceholderAPI, if you want to use placeholders.");
        }

        menuManager.load();
        voucherManager.loadVouchers();
        logsManager.load();
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(voucherManager, messages), this);
        pluginManager.registerEvents(new InventoryClickListener(menuManager), this);
        pluginManager.registerEvents(new PlayerChatListener(createVoucherProcessManager, editVoucherManager, menuManager, voucherManager, this, prepareToProcessManager, messages), this);
        pluginManager.registerEvents(new InventoryCloseListener(menuManager, createVoucherProcessManager, editVoucherManager), this);
        pluginManager.registerEvents(new InventoryOpenListener(menuManager), this);
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

    private boolean initPlaceHolderApi() {
        return getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}

