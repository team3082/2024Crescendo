package frc.robot.auto.commands;

import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;

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
