package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.utils.RTime;

public class FireShooter extends Command {
  double startTime;
  double exitTime = 0.7;

  public FireShooter() {}

  @Override
  public void initialize() {
    startTime = RTime.now();
  }

  @Override
  public void execute() {
    Shooter.shoot();
  }

  @Override
  public boolean isFinished() {
    return RTime.now() > startTime + exitTime && Shooter.canShoot() || RobotBase.isSimulation();
  }

  @Override
  public void end(boolean interrupted) {
    // Shooter.disable();
    Shooter.revTo(4000);
    ShooterPivot.setPosition(Math.toRadians(30));
  }
}
