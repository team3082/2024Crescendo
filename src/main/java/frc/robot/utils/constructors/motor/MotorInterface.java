package frc.robot.utils.constructors.motor;

public interface MotorInterface {
    public enum PositionalControlType {
        PIDF, // All with Internal Encoder
        MOTION_MAGIC, // TalonFX / TalonSRX
        SMART_MOTION // SparkMax / SparkFlex
    }

    public enum MotorControlType {
        POSITION_ROTATIONS,
        POSITION_DEGREES,
        POSITION_RADIANS,
        POSITION_TICKS,
        SPEED_PERCENT,
        SPEED_VELOCITY,
        SPEED_RPM
    }

    public enum PositionType {
        ROTATIONS,
        DEGREES,
        RADIANS,
        TICKS
    }

    public void set(MotorControlType motorControlType, double input);
    public void setEncoderPosition(PositionType positionType, double input);
    public double getPosition(PositionType positionType);
    public double getVelocity();
}
