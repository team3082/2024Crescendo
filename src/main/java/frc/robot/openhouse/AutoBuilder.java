package frc.robot.openhouse;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class AutoBuilder {
  /** A command that moves the robot in a simple triangle shape */
  public static Command exampleAuto() {
    SwervePosition.setPosition(new Vector2());
    return new SequentialCommandGroup(
        new MoveForward(36),
        new TurnRight(120),
        new MoveForward(72),
        new TurnRight(120),
        new MoveForward(72),
        new TurnRight(120),
        new MoveForward(36));
  }

  /*
   * List of usable commands:
   * MoveForward(double inches)
   * TurnRight(double degrees)
   * TurnLeft(double degrees)
   */

  /** (Add a description of your command here) */
  public static Command customAuto() {
    SwervePosition.setPosition(new Vector2());
    return new SequentialCommandGroup(
        // (Add your commands in here)
        // Format as: new MoveForward(double inches),

        );
  }
}
