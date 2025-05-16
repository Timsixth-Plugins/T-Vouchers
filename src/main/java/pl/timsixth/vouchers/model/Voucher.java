package pl.timsixth.vouchers.model;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.timsixth.guilibrary.core.model.Generable;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.guilibrary.core.util.UniversalItemMeta;
import pl.timsixth.vouchers.gui.actions.ManageVoucherAction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

import static pl.timsixth.vouchers.util.PlaceholderUtil.replacePlaceholders;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Voucher implements Generable {

    private String name;
    private List<String> commands = new ArrayList<>();
    private List<String> lore = new ArrayList<>();
    private String displayName;
    private Material material;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String textures; //this is important to build custom head
    private String permission;
    private List<ItemFlag> itemFlags;
    private boolean discordNotification;

    public static final Pattern VOUCHER_NAME_PATTERN = Pattern.compile("[a-zA-Z\\d]{2,30}");

    public Voucher(String name, List<String> commands, List<String> lore, String displayName, Material material) {
        this.name = name;
        this.commands = commands;
        this.lore = lore;
        this.displayName = displayName;
        this.material = material;
    }

    @Override
    public MenuItem getGeneratedItem(int slot) {
        ItemStack item = this.toItemStack();

        if (textures != null) item = this.toSkullItem();

        MenuItem menuItem = new MenuItem(slot, item);
        menuItem.setAction(new ManageVoucherAction());
        menuItem.setLocalizedName(name);

        if (enchantments != null) menuItem.setEnchantments(enchantments);
        else menuItem.setEnchantments(new HashMap<>());

        return menuItem;
    }

    private ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        item.setDurability((short) 0);
        ItemMeta meta = item.getItemMeta();
        meta = setVoucherDetails(meta);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack toSkullItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        item.setDurability((short) 0);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta = (SkullMeta) setVoucherDetails(meta);

        GameProfile profile = new GameProfile(UUID.fromString("6790e033-1da2-4e1b-9b2d-ffcb57b4b3b1"), "");
        profile.getProperties().put("textures", new Property("textures", textures));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            try {
                //This code is inspired by DecentHolograms, I got some code that I needed
                Class<?> resolvableProfileClass = Class.forName("net.minecraft.world.item.component.ResolvableProfile");

                Constructor<?> constructor = resolvableProfileClass.getConstructor(GameProfile.class);

                Method method = meta.getClass().getDeclaredMethod("setProfile", constructor.getDeclaringClass());
                method.setAccessible(true);

                method.invoke(meta, constructor.newInstance(profile));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                     ClassNotFoundException | InstantiationException ex) {
                Bukkit.getLogger().severe(ex.getMessage());
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    private ItemMeta setVoucherDetails(ItemMeta meta) {
        meta.setDisplayName(ChatUtil.hexColor(displayName));
        meta.setLore(ChatUtil.hexColor(lore));

        if (enchantments != null) {
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
        }

        if (itemFlags != null) {
            meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
        }

        UniversalItemMeta universalItemMeta = new UniversalItemMeta(meta);
        universalItemMeta.setLocalizedName(name);

        return universalItemMeta.toItemMeta();
    }

    public boolean isSkullItem() {
        return textures != null;
    }

    public boolean hasPermission() {
        return permission != null;
    }

    public void give(Player player, int amount) {
        ItemStack item = isSkullItem() ? toSkullItem() : toItemStack();
        item.setAmount(amount);

        player.getInventory().addItem(replacePlaceholders(player, item));
    }
}
