package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.model.discord.Embed;
import pl.timsixth.vouchers.model.discord.EmbedAuthor;
import pl.timsixth.vouchers.model.discord.Webhook;
import pl.timsixth.vouchers.util.URLUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private boolean useConfirmationMenu;
    private Webhook webhook;
    private String webhookMessage;
    private Embed embed;

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
        useConfirmationMenu = config.getBoolean("use_confirmation_menu");

        webhookMessage = config.getString("discord.message.content");
        webhook = createWebhook(config);
        embed = createEmbed(config);
    }

    private Embed createEmbed(FileConfiguration config) {
        String path = "discord.embed.";

        return new Embed(
                config.getString(path + "title"),
                config.getString(path + "description"),
                new EmbedAuthor(
                        config.getString(path + "author.name"),
                        URLUtil.parseURL(config.getString(path + "author.icon_url")).getValue()

                ),
                LocalDateTime.parse(config.getString(path + "timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                Integer.decode(config.getString(path + "color")),
                URLUtil.parseURL(config.getString(path + "image_url")).getValue(),
                URLUtil.parseURL(config.getString(path + "thumbnail_url")).getValue(),
                URLUtil.parseURL(config.getString(path + "footer.icon_url")).getValue()

        );
    }

    private Webhook createWebhook(FileConfiguration config) {
        String path = "discord.webhook.";

        return new Webhook(
                URLUtil.parseURL(config.getString(path + "url")).getValue(),
                config.getString(path + "name"),
                URLUtil.parseURL(config.getString(path + "avatar_url")).getValue()
        );
    }
}
