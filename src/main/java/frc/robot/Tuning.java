package frc.robot;

public class Tuning {
    //Swerve
    public static final double MOVEP = 0.0;
    public static final double MOVEI = 0.0;
    public static final double MOVED = 0.0;
    public static final double MOVEDEAD = 0.0;
    public static final double MOVEVELDEAD = 0.0;
    public static final double MOVEMAXSPEED = 0.0;
    public static final double ROTP = 0.0;
    public static final double ROTI = 0.0;
    public static final double ROTD = 0.0;
    public static final double ROTDEAD = 0.0;
    public static final double ROTVELDEAD = 0.0;
    public static final double ROTMAXSPEED = 0.0;

     // TODO Tune
     public static final double SWERVE_ROT_P = 0;
     public static final double SWERVE_ROT_I = 0;
     public static final double SWERVE_ROT_D = 0;
 
     // TODO Tune
     public static final double SWERVE_TRJ_PPOS = 0.2;
     public static final double SWERVE_TRJ_IPOS = 0.00;
     public static final double SWERVE_TRJ_DPOS = 0.02;

     public static final double SWERVE_TRJ_PROT = 0.05;
     public static final double SWERVE_TRJ_IROT = 0.0;
     public static final double SWERVE_TRJ_DROT = 0.02;

     public static final double SWERVE_KVPOS = 0.0;
     public static final double SWERVE_KVROT = 0.0;
 
     public static final int CURVE_RESOLUTION= 3000;
    

    //Vision

    public static final class OI{
        //driver
        public static final double KDYAW = 0.00;
        /**0 for never on, 1 for only on with no rotation input, 2 for always on */
        public static final int YAWRATEFEEDBACKSTATUS = 0;

        public static final double NORMALSPEED = 0.6;

        public static final double ROTSPEED = 0.3;
    }

    //NOTE CONTROL
    public static final class Shooter{
        public static final double FLYWHEELKD = 0.0;
        public static final double FLYWHEELKF = 0.0;

        public static final double PIVOTKP = 0.0;
        public static final double PIVOTKI = 0.0;
        public static final double PIVOTKD = 0.0;
        public static final double PIVOTKF = 0.0;

        public static final double PIVOT_CRUISE_VEL = 0.0;//ticks per 100ms
        public static final double PIVOT_MAX_ACCEL = 0.0;//in ticks per 100ms per second
        public static final int PIVOT_JERK_STRENGTH = 0; //[0,8], higher number means lower jerk 

        public static final double PIVOT_DEADBAND_POS = 0.0;//in motorticks
        public static final double PIVOT_DEADBAND_VEL = 0.0;//in motor ticks per 100ms

        public static final double PIVOT_AFF_SCALAR = 0.0;
    }
}
