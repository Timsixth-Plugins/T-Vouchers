package pl.timsixth.vouchers.config;

import pl.timsixth.vouchers.util.ChatUtil;
import pl.timsixth.vouchers.Main;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    public final File vouchersFile = new File(Main.getMain().getDataFolder(), "vouchers.yml");

    public static final String PERMISSION = Main.getMain().getConfig().getString("permission");
    public static final String ENCHANT_NAME = Main.getMain().getConfig().getString("enchant");
    public static final int LEVEL = Main.getMain().getConfig().getInt("level");

    public static final String NO_PERMISSION = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.no_permission"));
    public static final String CORRECT_USE = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.correct_use"));
    public static final String ADDEDVOUCHER = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.added_voucher"));
    public static final String DOESNTEXISTS = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.doesnt_exists"));
    public static final String OFFLINEPLAYER = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.offline_player"));
    public static final String ADDEDVOUCHER_TO_OTHER_PLAYER = ChatUtil.chatColor(Main.getMain().getConfig().getString("messages.added_voucher_other_player"));

    public ConfigFile() {
        createFileByBukkit("vouchers.yml");
    }

    private void createFileByBukkit(String name) {
        if (!Main.getMain().getDataFolder().mkdir()) {
            Main.getMain().getDataFolder().mkdirs();
        }
        File file = new File(Main.getMain().getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.getMain().saveResource(name, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
