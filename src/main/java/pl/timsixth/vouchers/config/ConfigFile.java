package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.VouchersPlugin;

import java.io.File;

@Getter
public class ConfigFile {
    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;
    private final File vouchersFile;
    private final File guisFile;
    private final File logsFile;
    private final YamlConfiguration ymlVouchers;
    private final YamlConfiguration ymlLogs;

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

}
