package frc.robot.tuning;

import frc.robot.utils.Vector2;

public class Constants {
    // SWERVE
    public class Swerve {
        // FL
        public static final int FL_DRIVE_ID = 0;
        public static final int FL_STEER_ID = 0;
        public static final int FL_ENCODER_ID = 0;

        // FR
        public static final int FR_DRIVE_ID = 0;
        public static final int FR_STEER_ID = 0;
        public static final int FR_ENCODER_ID = 0;

        // BL
        public static final int BL_DRIVE_ID = 0;
        public static final int BL_STEER_ID = 0;
        public static final int BL_ENCODER_ID = 0;

        // BR
        public static final int BR_DRIVE_ID = 0;
        public static final int BR_STEER_ID = 0;
        public static final int BR_ENCODER_ID = 0;
    }

    // SHOOTER
    public class Shooter {
        public static final int PIVOT_ID = 0;
        public static final int ENCODER_ID = 0;
        public static final int FLYWHEEL_TOP_ID = 0;
        public static final int FLYWHEEL_BOTTOM_ID = 0;

        public static final double FLYWHEEL_DIAMETER = 4.000;
        public static final double FLYWHEEL_INCHES_TO_ROTATIONS = FLYWHEEL_DIAMETER * Math.PI;

        public static final double PIVOT_DOWN = 0.0;

        public static final Vector2 PASSING_DEST = new Vector2(0.0, 0.0);
        public static final double PASSING_HEIGHT = 240.0;
    } 
 
    // INTAKE
    public class Intake {
        public static final int PIVOT_ID = 22;
        public static final int BELT_TOP_ID = 3;
        public static final int BELT_BOTTOM_ID = 25;
        public static final int INDEX_ID = 23;
        public static final int BEAMBREAK_ID = 2;

        public static final double INTAKE_WIDTH_mm = 635.0;
        public static final double NOTE_WIDTH_mm = 355.0;
    } 
 
    // CLIMBER
    public class Climber {
        public static final int WINCH_LEFT_ID = 0;
        public static final int HALL_LEFT_ID = 0;
        public static final int WINCH_RIGHT_ID = 0;
        public static final int HALL_RIGHT_ID = 0;
    } 

    public static final double GRAVITY_INCHES = 385.827;
}