package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class SetShooterVelocity extends Command {

    frc.robot.auto.autoframe.SetShooterVelocity setShooterVelocity;

    public SetShooterVelocity(double velocity) {
        setShooterVelocity = new frc.robot.auto.autoframe.SetShooterVelocity(velocity);
    }

    @Override
    public void initialize() {
        setShooterVelocity.start();
    }

    @Override
    public void execute(){
        setShooterVelocity.update();
    }

    @Override
    public boolean isFinished() {
        return setShooterVelocity.done;
    }
}
