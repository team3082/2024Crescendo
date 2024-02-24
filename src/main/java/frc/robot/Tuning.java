package frc.robot;

public class Tuning {
    //Swerve
    public static final double MOVEP = 2;
    public static final double MOVEI = 0.3;
    public static final double MOVED = 0.2;
    public static final double MOVEDEAD = 1.0;
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
     public static final double SWERVE_TRJ_PPOS = 0.007;
     public static final double SWERVE_TRJ_IPOS = 0.00;
     public static final double SWERVE_TRJ_DPOS = 0.0005;

     public static final double SWERVE_TRJ_PROT = 0.1;
     public static final double SWERVE_TRJ_IROT = 0.0;
     public static final double SWERVE_TRJ_DROT = 0.005;

     public static final double SWERVE_KVPOS = 0.0;
     public static final double SWERVE_KVROT = 0.0;
 
     public static final int CURVE_RESOLUTION = 3000;
     public static final double CURVE_DEADBAND = 0.001;
    

    //Vision

    public static final class OI {
        //driver
        public static final double KDYAW = 0.00;
        /**0 for never on, 1 for only on with no rotation input, 2 for always on */
        public static final int YAWRATEFEEDBACKSTATUS = 0;

        public static final double NORMALSPEED = 0.4;

        public static final double ROTSPEED = 0.3;
    }

    //NOTE CONTROL
    public static final class ShooterTuning {
        public static double FLYWHEELKD = 0.0;
        public static double FLYWHEELKF = 0.0;

        public static final double PIVOTKP = 0.06;
        public static final double PIVOTKI = 0.0;
        public static final double PIVOTKD = 0.002;
        public static final double PIVOTKF = 0.0;

        public static final double PIVOT_CRUISE_VEL = 15000.0; // Ticks per 100ms
        public static final double PIVOT_MAX_ACCEL = 30000.0; // In ticks per 100ms per second
        public static final int PIVOT_JERK_STRENGTH = 0; // [0,8], higher number means lower jerk 

        public static final double PIVOT_DEADBAND_POS = 0.0; // In motorticks
        public static final double PIVOT_DEADBAND_VEL = 0.0; // In motor ticks per 100ms

        public static double PIVOT_AFF_GRAVITY = 0.0;
        public static double PIVOT_AFF_SPRING = 0.0;
 
        public static final double SPEAKER_SPEED_TOP = 0.0; //in motorticks per 100ms
        public static final double SPEAKER_SPEED_BOTTOM = 0.0;

        public static final double AMP_SPEED_TOP = 0.0;
        public static final double AMP_SPEED_BOTTOM = 0.0;

        public static final double AMP_WHEEL_SPEED_DEADBAND = 0.0;
        public static final double SPEAKER_WHEEL_SPEED_DEADBAND = 0.0;
        public static final double VELOCITY_WHEEL_SPEED_DEADBAND = 0.0;

        // public static final double SHOOTER_STOW_ANGLE = 0.0;
    }

    public static final class Climbers {
        public static final double CLIMBER_KP = 0.0;
        public static final double CLIMBER_KI = 0.0;
        public static final double CLIMBER_KD = 0.0;
        public static final double CLIMBER_KF = 0.0;

        public static final double CLIMBER_CRUISE_VEL = 0.0;
        public static final double CLIMBER_MAX_ACCEL = 0.0;
        public static final int CLIMBER_JERK_STRENGTH = 1;

        public static final double CLIMBER_AFF_LOADED = 0.0;//aff for when holding the robot
        public static final double CLIMBER_AFF_UNLOADED = 0.0;//aff when not holding the robot

        

    }

    public static final class Intake {
        public static final double INTAKEPIVOTKP = 0.0;
        public static final double INTAKEPIVOTKI = 0.0;
        public static final double INTAKEPIVOTKD = 0.0;

        public static final double INTAKEGROUNDPOS = 18714 + 112911;

        public static final double INTAKEPIVOTMAXVEL = 0.0;
        public static final double INTAKEPIVOTMAXACCEL = 0.0;
        public static final int INTAKEPIVOTJERKSTRENGTH = 0;

        public static final double INTAKEBELTKP = 0.0;
        public static final double INTAKEBELTKI = 0.0;
        public static final double INTAKEBELTKD = 0.0;

        public static final double CONVEYORKP = 0.0;
        public static final double CONVEYORKI = 0.0;
        public static final double CONVEYORKD = 0.0;

        public static final double INTAKESTRENGTH = -0.8;
        public static final double FEEDSTRENGTH = -0.8;

    }
}
