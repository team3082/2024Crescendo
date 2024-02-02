package frc.lib.sensors;

public interface TimeOfFlight {
    /**
     * @return distance in inches
     */
    double getDist();

    /**
     * @return whether or not the reading is accurate
     */
    boolean goodReading();
}
