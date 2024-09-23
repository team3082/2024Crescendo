package frc.robot.openhouse;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class MoveForward extends Command {
  double moveBy;

  /**
   * A simple command that moves robot forward by a specified length
   *
   * @param inches
   */
  public MoveForward(double inches) {
    moveBy = inches;
  }

  @Override
  public void initialize() {
    // Calculate the point 'moveBy' inches away based on the rotation of the robot
    Vector2 desiredPosition =
        new Vector2(Math.cos(Pigeon.getRotationRad()), Math.sin(Pigeon.getRotationRad()))
            .norm()
            .mul(moveBy);

    SwervePID.setDestPt(SwervePosition.getPosition().add(desiredPosition));
  }

  @Override
  public void execute() {
    SwerveManager.rotateAndDrive(
        0, new Vector2(SwervePID.updateOutputX(), SwervePID.updateOutputY()));
  }

  @Override
  public boolean isFinished() {
    return SwervePID.atDest();
  }

  @Override
  public void end(boolean interrupted) {
    SwerveManager.rotateAndDrive(0, new Vector2());
  }
}
