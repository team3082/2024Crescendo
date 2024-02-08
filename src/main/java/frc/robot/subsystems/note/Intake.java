package frc.robot.subsystems.note;

import static frc.robot.Constants.Intake.*;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.sensors.Beambreak;

@SuppressWarnings("removal")
public class Intake {

    public static enum IntakePosition {
        INROBOT,
        GROUND,
        SOURCE,
    }

    public static enum IntakeMode {
        STOPPED,
        INTAKE,
        HANDOFF,
    }

    public static CANSparkMax topBeltMotor;
    public static CANSparkMax bottomBeltMotor; 

    public static TalonFX pivotMotor;

    public static CANCoder absEncoder;

    public static Beambreak beamSensor;

    public static IntakeMode intakeMode;
    public static IntakePosition targetPosition;

    public static void init(){

        topBeltMotor = new CANSparkMax(TOPINTAKE_ID, MotorType.kBrushless);
        bottomBeltMotor = new CANSparkMax(BOTTOMINTAKE_ID, MotorType.kBrushless);
        pivotMotor = new TalonFX(INTAKEPIVOT_ID);
        beamSensor = new Beambreak(LASER_ID, 0);

        absEncoder = new CANCoder(INTAKEPIVOT_ID);
        absEncoder.configFactoryDefault();

        intakeMode = IntakeMode.STOPPED;
        targetPosition = IntakePosition.INROBOT;
    }

    public static void update() {
        switch(intakeMode) {
            case STOPPED:
                setIntakeVelocity(0.0);
                break;

            case INTAKE:
                break;

            case HANDOFF:
                setIntakeVelocity(INTAKE_HANDOFF_SPEED);
                break;

        }

        switch (targetPosition) {
            case INROBOT:
                pivotMotor.set(TalonFXControlMode.Position, INROBOT_INTAKE_ANGLE);
                break;

            case GROUND:
                pivotMotor.set(TalonFXControlMode.Position, GROUND_INTAKE_ANGLE);
                break;

            case SOURCE:
                pivotMotor.set(TalonFXControlMode.Position, SOURCE_INTAKE_ANGLE);
                break;
        }
        
    }

    public static void setIntakePosition(IntakePosition position) {
        targetPosition = position;
    }

    public static void setIntakeVelocity(double vel) {
        topBeltMotor.set(vel);
        bottomBeltMotor.set(-vel);
    }

    public static boolean pieceGrabbed() {
        return beamSensor.isBroken();
    }

}