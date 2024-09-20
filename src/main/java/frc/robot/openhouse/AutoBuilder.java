package frc.robot.openhouse;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class AutoBuilder {
  /** A command that does something */
  public static Command myCommand() {
    SwervePosition.setPosition(new Vector2());
    return new SequentialCommandGroup(
        new Rotate(60), new MoveForward(200), new WaitCommand(1), new Rotate(90));
  }
}
