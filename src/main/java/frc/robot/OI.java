package frc.robot;

import static frc.robot.Tuning.OI.*;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwerveModule;
import frc.robot.swerve.SwervePID;
import frc.robot.utils.Vector2;
import frc.robot.utils.RMath;

public class OI {
    public static Joystick driverStick;

    static final int moveX     = LogitechF310.AXIS_LEFT_X;
    static final int moveY     = LogitechF310.AXIS_LEFT_Y;
    static final int rotateX   = LogitechF310.AXIS_RIGHT_X;
    static final int boost     = LogitechF310.AXIS_RIGHT_TRIGGER;

    static final int shooterFire   = LogitechF310.BUTTON_LEFT_BUMPER;
    static final int manualFire    = LogitechF310.BUTTON_X;
    static final int lock          = LogitechF310.BUTTON_A;
    static final int zero          = LogitechF310.BUTTON_Y;

    /**
     * Initialize OI with preset joystick ports.
     */
    public static void init() {
        driverStick = new Joystick(0);
    }

    /**
     * Instruct the robot to follow instructions from joysticks.
     * One call from this equals one frame of robot instruction.
     * Because we used TimedRobot, this runs 50 times a second,
     * so this lives in the teleopPeriodic() function.
     */
    public static void driverInput() {

        if (driverStick.getRawButton(zero)) Pigeon.zero();

        double boostStrength = driverStick.getRawAxis(boost);
        if(boostStrength < 0.1) boostStrength = 0;

        double kBoostCoefficient = NORMALSPEED + boostStrength * (1-NORMALSPEED);

        Vector2 drive = new Vector2(driverStick.getRawAxis(moveX), -driverStick.getRawAxis(moveY));
        double rotate = driverStick.getRawAxis(rotateX) * -ROTSPEED;

        double manualRPM = 5500.0;
        
        if (driverStick.getRawButton(manualFire)) 
            Shooter.revTo(manualRPM);
            // Shooter.fire();
        else
            Shooter.disable();
        
        if (drive.mag() < 0.125)
            drive = new Vector2();
        else
            drive = RMath.smoothJoystick2(drive).mul(kBoostCoefficient);

        if (Math.abs(rotate) < 0.005) {
            rotate = 0;
            int POV = driverStick.getPOV();
            if (POV != -1) {
                SwervePID.setDestRot(Math.PI / 2.0 - Math.toRadians(POV - 180));
            }
        }

        if (driverStick.getRawButton(lock)) {
            for (SwerveModule module: SwerveManager.mods) {
                module.rotateToRad((module.pos.atan2()));
            }
        }

        // Swerving and a steering! Zoom!
        switch (YAWRATEFEEDBACKSTATUS) {
            case 0:
                SwerveManager.rotateAndDrive(rotate, drive);
            return;
            case 1:
                if (rotate == 0)
                    SwerveManager.rotateAndDriveWithYawRateControl(rotate, drive);
                else
                    SwerveManager.rotateAndDrive(rotate, drive);
            return;
            case 2:
                SwerveManager.rotateAndDriveWithYawRateControl(rotate, drive);
            return;
        }
    }
}