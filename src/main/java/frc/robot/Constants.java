package frc.robot;

import au.grapplerobotics.LaserCan.RangingMode;
import au.grapplerobotics.LaserCan.RegionOfInterest;
import eggshell.constructors.motor.ctre.CTREMotorConfig;
import eggshell.constructors.motor.rev.RevMotorConfig;
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
        
        public static final CTREMotorConfig DRIVE_CONFIG = new CTREMotorConfig (
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

        public static final CTREMotorConfig STEER_CONFIG = new CTREMotorConfig (
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
        public static final boolean PIVOT_FLIPPED = false;
        public static final boolean PIVOT_BRAKED = false;
        public static final double PIVOT_RATIO = 50.0;
        public static final double PIVOT_DEADBAND = 0.001;
        public static final double PIVOT_VELOCITY = 0.0;
        public static final double PIVOT_ACCELERATION = 0.0;
        public static final double PIVOT_JERK = 0.0;

        public static final CTREMotorConfig PIVOT_CONFIG = new CTREMotorConfig (
            PIVOT_FLIPPED,
            PIVOT_BRAKED,
            PIVOT_RATIO,
            0.0,
            0.0,
            0.0,
            0.0,
            PIVOT_DEADBAND,
            false,
            PIVOT_VELOCITY,
            PIVOT_ACCELERATION,
            PIVOT_JERK
        );

        public static final int TOP_FLYWHEEL_ID = 0;
        public static final double TOP_FLYWHEEL_P = 0.0;
        public static final double TOP_FLYWHEEL_I = 0.0;
        public static final double TOP_FLYWHEEL_D = 0.0;
        public static final double TOP_FLYWHEEL_F = 0.0;

        public static final CTREMotorConfig TOP_FLYWHEEL_CONFIG = new CTREMotorConfig (
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
        public static final boolean BOTTOM_FLYWHEEL_FLIPPED = false;
        public static final boolean BOTTOM_FLYWHEEL_BRAKED = false;
        public static final double BOTTOM_FLYWHEEL_RATIO = 1.0;
        public static final double BOTTOM_FLYWHEEL_P = 0.0;
        public static final double BOTTOM_FLYWHEEL_I = 0.0;
        public static final double BOTTOM_FLYWHEEL_D = 0.0;
        public static final double BOTTOM_FLYWHEEL_F = 0.0;

        public static final CTREMotorConfig BOTTOM_FLYWHEEL_CONFIG = new CTREMotorConfig(
            BOTTOM_FLYWHEEL_FLIPPED, 
            BOTTOM_FLYWHEEL_BRAKED, 
            BOTTOM_FLYWHEEL_RATIO,
            BOTTOM_FLYWHEEL_P, 
            BOTTOM_FLYWHEEL_I, 
            BOTTOM_FLYWHEEL_D, 
            BOTTOM_FLYWHEEL_F, 
            0.0,
            false
        );
    }

    public class Intake {
        public static final int PIVOT_ID = 0;
        public static final double PIVOT_OFFSET = 0.0;
        public static final boolean PIVOT_FLIPPED = false;
        public static final boolean PIVOT_BRAKED = false;
        public static final double PIVOT_RATIO = 50.0;
        public static final double PIVOT_DEADBAND = 0.001;
        public static final double PIVOT_VELOCITY = 0.0;
        public static final double PIVOT_ACCELERATION = 0.0;
        public static final double PIVOT_JERK = 0.0;

        public static final CTREMotorConfig PIVOT_CONFIG = new CTREMotorConfig (
            PIVOT_FLIPPED,
            PIVOT_BRAKED,
            PIVOT_RATIO,
            0.0,
            0.0,
            0.0,
            0.0,
            PIVOT_DEADBAND,
            false,
            PIVOT_VELOCITY,
            PIVOT_ACCELERATION,
            PIVOT_JERK
        );

        public static final int BOTTOM_BELT_ID = 0;
        public static final boolean BOTTOM_BELT_FLIPPED = false;
        public static final boolean BOTTOM_BELT_BRAKED = false;

        public static final RevMotorConfig BOTTOM_BELT_CONFIG = new RevMotorConfig (
            BOTTOM_BELT_FLIPPED,
            BOTTOM_BELT_BRAKED,
            1.0
        );

        public static final int TOP_BELT_ID = 0;
        public static final boolean TOP_BELT_FLIPPED = false;
        public static final boolean TOP_BELT_BRAKED = false;

        public static final RevMotorConfig TOP_BELT_CONFIG = new RevMotorConfig (
            TOP_BELT_FLIPPED,
            TOP_BELT_BRAKED,
            1.0
        );

        public static final int BEAMBREAK_ID = 0;
        public static final RangingMode BEAMBREAK_RANGE_MODE = RangingMode.SHORT;
        public static final RegionOfInterest BEAMBREAK_REGION_OF_INTEREST = new RegionOfInterest(8, 8, 2, 2);
    }

    public class Climber {

    }
}
