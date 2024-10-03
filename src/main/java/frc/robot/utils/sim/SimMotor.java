package frc.robot.utils.sim;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.utils.RTime;

public class SimMotor implements SimDevice {
    private SimMotorSpecs specs;
    private SimMotorConfiguration config;

    private double velocity; // RPM
    private double position; // ticks

    private double lastVelocity;
    private double lastPosition;

    private double targetVelocity;
    private double targetPosition;

    private ControlType controlType;

    private PIDController pidController;

    public SimMotor(SimMotorSpecs specs) {

    }

    // enum for different control types velocity, position, and duty cycle
    public enum ControlType {
        VELOCITY, POSITION, PERCENT_OUTPUT
    }

    // update function that takes the current velocity and calulates the new position base it on delta time
    public void update() {
        // check for control type and apply the correct control
        switch(controlType) {
            case VELOCITY:
                // apply velocity control based on target velocity, max acceleration, and delta time
                velocity += RTime.deltaTime() * Math.min(specs.maxAcceleration, Math.abs(targetVelocity - velocity));
                break;
            case POSITION:
                // apply position control
                // update pid controller target position, input current position to pidcontroller, and get output, then set velocity to output based on deltaTime and max accel
                
                break;
            case PERCENT_OUTPUT:
                // apply duty cycle control
                velocity += RTime.deltaTime() * Math.min(specs.maxAcceleration, Math.abs(targetVelocity - velocity));
                break;
        }
    }

    // function that takes in control type and a double value to do the selected control type
    public void set(ControlType _controlType, double value) {
        controlType = _controlType;
        switch(controlType) {
            case VELOCITY:
                targetVelocity = value;
                break;
            case POSITION:
                targetPosition = value;
                break;
            case PERCENT_OUTPUT:
                targetVelocity = value * specs.maxVelocity;
                break;
        }
    }

    public void apply() {

    }

    public double getPosition() {
        return position;
    }

    @Override
    public void getID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getID'");
    }

    @Override
    public void getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}