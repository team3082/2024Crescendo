package frc.robot.subsystems;

import eggshell.constructors.limitswitch.LimitSwitch;
import eggshell.constructors.motor.Motor;

public class Climber {
    private Motor leftClimber, rightClimber;
    private LimitSwitch leftHallSensor, rightHallSensor;
    public Climber(Motor leftClimber, LimitSwitch leftHallSensor, Motor rightClimber, LimitSwitch rightHallSensor) {
        this.leftClimber = leftClimber;
        this.leftHallSensor = leftHallSensor;
        this.rightClimber = rightClimber;
        this.rightHallSensor = rightHallSensor;
    }

    public void zero() {

    }

    public void manualPull() {
        
    }

    public void manualExtend() {

    }

    public void autoPull() {

    }

    public void autoExtend() {

    }
}
