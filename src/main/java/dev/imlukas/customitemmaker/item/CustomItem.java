package dev.imlukas.customitemmaker.item;

import com.google.common.collect.Sets;
import dev.imlukas.customitemmaker.CustomItemMaker;
import dev.imlukas.customitemmaker.storage.items.registry.ItemRegistry;
import dev.imlukas.customitemmaker.utils.PDCUtils.PDCWrapper;
import dev.imlukas.customitemmaker.utils.schedulerutil.ScheduledTask;
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

import java.util.ArrayList;
import java.util.List;
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
    private final List<ScheduledTask> runningTasks = new ArrayList<>();

    public CustomItem(CustomItemMaker plugin) {
        this.plugin = plugin;
        this.itemRegistry = plugin.getItemRegistry();


    }

    public void autoRegister() {
        plugin.getCustomItemRegistry().register(this);
    }

    /**
     * This adds a task that runs every x ticks.
     * @param ticks How many ticks should pass between each run
     * @param runnable What should be run
     */
    public void addTickableRepeatingTask(int ticks, Runnable runnable) {
       runningTasks.add(new ScheduleBuilder(plugin).every(ticks).ticks().run(runnable).sync().start());
    }

    /**
     * This adds a task that runs every x seconds.
     * @param seconds How many seconds should pass between each run
     * @param runnable What should be run
     */
    public void addRepeatingTask(int seconds, Runnable runnable) {
        runningTasks.add(new ScheduleBuilder(plugin).every(seconds).seconds().run(runnable).sync().start());
    }

    /**
     * Sets the action that should be run when the item is right clicked.
     * @param rightClickAction What should be run
     */
    public void setRightClickAction(Consumer<PlayerInteractEvent> rightClickAction) {
        this.rightClickAction = rightClickAction;
    }

    /**
     * Cancels all the repeating tasks that were added with {@link #addRepeatingTask(int, Runnable)}
     * and {@link #addTickableRepeatingTask(int, Runnable)}.
     */
    public void cancelTasks() {
        runningTasks.forEach(ScheduledTask::cancel);
    }

    /**
     * Checks if an item is the same as this item.
     * @param itemStack The item to check
     */
    public boolean isItem(ItemStack itemStack) {
        PDCWrapper wrapper = new PDCWrapper(plugin, itemStack);
        String identifier = wrapper.getString("identifier");
        return identifier != null && identifier.equals(getIdentifier());
    }

    public Consumer<PlayerInteractEvent> getRightClickAction() {
        return rightClickAction;
    }

    /**
     * Item's identifier, should match configuration key.
     */
    public abstract String getIdentifier();

    public ItemStack getItem() {
        return itemRegistry.get(getIdentifier());
    }

    public void give(Player player) {
        player.getInventory().addItem(getItem());
    }

    // Event handling

    /**
     * Registers an event handler for the given event class. The handler will be called when the event is fired.
     * @param eventClass The event class
     * @param handler The handler
     * @param <T> The event class
     */
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

    private void acceptEvent(Event event) {
        Consumer<Event> handler = eventHandlers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        }
    }

    /**
     * Unregisters all event handlers for this item.
     */
    public void unregisterEvents() {
        HandlerList.unregisterAll(listener);
    }
}
