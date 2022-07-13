package pl.timsixth.vouchers.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ActionClickType;
import pl.timsixth.vouchers.model.menu.ClickAction;
import pl.timsixth.vouchers.model.menu.EmptySlotFilling;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.util.ChatUtil;
import pl.timsixth.vouchers.util.ItemBuilder;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.*;

public class MenuManager {

    private final Set<Menu> menuSet = new HashSet<>();
    private final YamlConfiguration ymlGuis;

    public MenuManager(ConfigFile configFile) {
        ymlGuis = YamlConfiguration.loadConfiguration(configFile.getGuisFile());
    }

    public void load() {
        load(ymlGuis);
    }

    private void load(YamlConfiguration yamlConfiguration) {
        ConfigurationSection guis = yamlConfiguration.getConfigurationSection("guis");
        for (String guiName : guis.getKeys(false)) {
            ConfigurationSection guiSection = guis.getConfigurationSection(guiName);
            if (guiSection.getConfigurationSection("empty_slots") == null) {
                Menu menu = new Menu(guiSection.getInt("size"), guiName, guiSection.getString("displayname"));
                createMenu(guiSection, menu);
            } else {
                ConfigurationSection emptySlots = guiSection.getConfigurationSection("empty_slots");
                Menu menu = new Menu(guiSection.getInt("size"), guiName, guiSection.getString("displayname"),
                        new EmptySlotFilling(
                                emptySlots.getInt("id"),
                                Material.getMaterial(emptySlots.getString("material"))
                        ));
                createMenu(guiSection, menu);
            }
        }

    }

    private void createMenu(ConfigurationSection guiSection, Menu menu) {
        ConfigurationSection slots = guiSection.getConfigurationSection("slots");
        Set<MenuItem> menuItemSet = new HashSet<>();
        for (String slotNumber : slots.getKeys(false)) {
            ConfigurationSection slot = slots.getConfigurationSection(slotNumber);
            createMenuItem(menuItemSet, slotNumber, slot);
        }
        menu.setItems(menuItemSet);
        menuSet.add(menu);
    }

    private void createMenuItem(Set<MenuItem> menuItemSet, String slotNumber, ConfigurationSection slot) {
        MenuItem menuItem = new MenuItem(
                Integer.parseInt(slotNumber),
                Material.getMaterial(slot.getString("material")),
                slot.getString("displayname"),
                slot.getStringList("lore")
        );
        setEnchants(slot, menuItem);
        setMaterialDataId(slot, menuItem);
        addAction(slot, menuItem);

        menuItemSet.add(menuItem);
    }

    private void addAction(ConfigurationSection slot, MenuItem menuItem) {
        if (slot.getConfigurationSection("click_action") != null) {
            ConfigurationSection clickActionSection = slot.getConfigurationSection("click_action");
            ClickAction clickAction = new ClickAction(ActionClickType.valueOf(clickActionSection.getString("type")), clickActionSection.getStringList("run_action"));
            menuItem.setClickAction(clickAction);
        }
    }

    private void setEnchants(ConfigurationSection slot, MenuItem menuItem) {
        if (slot.getStringList("enchants") != null) {
            Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(slot.getStringList("enchants"));
            menuItem.setEnchantments(enchantments);
        }
    }

    private void setMaterialDataId(ConfigurationSection slot, MenuItem menuItem) {
        if (slot.getInt("id") != 0) {
            menuItem.setMaterialDataId(slot.getInt("id"));
        }
    }

    public Menu getMenuByName(String name) {
        return menuSet.stream().filter(menu -> menu.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public Inventory createMenu(Menu menu) {
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), ChatUtil.chatColor(menu.getDisplayName()));

        if (menu.getEmptySlotFilling() != null) {
            for (int i = 0; i < menu.getSize(); i++) {
                inv.setItem(i, new ItemStack(menu.getEmptySlotFilling().getEmptySlotMaterial(), 1, (short) menu.getEmptySlotFilling().getEmptySlotId()));
            }
        }

        for (MenuItem menuItem : menu.getItems()) {
            if (menuItem.getMaterialDataId() == 0) {
                inv.setItem(menuItem.getSlot(), new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1))
                        .setLore(ChatUtil.chatColor(menuItem.getLore()))
                        .setName(ChatUtil.chatColor(menuItem.getDisplayName()))
                        .addEnchantmentsByMeta(menuItem.getEnchantments())
                        .toItemStack()
                );
            } else if (menuItem.getLocalizedName() != null) {
                inv.setItem(menuItem.getSlot(), new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1, (short) menuItem.getMaterialDataId()))
                        .setLore(ChatUtil.chatColor(menuItem.getLore()))
                        .setName(ChatUtil.chatColor(menuItem.getDisplayName()))
                        .addEnchantmentsByMeta(menuItem.getEnchantments())
                        .addLocalizedName(menuItem.getLocalizedName())
                        .toItemStack()
                );
            } else {
                inv.setItem(menuItem.getSlot(), new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1, (short) menuItem.getMaterialDataId()))
                        .setLore(ChatUtil.chatColor(menuItem.getLore()))
                        .setName(ChatUtil.chatColor(menuItem.getDisplayName()))
                        .addEnchantmentsByMeta(menuItem.getEnchantments())
                        .toItemStack()
                );
            }
        }
        return inv;
    }

    public Set<Menu> getMenus() {
        return menuSet;
    }

}
