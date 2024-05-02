package frc.robot.auto.commands;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;

public class StowShooter extends ChickenCommand{
    frc.robot.auto.autoframe.SetShooterAngle setShooterAngle;

    public StowShooter() {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(Math.toRadians(30.0));
    }

    @Override
    public void init() {
        isFinished=false;
        setShooterAngle.done=false;
        setShooterAngle.start();
    }

    @Override
    public void update(){
        setShooterAngle.update();
    }

    @Override
    public boolean isFinished() {
        return setShooterAngle.done;
    }
}
