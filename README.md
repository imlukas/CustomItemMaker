# CustomItemMaker

This plugin provides a way for developers to create their own custom items.

### Item implementation

```java
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
        return "showcase-item";
    }
```

### Configuration
Trough config you can define an item like this:
```yaml
showcase-item:
  type: BOW
  name: "&c&lShowcase Item"
  amount: 10 # Amount of items in the stack
  model-data: 1 # Model data for the item, used for custom texture packs.
  lore:
    - "&eItem used to showcase"
  recipe:
    materials:
      D: DIAMOND
      S: STICK
    shape:
      - "D D D"
      - "D S D"
      - "D D D"
  glow: false # If true the item will have enchantment glint
  enchantments:
    unbreakable: 1
```
Other Options:
 - skull / skull-hash -> Used for when an item is a PLAYER_HEAD to change it's texture.
 - unbreakable -> Makes the item unbreakable, infinite durability.

This will register an item and it's recipe. This is the item that's going to be given when someone gets the "ShowCaseItem" that's shown at the top.

You can get pretty creative with this and basically do any type of item you want. I for example have mode multiple custom bows and explosive weapons and also some miscellaneous stuff. 
It's all pretty simple and makes the process of creating those types of items way easier.
