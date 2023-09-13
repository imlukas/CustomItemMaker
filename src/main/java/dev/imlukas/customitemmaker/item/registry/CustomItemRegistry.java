package dev.imlukas.customitemmaker.item.registry;

import dev.imlukas.customitemmaker.item.CustomItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemRegistry {

    private final Map<String, CustomItem> customItemMap = new HashMap<>();

    public CustomItem get(String identifier) {
        if (!customItemMap.containsKey(identifier)) {
            return null;
        }

        return customItemMap.get(identifier);
    }

    public Map<String, CustomItem> getCustomItemMap() {
        return customItemMap;
    }

    public List<String> getCustomItemNames() {
        return new ArrayList<>(customItemMap.keySet());
    }

    public void register(CustomItem customItem) {
        customItemMap.put(customItem.getIdentifier(), customItem);
    }

    public void unregister(String identifier) {
        customItemMap.remove(identifier);
    }
}
