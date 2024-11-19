package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.StaticBrake;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.tuning.Constants;
import frc.robot.tuning.Tuning;

public class Intake {

    public enum IntakeState {
        OFF,
        INTAKE, // intakes a piece
        DOWN, // intake down but motors not running
        FEED, // runs handoff
        EJECT,
    }

    private enum IntakePosition {
        UP, 
        DOWN
    }

    private static IntakeState handoffState;
    private static IntakePosition intakePosition;

    private static TalonFX pivotMotor;
    private static TalonFX indexMotor;
    private static CANSparkMax topBeltMotor, bottomBeltMotor;
    private static DigitalInput beambreak;

    /**
     * Initializes the intake and handoff
     */
    public static void init() {

        pivotMotor = new TalonFX(Constants.Intake.PIVOT_ID, "CANivore");
        indexMotor = new TalonFX(Constants.Intake.INDEX_ID, "CANivore");

        pivotMotor.getConfigurator().apply(new TalonFXConfiguration());
        indexMotor.getConfigurator().apply(new TalonFXConfiguration());

        TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
        pivotConfiguration.Slot0.kP = Tuning.Intake.PIVOT_P;
        pivotConfiguration.Slot0.kI = Tuning.Intake.PIVOT_I;
        pivotConfiguration.Slot0.kD = Tuning.Intake.PIVOT_D;

        pivotMotor.getConfigurator().apply(pivotConfiguration);
        pivotMotor.setControl(new StaticBrake());

        topBeltMotor = new CANSparkMax(Constants.Intake.BELT_TOP_ID, MotorType.kBrushless);
        bottomBeltMotor = new CANSparkMax(Constants.Intake.BELT_BOTTOM_ID, MotorType.kBrushless);

        beambreak = new DigitalInput(Constants.Intake.BEAMBREAK_ID);

        handoffState = IntakeState.OFF;
        intakePosition = IntakePosition.UP;
    }

    /**
     * Updates the motors of intake and handoff
     */
    public static void update() {
        switch (handoffState) {
            case OFF:
                setIndexMotor(0);
                setIntakePosition(IntakePosition.UP);
                setIntakeMotors(0);
            break;
            
            case INTAKE:
                setIntakePosition(IntakePosition.DOWN);
                intakeUntilBroken();
                // TODO add logic to make this intake a piece and not feed it all the way through the intake
            break;

            case DOWN:
                setIntakePosition(IntakePosition.DOWN);
                setIndexMotor(0);
                setIntakeMotors(0);
            break;

            case FEED:
                setIntakePosition(IntakePosition.UP);
                setIndexMotor(Tuning.Intake.HANDOFF_SPEED);
            break;

            case EJECT:
                setIndexMotor(-Tuning.Intake.HANDOFF_SPEED);
                setIntakeMotors(-Tuning.Intake.INTAKE_SPEED);
            break;
        
            default: break;
        }

        switch (intakePosition) {
            case UP:
                setPivotAngle(Tuning.Intake.PIVOT_UP_ANGLE);
            break;

            case DOWN:
                setPivotAngle(Tuning.Intake.PIVOT_DOWN_ANGLE);
            break;
        
            default: break;
        }
    }

    public static void disable() {
        // TODO Auto-generated method stub
        pivotMotor.setControl(new CoastOut());
    }

    /**
     * Sets the desired intake state
     * @param newHandoffState The intake state to set
     */
    public static void setHandoffState(IntakeState newHandoffState) {
        handoffState = newHandoffState;
    }

    /**
     * Sets the desired intake position
     * @param newIntakePosition Intake position to set
     */
    private static void setIntakePosition(IntakePosition newIntakePosition) {
        intakePosition = newIntakePosition;
    }

    /**
     * Sets the pivot motor to go to an angle
     * @param pivotAngle Target angle for the pivot motor
     */
    private static void setPivotAngle(double pivotAngle) {
        pivotMotor.setControl(new PositionDutyCycle(pivotAngle));
    }

    /**
     * Sets the index motor to a given velocity
     * @param velocity Target velocity for index motor
     */
    private static void setIndexMotor(double velocity) {
        indexMotor.set(velocity);
    }

    /**
     * Sets the intake motors to target velocity
     * @param velocity Target velocity
     */
    private static void setIntakeMotors(double velocity) { //TODO something may need to be inverted here
        topBeltMotor.set(-velocity);
        bottomBeltMotor.set(-velocity);
    }

    /**
     * Intakes until a piece is detected in the intake
     */
    private static void intakeUntilBroken() {
        if(beambreakBroken()) {
            setIntakeMotors(0);
            setIndexMotor(0);
        } else {
            setIntakeMotors(Tuning.Intake.INTAKE_SPEED);
            setIndexMotor(Tuning.Intake.HANDOFF_SPEED);
            
        }
    }

    // Lasercan functions

    /**
     * Checks if a note is in the intake
     * @return If a note is in the intake or not
     */
    public static boolean beambreakBroken() {
        System.out.println(!beambreak.get());
        return !beambreak.get();
    }
    


}
