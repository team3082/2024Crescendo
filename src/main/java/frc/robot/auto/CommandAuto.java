package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auto.ChikenCommands.CommandRunner;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.auto.ChikenCommands.ChikenCommands.DebugCommand;
import frc.robot.auto.ChikenCommands.ChikenCommands.ParallelCommand;
import frc.robot.auto.ChikenCommands.ChikenCommands.ParallelDeadlineCommand;
import frc.robot.auto.ChikenCommands.ChikenCommands.SequentialCommand;
import frc.robot.auto.ChikenCommands.ChikenCommands.WaitCommand;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.FireShooter;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetIntakeFeedPos;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class CommandAuto {
  public static void threePieceSourceFarInit(){
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
  }

  // Starts on source side, shoots preload, grabs far, shoots, grabs far, shoots
  public static ChickenCommand[] threePieceSourceFar = new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // // shoot preload
      new ParallelCommand(
        new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      // new FireShooter(),

      // grab piece
      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Far Source.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Far Source.2", 1.0),

      // // shoot
      new WaitCommand(0.1),
      new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Far Source.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Far Source.4", 1.0),
      // shoot
      new WaitCommand(0.1),
      new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
  };

  public static void init(){
    CommandRunner.Init();
    CommandRunner.addRoutine(
      "threePieceSourceFar", 
      threePieceSourceFar, 
      CommandAuto::threePieceSourceFarInit
    );
  }

  public static void routineInit() {
    CommandRunner.RoutineInit();
  }

public static void update() {
    CommandRunner.update();
}
}