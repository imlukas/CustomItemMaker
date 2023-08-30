package dev.imlukas.customitemmaker.command;

import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.item.CustomItem;
import dev.imlukas.customitemmaker.item.registry.CustomItemRegistry;
import dev.imlukas.customitemmaker.storage.items.registry.ItemRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GiveDefaultCommand implements CommandExecutor, TabCompleter {
    private final ItemRegistry itemRegistry;

    public GiveDefaultCommand(CustomItemMaker plugin) {
        this.itemRegistry = plugin.getItemRegistry();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return true;
        }

        String name = args[0];

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("itemmaker.giveitem")) {
            return true;
        }

        if (!itemRegistry.getItemNames().contains(name)) {
            sender.sendMessage("§cThis item does not exist.");
            return true;
        }

        ItemStack itemStack = itemRegistry.get(name);
        player.getInventory().addItem(itemStack);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        return itemRegistry.getItemNames();
    }
}
