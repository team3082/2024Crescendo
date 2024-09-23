package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.*;
import static frc.robot.configs.Constants.ShooterConstants.*;

import java.util.Optional;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import frc.robot.OI.ShooterMode;
import frc.robot.configs.ShooterSettings;
import frc.robot.subsystems.sensors.Telemetry;
import frc.robot.subsystems.sensors.VisionManager;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

@SuppressWarnings("removal")
public final class Shooter {
    // Tracks current status of the shooter for telemetry
    public static enum ShooterStatus {
        FIRING,
        REVVING,
        IDLE
    }

    // Tracks the current status of shooter aiming to the target
    public static enum AimStatus {
        AIMED,
        AIMING_APRILTAG_2D,
        AIMING_PIGEON,
        NO_AIMING
    }

    // Tracks current state of the shooter for telemetry
    public static enum ShooterState {
        FIRE_APRILTAG_2D,
        FIRE_SUBWOOFER,
        FIRE_AMP,
        FIRE_TUNING,
        IDLE,
        DISABLED
    }

    public static ShooterStatus shooterStatus;
    public static AimStatus aimStatus;
    public static ShooterState shooterState;

    // Motors
    public static TalonFX topMotor, bottomMotor;

    // Target RPM
    public static double targetVelocity, simVel;
    public static double targetTop, targetBottom;

    // Target Radians
    public static double targetAngle;

    // Motors' measured RPMs
    public static double topRPM, bottomRPM;

    public static double temp;

    public static double handoffLiveTime = 0.0;

    // Shooter velocity deadband RPM
    public static final double deadband = 50.0;
    
    public static void init() {
        ShooterPivot.init();

        topMotor = new TalonFX(TOPFLYWHEEL_ID, "CANivore");
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID, "CANivore");

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 0.25;
        config.openloopRamp = 0.25;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Brake);
        bottomMotor.setNeutralMode(NeutralMode.Brake);

        topMotor.config_kP(0, 0.2);
        topMotor.config_kI(0, 0.000);
        topMotor.configMaxIntegralAccumulator(0, 1500);
        topMotor.config_kD(0, 0);
        topMotor.config_kF(0, 1023.0 * 0.698 / 12185.0);


        bottomMotor.config_kP(0, 0.2);
        bottomMotor.config_kI(0, 0.000);
        bottomMotor.configMaxIntegralAccumulator(0, 1000);
        bottomMotor.config_kD(0, 0);
        bottomMotor.config_kF(0, 1023.0 * 0.7 / 12121.0);

        topMotor.configVoltageCompSaturation(10);
        bottomMotor.configVoltageCompSaturation(10);
        topMotor.enableVoltageCompensation(true);
        bottomMotor.enableVoltageCompensation(true);

        // Zero vars
        targetVelocity = 0.0;
        targetTop = 0.0;
        targetBottom = 0.0;

        topRPM = 0.0;
        bottomRPM = 0.0;
        handoffLiveTime = 0.0;

        temp = topMotor.getTemperature();
    }

    public static void update() {
        // log statuses/state
        /* code here */

        // set shooter pivot
        ShooterPivot.setPosition(targetAngle);

        // set shooter velocities
        topMotor.set(TalonFXControlMode.Velocity, targetTop);
        bottomMotor.set(TalonFXControlMode.Velocity, targetBottom);

        // update shooter pivot
        ShooterPivot.update();
    }

    /** 
     * point towards the apriltag with 2d Data 
     * set shooter angle based on interpolating table result
     * rev to 4000 rpm
     * fire piece
     */
    public static void fireApriltag2D() {
        final double rpm = 4000.0; // if neccessary change this to use a table as well
        Optional<Double> shooterAngle = VisionManager.getShooterAngle();
        boolean aimed = VisionManager.rotateToTarget2D();

        // sets shooter state
        shooterState = ShooterState.FIRE_APRILTAG_2D;

        // set aim status
        if (aimed) {
            aimStatus = AimStatus.AIMED;
        } else {
            aimStatus= AimStatus.AIMING_APRILTAG_2D;
        }

        // set targetAngle
        if (shooterAngle.isPresent()) {
            targetAngle = shooterAngle.get();
        }

        // set shooter velocitied
        revTo(rpm);
        // set shooter status
        shooterStatus = ShooterStatus.REVVING;

        // if it is aimed fully the shooter angle has been met, and the 
        // wheel velocities have been met then fire
        if (aimed && ShooterPivot.atPos() && canShoot()) {
            shooterStatus = ShooterStatus.FIRING;
            Intake.runHandoff(); // handoff piece to shooter
        } else {
            Intake.no();
        }
    }

    /**
     * turn to and fire at Amp using constant values from Tuning.java
     */
    public static void fireAmp() {
        // aim to amp
        // set velocities
        // set angle
        // check then shoot
        /* code here */
    }


    /**
     * fire at Subwoofer using constant values from Tuning.java
     */
    public static void fireSubwoofer() {
        // set velocities
        // set angle
        // check then shoot
        /* code here */
    }

    /**
     * fire using manual values from dashboard for tuning
     * enabled this mode in dashboard
     */
    public static void fireTuning() {
        // get values from dashboard
        // set velocities
        // set angle
        // check then shoot
        /* code here */
    }

    public static void forceFire() {
        Intake.runHandoff();
    }

    /**
     * set velocities from Tuning.java
     * set down pivot then disable
     */
    public static void idle() {

    }

    /**
     * set velocities to 0
     * set down pivot then disable
     */
    public static void disable() {

    }

    /**
     * run on disabled init
     * turn off flywheel motors
     * turn off pivot motors
     */
    public static void off() {

    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revTo(double rpm) {
        targetTop = rpm * RPMToVel;
        targetBottom = rpm * RPMToVel;
    }

    /**
     * Rev the shooter to 2 specified RPMs.
     */
    public static void revTo(double top, double bottom) {
        targetTop = top * RPMToVel;
        targetBottom = bottom * RPMToVel;
    }

    /**
     * Returns whether or not we can fire the shooter by 
     * checking the velocities of both wheels.
     */
    public static boolean canShoot() {
        double top = topMotor.getSelectedSensorVelocity() * VelToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        double err = Math.abs(top - targetTop * VelToRPM);
        double err2 = Math.abs(bottom - targetBottom * VelToRPM);

        return err <= deadband && err2 <= deadband;
    }
}