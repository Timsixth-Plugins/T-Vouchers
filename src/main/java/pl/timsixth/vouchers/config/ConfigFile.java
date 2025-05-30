package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.manager.Reloadable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Getter
public final class ConfigFile {

    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;
    private final File vouchersFile;
    private final File guisFile;
    private final File logsFile;
    private final File voucherRedeemsFile;
    private final YamlConfiguration ymlLogs;
    private final YamlConfiguration ymlVoucherRedeems;
    private YamlConfiguration ymlVouchers;
    private YamlConfiguration ymlGuis;

    public ConfigFile(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        vouchersFile = createFile("vouchers.yml");
        guisFile = createFile("guis.yml");
        logsFile = createFile("logs.yml");
        voucherRedeemsFile = createFile("vouchers_redeems.yml");

        ymlVouchers = loadYaml(vouchersFile);
        ymlLogs = loadYaml(logsFile);
        ymlGuis = loadYaml(guisFile);
        ymlVoucherRedeems = loadYaml(voucherRedeemsFile);
        migrateGuisFile();
    }

    private File createFile(String name) {
        if (!vouchersPlugin.getDataFolder().mkdir()) {
            vouchersPlugin.getDataFolder().mkdirs();
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
        vouchersPlugin.getSettings().loadSettings();
        reloadableList.forEach(Reloadable::reload);
    }

    private void migrateGuisFile() {
        boolean migrated = false;
        ConfigurationSection clickActionOfVoucherCreatorGuiSection = ymlGuis.getConfigurationSection("guis.voucherCreator.slots.13.click_action");
        ConfigurationSection clickActionOfMainGuiSection = ymlGuis.getConfigurationSection("guis.main.slots.15.click_action");
        ConfigurationSection clickActionOfLogsGuiSection = ymlGuis.getConfigurationSection("guis.logs.slots.12.click_action");

        List<String> args = clickActionOfVoucherCreatorGuiSection.getStringList("args");
        if (args.contains("open_chat")) {
            args.set(0, "start_creation_process");
            clickActionOfVoucherCreatorGuiSection.set("args", args);
        }

        if (clickActionOfMainGuiSection.getString("type").equalsIgnoreCase("OPEN_MENU_AND_GENERATED_ITEMS")) {
            clickActionOfMainGuiSection.set("type", "OPEN_VOUCHERS_MENU");
        }

        if (clickActionOfLogsGuiSection.getString("type").equalsIgnoreCase("OPEN_MENU_AND_GENERATED_ITEMS")) {
            clickActionOfLogsGuiSection.set("type", "OPEN_LOGS_MENU");
            migrated = true;
        }

        if (!migrated) return;

        try {
            ymlGuis.save(guisFile);
            Bukkit.getLogger().info("Migrated guis.yml to v2.0 standard");
        } catch (IOException e) {
            Bukkit.getLogger().severe("Can not migrate guis.yml to v2.0 standard: Error: " + e.getMessage());
        }
    }

    private YamlConfiguration loadYaml(File file) {
        YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);

        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(file.getName())), StandardCharsets.UTF_8);

        YamlConfiguration defaultYamlFile = YamlConfiguration.loadConfiguration(reader);
        ymlFile.setDefaults(defaultYamlFile);

        ymlFile.options().copyDefaults(true);

        save(ymlFile, file);

        return ymlFile;
    }

    private void save(YamlConfiguration yamlConfiguration, File file) {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
