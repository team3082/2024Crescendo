package frc.robot.auto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.commands.SetIntakeFeedPos;
import frc.robot.swerve.SwerveManager;
import frc.robot.utils.Vector2;

public class CommandAuto {
  static double sourceAngle = 0.0;
  static double ampAngle = 0.0;

  public static void init(Command command) {
    new SequentialCommandGroup(command, stop()).schedule();
  }

  public static void update() {
    CommandScheduler.getInstance().run();
  }

  public static Command stop() {
    return Commands.runOnce(() -> SwerveManager.rotateAndDrive(0, new Vector2()));
  }

  public static Command testCommand() {
    return new SequentialCommandGroup(
        new SetIntakeFeedPos(),
        new WaitCommand(0.2),
        new ParallelDeadlineGroup(
            new WaitCommand(10.0),
            new RunCommand(() -> System.out.println("Running")).withTimeout(3.0)),
        new InstantCommand(() -> System.out.println("Done")));
  }
}
