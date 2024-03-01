package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.commands.FireShooter;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.auto.commands.StowShooter;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.subsystems.shooter.Intake;
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
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
          new FireShooter(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelCommandGroup(
            new SetIntake(IntakeState.GROUND),
            new ChoreoFollow("2 Piece Middle.1")
            ),
          new SetShooterVelocity(3200),
          new ChoreoFollow("2 Piece Middle.2"),
          new WaitCommand(0.1),
          new SetShooterAngle(Math.toRadians(57)),
          new FireShooter()
      );
  }

  public static Command twoPieceMid() {
    Pigeon.setYaw(90);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new ParallelCommandGroup(
            // Shoot preloaded from subwoofer
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
          new FireShooter(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Middle.1"),
              new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
            ),
            new SetIntake(IntakeState.GROUND)
          ),
          new SetShooterVelocity(3200),
          new ChoreoFollow("2 Piece Middle.2"),
          new WaitCommand(0.1),
          new SetShooterAngle(Math.toRadians(57)),
          new FireShooter()
      );
  }
  public static Command twoPieceAmp() {
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new ParallelCommandGroup(
            // Shoot preloaded from subwoofer
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
          new FireShooter(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Left.1"),
              new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
            ),
            new SetIntake(IntakeState.GROUND)
          ),
          new SetShooterVelocity(3200),
          new ChoreoFollow("2 Piece Left.2"),
          new WaitCommand(0.1),
          new SetShooterAngle(Math.toRadians(57)),
          new FireShooter()
      );
  }

  public static Command twoPieceSource() {
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new ParallelCommandGroup(
            // Shoot preloaded from subwoofer
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
          new FireShooter(),

          // Set intake to ground, intake for 3 seconds
          // while driving to piece, go back to subwoofer,
          // wait till Choreo is finished and then shoot.
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new ChoreoFollow("2 Piece Right.1"),
              new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
            ),
            new SetIntake(IntakeState.GROUND)
          ),
          new SetShooterVelocity(3200),
          new ChoreoFollow("2 Piece Right.2"),
          new WaitCommand(0.1),
          new SetShooterAngle(Math.toRadians(57)),
          new FireShooter()
      );
  }

  public static Command fourPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
        return new SequentialCommandGroup(
          new ParallelCommandGroup(
                new SetShooterAngle(Math.toRadians(57)),
                new SetShooterVelocity(3500)
              ),
            new FireShooter(),
    
          new ParallelCommandGroup(
                new SetIntake(IntakeState.GROUND),
                new ChoreoFollow("4Middle.1"),
                new SetShooterVelocity(3200)
              ),
            new ChoreoFollow("4Middle.2"),
            new WaitCommand(0.1),
            new SetShooterAngle(Math.toRadians(57)),
            new FireShooter(),
    
          new ParallelCommandGroup(
                new SetIntake(IntakeState.GROUND),
                new ChoreoFollow("4Middle.3"),
                new SetShooterVelocity(3200)
            ),
            new ChoreoFollow("4Middle.4"),
            new WaitCommand(0.1),
            new SetShooterAngle(Math.toRadians(56)),
            new FireShooter(),
    
          new ParallelCommandGroup(
                new SetIntake(IntakeState.GROUND),
                new ChoreoFollow("4Middle.5"),
                new SetShooterVelocity(3200)
            ),
          new ChoreoFollow("4Middle.6"),
            new WaitCommand(0.1),
            new SetShooterAngle(Math.toRadians(54.8)),
            new FireShooter()
        );
  }

  public static Command ThreePiece(String choreoFollow, double angle) {
        Pigeon.setYaw(angle);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(58.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow(choreoFollow + ".1"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
          new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
          new ChoreoFollow(choreoFollow + ".2"),
            new SetShooterAngle(Math.toRadians(59.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter().onlyIf(() -> Intake.reallyHasPiece),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow(choreoFollow + ".3"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
            new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(59.8)),
            new ChoreoFollow(choreoFollow + ".4"),
            new SetShooterVelocity(3500)
          ),
        new FireShooter());
  }

  public static Command fourPieceMiddle2() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));

    return new SequentialCommandGroup(
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(59.8)),
            new SetShooterVelocity(3500)),
        new FireShooter(),
        new StowShooter(),

        new ParallelCommandGroup(
            intakePiece(),
            new ChoreoFollow("4 Piece Middle.1")),
        
        new ParallelCommandGroup(
            new ChoreoFollow("4 Piece Middle.2"),
            new SetShooterVelocity(3500)),
        
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(60.3)),
            new FireShooter()),
        new StowShooter(),

        new ParallelCommandGroup(
            new ChoreoFollow("4 Piece Middle.3"),
            intakePiece()),

        new ParallelCommandGroup(
            new ChoreoFollow("4 Piece Middle.4"),
            new SetShooterVelocity(3500)),
        
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(60.3)),
            new FireShooter()),
        new StowShooter(),

        new ParallelCommandGroup(
            new ChoreoFollow("4 Piece Middle.5"),
            intakePiece()),

        new ParallelCommandGroup(
            new ChoreoFollow("4 Piece Middle.6"),
            new SetShooterVelocity(3500)),

        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(60.3)),
            new FireShooter()),
        new StowShooter());
  }

  

  public static Command threePieceMiddle() {
        Pigeon.setYaw(90);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(58.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Middle.1"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
          new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
          new ChoreoFollow("3 Piece Middle.2"),
            new SetShooterAngle(Math.toRadians(59.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter().onlyIf(() -> Intake.reallyHasPiece),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Middle.3"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
            new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(59.8)),
            new ChoreoFollow("3 Piece Middle.4"),
            new SetShooterVelocity(3500)
          ),
        new FireShooter());
  }

  public static Command threePieceSource() {
        Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    return new SequentialCommandGroup(
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(58.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Right.1"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
          new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
          new ChoreoFollow("3 Piece Right.2"),
            new SetShooterAngle(Math.toRadians(59.8)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter().onlyIf(() -> Intake.reallyHasPiece),
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(
            new ChoreoFollow("3 Piece Right.3"),
            new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
          ),
            new SetIntake(IntakeState.GROUND)
          ),
        new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(59.8)),
            new ChoreoFollow("3 Piece Right.4"),
            new SetShooterVelocity(3500)
          ),
        new FireShooter());
  }

  public static Command onePieceStationary(){
    return new SequentialCommandGroup(
      new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter()
    );
  }

  public static Command stop() {
    return Commands.runOnce(() -> SwerveManager.rotateAndDrive(0, new Vector2()));
  }

  public static Command intakePiece() {
    return new SequentialCommandGroup(
        new InstantCommand(() -> Intake.setState(IntakeState.GROUND)),
        new RunCommand(Intake::suck).repeatedly().until(Intake.beambreak::isBroken),
        new InstantCommand(Intake::no).alongWith(new InstantCommand(() -> Intake.setState(IntakeState.STOW))));
  }
  
}
