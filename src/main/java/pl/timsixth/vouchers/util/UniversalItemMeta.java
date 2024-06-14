package pl.timsixth.vouchers.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class UniversalItemMeta {

    private final VersionMatcher versionMatcher = VersionMatcher.getInstance();

    private final ItemMeta meta;

    public boolean hasLocalizedName() {
        if (versionMatcher.isSupported("1.20.5")) {
            try {
                Method method = meta.getClass().getMethod("hasItemName");
                method.setAccessible(true);

                return (boolean) method.invoke(meta);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return meta.hasLocalizedName();
    }

    public void setLocalizedName(String localizeName) {
        if (versionMatcher.isSupported("1.20.5")) {
            try {
                Method method = meta.getClass().getMethod("setItemName", String.class);
                method.setAccessible(true);

                method.invoke(meta, localizeName);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        meta.setLocalizedName(localizeName);
    }

    public String getLocalizedName() {
        if (versionMatcher.isSupported("1.20.5")) {
            try {
                Method method = meta.getClass().getMethod("getItemName");
                method.setAccessible(true);

                return (String) method.invoke(meta);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return meta.getLocalizedName();
    }

    public ItemMeta toItemMeta() {
        return meta;
    }

}
