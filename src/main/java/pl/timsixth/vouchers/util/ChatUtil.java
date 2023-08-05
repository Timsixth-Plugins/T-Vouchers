package pl.timsixth.vouchers.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
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

    public static String hexColor(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] charArray = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char character : charArray) {
                builder.append("&").append(character);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return chatColor(message);
    }

    public static List<String> hexColor(List<String> texts) {
        List<String> strings = new ArrayList<>();

        texts.forEach(text -> strings.add(hexColor(text)));

        return chatColor(strings);
    }
}
