package frc.robot.auto.commands;

import static frc.robot.configs.Constants.ShooterConstants.speakerPos;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class Aim extends ChickenCommand {
    @Override
    public void init() {
        isFinished=false;
    }
    
    @Override
    public void update() {
        Shooter.fireWhileMoving();

        SwerveManager.moveAndRotateTo(new Vector2(), speakerPos.sub(SwervePosition.getPosition()).norm().mul(-1.0).atan2());
    }
}
