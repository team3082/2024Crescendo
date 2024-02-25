package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class StowShooter extends Command{
    frc.robot.auto.autoframe.SetShooterAngle setShooterAngle;

    public StowShooter() {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(Math.toRadians(30.0));
    }

    @Override
    public void initialize() {
        setShooterAngle.start();
    }

    @Override
    public void execute(){
        setShooterAngle.update();
    }

    @Override
    public boolean isFinished() {
        return setShooterAngle.done;
    }
}
