package frc.robot.auto.commands;

import static frc.robot.configs.Constants.ShooterConstants.speakerPos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class Aim extends Command {
    @Override
    public void execute() {
        Shooter.fireWhileMoving();

        SwerveManager.moveAndRotateTo(new Vector2(), speakerPos.sub(SwervePosition.getPosition()).norm().mul(-1.0).atan2());
    }


}
