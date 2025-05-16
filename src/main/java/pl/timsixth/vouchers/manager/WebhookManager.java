package pl.timsixth.vouchers.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.timsixth.vouchers.config.Settings;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.model.discord.Embed;
import pl.timsixth.vouchers.model.discord.Webhook;
import pl.timsixth.vouchers.util.HttpClient;
import pl.timsixth.vouchers.util.placeholders.DynamicPlaceholder;
import pl.timsixth.vouchers.util.placeholders.Placeholders;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class WebhookManager {

    private final Settings settings;

    public void notifyVoucherRedeem(Player player, Voucher voucher) {
        Webhook webhook = settings.getWebhook();

        if (webhook.getName().isEmpty()) {
            throw new IllegalStateException("Webhook name can not be empty");
        }

        JsonObject jsonObject = createJsonObject(settings, player, voucher);

        try {
            HttpClient.Response response = HttpClient.create(settings.getWebhook().getUrl())
                    .post(jsonObject);

            if (response.getStatus() == 204) {
                Bukkit.getLogger().info("Webhook notification has been sent successfully");
            }

        } catch (IOException e) {
            Bukkit.getLogger().severe("Error during sending request to webhook: " + e.getMessage());
        }

    }

    private JsonObject createJsonObject(Settings settings, Player player, Voucher voucher) {
        Webhook webhook = settings.getWebhook();
        Embed embed = settings.getEmbed();
        String webhookMessage = settings.getWebhookMessage();

        JsonObject author = getAuthor(embed);
        JsonObject image = getImage(embed.getImageURL());
        JsonObject footer = getFooter(embed);
        JsonObject thumbnail = getImage(embed.getThumbnailURL());
        JsonObject jsonEmbed = getJsonEmbed(player, voucher, embed);

        jsonEmbed.add("author", author);
        jsonEmbed.add("image", image);
        jsonEmbed.add("footer", footer);
        jsonEmbed.add("thumbnail", thumbnail);

        JsonArray embeds = new JsonArray();
        embeds.add(jsonEmbed);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", Placeholders.applyDynamicPlaceholders(webhookMessage, () -> {
            Map<DynamicPlaceholder, String> placeholders = new LinkedHashMap<>();

            placeholders.put(Placeholders.VOUCHER_DISPLAY_NAME_WITH_STRIPED_COLORS, ChatColor.stripColor(voucher.getDisplayName()));
            placeholders.put(Placeholders.PLAYER_NAME, player.getName());

            return placeholders;
        }));
        jsonObject.addProperty("tts", false);
        jsonObject.addProperty("username", Placeholders.applyStaticPlaceholder(webhook.getName(), Placeholders.PLUGIN_VERSION));
        jsonObject.addProperty("avatar_url", webhook.getAvatarUrl().toString());
        jsonObject.add("embeds", embeds);

        return jsonObject;
    }

    private @NotNull JsonObject getFooter(Embed embed) {
        JsonObject footer = new JsonObject();
        footer.addProperty("icon_url", embed.getFooterIconURL().toString());

        return footer;
    }

    private @NotNull JsonObject getImage(URL url) {
        JsonObject image = new JsonObject();
        image.addProperty("url", url.toString());

        return image;
    }

    private @NotNull JsonObject getAuthor(Embed embed) {
        JsonObject author = new JsonObject();
        author.addProperty("name", Placeholders.applyStaticPlaceholder(embed.getAuthor().getName(), Placeholders.PLUGIN_VERSION));
        author.addProperty("icon_url", embed.getAuthor().getIconURL().toString());

        return author;
    }

    private @NotNull JsonObject getJsonEmbed(Player player, Voucher voucher, Embed embed) {
        JsonObject jsonEmbed = new JsonObject();
        jsonEmbed.addProperty("id", 250870663);
        jsonEmbed.addProperty("description", Placeholders.applyDynamicPlaceholders(embed.getDescription(), () -> {
            Map<DynamicPlaceholder, String> placeholders = new LinkedHashMap<>();

            placeholders.put(Placeholders.VOUCHER_DISPLAY_NAME_WITH_STRIPED_COLORS, ChatColor.stripColor(voucher.getDisplayName()));
            placeholders.put(Placeholders.PLAYER_NAME, player.getName());

            return placeholders;
        }));
        jsonEmbed.addProperty("title", Placeholders.applyStaticPlaceholder(embed.getTitle(), Placeholders.PLUGIN_VERSION));
        jsonEmbed.addProperty("color", embed.getColor());
        jsonEmbed.addProperty("timestamp", embed.getTimestamp().toString());

        return jsonEmbed;
    }
}
