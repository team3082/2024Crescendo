package frc.robot.auto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.auto.commands.FireShooter;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.utils.Vector2;

public class CommandAuto {
  public static void init(Command command){
    new SequentialCommandGroup(command,stop()).schedule();
  }
  
  public static void update(){
    CommandScheduler.getInstance().run();
  }

  public static Command stop(){
    return Commands.runOnce(() -> SwerveManager.rotateAndDrive(0, new Vector2()));
  }

  public static double sourceAngle() {
    return 0.0;
  }

  public static double ampAngle() {
    return 0.0;
  }

  // starts in middle, shoots preload
  public static Command onePieceMiddle() {
    return new SequentialCommandGroup(
      new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter()

    );
  }

  // starts on source side, shoots preload
  public static Command onePieceSource() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload
  public static Command onePieceAmp() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts in middle, shoots preload, grabs the middle close piece, shoots
  public static Command twoPieceMiddle() {
    return new SequentialCommandGroup(
      // shoot preload
      new ParallelCommandGroup(
        new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelCommandGroup(
        new SetIntake(IntakeState.GROUND),
        new ChoreoFollow("2 Piece Middle.1"),
        new SetShooterVelocity(3200)
      ),
      new ChoreoFollow("2 Piece Middle.2"),

      // shoot
      new WaitCommand(0.1),
      new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on source side, shoots preload, grabs the source close piece, shoots
  public static Command twoPieceSource() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload, grabs the amp close piece, shoots
  public static Command twoPieceAmp() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on source side, shoots preload, grabs far piece on source side, shoots
  public static Command twoPieceSourceFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload, grabs far piece on amp side, shoots
  public static Command twoPieceAmpFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on source side, shoots preload, grabs far, shoots, grabs far, shoots
  public static Command threePieceSourceFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload, grabs far, shoots, grabs far, shoots
  public static Command threePieceAmpFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs far piece, shoots
  public static Command threePieceSourceHalfFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload, grabs amp side close piece, shoots, grabs far piece, shoots
  public static Command threePieceAmpHalfFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs middle close piece, shoots
  public static Command threePieceSource() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on amp side, shoots preload, grabs amp close piece, shoots, grabs far piece, shoots
  public static Command threePieceAmp() {
    return new SequentialCommandGroup(

    );
  }

  public static Command fourPieceMiddle() {
    return new SequentialCommandGroup(
      new ParallelCommandGroup(
            new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),

      new ParallelDeadlineGroup(
            new ChoreoFollow("4Middle.1"),
            new SetIntake(IntakeState.GROUND),
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

  public static Command testCommand(){
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(
        new WaitCommand(10.0), 
        new RunCommand(() -> System.out.println("Running")).withTimeout(3.0)
        ),
      new InstantCommand(() -> System.out.println("Done"))
    );
  }
}