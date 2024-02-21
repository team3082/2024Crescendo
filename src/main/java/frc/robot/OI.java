package frc.robot;

import static frc.robot.Tuning.OI.*;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.climber.ClimberManager;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwerveModule;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;
import frc.robot.utils.RMath;

public class OI {
    public static Joystick driverStick, operatorStick;

    // Movement
    static final int moveX         = LogitechF310.AXIS_LEFT_X;
    static final int moveY         = LogitechF310.AXIS_LEFT_Y;
    static final int rotateX       = LogitechF310.AXIS_RIGHT_X;
    static final int boost         = LogitechF310.AXIS_RIGHT_TRIGGER;

    /** Pre-rev the shooter to a velocity for somewhere in the field */
    static final int shooterRev    = LogitechF310.BUTTON_LEFT_BUMPER;
    /** Automatically adjusts angle & velocity for both amp and speaker */
    static final int shooterFire   = LogitechF310.BUTTON_RIGHT_BUMPER;
    /** Manually revs to and ejects the piece at a pre-defined velocity */
    static final int manualFire    = LogitechF310.BUTTON_X;
    /** Ejects the gamepiece without regard for our field position */
    static final int eject         = LogitechF310.BUTTON_B;

    // Intake
    static final int intake        = LogitechF310.AXIS_LEFT_TRIGGER;

    // Others
    static final int lock          = LogitechF310.BUTTON_A;
    static final int zero          = LogitechF310.BUTTON_Y;
   

    static boolean isGround = false;

    /**
     * Initialize OI with preset joystick ports.
     */
    public static void init() {
        driverStick = new Joystick(0);
        operatorStick = new Joystick(1);
    }

    public static void userInput() {
        driverInput();
        operatorInput();
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

        double manualRPM = 3500.0;
        
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

        if (driverStick.getRawButton(eject)) Shooter.eject();

        // Manually rev
        boolean shooterRevv = driverStick.getRawButton(shooterRev);
        // Auto-rev and fire
        boolean shooterAutoFire = driverStick.getRawButton(shooterFire);
        // Manual fire
        boolean shooterManualFire = driverStick.getRawButton(manualFire);

        double[] arr = Shooter.getDesiredShooterPos(SwervePosition.getPosition(), SwerveManager.getRobotDriveVelocity());

        // ONLY align if we are in auto-fire mode
        // silly things 🤣😁
        // if (shooterAutoFire && !shooterManualFire) {
        //     SwerveManager.moveAndRotateTo(drive, arr[2]);
        // }

        // If we choose to fire at our manual RPM...
        if (shooterManualFire) {
            // Manually set a position as a fallback, ensures we can make a shot in our wing
            ShooterPivot.setPosition(Math.toRadians(55.0));
            Shooter.revTo(manualRPM);
            Shooter.shoot();
        } else if (shooterAutoFire) { // Otherwise if we want to automatically fire...
            ShooterPivot.setPosition(arr[0]);
            Shooter.revTo(arr[1]);
            Shooter.shoot();
        } else {
            Shooter.disable(); // Leave the shooter off if not in use
        }

        // Scoring in the amp
        if (shooterRevv) {
           Shooter.revToVaried(530, 700); // Vector speeds to flip piece into amp
        } else {
            Shooter.disable(); // Leave the shooter off if not in use
        }

        if (driverStick.getRawAxis(intake) > 0.5) isGround = !isGround;

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

    public static void operatorInput() {
       if (operatorStick.getRawButtonPressed(LogitechF310.BUTTON_X)) {
            ClimberManager.pullHooks();
       } else {
            ClimberManager.brake();
       }

       if (operatorStick.getRawButtonPressed(LogitechF310.BUTTON_A)) {
       // ShooterPivot.setPosition(Math.PI / 6);
       }

    }

}