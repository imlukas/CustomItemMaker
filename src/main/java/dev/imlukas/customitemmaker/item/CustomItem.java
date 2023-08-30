package dev.imlukas.customitemmaker.item;

import com.google.common.collect.Sets;
import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.storage.items.registry.ItemRegistry;
import dev.imlukas.customitemmaker.utils.PDCUtils.PDCWrapper;
import dev.imlukas.customitemmaker.utils.schedulerutil.builders.ScheduleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class CustomItem {
    protected final CustomItemMaker plugin;
    protected final ItemRegistry itemRegistry;

    private Consumer<PlayerInteractEvent> rightClickAction;
    private final Listener listener = new Listener() {};
    private final Map<Class<? extends Event>, Consumer<Event>> eventHandlers = new ConcurrentHashMap<>();
    private final Set<Class<? extends Event>> registeredEvents = Sets.newConcurrentHashSet();

    protected CustomItem(CustomItemMaker plugin) {
        this.plugin = plugin;
        this.itemRegistry = plugin.getItemRegistry();


    }

    public void addTickableRepeatingTask(int ticks, Runnable runnable) {
        new ScheduleBuilder(plugin).every(ticks).ticks().run(runnable).sync().start();
    }

    public void addRepeatingTask(int seconds, Runnable runnable) {
        new ScheduleBuilder(plugin).every(seconds).seconds().run(runnable).sync().start();
    }

    public void setRightClickAction(Consumer<PlayerInteractEvent> rightClickAction) {
        this.rightClickAction = rightClickAction;
    }

    public boolean isItem(ItemStack itemStack) {
        PDCWrapper wrapper = new PDCWrapper(plugin, itemStack);
        String identifier = wrapper.getString("identifier");
        return identifier != null && identifier.equals(getIdentifier());
    }

    public Consumer<PlayerInteractEvent> getRightClickAction() {
        return rightClickAction;
    }

    public abstract String getIdentifier();

    public ItemStack getItem() {
        return itemRegistry.get(getIdentifier());
    }

    public void give(Player player) {
        player.getInventory().addItem(getItem());
    }

    public final <T extends Event> void registerEventHandler(Class<T> eventClass, Consumer<T> handler) {
        Consumer<Event> eventHandler = event -> handler.accept((T) event);

        if (registeredEvents.add(eventClass)) { // Only register the listener once
            Bukkit.getPluginManager().registerEvent(eventClass, listener, EventPriority.HIGH, (listener, event) -> {
                acceptEvent(event);
            }, plugin, false);

        }

        eventHandlers.merge(eventClass, eventHandler, (a, b) -> event -> {
            a.accept(event);
            b.accept(event);
        });
    }

    public void acceptEvent(Event event) {
        Consumer<Event> handler = eventHandlers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        }
    }

    public void unregisterEvents() {
        HandlerList.unregisterAll(listener);
    }

    public void autoRegister() {
        plugin.getCustomItemRegistry().register(this);
    }

}
