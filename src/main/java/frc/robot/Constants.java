package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

public final class Constants {

    public static final class Shooter {

        // Talon IDs: Flywheel
        public static final int TOPFLYWHEEL_ID = 0;
        public static final int BOTTOMFLYWHEEL_ID = 0;

        // Talon ID: Pivot
        public static final int FLYWHEELPIVOT_ID = 0;

        // CANCoder offset
        public static final double PIVOT_OFFSET = 0;

        public static final double PIVOT_GEAR_RATIO = 0.0;//TODO find this

        // Falcon : Wheel(s)
        public static final double shooterBeltRatio = 1;
        // Pivot gear ratio
        public static final double shooterGearRatio = 0;

        // Converting from RPM to Phoenix native sensor units (1u/100ms)
        public static final double RPMToVel = 2048.0 * shooterBeltRatio / 60.0 / 10.0;
        // Converting from Phoenix native sensor units (1u/100ms) to RPM
        public static final double VelToRPM = 10.0 * 60.0 / 2048.0 / shooterBeltRatio;
    }

    public static final class Intake {

        // Motor IDs: Belt motors
        public static final int INDEXER_ID = 0;
        public static final int TOPINTAKE_ID = 0;
        public static final int BOTTOMINTAKE_ID = 0;

        // Motor ID: Pivot
        public static final int INTAKEPIVOT_ID = 0;

        // CAN ID: Beambreak
        public static final int LASER_ID = 0;

        // CANCoder offset
        public static final double INTAKE_OFFSET = 0;

        // Motor : belt(s)
        public static final double intakeBeltRatio = 0;
        // Pivot gear ratio
        public static final double intakeGearRatio = 0;

        public static final double HANDOFF_SPEED = 0.5;
        public static final double INTAKE_HANDOFF_SPEED = 0.5;

        //intake and note width in millimeters, used for lasercan detection
        public static final int INTAKE_WIDTH_mm = 635;
        public static final int NOTE_WIDTH_mm = 355;

        public static final double INROBOT_INTAKE_ANGLE = 0.0;
        public static final double SOURCE_INTAKE_ANGLE = 0.0;
        public static final double GROUND_INTAKE_ANGLE = 0.0;
    }

    public static final class Swerve {

        public static final int DRIVEID0 = 5;
        public static final int DRIVEID1 = 7;
        public static final int DRIVEID2 = 1;
        public static final int DRIVEID3 = 3;

        public static final int STEERID0 = 6;
        public static final int STEERID1 = 8;
        public static final int STEERID2 = 2;
        public static final int STEERID3 = 4;

        public static final double MODOFFSET0 = 286.348;
        public static final double MODOFFSET1 = 221.309;
        public static final double MODOFFSET2 = 174.551;
        public static final double MODOFFSET3 = 134.736;

        public static final double WIDTH = 26;
        public static final double LENGTH = 26;
        public static final double MODULEOFFSET = 2.625;

        public static final double SWERVEMODX0 = (WIDTH / 2) - MODULEOFFSET;
        public static final double SWERVEMODX1 = -1 * (WIDTH / 2) + MODULEOFFSET;
        public static final double SWERVEMODX2 = -1 * (WIDTH / 2) + MODULEOFFSET;
        public static final double SWERVEMODX3 = (WIDTH / 2) - MODULEOFFSET;

        public static final double SWERVEMODY0 = -1 * (LENGTH / 2) + MODULEOFFSET;
        public static final double SWERVEMODY1 = -1 * (LENGTH / 2) + MODULEOFFSET;
        public static final double SWERVEMODY2 = (LENGTH / 2) - MODULEOFFSET;
        public static final double SWERVEMODY3 = (LENGTH / 2) - MODULEOFFSET;

        public static final double driveTrackwidth = 0.0;
        public static final double driveWheelbase = 0.0;
    
        // The unadjusted maximum velocity of the robot, in inches per second.
        public static final double maxChassisVelocity = 6380.0 / 60.0 * 6.12 * (4.0 * Math.PI);
        // The unadjusted theoretical maximum angular velocity of the robot, in radians per second.
        public static final double maxAngularVelocity = (maxChassisVelocity / Math.hypot(driveTrackwidth / 2.0, driveWheelbase / 2.0));

        // Rotations to Radians
        public static final double rotConversionFactor = 2 * Math.PI;

        public static final class MK4iDriveRatios {
            // SDS MK4i - (8.14 : 1)
            public static final double L1 = (8.14 / 1.0);
            // SDS MK4i - (6.75 : 1)
            public static final double L2 = (6.75 / 1.0);
            // SDS MK4i - (6.12 : 1)
            public static final double L3 = (6.12 / 1.0);
        }

        public static final class MK4DriveRatios {
            // SDS MK4 - (8.14 : 1)
            public static final double L1 = (8.14 / 1.0);
            // SDS MK4 - (6.75 : 1)
            public static final double L2 = (6.75 / 1.0);
            // SDS MK4 - (6.12 : 1)
            public static final double L3 = (6.12 / 1.0);
            // SDS MK4 - (5.14 : 1)
            public static final double L4 = (5.14 / 1.0);
        }

        public static final class MK4iConstants {

            // Swerve module's wheel diameter
            public static final double wheelDiameter = 4;
            // The gear ratio of the steering motor
            public static final double angleGearRatio = ((150.0 / 7.0) / 1.0);

            // Whether or not, based on our module, the motors should be inverted
            public static final InvertedValue driveMotorInvert = InvertedValue.CounterClockwise_Positive;
            public static final InvertedValue angleMotorInvert = InvertedValue.Clockwise_Positive;

            // Should the CANCoder's magnet be inverted as well?
            public static final SensorDirectionValue cancoderInvert = SensorDirectionValue.CounterClockwise_Positive;

        }

        public static final class MK4Constants {
        
            // Swerve module's wheel diameter
            public static final double wheelDiameter = 4;

            // The gear ratio of the steering motor
            public static final double angleGearRatio = (12.8 / 1.0);

            // Whether or not, based on our module, the motors should be inverted
            public static final InvertedValue driveMotorInvert = InvertedValue.CounterClockwise_Positive;
            public static final InvertedValue angleMotorInvert = InvertedValue.CounterClockwise_Positive;

            // Should the CANCoder's magnet be inverted as well?
            public static final SensorDirectionValue cancoderInvert = SensorDirectionValue.CounterClockwise_Positive;

        }

    }

    
    public static final double METERSTOINCHES = 39.3701;
}
