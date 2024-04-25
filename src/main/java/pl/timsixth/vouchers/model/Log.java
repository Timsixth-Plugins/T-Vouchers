package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import pl.timsixth.guilibrary.core.model.Generable;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.model.action.custom.NoneClickAction;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.enums.ProcessType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@ToString
@RequiredArgsConstructor
public class Log implements Generable {

    private final UUID senderUuid;
    private final String content;
    private final LocalDateTime creationDate;
    private final ProcessType processType;

    public static final DateTimeFormatter LOG_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm a", Locale.ENGLISH);
    public static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-M-yyyy", Locale.ENGLISH);

    public Log(UUID senderUuid, String content, ProcessType processType) {
        this.senderUuid = senderUuid;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        this.processType = processType;
    }

    @Override
    public MenuItem getGeneratedItem(int slot) {
        MenuItem menuItem = new MenuItem(slot, Material.MAP, ChatUtil.chatColor("&a" + content),
                ChatUtil.chatColor(Arrays.asList("&7Player:&a " + Bukkit.getOfflinePlayer(senderUuid).getName(),
                        "&7Creation date:&a " + LOG_DATE_TIME_FORMATTER.format(creationDate), "&7Process type:&a " + processType)));
        menuItem.setAction(new NoneClickAction());
        menuItem.setEnchantments(new HashMap<>());

        return menuItem;
    }

    public String toText() {
        return senderUuid +
                ";" +
                content +
                ";" +
                LOG_DATE_TIME_FORMATTER.format(creationDate) +
                ";" +
                processType.name() +
                ";" +
                Bukkit.getOfflinePlayer(senderUuid).getName();
    }

    @RequiredArgsConstructor
    public enum LogMessages {
        CREATE_VOUCHER_MESSAGE("Voucher of name %s was created", ProcessType.CREATE),
        DELETE_VOUCHER_MESSAGE("Voucher of name %s was deleted", ProcessType.DELETE),
        UPDATE_VOUCHER_MESSAGE("Voucher of name %s has been updated", ProcessType.EDIT);

        private final String message;
        private final ProcessType processType;

        public static String matchMessage(ProcessType processType) {
            for (LogMessages logMessage : values()) {
                if (logMessage.processType == processType) return logMessage.message;
            }
            return "";
        }
    }
}
