package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.commands.Shoot;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.IntakePiece;
import frc.robot.auto.commands.PrepShooter;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class CommandAuto {
  public enum PieceCount{
    TWO,
    THREE,
    FOUR
  }

  public enum AutoSide{
    RIGHT,
    LEFT,
    MIDDLE
  }
  public static void init(Command command) {
    new SequentialCommandGroup(command, stop()).schedule();
  }

  public static void update() {
    CommandScheduler.getInstance().run();
  }

  public static Command twoPiece(String choreoFollow) {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new ParallelCommandGroup(
            // Shoot preloaded from subwoofer
            new PrepShooter()
          ),
          new Shoot(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelCommandGroup(
            new IntakePiece(),
            new ChoreoFollow("2 Piece Middle.1")
            ),
          new ChoreoFollow("2 Piece Middle.2"),
          new WaitCommand(0.1),
          new Shoot()
      );
  }

  public static Command twoPieceMid() {
    Pigeon.setYaw(90);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new Shoot(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Middle.1"),
              new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
            ),
            new IntakePiece()
          ),
          new ChoreoFollow("2 Piece Middle.2"),
          new WaitCommand(0.1),
          new Shoot()
      );
  }
  public static Command twoPieceAmp() {
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new Shoot(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Left.1"),
              new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
            ),
            new IntakePiece()
          ),
          new ChoreoFollow("2 Piece Left.2"),
          new PrepShooter(),
          new WaitCommand(0.1),
          new Shoot()
      );
  }

  public static Command twoPieceSource() {
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new Shoot(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Right.1"),
              new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
            ),
            new IntakePiece()
          ),
          new ChoreoFollow("2 Piece Right.2"),
          new WaitCommand(0.1),
          new Shoot()
      );
  }

  public static Command fourPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
            new Shoot(),
    
          new ParallelCommandGroup(
                new IntakePiece(),
                new ChoreoFollow("4Middle.1")
              ),
            new ChoreoFollow("4Middle.2"),
            new WaitCommand(0.1),
            new Shoot(),
    
          new ParallelCommandGroup(
                new IntakePiece(),
                new ChoreoFollow("4Middle.3")
            ),
            new ChoreoFollow("4Middle.4"),
            new WaitCommand(0.1),
            new Shoot(),
    
          new ParallelCommandGroup(
                new IntakePiece(),
                new ChoreoFollow("4Middle.5")
            ),
          new ChoreoFollow("4Middle.6"),
            new WaitCommand(0.1),
            new Shoot()
        );
  }

  public static Command ThreePiece(String choreoFollow, double angle) {
        Pigeon.setYaw(angle);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new Shoot(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow(choreoFollow + ".1"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
          new IntakePiece()
          ),
          new ChoreoFollow(choreoFollow + ".2"),
        new Shoot().onlyIf(() -> ShooterManager.hasPiece()),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow(choreoFollow + ".3"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
            new IntakePiece()
          ),
            new ChoreoFollow(choreoFollow + ".4"),
        new Shoot());
  }

  

  public static Command threePieceMiddle() {
        Pigeon.setYaw(90);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new Shoot(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Middle.1"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
          new IntakePiece()
          ),
          new ChoreoFollow("3 Piece Middle.2"),
        new Shoot().onlyIf(() -> ShooterManager.hasPiece()),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Middle.3"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
            new IntakePiece()
          ),
            new ChoreoFollow("3 Piece Middle.4"),
        new Shoot());
  }

  public static Command threePieceSource() {
        Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new Shoot(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Right.1"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
          new IntakePiece()
          ),
          new ChoreoFollow("3 Piece Right.2"),
        new Shoot().onlyIf(() -> ShooterManager.hasPiece()),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Right.3"),
            new WaitCommand(.5).onlyIf(() -> !ShooterManager.hasPiece())
          ),
            new IntakePiece()
          ),
            new ChoreoFollow("3 Piece Right.4"),
        new Shoot());
  }

  public static Command onePieceStationary(){
    return new SequentialCommandGroup(
        new Shoot()
    );
  }

  public static Command stop() {
    return Commands.runOnce(() -> SwerveManager.rotateAndDrive(0, new Vector2()));
  }
  
}
