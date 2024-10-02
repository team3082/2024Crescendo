package frc.robot.utils.sim;

public class SimMotorSpecs {
    // Motor specs
    public int ticksPerRot;
    public double maxRPM;
    public double stallTorque;

    public SimMotorSpecs() {
        ticksPerRot = 0;
        maxRPM = 0;
        stallTorque = 0;
    }
}
