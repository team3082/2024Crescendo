package frc.robot.subsystems.climber;

import static frc.robot.Constants.Climber.*;

import frc.robot.subsystems.climber.Climber.ClimberControlState;

public final class ClimberManager {
    
    private static Climber leftClimber;
    private static Climber rightClimber;

    public static void init(){
        leftClimber = new Climber(LEFT_MOTOR_ID, LEFT_HALL_ID, LEFT_MOTOR_INVERTED);
        rightClimber = new Climber(RIGHT_MOTOR_ID, RIGHT_HALL_ID, RIGHT_MOTOR_INVERTED);
    }

    // THIS NEEDS TO RUN AFTER OPERATOR INPUT
    public static void update() {
        leftClimber.update();
        rightClimber.update();
    }

    // raise the hooks unless the max extension has been reached
    public static void manualExtend() {
        leftClimber.climberControlState = ClimberControlState.MANUAL_EXTENDING;
        rightClimber.climberControlState = ClimberControlState.MANUAL_EXTENDING;
        
    }

    // lower the hooks unless the magnet is tripped
    public static void manualPull() { 
        leftClimber.climberControlState = ClimberControlState.MANUAL_PULLING;
        rightClimber.climberControlState = ClimberControlState.MANUAL_PULLING;
    }

    // raise the hooks unless the max extension has been reached
    public static void autoExtend() {
        leftClimber.climberControlState = ClimberControlState.AUTO_EXTENDING;
        rightClimber.climberControlState = ClimberControlState.AUTO_EXTENDING;
        
    }

    // lower the hooks unless the magnet is tripped
    public static void autoPull() { 
        leftClimber.climberControlState = ClimberControlState.AUTO_PULLING;
        rightClimber.climberControlState = ClimberControlState.AUTO_PULLING;
    }

    // zero the climbers
    public static void zero() {
        leftClimber.hasBeenZeroed = false;
        rightClimber.hasBeenZeroed = false;
        leftClimber.climberControlState = ClimberControlState.ZEROING;
        rightClimber.climberControlState = ClimberControlState.ZEROING;
    }

    // stops moving
    public static void brake() {
        leftClimber.motor.neutralOutput();
        rightClimber.motor.neutralOutput();
    }
}
