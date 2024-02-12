package frc.robot.subsystems.shooter;

import static frc.robot.Constants.Intake.*;
import static frc.robot.Tuning.Intake.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.revrobotics.CANSparkMax;
import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Robot;
import frc.robot.utils.Beambreak;

@SuppressWarnings("removal")
public final class Intake {
    
    private static TalonFX pivotMotor;
    private static CANCoder absEncoder;
    private static CANSparkMax topBeltMotor, bottomBeltMotor, conveyorMotor;
    private static SparkPIDController topPIDController, bottomPIDController, conveyorPIDController;
    private static Beambreak beambreak;

    private static IntakeState state = IntakeState.STOW;


    public static void init() {
        absEncoder = new CANCoder(INTAKEPIVOT_ID);
        absEncoder.configFactoryDefault();
        absEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);

        pivotMotor = new TalonFX(INTAKEPIVOT_ID);
        pivotMotor.configFactoryDefault();
        pivotMotor.setNeutralMode(NeutralMode.Brake);
        pivotMotor.configNominalOutputForward(0.01);
        pivotMotor.configNominalOutputReverse(0.01);
        pivotMotor.configNeutralDeadband(0.01);
        pivotMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);

        double absPosition = (absEncoder.getAbsolutePosition() + INTAKE_OFFSET) / 360.0 * INTAKERATIO * 2048;
        pivotMotor.setSelectedSensorPosition(absPosition, 0, 30);
        
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
        
        topBeltMotor = new CANSparkMax(TOPINTAKE_ID, MotorType.kBrushless);
        topBeltMotor.setIdleMode(IdleMode.kCoast);//TODO may want to set this to brake, depends on how motor.stopMotor() works
        topBeltMotor.setInverted(false);
        topBeltMotor.setSmartCurrentLimit(39);
        topBeltMotor.enableVoltageCompensation(11);

        RelativeEncoder topEncoder = topBeltMotor.getEncoder();
        topEncoder.setVelocityConversionFactor(42);

        topPIDController = topBeltMotor.getPIDController();
        topPIDController.setFeedbackDevice(topEncoder);
        topPIDController.setP(INTAKEBELTKP);
        topPIDController.setI(INTAKEBELTKI);
        topPIDController.setD(INTAKEBELTKD);
        

        bottomBeltMotor = new CANSparkMax(BOTTOMINTAKE_ID, MotorType.kBrushless);
        bottomBeltMotor.setIdleMode(IdleMode.kCoast);//TODO may want to set this to brake, depends on how stop motor works
        bottomBeltMotor.setInverted(false);
        bottomBeltMotor.setSmartCurrentLimit(39);
        bottomBeltMotor.enableVoltageCompensation(11);

        RelativeEncoder bottomEncoder = bottomBeltMotor.getEncoder();
        bottomEncoder.setVelocityConversionFactor(42);

        bottomPIDController = bottomBeltMotor.getPIDController();
        bottomPIDController.setFeedbackDevice(bottomEncoder);
        bottomPIDController.setP(INTAKEBELTKP);
        bottomPIDController.setI(INTAKEBELTKI);
        bottomPIDController.setD(INTAKEBELTKD);

        conveyorMotor = new CANSparkMax(INDEXER_ID, MotorType.kBrushless);
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

        beambreak = new Beambreak(LASER_ID, 300);

    }

    private static double ticksToRad(double angleTicks){
        return angleTicks / 2048 / INTAKERATIO * Math.PI * 2;
    }

    private static double radToTicks(double angleRad){
        return angleRad * INTAKERATIO * 2048 / Math.PI / 2;
    }

    public static void update() {//TODO add logic for using the beambreak
        switch(state){
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
                stow();//didn't feel like making anything fancy
                break;
        }
    }



    private static void stow(){
        pivotMotor.set(ControlMode.MotionMagic, radToTicks(INROBOT_INTAKE_ANGLE));
        topBeltMotor.stopMotor();
        bottomBeltMotor.stopMotor();
        conveyorMotor.stopMotor();
    }

    private static void ground(){
        pivotMotor.set(ControlMode.MotionMagic, radToTicks(GROUND_INTAKE_ANGLE));
        topPIDController.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);//TODO currently have everything running on duty cycle, may want to switch to velocity control
        bottomPIDController.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);
        conveyorMotor.stopMotor();
    }

    private static void feed(){
        pivotMotor.set(ControlMode.MotionMagic, radToTicks(INROBOT_INTAKE_ANGLE));
        topPIDController.setReference(FEEDSTRENGTH, ControlType.kDutyCycle);
        bottomPIDController.setReference(FEEDSTRENGTH, ControlType.kDutyCycle);
        conveyorPIDController.setReference(FEEDSTRENGTH, ControlType.kDutyCycle);
    }

    private static void source(){
        pivotMotor.set(ControlMode.MotionMagic, radToTicks(SOURCE_INTAKE_ANGLE));
        topPIDController.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);
        bottomPIDController.setReference(INTAKESTRENGTH, ControlType.kDutyCycle);
        conveyorMotor.stopMotor();
    }

    public static boolean pieceGrabbed(){
        return beambreak.isBroken();
    }


    public static void setState(IntakeState newState){
        state = newState;
    }

    public static double getIntakeAngleRad(){
        if(Robot.isReal()){
            return ticksToRad(pivotMotor.getSelectedSensorPosition());
        }
        return 0.0;
    }

    public static enum IntakeState {
        STOW,
        FEED,
        GROUND,
        SOURCE,
        BALANCE
    }
}
