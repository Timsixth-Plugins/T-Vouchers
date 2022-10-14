package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.util.ChatUtil;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Getter
@ToString
public class Log implements IGenerable {

    private final UUID senderUuid;
    private final String content;
    private final Date creationDate;
    private final ProcessType processType;

    @Override
    public MenuItem getGeneratedItem(int slot) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm a", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        MenuItem menuItem =  new MenuItem(slot, Material.MAP, ChatUtil.chatColor("&a"+content),
                ChatUtil.chatColor(Arrays.asList("&7Player:&a "+ Bukkit.getOfflinePlayer(senderUuid).getName(),
                        "&7Creation date:&a "+formatter.format(creationDate),"&7Process type:&a "+ processType)));
        menuItem.setEnchantments(new HashMap<>());

        return menuItem;
    }
}
