package pl.timsixth.vouchers.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatUtil {
    public static String chatColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> chatColor(List<String> stringList) {
        List<String> strings = new ArrayList<>();
        for (String text : stringList) {
            String msg = ChatColor.translateAlternateColorCodes('&', text);
            strings.add(msg);
        }
        return strings;
    }
}
