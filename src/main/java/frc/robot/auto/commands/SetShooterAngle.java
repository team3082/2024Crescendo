package frc.robot.auto.commands;

import frc.robot.auto.ChickenAutoSystem.ChikenCommands.ChickenCommand;
import frc.robot.utils.Vector2;

public class SetShooterAngle extends ChickenCommand {

    frc.robot.auto.autoframe.SetShooterAngle setShooterAngle;

    public SetShooterAngle(double angle) {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(angle);
    }

    public SetShooterAngle(Vector2 position) {
        setShooterAngle = new frc.robot.auto.autoframe.SetShooterAngle(position);
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
