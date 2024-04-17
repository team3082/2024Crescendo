package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;

public class FireShooter extends ChickenCommand {

    public frc.robot.auto.autoframe.FireShooter fireShooter;

    public FireShooter(){
        fireShooter = new frc.robot.auto.autoframe.FireShooter();
    }

    @Override
    public void init() {
        isFinished=false;
        fireShooter.done=false;
        fireShooter.start();
    }

    @Override
    public void update() {
        fireShooter.update();
    }

    @Override
    public boolean isFinished() {
        return fireShooter.done;
    }

    @Override
    public void whenFinished(boolean interrupted) {
        // Shooter.disable();
        Shooter.revTo(4000);
        ShooterPivot.setPosition(Math.toRadians(30));
    }
}
