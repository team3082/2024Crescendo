package frc.lib.sensors;

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
}
