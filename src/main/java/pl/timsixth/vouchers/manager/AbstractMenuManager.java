package pl.timsixth.vouchers.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.vouchers.manager.registration.ActionRegistration;
import pl.timsixth.vouchers.model.menu.EmptySlotFilling;
import pl.timsixth.vouchers.model.menu.Menu;
import pl.timsixth.vouchers.model.menu.MenuItem;
import pl.timsixth.vouchers.model.menu.action.Action;
import pl.timsixth.vouchers.model.menu.action.click.ClickAction;
import pl.timsixth.vouchers.model.menu.action.custom.GiveItemsAction;
import pl.timsixth.vouchers.model.menu.action.custom.impl.GiveItemsActionImpl;
import pl.timsixth.vouchers.model.menu.action.custom.impl.NoneClickAction;
import pl.timsixth.vouchers.util.ChatUtil;
import pl.timsixth.vouchers.util.ItemBuilder;
import pl.timsixth.vouchers.util.ItemUtil;

import java.util.*;

@RequiredArgsConstructor
public abstract class AbstractMenuManager {

    private final ActionRegistration actionRegistration;

    private final Set<Menu> menuSet = new HashSet<>();

    public abstract void load();

    protected void load(YamlConfiguration yamlConfiguration) {
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
        setPrice(slot, menuItem);
        setEnchants(slot, menuItem);
        setMaterialDataId(slot, menuItem);
        setAction(slot, menuItem);

        menuItemSet.add(menuItem);
    }

    private void setAction(ConfigurationSection slot, MenuItem menuItem) {
        if (slot.getConfigurationSection("click_action") != null) {
            ConfigurationSection clickActionSection = slot.getConfigurationSection("click_action");

            String name = clickActionSection.getString("type");

            Optional<Action> actionOptional = actionRegistration.getActionByName(name);
            if (!actionOptional.isPresent()) {
                ClickAction clickAction = new NoneClickAction();
                menuItem.setAction(clickAction);
                return;
            }

            ClickAction action = (ClickAction) actionOptional.get();

            if (action instanceof GiveItemsAction) {
                ClickAction giveItemsClickAction = setItemsToGive(clickActionSection);
                menuItem.setAction(giveItemsClickAction);
                return;
            }

            ClickAction clickAction;
            try {
                clickAction = action.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            clickAction.setArgs(clickActionSection.getStringList("args"));

            if (useActionWhichIncludingSection()) {
                addActionsWhichIncludingSection(clickActionSection);
                return;
            }

            menuItem.setAction(clickAction);
        }
    }

    private ClickAction setItemsToGive(ConfigurationSection clickActionSection) {
        if (clickActionSection.getConfigurationSection("items") == null) {
            return new NoneClickAction();
        }
        GiveItemsAction giveItemsAction = new GiveItemsActionImpl();
        ConfigurationSection itemsSection = clickActionSection.getConfigurationSection("items");
        List<ItemStack> items = new ArrayList<>();
        for (String materialName : itemsSection.getKeys(false)) {
            ConfigurationSection materialSection = itemsSection.getConfigurationSection(materialName);
            ItemStack item = new ItemBuilder(new ItemStack(Material.getMaterial(materialName), materialSection.getInt("amount"))).toItemStack();

            if (materialSection.getInt("id") != 0) {
                item = new ItemBuilder(new ItemStack(Material.getMaterial(materialName), materialSection.getInt("amount"), (short) materialSection.getInt("id"))).toItemStack();
            }
            if (materialSection.getStringList("enchants") != null) {
                Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(materialSection.getStringList("enchants"));
                item = new ItemBuilder(new ItemStack(Material.getMaterial(materialName), materialSection.getInt("amount"), (short) materialSection.getInt("id")))
                        .addEnchantments(enchantments)
                        .toItemStack();
            }
            items.add(item);
        }
        giveItemsAction.setItems(items);
        return giveItemsAction;
    }

    private void setPrice(ConfigurationSection slot, MenuItem menuItem) {
        if (slot.getInt("price") != 0) {
            menuItem.setPrice(slot.getInt("price"));
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

    public Optional<Menu> getMenuByName(String name) {
        return menuSet.stream().
                filter(menu -> menu.getName().equalsIgnoreCase(name))
                .findAny();
    }

    public Inventory createMenu(Menu menu) {
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), ChatUtil.chatColor(menu.getDisplayName()));

        if (menu.getEmptySlotFilling() != null) {
            for (int i = 0; i < menu.getSize(); i++) {
                inv.setItem(i, new ItemStack(menu.getEmptySlotFilling().getEmptySlotMaterial(), 1, (short) menu.getEmptySlotFilling().getEmptySlotId()));
            }
        }

        for (MenuItem menuItem : menu.getItems()) {
            List<String> lore = menuItem.getLore();
            List<String> replaceLore = new ArrayList<>();
            for (String line : lore) {
                replaceLore.add(line.replace("{PRICE}", String.valueOf(menuItem.getPrice())));
            }
            if (menuItem.getMaterialDataId() == 0) {
                inv.setItem(menuItem.getSlot(), new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1))
                        .setLore(ChatUtil.chatColor(replaceLore))
                        .setName(ChatUtil.chatColor(menuItem.getDisplayName()))
                        .addEnchantmentsByMeta(menuItem.getEnchantments())
                        .toItemStack()
                );
            } else {
                inv.setItem(menuItem.getSlot(), new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1, (short) menuItem.getMaterialDataId()))
                        .setLore(ChatUtil.chatColor(replaceLore))
                        .setName(ChatUtil.chatColor(menuItem.getDisplayName()))
                        .addEnchantmentsByMeta(menuItem.getEnchantments())
                        .toItemStack()
                );
            }
        }
        return inv;
    }

    protected abstract void addActionsWhichIncludingSection(ConfigurationSection clickActionSection);

    protected abstract boolean useActionWhichIncludingSection();

    public Set<Menu> getMenus() {
        return menuSet;
    }
}
