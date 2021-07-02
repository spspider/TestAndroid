package fr.frazew.virtualgyroscope.hooks.sensorchange;

/**
 * Created by sergey on 16.12.2017.
 */

public interface Singleton {
    float[] values = new float[3];

    void setVal();

    float getVal();

    ;
}
