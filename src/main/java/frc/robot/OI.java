package frc.robot;

import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Radians;
import static frc.robot.configs.Constants.ShooterConstants.speakerPos;
import static frc.robot.configs.Tuning.OI.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.controllermaps.LogitechF310;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.configs.ShooterSettings;
import frc.robot.subsystems.climber.ClimberManager;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.subsystems.shooter.ShooterTables;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwerveModule;
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
    static final int switchAmpMode      = LogitechF310.BUTTON_LEFT_BUMPER;
    static final int switchShooterMode  = LogitechF310.BUTTON_RIGHT_BUMPER;
    static final int setManualShoot     = LogitechF310.BUTTON_Y;

    // Climber
    static final int zeroClimber        = LogitechF310.BUTTON_X;
    static final int setManualClimb     = LogitechF310.BUTTON_A;
    static final int climberUp          = LogitechF310.DPAD_UP;
    static final int climberDown        = LogitechF310.DPAD_DOWN;

    static public enum ShooterMode {
        SPEAKER,
        SPEAKER_MANUAL,
        AMP
    }

    public static ShooterMode currentShooterMode = ShooterMode.SPEAKER_MANUAL;
    public static boolean manualFireSet = true;
    public static boolean manualClimbSet = true;
    public static boolean aligning = false;
    public static boolean ejecting = false;

    public static int lastPOV = -1;

    static boolean isGround = false;

    public static double topVector = 250;
    public static double bottomVector = 850;

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

        double kBoostCoefficient = NORMALSPEED + boostStrength * (1.0 - NORMALSPEED);

        /*--------------------------------------------------------------------------------------------------------*/
        // INTAKE

        if (driverStick.getRawAxis(intake) > 0.5) {
            Intake.suck();
            // TODO slow the drive if intaking
            kBoostCoefficient = 0.6;

            if (Intake.reallyHasPiece)
                driverStick.setRumble(RumbleType.kBothRumble, 0.9);
            else 
                driverStick.setRumble(RumbleType.kBothRumble, 0.0);
        } else {
            driverStick.setRumble(RumbleType.kBothRumble, 0.0);
            if (!Shooter.firing())
                Intake.setState(IntakeState.STOW); 
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // SETUP

        Vector2 drive = new Vector2(driverStick.getRawAxis(moveX), -driverStick.getRawAxis(moveY));
        double rotate = RMath.smoothJoystick1(driverStick.getRawAxis(rotateX)) * -ROTSPEED;

        double manualRPM = 3800.0;
        double manualAngle = 54.0;

        if (drive.mag() < 0.125) 
            drive = new Vector2();
        else
            drive = RMath.smoothJoystick2(drive).mul(kBoostCoefficient);

        if (Math.abs(rotate) < 0.005) 
            rotate = 0;

        /*--------------------------------------------------------------------------------------------------------*/
        // SHOOTER

        if (driverStick.getRawButton(eject))
            ejecting = true;
        else
            ejecting = false;

        // Auto-rev and fire
        boolean shooterFire = driverStick.getRawButton(fireShooter);
        ShooterSettings shooterSettings = ShooterTables.calculate(SwervePosition.getPosition().sub(speakerPos).mag() / 12.0);

        // checks current shooter mode and sets the angle and velocities accordingly
        if (shooterFire) {
            switch (currentShooterMode) {
                case AMP:
                    aligning = false;
                    ShooterPivot.setPosition(Math.toRadians(56.0));
                    Shooter.revTo(topVector, bottomVector);
                    Shooter.shoot();
                break;

                case SPEAKER:
                    aligning = true;
                    ShooterPivot.setPosition(shooterSettings.getAngle().in(Radians));
                    Shooter.revTo(shooterSettings.getVelocity().in(RPM));
                    Shooter.shoot();
                break;

                // For shooting while moving
                // case SPEAKER:
                //     aligning = true;
                //     Shooter.fireWhileMoving();
                //     Shooter.shoot();
                // break;

                case SPEAKER_MANUAL:
                    aligning = false;
                    ShooterPivot.setPosition(Math.toRadians(manualAngle));
                    // Shooter.revTo(manualRPM);
                    Shooter.revTo(manualRPM, manualRPM);
                    Shooter.shoot();
                break;
            
                default:
                break;
            }
        } else {
            // aligning = false;
            // if (!ejecting) 
                Shooter.neutral();
            // else
            //     Shooter.eject();
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // SWERVE

        if (driverStick.getRawButton(lock)) {
            for (SwerveModule module: SwerveManager.mods) {
                module.rotateToRad((module.pos.atan2()));
            }
        }

        if (!aligning) {
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
        } else {
            // Allows free translation while locking the robot's angle to the speaker.
            SwerveManager.moveAndRotateTo(drive, speakerPos.sub(SwervePosition.getPosition()).norm().atan2());
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
            // DPAD UP
            if (operatorStick.getPOV() == climberUp) {
                ClimberManager.manualExtend();
                System.out.println("extending");
            } 
            // DPAD DOWN
            else if (operatorStick.getPOV() == climberDown) {
                ClimberManager.manualPull();
                System.out.println("pulling");
            }
            // NO CLIMBER INPUT
            else {
                ClimberManager.brake();
            }
            // DPAD UP
            if (operatorStick.getPOV() == climberUp && operatorStick.getPOV() != lastPOV) {
                ClimberManager.autoExtend();
            } 
            // DPAD DOWN
            else if (operatorStick.getPOV() == climberDown && operatorStick.getPOV() != lastPOV) {
                ClimberManager.autoPull();
            }

        // LEFT BUMPER
        if (operatorStick.getRawButtonPressed(switchAmpMode)) {
            switch (currentShooterMode) {
                case AMP:
                    currentShooterMode = ShooterMode.SPEAKER_MANUAL;
                break;
                
                case SPEAKER_MANUAL:
                    currentShooterMode = ShooterMode.AMP;
                break;
            }
        }

        // RIGHT BUMPER
        // if (operatorStick.getRawButtonPressed(switchShooterMode)) {
        //     switch (currentShooterMode) {
        //         case SPEAKER:
        //             currentShooterMode = ShooterMode.SPEAKER_MANUAL;
        //         break;
                
        //         case SPEAKER_MANUAL:
        //             currentShooterMode = ShooterMode.SPEAKER;
        //         break;
        //     }
        // }

        lastPOV = operatorStick.getPOV();
    }
}