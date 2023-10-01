package dev.adventurecraft.awakening.common;

import dev.adventurecraft.awakening.common.customItem.CustomItemData;
import dev.adventurecraft.awakening.extension.world.ExWorld;
import dev.adventurecraft.awakening.script.ScriptItem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;

public class AC_ItemCustom extends Item implements AC_IUseDelayItem {

    static IntArrayList loadedItemIDs = new IntArrayList();

    public String fileName;
    public String onItemUsedScript;
    private int itemUseDelay;

    private static AC_ItemCustom loadScript(File file) {
        CustomItemData customItemData = CustomItemData.fromFile(file);

        if (customItemData == null) return null;

        var isValid = isCustomDataValid(customItemData, file.getName());

        if (isValid) return new AC_ItemCustom(customItemData, file.getName());

        return null;
    }

    private static boolean isCustomDataValid(CustomItemData customItemData, String filename) {
        var id = customItemData.itemID;

        if (id == -1) {
            Minecraft.instance.overlay.addChatMessage(String.format("ItemID for %s is unspecified", filename));
            return false;
        } else if (id <= 0) {
            Minecraft.instance.overlay.addChatMessage(String.format("ItemID for %s specifies a negative itemID or zero", filename));
            return false;

        } else if (Item.byId[id] != null) {
            Minecraft.instance.overlay.addChatMessage(String.format("ItemID (%d) for %s is already in use by %s", id, filename, Item.byId[id].toString()));
            return false;

        }

        return true;
    }

    public AC_ItemCustom(CustomItemData properties, String fileName) {
        super(properties.itemID - 256);
        this.fileName = fileName;

        this.setTexturePosition(properties.iconIndex);

        this.setDurability(properties.maxItemDamage);

        this.maxStackSize = properties.maxStackSize;

        this.setTranslationKey(properties.name);
        this.onItemUsedScript = properties.onItemUsedScript;
        this.itemUseDelay = 1;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity player) {
        if (!this.onItemUsedScript.isEmpty()) {
            ScriptItem scriptItem = new ScriptItem(stack);
            Scriptable scope = ((ExWorld) world).getScope();
            Object jsObj = Context.javaToJS(scriptItem, scope);
            ScriptableObject.putProperty(scope, "itemUsed", jsObj);
            ((ExWorld) world).getScriptHandler().runScript(this.onItemUsedScript, scope);
        }

        return stack;
    }

    /**
     * Loads all the custom items in a given folder, non recursively.
     *
     * @param directory a directory containing the files to load as items.
     */
    public static void loadItems(File directory) {
        for (int loadedItemID : loadedItemIDs) {
            Item.byId[loadedItemID] = null;
        }
        loadedItemIDs.clear();

        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (file.isFile()) {
                    AC_ItemCustom item = loadScript(file);
                    if (item != null) {
                        loadedItemIDs.add(item.id);
                    }
                }
            }
        }
    }

    @Override
    public int getItemUseDelay() {
        return this.itemUseDelay;
    }
}
