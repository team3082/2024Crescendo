package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;


public class StowShooter extends SetShooterAngle{
    Command setShooterAngle;

    public StowShooter() {
        super(Math.toRadians(30.0));
    }
}
