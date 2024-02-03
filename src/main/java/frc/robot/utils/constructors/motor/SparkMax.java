package frc.robot.utils.constructors.motor;

public class SparkMax extends Motor{
    public SparkMax(int id, boolean flipped, boolean brake, double mechanismRatio, double kP, double kI, double kD, double kF, double deadband, boolean wrapping) {
        super(id, flipped, brake, mechanismRatio, kP, kI, kD, kF, deadband, wrapping);


    }

    public void set(MotorControlType motorControlType, double input) {
        
    }

    public void setEncoderPosition(PositionType positionType, double input) {
        
    }

    public double getPosition(PositionType positionType) {
        return 0.0;
    }

    public double getVelocity() {
        return 0.0;
    }
}