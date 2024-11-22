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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.timsixth.guilibrary.core.model.Generable;
import pl.timsixth.guilibrary.core.model.MenuItem;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.gui.actions.ManageVoucherAction;
import pl.timsixth.vouchers.util.UniversalItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

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

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta = setVoucherDetails(meta);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack toSkullItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta = (SkullMeta) setVoucherDetails(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
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
}
