package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class FireShooter extends Command {

    public frc.robot.auto.autoframe.FireShooter fireShooter;

    public FireShooter(){
        fireShooter = new frc.robot.auto.autoframe.FireShooter();
    }

    @Override
    public void initialize() {
        fireShooter.start();
    }

    @Override
    public void execute() {
        fireShooter.update();
    }

    @Override
    public boolean isFinished() {
        return fireShooter.done;
    }
}
