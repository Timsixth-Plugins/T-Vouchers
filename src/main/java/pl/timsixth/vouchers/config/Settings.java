package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;

@Getter
public final class Settings {

    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;
    private String permission;
    private int itemsPerPage;
    private int guiSize;
    private String vouchersGuiName;
    private String logsGuiName;
    private String voucherNameInputName;
    private String voucherDisplayNameInputName;
    private String voucherMaterialInputName;

    public Settings(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        loadSettings();
    }

    void loadSettings() {
        FileConfiguration config = vouchersPlugin.getConfig();
        permission = config.getString("permission");
        itemsPerPage = config.getInt("items_per_page");
        guiSize = config.getInt("gui_size");
        vouchersGuiName = ChatUtil.chatColor(config.getString("vouchers_gui_name"));
        logsGuiName = ChatUtil.chatColor(config.getString("logs_gui_name"));
        voucherNameInputName = ChatUtil.chatColor(config.getString("inputs_names.name"));
        voucherDisplayNameInputName = ChatUtil.chatColor(config.getString("inputs_names.display_name"));
        voucherMaterialInputName = ChatUtil.chatColor(config.getString("inputs_names.material"));
    }
}
