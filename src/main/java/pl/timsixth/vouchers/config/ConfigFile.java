package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.Reloadable;

import java.io.File;
import java.util.List;

@Getter
public class ConfigFile {
    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;
    private final File vouchersFile;
    private final File guisFile;
    private final File logsFile;
    private final YamlConfiguration ymlLogs;
    private YamlConfiguration ymlVouchers;
    private YamlConfiguration ymlGuis;
    private String permission;

    public ConfigFile(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        vouchersFile = new File(vouchersPlugin.getDataFolder(), "vouchers.yml");
        guisFile = new File(vouchersPlugin.getDataFolder(), "guis.yml");
        logsFile = new File(vouchersPlugin.getDataFolder(), "logs.yml");
        createFile("vouchers.yml");
        createFile("guis.yml");
        createFile("logs.yml");
        ymlVouchers = YamlConfiguration.loadConfiguration(vouchersFile);
        ymlLogs = YamlConfiguration.loadConfiguration(logsFile);
        ymlGuis = YamlConfiguration.loadConfiguration(guisFile);
        loadSettings();
    }

    private void loadSettings() {
        permission = vouchersPlugin.getConfig().getString("permission");
    }

    private void createFile(String name) {
        if (!vouchersPlugin.getDataFolder().exists()) {
            vouchersPlugin.getDataFolder().mkdir();
        }
        File file = new File(vouchersPlugin.getDataFolder(), name);
        if (!file.exists()) {
            vouchersPlugin.saveResource(name, true);
        }
    }

    public void reloadFiles(List<Reloadable> reloadableList) {
        ymlGuis = YamlConfiguration.loadConfiguration(guisFile);
        ymlVouchers = YamlConfiguration.loadConfiguration(vouchersFile);
        vouchersPlugin.reloadConfig();
        vouchersPlugin.getMessages().load();
        loadSettings();
        reloadableList.forEach(Reloadable::reload);
    }
}
