package frc.robot.utils.constructors.motor;

public class Motor implements MotorInterface {
    public int id = 0;
    public boolean flipped = false;
    public boolean brake = false;
    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0;
    public double deadband = 0;
    public double mechanismRatio = 1;
    public boolean wrapping = false;

    public Motor(int id, boolean flipped, boolean brake, double mechanismRatio, double kP, double kI, double kD, double kF, double deadband, boolean wrapping) {
        this.id = id;
        this.flipped = flipped;
        this.brake = brake;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.deadband = deadband;
        this.mechanismRatio = mechanismRatio;
        this.wrapping = wrapping;
    }

    public void set(MotorControlType motorControlType, double input) {
        System.out.println("use one of the motor controller classes this base class is purely abstract");
    }

    public void setEncoderPosition(PositionType positionType, double input) {
        System.out.println("use one of the motor controller classes this base class is purely abstract");
    }

    public double getPosition(PositionType positionType) {
        System.out.println("use one of the motor controller classes this base class is purely abs'tract");
        return 0.0;
    }

    public double getVelocity() {
        System.out.println("use one of the motor controller classes this base class is purely abstract");
        return 0.0;
    }
}
