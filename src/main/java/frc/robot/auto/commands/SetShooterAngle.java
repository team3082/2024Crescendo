package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.Vector2;

public class SetShooterAngle extends Command {

    frc.robot.auto.autoframe.SetShooterAngle setShooterAngle;

    public SetShooterAngle(double angle) {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(angle);
    }

    public SetShooterAngle(Vector2 position) {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(position);
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
