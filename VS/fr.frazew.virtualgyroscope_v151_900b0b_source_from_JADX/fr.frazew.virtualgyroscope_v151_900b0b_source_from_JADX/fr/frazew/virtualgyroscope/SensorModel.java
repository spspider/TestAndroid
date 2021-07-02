package fr.frazew.virtualgyroscope;

public class SensorModel {
    public int handle;
    public boolean isAlreadyNative = false;
    public float maxRange;
    public int minDelay;
    public String name;
    public String permission;
    public float resolution;
    public String stringType;

    public SensorModel(int sensorType, String name, int handle, float resolution, int minDelay, float maxRange, String stringType, String permission) {
        this.name = name;
        this.handle = handle;
        this.resolution = resolution;
        this.minDelay = minDelay;
        this.maxRange = maxRange;
        this.permission = permission;
        this.stringType = stringType;
    }
}
