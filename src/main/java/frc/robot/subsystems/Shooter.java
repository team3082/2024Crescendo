package frc.robot.subsystems;

import eggshell.constructors.absoluteEncoder.AbsoluteEncoder;
import eggshell.constructors.motor.Motor;
import eggshell.constructors.motor.MotorInterface.MotorControlType;
import eggshell.constructors.motor.MotorInterface.PositionType;

public class Shooter {
    private Motor topFlywheel, bottomFlywheel, pivot;
    private AbsoluteEncoder pivotEncoder;
    private double targetTopVelocity, targetBottomVelocity, targetAngle;

    public Shooter(Motor topFlywheel, Motor bottomFlywheel, Motor pivot, AbsoluteEncoder pivotEncoder) {
        this.topFlywheel = topFlywheel;
        this.bottomFlywheel = bottomFlywheel;
        this.pivot = pivot;
        this.pivotEncoder = pivotEncoder;
    }

    public void update() {
        this.topFlywheel.set(MotorControlType.SPEED_RPM, this.targetTopVelocity);
        this.bottomFlywheel.set(MotorControlType.SPEED_RPM, this.targetBottomVelocity);
        this.pivot.set(MotorControlType.POSITION_RADIANS, this.targetAngle);
    }

    public void setVelocity(double rpm) {
        this.targetTopVelocity = rpm;
        this.targetBottomVelocity = rpm;
    }

    public void setVelocity(double topRPM, double bottomRPM) {
        this.targetTopVelocity = topRPM;
        this.targetBottomVelocity = bottomRPM;
    }

    public void setAngle(double angle) {
        this.targetAngle = angle;
    }

    public void zeroAngle() {
        this.pivot.setEncoderPosition(PositionType.RADIANS, this.pivotEncoder.getPosition());
    }

    public boolean atAngle() {
        return true;
    }

    public boolean atVelocity() {
        return true;
    }

    public boolean canShoot() {
        return (atAngle() && atVelocity());
    }
}
