package frc.robot.utils.constructors.motor;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonFXP6 extends Motor {
    public TalonFX motor;

    public TalonFXP6(int id, boolean flipped, boolean brake, double mechanismRatio, double kP, double kI, double kD, double kF, double deadband, boolean wrapping) {
        super(id, flipped, brake, mechanismRatio, kP, kI, kD, kF, deadband, wrapping);
        motor = new TalonFX(id);

        // Factory Reset
        motor.getConfigurator().apply(new TalonFXConfiguration(), 0.050);

        // Set Configurations
        TalonFXConfiguration motorConfig = new TalonFXConfiguration();
        motorConfig.MotorOutput.Inverted = flipped ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive;
        motorConfig.MotorOutput.NeutralMode = brake ? NeutralModeValue.Brake : NeutralModeValue.Coast;
        motorConfig.Slot0.kP = kP;
        motorConfig.Slot0.kI = kI;
        motorConfig.Slot0.kD = kD;
        motorConfig.Slot0.kV = kF;
        motorConfig.MotorOutput.DutyCycleNeutralDeadband = deadband;
        motorConfig.Feedback.SensorToMechanismRatio = mechanismRatio;
        motorConfig.ClosedLoopGeneral.ContinuousWrap = wrapping;

        // Apply Settings
        motor.getConfigurator().apply(motorConfig, 0.050);

        System.out.println("Startup Complete for TalonFX (Phoenix 6) Motor ID: " + id + " Complete");
    }

    // motor control function
    public void set(MotorControlType motorControlType, double input) {
        switch (motorControlType) {
            case POSITION_ROTATIONS:
                motor.setControl(new PositionDutyCycle(input));
                break;

            case POSITION_RADIANS:
                motor.setControl(new PositionDutyCycle(input / (Math.PI * 2.0)));
                break;
                
            case POSITION_DEGREES:
                motor.setControl(new PositionDutyCycle(input / 360.0));
                break;

            case POSITION_TICKS:
                System.out.println("TalonFXP6: since phoenix 6 doesnt use ticks just dont use this(im talking to you ayman :))");
                break;

            case SPEED_PERCENT:
                motor.setControl(new DutyCycleOut(input));
                break;

            case SPEED_RPM:
                motor.setControl(new VelocityDutyCycle(input / 60.0));
                break;

            case SPEED_VELOCITY:
                System.out.println("gonna add some more stuff before i add this since it needs another initialized variable");
                break;
        
            default:
                break;
        }
    }

    // sets the internal encoder position based on your desired unit
    public void setEncoderPosition(PositionType positionType, double input) {
        switch (positionType) {
            case ROTATIONS:
                motor.setPosition(input, 0.050);
                break;

            case RADIANS:
                motor.setPosition(input / (2.0 * Math.PI), 0.050);
                break;

            case DEGREES:
                motor.setPosition(input / 360.0, 0.050);
                break;

            case TICKS:
                System.out.println("yeah ayman, i didnt add this feature in(how does that make you feel bud?)");
                break;
        
            default:
                break;
        }
    }

    // gets the position in the desired unit rotations, radians, or ticks :)
    public double getPosition(PositionType positionType) {
        switch (positionType) {
            case ROTATIONS:
                return motor.getPosition().refresh().getValueAsDouble();
        
            case RADIANS:
                return motor.getPosition().refresh().getValueAsDouble() * 2.0 * Math.PI;

            case DEGREES:
                return motor.getPosition().refresh().getValueAsDouble() * 360.0;

            case TICKS:
                System.out.println("typical ayman L trying to use ticks(AGAIN)");
                return 0.0;

            default:
                return 0.0;
        }
    }

    // returns the velocity it RPMs
    public double getVelocity() {
        return motor.getVelocity().refresh().getValueAsDouble() / 60;
    }
}
