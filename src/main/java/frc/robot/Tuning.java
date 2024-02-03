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
     // These are BS numbers rn
     public static final double SWERVE_TRL_P = 0.02;
     public static final double SWERVE_TRL_I = 0.00;
     public static final double SWERVE_TRL_D = 0.002;
 
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
    }
}
