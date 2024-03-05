package frc.robot;

import static frc.robot.Tuning.OI.*;
import static frc.controllermaps.LogitechF310.*;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.climber.ClimberManager;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.subsystems.shooter.ShooterManager.ShooterState;
import frc.robot.subsystems.shooter.ShooterManager.TargetMethod;
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
    static final int lock          = LogitechF310.BUTTON_X;
    static final int zero          = LogitechF310.BUTTON_Y;

    /*-------------------------------------------------------------------*/

    static private enum ShooterMode {
        SPEAKER,
        SPEAKER_MANUAL,
        AMP
    }

    public static ShooterMode currentShooterMode = ShooterMode.AMP;
    public static boolean manualFireSet = true;
    public static boolean manualClimbSet = true;

    public static int lastPOV = -1;

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

        double kBoostCoefficient = NORMALSPEED + boostStrength * (1.0 - NORMALSPEED);

        /*--------------------------------------------------------------------------------------------------------*/
        // INTAKE

        if (driverStick.getRawAxis(intake) > 0.5) {
            ShooterManager.setState(ShooterState.INTAKE);
            kBoostCoefficient = 0.4;
        } else {
            ShooterManager.setState(ShooterState.STOW);
        }

        /*--------------------------------------------------------------------------------------------------------*/

        Vector2 drive = new Vector2(driverStick.getRawAxis(moveX), -driverStick.getRawAxis(moveY));
        double rotate = driverStick.getRawAxis(rotateX) * -ROTSPEED;

        double manualRPM = 3500.0;
        // double manualAngle = 32.0;
        double manualAngle = 57.0;
        
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

        if (driverStick.getRawButton(eject)) ShooterManager.setState(ShooterState.EJECT);

        // checks current shooter mode and sets the angle and velocities accordingly
        if (driverStick.getRawButton(fireShooter)) {
            switch (currentShooterMode) {
                case AMP:
                    ShooterManager.setTargetMethod(TargetMethod.AMP);
                    ShooterManager.setState(ShooterState.SHOOT);
                break;

                case SPEAKER_MANUAL:
                    ShooterManager.setTargetMethod(TargetMethod.SUB);
                    ShooterManager.setState(ShooterState.SHOOT);
                break;
            
                default:
                break;
            }
        }

        /*--------------------------------------------------------------------------------------------------------*/
        // SWERVE

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
        //=====CLIMBER=====
        if(operatorStick.getRawButtonPressed(BUTTON_A)){
            ClimberManager.zero();
        }

        if(operatorStick.getRawAxis(AXIS_LEFT_TRIGGER) + operatorStick.getRawAxis(AXIS_RIGHT_TRIGGER) < 0.2){
            if(operatorStick.getPOV() == DPAD_DOWN){
                ClimberManager.pull();
            }else if(operatorStick.getPOV() == DPAD_UP){
                ClimberManager.extend();
            }else {
                ClimberManager.brake();
            }
        }else{
            if(operatorStick.getPOV() == DPAD_DOWN && operatorStick.getRawAxis(AXIS_LEFT_TRIGGER) > 0.2)
                ClimberManager.leftClimber.manualPull();
            else if(operatorStick.getPOV() == DPAD_UP && operatorStick.getRawAxis(AXIS_LEFT_TRIGGER) > 0.2)
                ClimberManager.leftClimber.manualExtend();
            else if(operatorStick.getPOV() == DPAD_DOWN && operatorStick.getRawAxis(AXIS_RIGHT_TRIGGER) > 0.2)
                ClimberManager.rightClimber.manualPull();
            else if(operatorStick.getPOV() == DPAD_UP && operatorStick.getRawAxis(AXIS_RIGHT_TRIGGER) > 0.2)
                ClimberManager.rightClimber.manualExtend();
            else 
                ClimberManager.brake();
            
        }

        //=====INTAKE======

        //=====SHOOTER=====

        if(operatorStick.getRawButtonPressed(BUTTON_LEFT_BUMPER)){
            currentShooterMode = ShooterMode.SPEAKER_MANUAL;
        }

        if(operatorStick.getRawButtonPressed(BUTTON_RIGHT_BUMPER)){
            currentShooterMode = ShooterMode.AMP;
        } else {
            currentShooterMode = ShooterMode.SPEAKER_MANUAL;
        }
    }
}