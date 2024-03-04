package frc.robot;

import eggshell.constructors.motor.MotorConfig;
import eggshell.constructors.motor.MotionMagicConfig;
import eggshell.constructors.swerve.SwerveConstants;

public class Constants {
    public class Swerve {
        public static final int FL_STEER_ID = 1;
        public static final int FL_DRIVE_ID = 2;
        public static final double FL_OFFSET = 0.0;

        public static final int FR_STEER_ID = 3;
        public static final int FR_DRIVE_ID = 4;
        public static final double FR_OFFSET = 0.0;

        public static final int BL_STEER_ID = 5;
        public static final int BL_DRIVE_ID = 6;
        public static final double BL_OFFSET = 0.0;

        public static final int BR_STEER_ID = 7;
        public static final int BR_DRIVE_ID = 8;
        public static final double BR_OFFSET = 0.0;

        public static final int GYRO_ID = 0;
        
        public static final double DRIVE_P = 0.0;
        public static final double DRIVE_I = 0.0;
        public static final double DRIVE_D = 0.0;
        public static final double DRIVE_F = 0.0;
        public static final double DRIVE_DEADBAND = 0.001;

        public static final double STEER_P = 0.0;
        public static final double STEER_I = 0.0;
        public static final double STEER_D = 0.0;
        public static final double STEER_F = 0.0;
        public static final double STEER_DEADBAND = 0.001;

        public static final double TRANSLATION_P = 0.0;
        public static final double TRANSLATION_I = 0.0;
        public static final double TRANSLATION_D = 0.0;
        public static final double TRANSLATION_F = 0.0;

        public static final double ROTATION_P = 0.0;
        public static final double ROTATION_I = 0.0;
        public static final double ROTATION_D = 0.0;
        public static final double ROTATION_F = 0.0;
        
        public static final MotorConfig DRIVE_CONFIG = new MotorConfig (
            true, 
            true, 
            SwerveConstants.SwerveModule.MK4I_L3.steerRatio, 
            DRIVE_P, 
            DRIVE_I, 
            DRIVE_D, 
            DRIVE_F, 
            DRIVE_DEADBAND, 
            false
        );

        public static final MotorConfig STEER_CONFIG = new MotorConfig (
            true, 
            true, 
            SwerveConstants.SwerveModule.MK4I_L3.steerRatio, 
            STEER_P, 
            STEER_I, 
            STEER_D, 
            STEER_F, 
            STEER_DEADBAND, 
            false
        );
    }

    public class Shooter {
        public static final int PIVOT_ID = 0;
        public static final double PIVOT_OFFSET = 0.0;

        public static final MotorConfig PIVOT_CONFIG = new MotorConfig (
            false,
            true,
            50.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.001,
            false
        );

        public static final double PIVOT_VELOCITY = 0.0;
        public static final double PIVOT_ACCELERATION = 0.0;
        public static final double PIVOT_JERK = 0.0;

        public static final MotionMagicConfig PIVOT_MOTION_MAGIC_CONFIG = new MotionMagicConfig (
            PIVOT_VELOCITY,
            PIVOT_ACCELERATION,
            0.0,
            0.0,
            PIVOT_JERK
        );

        public static final int TOP_FLYWHEEL_ID = 0;
        public static final double TOP_FLYWHEEL_P = 0.0;
        public static final double TOP_FLYWHEEL_I = 0.0;
        public static final double TOP_FLYWHEEL_D = 0.0;
        public static final double TOP_FLYWHEEL_F = 0.0;

        public static final MotorConfig TOP_FLYWHEEL_CONFIG = new MotorConfig (
            false, 
            false, 
            1.0,
            TOP_FLYWHEEL_P, 
            TOP_FLYWHEEL_I, 
            TOP_FLYWHEEL_D, 
            TOP_FLYWHEEL_F, 
            0.0,
            false
        );

        public static final int BOTTOM_FLYWHEEL_ID = 0;
        public static final double BOTTOM_FLYWHEEL_P = 0.0;
        public static final double BOTTOM_FLYWHEEL_I = 0.0;
        public static final double BOTTOM_FLYWHEEL_D = 0.0;
        public static final double BOTTOM_FLYWHEEL_F = 0.0;

        public static final MotorConfig BOTTOM_FLYWHEEL_CONFIG = new MotorConfig(
            false, 
            false, 
            1.0,
            BOTTOM_FLYWHEEL_P, 
            BOTTOM_FLYWHEEL_I, 
            BOTTOM_FLYWHEEL_D, 
            BOTTOM_FLYWHEEL_F, 
            0.0,
            false
        );
    }

    public class Intake {

    }

    public class Climber {

    }
}
