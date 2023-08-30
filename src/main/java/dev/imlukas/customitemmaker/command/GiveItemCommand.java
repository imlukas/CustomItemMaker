package dev.imlukas.customitemmaker.command;

import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.item.CustomItem;
import dev.imlukas.customitemmaker.item.registry.CustomItemRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GiveItemCommand implements CommandExecutor, TabCompleter {
    private final CustomItemRegistry customItemRegistry;

    public GiveItemCommand(CustomItemMaker plugin) {
        this.customItemRegistry = plugin.getCustomItemRegistry();
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

        if (!customItemRegistry.getCustomItemNames().contains(name)) {
            sender.sendMessage("§cThis item does not exist.");
            return true;
        }

        CustomItem customItem = customItemRegistry.get(name);
        customItem.give((Player) sender);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        return customItemRegistry.getCustomItemNames();
    }
}
