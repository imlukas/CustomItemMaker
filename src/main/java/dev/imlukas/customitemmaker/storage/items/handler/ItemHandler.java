package dev.imlukas.customitemmaker.storage.items.handler;

import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.item.CustomItem;
import dev.imlukas.customitemmaker.storage.items.registry.ItemRegistry;
import dev.imlukas.customitemmaker.utils.PDCUtils.PDCWrapper;
import dev.imlukas.customitemmaker.utils.item.ItemBuilder;
import dev.imlukas.customitemmaker.utils.recipe.RecipeUtil;
import dev.imlukas.customitemmaker.utils.storage.YMLBase;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemHandler extends YMLBase {
    private final CustomItemMaker plugin;
    private final ItemRegistry itemRegistry;

    public ItemHandler(CustomItemMaker plugin) {
        super(plugin, "items.yml");
        this.plugin = plugin;
        this.itemRegistry = plugin.getItemRegistry();
        load();
    }

    public void load() {
        for (String key : getConfiguration().getKeys(false)) {
            ItemStack item = ItemBuilder.fromSection(getConfiguration().getConfigurationSection(key));

            PDCWrapper.modifyItem(plugin, item, wrapper -> wrapper.setString("identifier", key));

            ConfigurationSection recipeSection = getConfiguration().getConfigurationSection(key + ".recipe");

            if (recipeSection != null) {
                RecipeUtil.create(plugin, recipeSection, item);
            }

            itemRegistry.register(key, item);
        }

        System.out.println("[CustomItemMaker] Loaded " + itemRegistry.getItems().size() + " items");
    }


}
