package dev.imlukas.customitemmaker.storage.items.registry;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private final Map<String, ItemStack> items = new HashMap<>();

    public ItemStack get(String identifier) {
        return items.get(identifier).clone();
    }

    public Map<String, ItemStack> getItems() {
        return items;
    }

    public void register(String identifier, ItemStack item) {
        items.put(identifier, item);
    }

    public void unregister(String identifier) {
        items.remove(identifier);
    }


}
