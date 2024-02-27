package frc.robot.utils;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import au.grapplerobotics.LaserCan.Measurement;
import au.grapplerobotics.LaserCan.TimingBudget;
import edu.wpi.first.wpilibj.RobotBase;

public class Beambreak {
    private final LaserCan sensor;
    /**The minimum distance in which the beam is not broken in mm*/
    private final double distThreshold;
    private boolean isBroken;

    public Beambreak(int canID, double distThreshold) {
        this.sensor = new LaserCan(canID);
        this.distThreshold = distThreshold;
        try {
            //Setting max distance lower if possible for more accurate measurements
            if (this.distThreshold < 1300) {
                sensor.setRangingMode(LaserCan.RangingMode.SHORT);
            } else {
                sensor.setRangingMode(LaserCan.RangingMode.LONG);
            }

            //setting to fastest refresh rate
            sensor.setTimingBudget(TimingBudget.TIMING_BUDGET_20MS);

            //setting to narrow fov
            sensor.setRegionOfInterest(new LaserCan.RegionOfInterest(8,8,2,2)); //TODO probably tune this and also make sure it is centered
        } catch(ConfigurationFailedException e) {
            System.err.println("LaserCan, more like LaserCan't");
            e.printStackTrace();
        }
    }

    public void update() {
        if (RobotBase.isSimulation()) {
            this.isBroken = true;
        } else {
            Measurement measurement = sensor.getMeasurement();
            //if the measurement is funny, we don't consider it. I don't feel like throwing an exception or logging rn
            if (measurement.status != LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) {
                this.isBroken = false;
            } else {
                this.isBroken = measurement.distance_mm < distThreshold;
            }
        }
    }

    public boolean isBroken() {
        return this.isBroken;
    }
}