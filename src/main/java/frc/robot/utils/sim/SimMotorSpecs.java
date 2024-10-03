package frc.robot.utils.sim;

public class SimMotorSpecs {
    // Motor specs
    public int ticksPerRot;
    public double maxVelocity;
    public double stallTorque;
    public double maxAcceleration;

    public SimMotorSpecs() {
        ticksPerRot = 0;
        maxVelocity = 0;
        stallTorque = 0;
        maxAcceleration = 0;
    }
}
