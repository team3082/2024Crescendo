package frc.robot.utils.constructors.motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

// DEPRECATE NEXT YEAR
@SuppressWarnings(value = { "removal" })
public class TalonFXP5 extends Motor{

    public static TalonFX motor;

    private final int ticksPerRotation = 2048;

    public TalonFXP5(int id, boolean flipped, boolean brake, double mechanismRatio, double kP, double kI, double kD, double kF, double deadband, boolean wrapping) {
        // store stuff and things
        super(id, flipped, brake, mechanismRatio, kP, kI, kD, kF, deadband, wrapping);
        
        // create motor class
        motor = new TalonFX(id);

        // set to factory defaults
        motor.configFactoryDefault(50);

        // set configurations
        motor.setInverted(flipped);
        motor.setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
		motor.config_kP(0, kP, 50);
		motor.config_kI(0, kI, 50);
		motor.config_kD(0, kD, 50);
        motor.config_kF(0, kF, 50);
        motor.configNeutralDeadband(deadband, 50);
        motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 50);

        System.out.println("Startup for TalonFX (Phoenix 5) Motor ID: " + id + " COMPLETE");
    }

    // input needs to be in ticks for this to work
    private double wrapping(double input) {
        double output;
        double currentPos = getPosition(PositionType.TICKS);

        if (Math.abs(input - currentPos / mechanismRatio) > ((2.0 * Math.PI) / 2.0)) {
            output =  (input - ((currentPos / mechanismRatio)) > 0.0) ? input - (ticksPerRotation * mechanismRatio) : input + (ticksPerRotation * mechanismRatio);
        }

        else { output = input; }

        return output;
    }

    // in phoenix 5 motor.set(ControlMode.Position) takes in ticks so this is sightly different from what it kinda normally is
    public void set(MotorControlType motorControlType, double input) {
        double output;
        switch (motorControlType) {
            case POSITION_ROTATIONS:
                output = (input) * this.ticksPerRotation;
                output = wrapping ? wrapping(input) : output;
                motor.set(ControlMode.Position, output);
                break; 

            case POSITION_DEGREES:
                output = (input / 360.0) * this.ticksPerRotation;
                output = wrapping ? wrapping(input) : output;
                motor.set(ControlMode.Position, output);
                break;
            
            case POSITION_RADIANS:
                output = (input / (2.0 * Math.PI)) * this.ticksPerRotation;
                output = wrapping ? wrapping(input) : output;
                motor.set(ControlMode.Position, output);
                break;

            case POSITION_TICKS:
                output = wrapping ? wrapping(input) : input;
                motor.set(ControlMode.Position, (input) * this.mechanismRatio);
                break;

            case SPEED_PERCENT:
                motor.set(ControlMode.PercentOutput, (input) * this.mechanismRatio);
                break;

            case SPEED_RPM:
                motor.set(ControlMode.Velocity, (input) * this.mechanismRatio);
                break;

            case SPEED_VELOCITY: // gotta change this one its not quite right lol
                motor.set(ControlMode.Velocity, (input) / this.mechanismRatio);
                break;
        
            default:
                break;
        }
    }

    public void setEncoderPosition(PositionType positionType, double input) {
        switch (positionType) {
            case ROTATIONS:
                motor.setSelectedSensorPosition((input) * this.ticksPerRotation * this.mechanismRatio);
                break;

            case RADIANS:
                motor.setSelectedSensorPosition((input / (2.0 * Math.PI)) * this.ticksPerRotation * this.mechanismRatio);
                break;

            case DEGREES:
                motor.setSelectedSensorPosition((input / 360.0) * this.ticksPerRotation * this.mechanismRatio);
                break;

            case TICKS:
                motor.setSelectedSensorPosition(input * this.mechanismRatio);
                break;

            default:
                break;
        }
    }

    public double getVelocity() {
        return motor.getSelectedSensorVelocity() * 10.0 * 60.0 * mechanismRatio;
    }

    public double getPosition(PositionType positionType) {
        switch (positionType) {
            case ROTATIONS:
                return motor.getSelectedSensorPosition() / 2048.0;

            case RADIANS:
                return (motor.getSelectedSensorPosition() / 2048.0) * (2.0 * Math.PI);

            case DEGREES:
                return (motor.getSelectedSensorPosition() / 2048.0) * 360.0;

            case TICKS:
                return motor.getSelectedSensorPosition();
        
            default:
                return 0.0;
        }
    }
}