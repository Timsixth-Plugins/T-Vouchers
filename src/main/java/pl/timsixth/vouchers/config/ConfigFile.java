package pl.timsixth.vouchers.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.util.ChatUtil;

import java.io.File;

@Getter
public class ConfigFile {


    private final File vouchersFile = new File(VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder(), "vouchers.yml");
    private final File guisFile = new File(VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder(), "guis.yml");
    private final File logsFile = new File(VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder(), "logs.yml");
    private final YamlConfiguration ymlVouchers = YamlConfiguration.loadConfiguration(vouchersFile);
    private final YamlConfiguration ymlLogs = YamlConfiguration.loadConfiguration(logsFile);

    public static final String PERMISSION = VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("permission");
    public static final String NO_PERMISSION = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.no_permission"));
    public static final String CORRECT_USE = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.correct_use"));
    public static final String ADDED_VOUCHER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.added_voucher"));
    public static final String VOUCHER_DOESNT_EXISTS = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.doesnt_exists"));
    public static final String OFFLINE_PLAYER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.offline_player"));
    public static final String ADDED_VOUCHER_TO_OTHER_PLAYER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.added_voucher_other_player"));
    public static final String CREATED_VOUCHER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.created_voucher"));

    public static final String UPDATED_VOUCHER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.updated_voucher"));

    public static final String DELETED_VOUCHER = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.deleted_voucher"));

    public static final String TYPE_VOUCHER_NAME = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.type_voucher_name"));

    public static final String TYPE_VOUCHER_DISPLAY_NAME = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.type_voucher_display_name"));

    public static final String CANCEL_PROCESS = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.cancel_process"));

    public static final String VOUCHER_ALREADY_EXISTS = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.voucher_already_exits"));
    public static final String TYPE_VOUCHER_LORE = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.type_voucher_lore"));

    public static final String TYPE_VOUCHER_COMMAND = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.type_voucher_command"));
    public static final String SET_VOUCHER_ENCHANTS = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.set_voucher_enchants"));

    public static final String INVALID_FORMAT_OF_NAME = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.invalid_name_format"));
    public static final String CLEAR_ALL_TODAY_LOGS = ChatUtil.chatColor(VouchersPlugin.getPlugin(VouchersPlugin.class).getConfig().getString("messages.clear_all_today_logs"));
    public ConfigFile() {
        createFileByBukkit("vouchers.yml");
        createFileByBukkit("guis.yml");
        createFileByBukkit("logs.yml");
    }

    private void createFileByBukkit(String name) {
        if (!VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder().exists()) {
            VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder().mkdir();
        }
        File file = new File(VouchersPlugin.getPlugin(VouchersPlugin.class).getDataFolder(), name);
        if (!file.exists()) {
            VouchersPlugin.getPlugin(VouchersPlugin.class).saveResource(name, true);
        }
    }
}
