package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;

public class SetShooterVelocity extends ChickenCommand {

    frc.robot.auto.autoframe.SetShooterVelocity setShooterVelocity;

    public SetShooterVelocity(double velocity) {
        setShooterVelocity = new frc.robot.auto.autoframe.SetShooterVelocity(velocity);
    }

    @Override
    public void init() {
        isFinished=false;
        setShooterVelocity.done=false;
        setShooterVelocity.start();
    }

    @Override
    public void update(){
        setShooterVelocity.update();
    }

    @Override
    public boolean isFinished() {
        return setShooterVelocity.done;
    }
}
