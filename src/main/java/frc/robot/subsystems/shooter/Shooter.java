package frc.robot.subsystems.shooter;

import static frc.robot.Constants.ShooterConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

@SuppressWarnings("removal")
public final class Shooter {

    // Status of the shooter
    public static enum ShooterStatus {
        DISABLED, // aka a dead shooter
        REVVING, // actively revving up to our target velocity
        FIRING, // handoff pumping note into the shooter
        EJECT  // force-ejecting piece, regardless of our current status
    }

    // Status of the handoff
    public static enum HandoffStatus {
        DISABLED, // lying in wait until at velocity
        FEED,    // actively feeding piece to shooter
        EJECT,  // rejecting piece through intake
        STOP   // aka a dead handoff
    }

    public static ShooterStatus shooterMode;
    public static HandoffStatus handoffMode;

    public static TalonFX topMotor, bottomMotor;

    // Target RPM
    public static double targetVelocity, simVel;
    public static double targetTop, targetBottom;

    // Motors' measured RPMs
    public static double topRPM, bottomRPM;

    public static double temp;

    public static boolean varied = false;

    public static double handoffLiveTime = 0.0;

    public static final double deadband = 50.0;
    
    public static void init() {
        ShooterPivot.init();

        topMotor = new TalonFX(TOPFLYWHEEL_ID, "CANivore");
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID, "CANivore");

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 0.5;
        config.openloopRamp = 0.5;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        topMotor.config_kP(0, 0.28);
        topMotor.config_kI(0, 0.00017);
        topMotor.configMaxIntegralAccumulator(0, 1500);
        topMotor.config_kD(0, 0);
        topMotor.config_kF(0, 1023.0 * 0.698 / 12185.0);

