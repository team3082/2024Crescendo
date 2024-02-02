package frc.lib.sensors;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import au.grapplerobotics.LaserCan.Measurement;
import au.grapplerobotics.LaserCan.TimingBudget;


public final class BeamBreakFactory {
    
    /**
     * creates a new beambreak from a timeofflight object
     * @param tof Time of flight sensor
     * @param min minimum distance to be registered by sensor
     * @param max maximum distance to be registered by sensor
     * @return new beambreak object
     */
    public static BeamBreak create(TimeOfFlight tof, double min, double max){
        if(min > max){
            throw new IllegalArgumentException("min must be less than max");
        }
        //annonymouse beambreak object
        BeamBreak bb = new BeamBreak() {
            private TimeOfFlight _tof = tof;
            private double _min = min;
            private double _max = max;

            @Override
            public boolean isBroken(){
                if(!_tof.goodReading()){
                    return false;
                }
                double dist = _tof.getDist();
                return _min < dist && _max > dist;
            }

        };

        return bb;
    }

    /**
     * creates a new beambreak from a timeofflight object
     * @param tof Time of flight sensor
     * @param max maximum distance to be registered by sensor
     * @return new beambreak object
     */
    public static BeamBreak create(TimeOfFlight tof, double max){
        if(max < 0){
            throw new IllegalArgumentException("max must be greater than 0");
        }
        return create(tof, 0, max);
    }

    public static BeamBreak createLazerCAN(int canID, double distThreshold){

        @SuppressWarnings("resource")
        LaserCan sensor = new LaserCan(canID);

        try{
            //Setting max distance lower if possible for more accurate measurements
            if(distThreshold < 1300){
                sensor.setRangingMode(LaserCan.RangingMode.SHORT);
            }else{
                sensor.setRangingMode(LaserCan.RangingMode.LONG);
            }

            //setting to fastest refresh rate
            sensor.setTimingBudget(TimingBudget.TIMING_BUDGET_20MS);

            //setting to narrow fov
            sensor.setRegionOfInterest(new LaserCan.RegionOfInterest(8,8,6,6));//TODO probably tune this and also make sure it is centered
        }catch(ConfigurationFailedException e){
            System.err.println("LaserCan, more like LaserCan't");
            e.printStackTrace();
        }

        BeamBreak bb = new BeamBreak() {
            private final LaserCan _sensor = sensor;
            /**The minimum distance in which the beam is not broken in mm*/
            private final double _distThreshold = distThreshold;


            @Override
            public boolean isBroken(){
                Measurement measurement = _sensor.getMeasurement();
                //if the measurement is funny, we don't consider it. I don't feel like throwing an exception or logging rn
                if(measurement.status != LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT){
                    return false;
                }

                return measurement.distance_mm < _distThreshold;
            }
        };

        return bb;
    }
}
