package frc.robot.openhouse;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class MoveForward extends Command {
  double moveBy;

  public MoveForward(double inches) {
    moveBy = inches;
  }

  @Override
  public void initialize() {

    Vector2 desiredPosition =
        new Vector2(Math.cos(Pigeon.getRotationRad()), Math.sin(Pigeon.getRotationRad()))
            .norm()
            .mul(moveBy);
    SwervePID.setDestPt(SwervePosition.getPosition().add(desiredPosition));
    System.out.println("Desired Pos X: " + SwervePID.getDest().x);
    System.out.println("Desired Pos Y: " + SwervePID.getDest().y);
  }

  @Override
  public void execute() {
    System.out.printf(
        "UOV X: %f%nOUV Y: %f%nCurrent X: %f%nCurrent Y: %f%n%n",
        SwervePID.updateOutputX(),
        SwervePID.updateOutputY(),
        SwervePosition.getPosition().x,
        SwervePosition.getPosition().y);
    SwerveManager.rotateAndDrive(
        0, new Vector2(-SwervePID.updateOutputY(), -SwervePID.updateOutputX()));
  }

  @Override
  public boolean isFinished() {
    System.out.println("At Dest?: " + SwervePID.atDest());
    return SwervePID.atDest();
  }

  @Override
  public void end(boolean interrupted) {
    SwerveManager.rotateAndDrive(0, new Vector2());
  }
}
