package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;

public final class Constants {
    //GENERAL


    //SWERVE
    public static final int DRIVEID0 = 0;
    public static final int DRIVEID1 = 0;
    public static final int DRIVEID2 = 0;
    public static final int DRIVEID3 = 0;

    public static final int STEERID0 = 0;
    public static final int STEERID1 = 0;
    public static final int STEERID2 = 0;
    public static final int STEERID3 = 0;

    public static final double MODOFFSET0 = Rotation2d.fromDegrees(0).getRotations();
    public static final double MODOFFSET1 = Rotation2d.fromDegrees(0).getRotations();
    public static final double MODOFFSET2 = Rotation2d.fromDegrees(0).getRotations();
    public static final double MODOFFSET3 = Rotation2d.fromDegrees(0).getRotations();

    public static final double SWERVEMODX0 = 0.0;
    public static final double SWERVEMODX1 = 0.0;
    public static final double SWERVEMODX2 = 0.0;
    public static final double SWERVEMODX3 = 0.0;

    public static final double SWERVEMODY0 = 0.0;
    public static final double SWERVEMODY1 = 0.0;
    public static final double SWERVEMODY2 = 0.0;
    public static final double SWERVEMODY3 = 0.0;

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
