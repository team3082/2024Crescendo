package frc.robot;

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

    public static final double MODOFFSET0 = 0.0;
    public static final double MODOFFSET1 = 0.0;
    public static final double MODOFFSET2 = 0.0;
    public static final double MODOFFSET3 = 0.0;

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

    
}
