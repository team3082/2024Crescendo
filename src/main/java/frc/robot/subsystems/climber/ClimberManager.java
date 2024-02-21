package frc.robot.subsystems.climber;

import static frc.robot.Constants.Climber.*;

public final class ClimberManager {
    
    private static Climber leftClimber;
    private static Climber rightClimber;

    private static boolean zeroing = true; // setting to zero at the start so that it zeroes at start of match
    private static boolean balancing = false;

    public static void init(){
        leftClimber = new Climber(LEFT_MOTOR_ID, LEFT_HALL_ID, LEFT_MOTOR_INVERTED);
        rightClimber = new Climber(RIGHT_MOTOR_ID, RIGHT_HALL_ID, RIGHT_MOTOR_INVERTED);
    }

    // THIS NEEDS TO RUN AFTER OPERATOR INPUT
    public static void update() {
        if (zeroing) {
            zeroing = !(zero());
        }
        else if (balancing) {
            balancing = !(balance());
        }
        else {
            brake();
        }
    }

    // raise the hooks unless the max extension has been reached
    public static void raiseHooks() {
        leftClimber.extend();
        rightClimber.extend();
    }

    // lower the hooks unless the magnet is tripped
    public static void pullHooks() { 
        leftClimber.pull();
        rightClimber.pull();
    }

    // are both the magnets tripped
    public static boolean trippedMagnet() {
        return (leftClimber.sensor.get() && rightClimber.sensor.get());
    }

    // stops moving
    public static void brake() {
        leftClimber.motor.neutralOutput();
        rightClimber.motor.neutralOutput();
    }

    // sets the climbers to be zeroed
    public static void setZero() {
        zeroing = true;
    }

    // zero the climbers
    public static boolean zero() {
        if (leftClimber.zeroMotor() && rightClimber.zeroMotor()) {
            return true;
        }
        else {
            return false;
        }
    }

    // set the robot to start balancing
    public static void setBalance(){
        balancing = true;
    } 

    // balance the robot on the chain
    public static boolean balance() {
        // TODO implement this (if needed)
        return true;
    }

}
