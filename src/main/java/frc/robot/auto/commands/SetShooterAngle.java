package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Swerve;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.swerve.SwerveManager;
import frc.robot.utils.Vector2;

public class SetShooterAngle extends Command {

    frc.robot.auto.autoframe.SetShooterAngle setShoterAngle;

    public SetShooterAngle(double angle) {
        setShoterAngle = new frc.robot.auto.autoframe.SetShooterAngle(angle);
    }

    public SetShooterAngle(Vector2 position) {
        setShoterAngle = new frc.robot.auto.autoframe.SetShooterAngle(position);
    }

    @Override
    public void initialize() {
        setShoterAngle.start();
    }

    @Override
    public void execute(){
        setShoterAngle.update();
    }

    @Override
    public boolean isFinished() {
        return setShoterAngle.done;
    }
}
