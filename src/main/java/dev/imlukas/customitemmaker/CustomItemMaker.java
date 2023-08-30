package dev.imlukas.customitemmaker;

import dev.imlukas.customitemmaker.command.GiveCustomCommand;
import dev.imlukas.customitemmaker.command.GiveDefaultCommand;
import dev.imlukas.customitemmaker.item.registry.CustomItemRegistry;
import dev.imlukas.customitemmaker.listener.RightClickListener;
import dev.imlukas.customitemmaker.storage.items.handler.ItemHandler;
import dev.imlukas.customitemmaker.storage.items.registry.ItemRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItemMaker extends JavaPlugin {

    private static CustomItemMaker INSTANCE;
    private ItemRegistry itemRegistry;
    private ItemHandler itemHandler;
    private CustomItemRegistry customItemRegistry;
    @Override
    public void onEnable() {
        itemRegistry = new ItemRegistry();
        itemHandler = new ItemHandler(this);
        customItemRegistry = new CustomItemRegistry();

        getCommand("giveitem").setExecutor(new GiveDefaultCommand(this));
        getCommand("givecustom").setExecutor(new GiveCustomCommand(this));
        registerListener(new RightClickListener(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public CustomItemRegistry getCustomItemRegistry() {
        return customItemRegistry;
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public static CustomItemMaker getInstance() {
        return INSTANCE;
    }
}
