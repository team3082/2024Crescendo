package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;

public class SetShooterVelocity extends Command {
    double velocity; 

    public SetShooterVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public void initialize() {
        Shooter.revTo(velocity);
    }

    @Override
    public boolean isFinished() {
        return (Shooter.canShoot() || RobotBase.isSimulation());
    }
}
