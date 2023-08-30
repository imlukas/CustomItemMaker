package dev.imlukas.customitemmaker.listener;

import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.item.CustomItem;
import dev.imlukas.customitemmaker.item.registry.CustomItemRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {

    private final CustomItemRegistry itemRegistry;

    public RightClickListener(CustomItemMaker plugin) {
        this.itemRegistry = plugin.getCustomItemRegistry();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        for (CustomItem value : itemRegistry.getCustomItemMap().values()) {
            if (!value.isItem(item)) {
                continue;
            }

            if (value.getRightClickAction() == null) {
                continue;
            }

            value.getRightClickAction().accept(event);
        }
    }
}
