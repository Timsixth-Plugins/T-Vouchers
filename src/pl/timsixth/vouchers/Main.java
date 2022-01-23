package pl.timsixth.vouchers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.vouchers.command.VoucherCommand;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.listener.PlayerInteractListner;
import pl.timsixth.vouchers.manager.VoucherManager;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigFile configFile = new ConfigFile();
        VoucherManager voucherManager = new VoucherManager(configFile);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getCommand("voucher").setExecutor(new VoucherCommand(voucherManager));
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListner(voucherManager),this);
        voucherManager.loadVouchers();
    }

    public static Main getMain() {
        return Main.getPlugin(Main.class);
    }
}

