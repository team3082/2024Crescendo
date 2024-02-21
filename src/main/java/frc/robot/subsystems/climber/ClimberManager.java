package frc.robot.subsystems.climber;

import static frc.robot.Constants.Climber.*;

public final class ClimberManager {
    
    private static Climber leftClimber;
    private static Climber rightClimber;

    public static void init(){
        leftClimber = new Climber(LEFT_MOTOR_ID, LEFT_HALL_ID, LEFT_MOTOR_INVERTED);
        rightClimber = new Climber(RIGHT_MOTOR_ID, 0, RIGHT_MOTOR_INVERTED);
    }

    public static void stow(){
        leftClimber.stow();
        rightClimber.stow();
    }

    public static void raiseHooks() { }

    public static boolean trippedMagnet() {
        return (leftClimber.sensor.get() && rightClimber.sensor.get());
    }

    /**
     * lower the hooks while lifting the robot
     */
    public static void pullHooks() { 
        if (trippedMagnet()) {
            leftClimber.motor.neutralOutput();
            rightClimber.motor.neutralOutput();
        } else {
            leftClimber.setPosition(-0.2);
            rightClimber.setPosition(-0.2);
        }
    }

    public static void brake() {
        leftClimber.motor.neutralOutput();
        rightClimber.motor.neutralOutput();
    }


    /**
     * lower the hooks, without raising the robot
     */
    public static void lowerHooks() { }

    /**
     * raise the robot while keeping the drivetrain level
     */
    public static void balance(){ } 


}
