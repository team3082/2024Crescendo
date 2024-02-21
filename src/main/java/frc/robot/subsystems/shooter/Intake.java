package frc.robot.subsystems.shooter;

import static frc.robot.Constants.Intake.*;
import static frc.robot.Tuning.Intake.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import frc.robot.utils.Beambreak;

@SuppressWarnings("removal")
public final class Intake {
    
    private static TalonFX pivotMotor;
    private static TalonSRX topBeltMotor, bottomBeltMotor;
    public static CANSparkMax conveyorMotor;
    private static RelativeEncoder conveyorEncoder;
    private static SparkPIDController conveyorPIDController;

    private static Beambreak beambreak;

    private static IntakeState state = IntakeState.STOW;

    public static void init() {

        pivotMotor = new TalonFX(INTAKEPIVOT_ID);
        pivotMotor.configFactoryDefault();
        pivotMotor.setNeutralMode(NeutralMode.Brake);
        pivotMotor.configNominalOutputForward(0.01);
        pivotMotor.configNominalOutputReverse(0.01);
        pivotMotor.configNeutralDeadband(0.01);
        pivotMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
        
        pivotMotor.config_kP(0, INTAKEPIVOTKP, 30);
        pivotMotor.config_kI(0, INTAKEPIVOTKI, 30);
        pivotMotor.config_kD(0, INTAKEPIVOTKD, 30);

        pivotMotor.configMotionAcceleration(INTAKEPIVOTMAXACCEL);
        pivotMotor.configMotionCruiseVelocity(INTAKEPIVOTMAXACCEL);
        pivotMotor.configMotionSCurveStrength(INTAKEPIVOTJERKSTRENGTH);

        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        pivotMotor.configSupplyCurrentLimit(pivotCurrentLimit);
        pivotMotor.configVoltageCompSaturation(12.2);
        pivotMotor.enableVoltageCompensation(true);

        topBeltMotor = new TalonSRX(TOPINTAKE_ID);
        topBeltMotor.configFactoryDefault();
        topBeltMotor.setNeutralMode(NeutralMode.Coast);
        topBeltMotor.setInverted(false);
        topBeltMotor.config_kP(0, 0);
        topBeltMotor.config_kI(0, 0);
        topBeltMotor.config_kD(0, 0);
        topBeltMotor.configSupplyCurrentLimit(pivotCurrentLimit);
        topBeltMotor.configVoltageCompSaturation(12.2);
        topBeltMotor.enableVoltageCompensation(true);

        bottomBeltMotor = new TalonSRX(BOTTOMINTAKE_ID);
        bottomBeltMotor.configFactoryDefault();
        bottomBeltMotor.setNeutralMode(NeutralMode.Coast);
        bottomBeltMotor.setInverted(false);
        bottomBeltMotor.config_kP(0, 0);
        bottomBeltMotor.config_kI(0, 0);
        bottomBeltMotor.config_kD(0, 0);
        bottomBeltMotor.configSupplyCurrentLimit(pivotCurrentLimit);
        bottomBeltMotor.configVoltageCompSaturation(12.2);
        bottomBeltMotor.enableVoltageCompensation(true);

        conveyorMotor = new CANSparkMax(25, MotorType.kBrushless);
        conveyorMotor.setIdleMode(IdleMode.kCoast);
        conveyorMotor.setInverted(false);
        conveyorMotor.setSmartCurrentLimit(39);
        conveyorMotor.enableVoltageCompensation(11);

        RelativeEncoder conveyorEncoder = conveyorMotor.getEncoder();
        conveyorEncoder.setVelocityConversionFactor(42);

        conveyorPIDController = conveyorMotor.getPIDController();
        conveyorPIDController.setFeedbackDevice(conveyorEncoder);
        conveyorPIDController.setP(CONVEYORKP);
        conveyorPIDController.setI(CONVEYORKI);
        conveyorPIDController.setD(CONVEYORKD);

       // beambreak = new Beambreak(LASER_ID, 300);

       // Zero the Falcon's relative encoder LAST
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
        return angleRad * INTAKERATIO * 2048.0 / Math.PI / 2.0;
    }

    public static void update() {//TODO add logic for using the beambreak
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
        topBeltMotor.set(TalonSRXControlMode.Disabled, 0);
        bottomBeltMotor.set(TalonSRXControlMode.Disabled, 0);
        conveyorMotor.set(0);
    }

    private static void ground() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, GROUND_INTAKE_ANGLE);
        topBeltMotor.set(TalonSRXControlMode.PercentOutput, INTAKESTRENGTH);
        bottomBeltMotor.set(TalonSRXControlMode.PercentOutput, INTAKESTRENGTH);
        conveyorMotor.set(0);
    }

    public static void suck() {
        topBeltMotor.set(TalonSRXControlMode.PercentOutput, 1);
        bottomBeltMotor.set(TalonSRXControlMode.PercentOutput, 1);
    }

    public static void no() {
        topBeltMotor.set(TalonSRXControlMode.Disabled, 0);
        bottomBeltMotor.set(TalonSRXControlMode.Disabled, 0);
    }

    // NEVER CALL THIS ANYWHERE ELSE OTHER THAN SHOOTER
    private static void feed() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, INROBOT_INTAKE_ANGLE);
        topBeltMotor.set(TalonSRXControlMode.PercentOutput, FEEDSTRENGTH);
        bottomBeltMotor.set(TalonSRXControlMode.PercentOutput, FEEDSTRENGTH);
        conveyorMotor.set(FEEDSTRENGTH);
    }

    private static void source() {
        pivotMotor.set(TalonFXControlMode.MotionMagic, SOURCE_INTAKE_ANGLE);
        topBeltMotor.set(TalonSRXControlMode.PercentOutput, INTAKESTRENGTH);
        bottomBeltMotor.set(TalonSRXControlMode.PercentOutput, INTAKESTRENGTH);
        conveyorMotor.set(0);
    }

    public static boolean pieceGrabbed() {
        return true;
    }


    // public static void setState(IntakeState newState) {
        //state = newState;
    //}

    public static double getIntakeAngleRad() {
        return ticksToRad(pivotMotor.getSelectedSensorPosition());
    }

    public static enum IntakeState {
        STOW,
        FEED,
        GROUND,
        SOURCE,
        BALANCE
    }
}
