package dev.imlukas.customitemmaker.utils.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeUtil {

    public static ShapedRecipe create(JavaPlugin plugin, String recipeKey, ConfigurationSection section, ItemStack item) {
        ShapedRecipe recipe = new ShapedRecipe(createKey(plugin, recipeKey), item);

        String[] shape = new String[3];
        List<String> shapeList = section.getStringList("shape");
        for (int i = 0; i < shapeList.size(); i++) {
            shape[i] = shapeList.get(i).replace(" ", "");
        }
        recipe.shape(shape);

        for (String key : section.getConfigurationSection("materials").getKeys(false)) {
            recipe.setIngredient(key.charAt(0), Material.valueOf(section.getString("materials." + key)));
        }

        if (Arrays.stream(shape).anyMatch(s -> s.contains("."))) {
            recipe.setIngredient('.', Material.AIR);
        }

        registerRecipe(recipe);
        return recipe;
    }

    public static ShapedRecipe create(JavaPlugin plugin, ConfigurationSection section, ItemStack item) {
        String recipeKey = section.getString("key");
        return create(plugin, recipeKey, section, item);
    }
    public static ShapedRecipe create(JavaPlugin plugin, String key, ItemStack item,
                                      Map<Character, ItemStack> ingredients, String... shape) {
        ShapedRecipe recipe = new ShapedRecipe(createKey(plugin, key), item);
        recipe.shape(shape);
        for (Map.Entry<Character, ItemStack> stringItemStackEntry : ingredients.entrySet()) {
            recipe.setIngredient(stringItemStackEntry.getKey(), stringItemStackEntry.getValue().getType());
        }

        if (Arrays.stream(shape).anyMatch(s -> s.contains("."))) {
            recipe.setIngredient('.', Material.AIR);
        }

        registerRecipe(recipe);
        return recipe;
    }

    private static void registerRecipe(ShapedRecipe recipe) {
        if (Bukkit.addRecipe(recipe)) {
            System.out.println("Registered recipe " + recipe.getKey().getKey());
        } else {
            System.out.println("Failed to register recipe " + recipe.getKey().getKey());
        }
    }

    public static void remove(JavaPlugin plugin, String key) {
        Bukkit.removeRecipe(createKey(plugin, key));
    }

    private static NamespacedKey createKey(JavaPlugin plugin, String name) {
        return new NamespacedKey(plugin, name);
    }
}