package frc.robot.auto.commands;

import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import static frc.robot.subsystems.shooter.Intake.IntakeState.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.RTime;

public class SetShoot extends Command {
    public boolean pieceIndexed = false;
    public double indexTime;

    public frc.robot.auto.autoframe.SetShoot setShoot;

    public SetShoot(){
        setShoot = new frc.robot.auto.autoframe.SetShoot();
    }

    @Override
    public void initialize() {
        setShoot.start();
    }

    @Override
    public void execute() {
        setShoot.update();
    }

    @Override
    public boolean isFinished() {
        return setShoot.done;
    }
}
