package frc.robot.utils.sim;

import java.util.List;

public class SimDevices {
    // central class that holds all sim devices and updates them in one place
    public static List<SimDevice> devices;

    public static void addDevice(SimDevice device) {
        // add a device to the list of devices
        devices.add(device);
    }

    public static void update() {
        // update all devices
        for (SimDevice device : devices) {
            device.update();
        }
    }
}
