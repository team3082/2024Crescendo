package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalOutput;

public class Sensor {
    public static DigitalOutput sensor;

    public static void init() {
        sensor = new DigitalOutput(3);
    }

    public static boolean isBroken() {
        return sensor.get();
    }
}
