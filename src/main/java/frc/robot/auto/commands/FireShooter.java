package frc.robot.auto.commands;

import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import static frc.robot.subsystems.shooter.Intake.IntakeState.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.RTime;

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
