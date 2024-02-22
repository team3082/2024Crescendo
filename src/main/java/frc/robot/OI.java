package frc.robot;

import static frc.robot.Tuning.OI.*;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.Constants.Climber;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.BannerLight;
import frc.robot.subsystems.climber.ClimberManager;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwerveModule;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;
import frc.robot.utils.RMath;

public class OI {
    public static Joystick driverStick, operatorStick;

    // Driver Controls

    // Movement
    static final int moveX         = LogitechF310.AXIS_LEFT_X;
    static final int moveY         = LogitechF310.AXIS_LEFT_Y;
    static final int rotateX       = LogitechF310.AXIS_RIGHT_X;
    static final int boost         = LogitechF310.AXIS_RIGHT_TRIGGER;

    // Shooter
    /** Pre-rev the shooter to a velocity for somewhere in the field */
    static final int revShooter    = LogitechF310.BUTTON_LEFT_BUMPER;
    /** Automatically adjusts angle & velocity for both amp and speaker or goes to manual angle/velocity if in manual mode */
    static final int fireShooter   = LogitechF310.BUTTON_RIGHT_BUMPER;
    /** Ejects the gamepiece without regard for our field position */
    static final int eject         = LogitechF310.BUTTON_B;

    // Intake
    static final int intake        = LogitechF310.AXIS_LEFT_TRIGGER;

    // Others
    static final int lock          = LogitechF310.BUTTON_A;
    static final int zero          = LogitechF310.BUTTON_Y;

    /*-------------------------------------------------------------------*/

    // Operator Controls

    // Shooter
    static final int switchShooterMode  = LogitechF310.BUTTON_LEFT_BUMPER;
    static final int setManualShoot     = LogitechF310.BUTTON_Y;

    // Climber
    static final int zeroClimber   = LogitechF310.BUTTON_X;
    static final int setManualClimb= LogitechF310.BUTTON_A;
    static final int climberUp     = LogitechF310.DPAD_UP;
    static final int climberDown   = LogitechF310.DPAD_DOWN;

    static private enum ShooterMode {
        SPEAKER,
        SPEAKER_MANUAL,
        AMP
    }

    public static ShooterMode currentShooterMode = ShooterMode.SPEAKER;
    public static boolean manualFireSet = true;
    public static boolean manualClimbSet = true;

    static boolean isGround = false;

    public static double topVector = 530;
    public static double bottomVector = 700;

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
        // INPUT

        if (driverStick.getRawButton(zero)) Pigeon.zero();

        double boostStrength = driverStick.getRawAxis(boost);
        if(boostStrength < 0.1) boostStrength = 0;

        double kBoostCoefficient = NORMALSPEED + boostStrength * (1 - NORMALSPEED);

        Vector2 drive = new Vector2(driverStick.getRawAxis(moveX), -driverStick.getRawAxis(moveY));
        double rotate = driverStick.getRawAxis(rotateX) * -ROTSPEED;

        double manualRPM = 2500.0;
        
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

        /*--------------------------------------------------------------------------------------------------------*/
        // SHOOTER

        if (driverStick.getRawButton(eject)) Shooter.eject();

        // Manually rev
        boolean shooterRevv = driverStick.getRawButton(revShooter);
        // Auto-rev and fire
        boolean shooterFire = driverStick.getRawButton(fireShooter);

        double[] arr = Shooter.getDesiredShooterPos(SwervePosition.getPosition(), SwerveManager.getRobotDriveVelocity());

        // ONLY align if we are in auto-fire mode
        // silly things 🤣😁
        // if (shooterAutoFire && !shooterManualFire) {
        //     SwerveManager.moveAndRotateTo(drive, arr[2]);
        // }

        // checks current shooter mode and sets the angle and velocities accordingly
        if (shooterFire) {
            switch (currentShooterMode) {
                case AMP:
                    ShooterPivot.setPosition(Math.toRadians(55.0));
                    Shooter.revToVaried(topVector, bottomVector);
                    Shooter.shoot();
                break;

                 // manual for now, change to auto when tuned
                 // use the arr variable above for that
                case SPEAKER:
                    ShooterPivot.setPosition(Math.toRadians(57.0));
                    Shooter.revTo(manualRPM);
                    Shooter.shoot();
                break;

                case SPEAKER_MANUAL:
                    ShooterPivot.setPosition(Math.toRadians(57.0));
                    Shooter.revTo(manualRPM);
                    Shooter.shoot();
                break;
            
                default:
                break;
            }
        } else {
            ShooterPivot.setPosition(Math.toRadians(30.0)); // stow shooter
            Shooter.disable(); // Leave the shooter off if not in use
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // INTAKE


        if (driverStick.getRawAxis(intake) > 0.5)
            Intake.setState(IntakeState.GROUND);
        else
            Intake.no();

        if (driverStick.getRawButton(lock)) {
            for (SwerveModule module: SwerveManager.mods) {
                module.rotateToRad((module.pos.atan2()));
            }
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // SWERVE

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
        // CLIMBER

        // X
        if (operatorStick.getRawButtonPressed(zeroClimber)) {
            ClimberManager.zero();
        }

        if (operatorStick.getRawButtonPressed(setManualClimb)) {
            manualClimbSet = true;
        }

        if (!(manualClimbSet)) {
            // DPAD UP
            if (operatorStick.getPOV() == climberUp) {
                ClimberManager.manualExtend();
            } 
            // DPAD DOWN
            else if (operatorStick.getPOV() == climberDown) {
                ClimberManager.manualPull();
            }
            // NO CLIMBER INPUT
            else {
                ClimberManager.brake();
            }
        } else {
            // DPAD UP
            if (operatorStick.getPOV() == climberUp) {
                ClimberManager.autoExtend();
            } 
            // DPAD DOWN
            else if (operatorStick.getPOV() == climberDown) {
                ClimberManager.autoPull();
            }
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // SHOOTER

        // Y
        if (operatorStick.getRawButtonPressed(setManualShoot)) {
            manualFireSet = !(manualFireSet);
        }

        // RIGHT BUMPER
        if (operatorStick.getRawButtonPressed(switchShooterMode)) {
            switch (currentShooterMode) {
                case AMP:
                    currentShooterMode = manualFireSet ? ShooterMode.SPEAKER_MANUAL : ShooterMode.SPEAKER;
                break;
                case SPEAKER:
                    currentShooterMode = ShooterMode.AMP;
                break;
                case SPEAKER_MANUAL:
                    currentShooterMode = ShooterMode.AMP;
                break;

                default:
                break;
            }
        }
    }
}