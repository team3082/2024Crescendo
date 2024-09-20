package frc.robot.openhouse;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoBuilder {
  /** A command that does something */
  public static Command myCommand() {
    return new SequentialCommandGroup(
        new Rotate(60), new MoveForward(50), new WaitCommand(1), new Rotate(90));
  }
}
