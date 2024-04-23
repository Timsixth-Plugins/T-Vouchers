package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.guilibrary.core.util.ChatUtil;
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
    private int itemsPerPage;
    private int guiSize;
    private String vouchersGuiName;
    private String logsGuiName;
    private String voucherNameInputName;
    private String voucherDisplayNameInputName;
    private String voucherMaterialInputName;

    public ConfigFile(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        vouchersFile = createFile("vouchers.yml");
        guisFile = createFile("guis.yml");
        logsFile = createFile("logs.yml");

        ymlVouchers = YamlConfiguration.loadConfiguration(vouchersFile);
        ymlLogs = YamlConfiguration.loadConfiguration(logsFile);
        ymlGuis = YamlConfiguration.loadConfiguration(guisFile);
        loadSettings();
    }

    private void loadSettings() {
        FileConfiguration config = vouchersPlugin.getConfig();
        permission = config.getString("permission");
        itemsPerPage = config.getInt("items_par_page");
        guiSize = config.getInt("gui_size");
        vouchersGuiName = ChatUtil.chatColor(config.getString("vouchers_gui_name"));
        logsGuiName = ChatUtil.chatColor(config.getString("logs_gui_name"));
        voucherNameInputName = ChatUtil.chatColor(config.getString("inputs_names.name"));
        voucherDisplayNameInputName = ChatUtil.chatColor(config.getString("inputs_names.display_name"));
        voucherMaterialInputName = ChatUtil.chatColor(config.getString("inputs_names.material"));
    }

    private File createFile(String name) {
        if (!vouchersPlugin.getDataFolder().exists()) {
            vouchersPlugin.getDataFolder().mkdir();
        }
        File file = new File(vouchersPlugin.getDataFolder(), name);
        if (!file.exists()) {
            vouchersPlugin.saveResource(name, true);
        }

        return file;
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
