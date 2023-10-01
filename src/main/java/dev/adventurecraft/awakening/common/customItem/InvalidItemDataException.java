package dev.adventurecraft.awakening.common.customItem;

public class InvalidItemDataException extends Exception {
    String filename;
    String propertyName;
    String propertyValue;

    Exception parentException= null;

    public InvalidItemDataException(String filename, String propertyName, String propertyValue) {
        this.filename = filename;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public InvalidItemDataException(String filename, String propertyName, String propertyValue, Exception parentException) {
        this.parentException = parentException;
        this.filename = filename;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}
