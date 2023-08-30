package dev.imlukas.customitemmaker.item.impl;

import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.item.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class ShowCaseItem extends CustomItem {

    protected ShowCaseItem(CustomItemMaker plugin) {
        super(plugin);

        setRightClickAction(event -> {
            event.getPlayer().sendMessage("§aYou right clicked the item!");
        });

        registerEventHandler(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (isItem(itemInHand)) {
                player.sendMessage("You're walking with the item in your hand!");
            }
        });

        // autoRegister(); Not recommended, but it's an option
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