        bottomMotor.config_kP(0, 0.265);
        bottomMotor.config_kI(0, 0.00017);
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
        shooterMode = ShooterStatus.DISABLED;
        handoffMode = HandoffStatus.DISABLED;
    }

    public static void update() {
        // Update our pivot & intake
        ShooterPivot.update();
        Intake.update();

        // Get our vars
        topRPM = topMotor.getSelectedSensorVelocity() * VelToRPM;
        bottomRPM = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        temp = topMotor.getTemperature();

        boolean atVelocity = canShoot();

        switch (shooterMode) {
            case FIRING:
                Intake.setState(IntakeState.FEED);
                if (atVelocity) {
                    Intake.runHandoff(); // fire if we are ready
                } else { // otherwise keep revvin till we are
                    if (varied)
                        setVariedVelocity(targetTop, targetBottom);
                    else
                        setVelocity(targetVelocity);
                }
            break;

            case REVVING:
                Intake.setState(IntakeState.FEED);
                // Rev the flywheel up to our set velocity
                if (varied)
                    setVariedVelocity(targetTop, targetBottom);
                else
                    setVelocity(targetVelocity);
            break;

            case EJECT:
                // Run the shooter forward, and the handoff/intake backwards.
                topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
                bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
            break;

            case DISABLED:
                topMotor.set(TalonFXControlMode.Disabled, 0.0);
                bottomMotor.set(TalonFXControlMode.Disabled, 0.0);
                targetVelocity = 0.0;
            break;
        }
    }

    /**
     * Set the desired velocity for our shooter to maintain.
     * @param newVelocity Velocity in RPM
     */
    private static void setVelocity(double newVelocity) {
        varied = false;
        targetVelocity = newVelocity;
        simVel = newVelocity;
        topMotor.set(TalonFXControlMode.Velocity, targetVelocity);
        bottomMotor.set(TalonFXControlMode.Velocity, targetVelocity);
    }

    /**
     * Set individual velocities for each of the motors.
     * Allows us to vector the wheels for amp/trap scoring.
     */
    private static void setVariedVelocity(double topSpeed, double bottomSpeed) {
        varied = true;
        targetTop = topSpeed;
        targetBottom = bottomSpeed;
        topMotor.set(TalonFXControlMode.Velocity, targetTop);
        bottomMotor.set(TalonFXControlMode.Velocity, targetBottom);
    }

    /**
     * Set the shooter's angle depending on our position on the field.
     * @param ftPos Distance from the target
     */
    public static void setShooterForDist(double ftPos) {
        double targetHeight = 6.5;
        final double g = 32.1740485564;

        double noteVel = 0; // Initial velocity of the note

        double shooterAngle = Math.atan(Math.pow(noteVel, 2) / (g * ftPos) - Math.sqrt((Math.pow(noteVel, 2) * (Math.pow(noteVel, 2) - 2 * g * targetHeight)) / (Math.pow(g, 2) * Math.pow(ftPos, 2)) - 1)); // terrible! ew! 🤢 (DO NOT CHANGE)
        // ShooterPivot.setPosition(shooterAngle);
    }

    /**
     * returns the desired shooter angle swerve angle and flywheel speed to make a shot from the given position
     * vel should be in inches per second
     * @return [shooter angle, shooter speed, swerve angle]
     */
    public static double[] getDesiredShooterPos(Vector2 pos, Vector2 vel){
        double g = 386.08858267717;
        //desired exit velocities of the note
        double dz = Math.sqrt(2 * g * (TARGETHEIGHT - SHOOTERPOSZ));
        double dx;
        try{
            dx = g * (((DriverStation.getAlliance().get() == Alliance.Blue) ? -targetX : targetX) - pos.x) / dz;
        } catch(Exception e){
            dx = g *  (-targetX - pos.x) / dz;
        }

        double dy = g * (targetY - pos.y) / dz;

        double shooterdx = dx - vel.x;
        double shooterdy = dy - vel.y;
        
        double shooterAngle = Math.atan2(dz, Math.sqrt(Math.pow(shooterdx,2) + Math.pow(shooterdy, 2)));
        double shooterSpeed = Math.sqrt(Math.pow(dz,2) + Math.pow(shooterdx,2) + Math.pow(shooterdy, 2));
        double swerveAngle = Math.atan2(shooterdy,shooterdx);
        return new double[] {shooterAngle, shooterSpeed, swerveAngle};
    }

    public static void setShooterAngleForSpeaker() {
         //rotates the pivot motor to the desired angle for the current position
        double dist;
        Vector2 speakerPos;

        if (RobotBase.isSimulation() ||  DriverStation.getAlliance().get() == Alliance.Blue)
            speakerPos = new Vector2(56.78, 327.1);
        else
            speakerPos = new Vector2(56.78, -327.13);

        dist = Math.hypot(SwervePosition.getPosition().x - speakerPos.x, SwervePosition.getPosition().y - speakerPos.y);
        // setShooterForDist(dist);
    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revTo(double rpm) {
        targetVelocity = rpm * RPMToVel;
        shooterMode = ShooterStatus.REVVING;
    }

    public static void setIntakeMode(HandoffStatus status) {
        handoffMode = status;
    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revToVaried(double top, double bottom) {
        targetTop = top * RPMToVel;
        targetBottom = bottom * RPMToVel;
        shooterMode = ShooterStatus.REVVING;
    }

    /**
     * Ejects the gamepiece if the drivetrain, arm 
     * and wheels are at the proper position & velocity.
     * This method should be called after the revTo() method.
     */
    public static void shoot() { 
        shooterMode = ShooterStatus.FIRING;
    }

    /**
     * Shoots the gamepiece regardless of whether 
     * or not the arm and wheels are ready.
     */
    public static void forceShoot() { 
        shooterMode = ShooterStatus.EJECT;
    }

    /**
     * Disable the shooter & handoff.
     */
    public static void disable() {
        shooterMode = ShooterStatus.DISABLED;
        handoffMode = HandoffStatus.DISABLED;
    }

    /**
     * Eject the shooter
     */
    public static void eject() {
        shooterMode = ShooterStatus.EJECT;
    }

    /**
     * Can we fire a gamepiece?
     * Checks the top flywheel first.
     */
    public static boolean canShoot() {
        double top = topMotor.getSelectedSensorVelocity() * VelToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        double err = Math.abs(top - targetVelocity * VelToRPM);
        double err2 = Math.abs(bottom - targetVelocity * VelToRPM);
        return err <= deadband && err2 <= deadband;
    }

    /**
     * Is the shooter currently revving up?
     */
    public static boolean revving() {
        return shooterMode == ShooterStatus.REVVING;
    }

    /**
     * Is the shooter currently trying to fire?
     */
    public static boolean firing() {
        return shooterMode == ShooterStatus.FIRING;
    }
}