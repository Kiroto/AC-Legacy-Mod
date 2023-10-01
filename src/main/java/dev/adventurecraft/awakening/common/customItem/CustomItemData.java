package dev.adventurecraft.awakening.common.customItem;

import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CustomItemData {

    private static Integer loadPropertyInt(String propertyName, Properties properties, String fileName) throws InvalidItemDataException {
        String propertyValue = properties.getProperty(propertyName);
        try {
            return Integer.parseInt(propertyValue);
        } catch (NumberFormatException ex) {
            throw new InvalidItemDataException(fileName, propertyName, propertyValue, ex);
        }
    }

    public static CustomItemData fromPropertiesFile(File propertiesFile) {
        var properties = new Properties();

        try {
            var itemData = new CustomItemData();
            properties.load(new FileInputStream(propertiesFile));

            itemData.itemID = loadPropertyInt("itemID", properties, propertiesFile.getName());
            itemData.iconIndex = loadPropertyInt("iconIndex", properties, propertiesFile.getName());
            itemData.maxItemDamage = loadPropertyInt("maxItemDamage", properties, propertiesFile.getName());
            itemData.maxStackSize = loadPropertyInt("maxStackSize", properties, propertiesFile.getName());

            itemData.name = properties.getProperty("name", itemData.name);
            itemData.onItemUsedScript = properties.getProperty("onItemUsedScript", itemData.onItemUsedScript);

            return itemData;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InvalidItemDataException ex) {
            String messageString = String.format("'%s' for %s is specified invalidly '%s'", ex.propertyName, ex.filename, ex.propertyValue);
            if (ex.parentException != null)
                messageString += " because: " + ex.getMessage();
            Minecraft.instance.overlay.addChatMessage(messageString);
        }
        return null;
    }

    public static CustomItemData fromJsonFile(File jsonFile) throws JsonSyntaxException {
        try {
            Gson gson = new Gson();
            var fileReader = new FileReader(jsonFile);
            return gson.fromJson(fileReader, CustomItemData.class);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex) {
            Minecraft.instance.overlay.addChatMessage(String.format("Json item file '%s' contains syntax errors.", jsonFile.getName()));
        }
        return null;
    }

    public static CustomItemData fromFile(File file) {
        String fileExtension = Files.getFileExtension(file.getName()).toLowerCase();
        if (fileExtension.equals("json")) {
            return CustomItemData.fromJsonFile(file);
        } else {
            return CustomItemData.fromPropertiesFile(file);
        }
    }

    public int itemID = -1;
    public String name = "Unnamed";
    public int iconIndex = 182;
    public int maxItemDamage = 1;
    public int maxStackSize = 1;
    public String onItemUsedScript = "";
}
