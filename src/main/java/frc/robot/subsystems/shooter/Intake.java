package frc.robot.subsystems.shooter;

import static frc.robot.Constants.Intake.*;
import static frc.robot.Tuning.Intake.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import frc.robot.utils.Beambreak;
import frc.robot.utils.RTime;

@SuppressWarnings("removal")
public final class Intake {
    
    private static TalonFX pivotMotor;
    private static CANSparkMax topBeltMotor, bottomBeltMotor; // bottom = handoff too
    private static RelativeEncoder topEncoder, bottomEncoder;
    private static SparkPIDController topPID;
    public static SparkPIDController bottomPID;
    public static Beambreak beambreak;

    private static IntakeState state = IntakeState.STOW;

    public static void init() {

        pivotMotor = new TalonFX(INTAKEPIVOT_ID);
        pivotMotor.configFactoryDefault();
        pivotMotor.setNeutralMode(NeutralMode.Brake);
        pivotMotor.configNominalOutputForward(0.01);
        pivotMotor.configNominalOutputReverse(0.01);
        pivotMotor.configNeutralDeadband(0.01);
        pivotMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
        
        pivotMotor.config_kP(0, 0.1, 30);
        pivotMotor.config_kI(0, 0.0, 30);
        pivotMotor.config_kD(0, 0.02, 30);

        pivotMotor.configMotionAcceleration(40000);
        pivotMotor.configMotionCruiseVelocity(15000);
        pivotMotor.configMotionSCurveStrength(1);

        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        pivotMotor.configSupplyCurrentLimit(pivotCurrentLimit);
        pivotMotor.configVoltageCompSaturation(11.6);
        pivotMotor.enableVoltageCompensation(true);
        
        topBeltMotor = new CANSparkMax(31, MotorType.kBrushless);
        topBeltMotor.restoreFactoryDefaults();
        topBeltMotor.setIdleMode(IdleMode.kCoast);
        topBeltMotor.enableVoltageCompensation(11.6);
        topBeltMotor.setSmartCurrentLimit(30);
        topBeltMotor.setCANTimeout(30);

        topPID = topBeltMotor.getPIDController();
        topEncoder = topBeltMotor.getEncoder();

        topPID.setP(0.005);
        topPID.setI(0);
        topPID.setD(0.003);

        bottomBeltMotor = new CANSparkMax(30, MotorType.kBrushless);
        bottomBeltMotor.restoreFactoryDefaults();
        bottomBeltMotor.setIdleMode(IdleMode.kCoast);
        bottomBeltMotor.enableVoltageCompensation(11.8);
        bottomBeltMotor.setSmartCurrentLimit(35); // works harder than the top motor
        bottomBeltMotor.setCANTimeout(30);

        bottomPID = bottomBeltMotor.getPIDController();
        bottomEncoder = bottomBeltMotor.getEncoder();

        bottomPID.setP(0.005);
        bottomPID.setI(0);
        bottomPID.setD(0.001);

        topBeltMotor.burnFlash();
        bottomBeltMotor.burnFlash();

        beambreak = new Beambreak(LASER_ID, LASER_BREAK_DIST);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        topBeltMotor.burnFlash();
        bottomBeltMotor.burnFlash();

       beambreak = new Beambreak(LASER_ID, 300);

       // Zero the Falcon's relative encoder LAST
       // 0 points up
       pivotMotor.setSelectedSensorPosition(0);
    }

    public static void killHandoff() {
        Shooter.handoffLiveTime = 0;
        state = IntakeState.STOW;
    }

    private static double ticksToRad(double angleTicks) {
        return angleTicks / 2048.0 / INTAKERATIO * Math.PI * 2.0;
    }

    private static double radToTicks(double angleRad) {
        return (angleRad / (2.0 * Math.PI)) * INTAKERATIO * 2048.0;
    }

    public static void update() { //TODO add logic for using the beambreak
        switch (state) {
            case SOURCE:
                source();
            break;
            case STOW:
                stow();
            break;
            case GROUND:
                ground();
            break;
            case FEED:
                feed();
            break;
            case BALANCE:
                stow();
            break;
        }
    }

    private static void stow() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, INROBOT_INTAKE_ANGLE);
        topPID.setReference(0, ControlType.kDutyCycle);
        bottomPID.setReference(0, ControlType.kDutyCycle);
    }

    private static void ground() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, GROUND_INTAKE_ANGLE);
        topPID.setReference(-0.35, ControlType.kDutyCycle);
        bottomPID.setReference(-0.35, ControlType.kDutyCycle);
    }

    public enum SuckState {
        CONTINUE_SUCK,
        STOP_SUCK
    }

    public static SuckState suckState = SuckState.CONTINUE_SUCK;
    public static double suckTime = 0.0;
    public static boolean hasPiece;

    public static void suck() {

        if (beambreak.isBroken()) {
            hasPiece = true;
        } else if (!beambreak.isBroken()) {
            hasPiece = false;
        }

        // if it has the piece it can intake if it doesnt it cant
        if (!hasPiece) {
            topPID.setReference(-0.5, ControlType.kDutyCycle);
            bottomPID.setReference(-0.5, ControlType.kDutyCycle);
        } else {
            topPID.setReference(0.0, ControlType.kDutyCycle);
            bottomPID.setReference(0.0, ControlType.kDutyCycle);
        }
        // System.out.println(suckState.name());
    }

    public static void suck2() {
        topPID.setReference(-0.8, ControlType.kDutyCycle);
        bottomPID.setReference(-0.8, ControlType.kDutyCycle);
    }

    public static void no() {
        topPID.setReference(0, ControlType.kDutyCycle);
        bottomPID.setReference(0, ControlType.kDutyCycle);
    }

    // NEVER CALL THIS ANYWHERE ELSE OTHER THAN SHOOTER
    private static void feed() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, INROBOT_INTAKE_ANGLE);
        topPID.setReference(-0.35, ControlType.kDutyCycle);
        bottomPID.setReference(-0.35, ControlType.kDutyCycle);
    }

    private static void source() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, SOURCE_INTAKE_ANGLE);
        topPID.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);
        bottomPID.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);
    }

    public static boolean pieceGrabbed() {
        return beambreak.isBroken();
    }


    public static void setState(IntakeState newState) {
        state = newState;
    }

    public static double getIntakeAngleRad() {
        return ticksToRad(pivotMotor.getSelectedSensorPosition());
    }

    public static void enableCoast() {
        pivotMotor.setNeutralMode(NeutralMode.Coast);
    }

    public static void setCoast() {
        pivotMotor.neutralOutput();
    }

    public static enum IntakeState {
        STOW,
        FEED,
        GROUND,
        SOURCE,
        BALANCE
    }
}
