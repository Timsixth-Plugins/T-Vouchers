package pl.timsixth.vouchers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.guilibrary.core.GUIApi;
import pl.timsixth.guilibrary.core.manager.registration.ActionRegistration;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.custom.NextPageAction;
import pl.timsixth.guilibrary.core.model.action.custom.PreviousPageAction;
import pl.timsixth.guilibrary.core.model.pagination.PaginatedMenu;
import pl.timsixth.versionchecker.VersionChecker;
import pl.timsixth.vouchers.bstats.Metrics;
import pl.timsixth.vouchers.command.VoucherCommand;
import pl.timsixth.vouchers.command.api.CommandRegistration;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.config.Settings;
import pl.timsixth.vouchers.gui.actions.*;
import pl.timsixth.vouchers.listener.*;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.MenuManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.manager.WebhookManager;
import pl.timsixth.vouchers.manager.process.CreateVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.DeleteVoucherProcessManager;
import pl.timsixth.vouchers.manager.process.EditVoucherProcessManager;

import java.util.Arrays;

@Getter
public final class VouchersPlugin extends JavaPlugin {

    private MenuManager menuManager;
    private VoucherManager voucherManager;
    private CreateVoucherProcessManager createVoucherProcessManager;
    private EditVoucherProcessManager editVoucherManager;
    private DeleteVoucherProcessManager deleteVoucherManager;
    private ActionRegistration actionRegistration;
    private LogsManager logsManager;
    private WebhookManager webhookManager;

    private ConfigFile configFile;
    private Messages messages;
    private Settings settings;

    @Override
    public void onEnable() {
        loadConfig();

        GUIApi guiApi = new GUIApi(this);
        actionRegistration = guiApi.getActionRegistration();

        voucherManager = new VoucherManager(configFile);
        menuManager = new MenuManager(actionRegistration, configFile);
        logsManager = new LogsManager(configFile);
        createVoucherProcessManager = new CreateVoucherProcessManager(configFile, voucherManager, logsManager);
        editVoucherManager = new EditVoucherProcessManager(configFile, voucherManager, logsManager);
        deleteVoucherManager = new DeleteVoucherProcessManager(configFile, voucherManager, this, logsManager);
        webhookManager = new WebhookManager(settings);

        guiApi.setMenuManager(menuManager);

        new VersionChecker(this, messages).checkVersion();

        new Metrics(this, 19403);

        registerCommands();

        registerListeners();
        guiApi.registerMenuListener();
        registerActions();

        if (!initPlaceHolderApi()) {
            Bukkit.getLogger().warning("[T-Vouchers] Please download PlaceholderAPI, if you want to use placeholders.");
        }

        setupPaginatedMenus();

        menuManager.load();
        voucherManager.loadVouchers();
        logsManager.load();
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(voucherManager, menuManager, messages, settings, webhookManager), this);
        pluginManager.registerEvents(new InventoryCloseListener(menuManager, createVoucherProcessManager, editVoucherManager), this);
        pluginManager.registerEvents(new InventoryOpenListener(menuManager), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(createVoucherProcessManager, this, messages, menuManager), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(editVoucherManager, this, messages, menuManager), this);
        pluginManager.registerEvents(new BlockPlaceListener(voucherManager), this);
    }

    @Override
    public void onLoad() {
        loadConfig();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        configFile = new ConfigFile(this);
        messages = new Messages(this);
        settings = new Settings(this);
    }

    private void registerActions() {
        actionRegistration.register(new OpenMenuAction()
                , new CloseMenuAction()
                , new NoneEnchantsAction()
                , new ChooseEnchantAction()
                , new ChooseLevelAction()
                , new CreateVoucherAction()
                , new ManageVoucherAction()
                , new EditVoucherAction()
                , new DeleteVoucherAction()
                , new ReplaceVoucherAction()
                , new ClearAllToDayLogsAction()
                , new OpenLogsMenuAction()
                , new OpenVouchersMenuAction()
                , new NextPageAction()
                , new PreviousPageAction()
                , new AcceptVoucherRedeemAction()
                , new RejectVoucherRedeemAction()
        );
    }

    private boolean initPlaceHolderApi() {
        return getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public void setupPaginatedMenu(String name, String displayName) {
        PaginatedMenu paginatedMenu = new PaginatedMenu(settings.getGuiSize(), name, displayName);
        paginatedMenu.setItemsPerPage(settings.getItemsPerPage());

        MenuItem defaultPreviousPageItem = PaginatedMenu.DEFAULT_PREVIOUS_PAGE_ITEM;
        MenuItem defaultNextPageItem = PaginatedMenu.DEFAULT_NEXT_PAGE_ITEM;

        defaultPreviousPageItem.setSlot(52);
        defaultNextPageItem.setSlot(53);

        paginatedMenu.setStaticItems(Arrays.asList(
                defaultPreviousPageItem,
                defaultNextPageItem
        ));

        menuManager.getPaginatedMenus().add(paginatedMenu);
    }

    private void setupPaginatedMenus() {
        setupPaginatedMenu("logsList", settings.getLogsGuiName());
        setupPaginatedMenu("vouchersList", settings.getVouchersGuiName());
    }

    private void registerCommands() {
        CommandRegistration commandRegistration = new CommandRegistration(this);

        commandRegistration.registerCommandWithTabCompleter(new VoucherCommand(voucherManager, menuManager, configFile, messages, settings));
    }
}

