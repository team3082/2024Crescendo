package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import static frc.robot.Constants.Climber.*;
import edu.wpi.first.wpilibj.DigitalInput;

@SuppressWarnings("removal")
public class Climber {
    public enum ClimberMode {
        RETRACTED, // retract position, its in retracted position when hall sensr is activated
        BALANCING, // use pigeon to climb and keep the robot flat to the ground
        EXTENDED // goes to maximum retraction of the climber
    }

    TalonFX leftMotor;
    DigitalInput leftHallSensor;

    TalonFX rightMotor;
    DigitalInput rightHallSensor;

    public void init() {
        this.leftMotor = new TalonFX(LEFTWINCH_ID);
        this.leftHallSensor = new DigitalInput(LEFTHALL_ID); // DIO Pin ID

        this.rightMotor = new TalonFX(RIGHTWINCH_ID);
        this.rightHallSensor = new DigitalInput(RIGHTHALL_ID); // DIO Pin ID

        // TODO Falcon Setup
    }

    public void update() {

    }
}
