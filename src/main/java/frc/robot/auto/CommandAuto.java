package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotBase;
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
import frc.robot.auto.commands.ForceFire;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetIntakeFeedPos;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
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
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -275));
    Pigeon.setYaw(90);
    
    Intake.setState(IntakeState.FEED);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelCommandGroup(
            // new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
        new WaitCommand(9.5),
        new ChoreoFollow("2 Piece Middle.1")
    );
  }

  // starts on source side, shoots preload
  public static Command onePieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelCommandGroup(
            // new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
      new ParallelCommandGroup(
        new ChoreoFollow("1 Piece Source.1"),
        new SetIntake()
      )
    );
  }

  // starts on amp side, shoots preload
  public static Command onePieceAmp() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelCommandGroup(
            // new SetShooterAngle(Math.toRadians(57)),
            new SetShooterVelocity(3500)
          ),
        new FireShooter(),
      new ParallelCommandGroup(
        new ChoreoFollow("1 Piece Amp.1"),
        new SetIntake()
      )
    );
  }

  // starts in middle, shoots preload, grabs the middle close piece, shoots
  public static Command twoPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -275));
    Pigeon.setYaw(90);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("2 Piece Middle.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("2 Piece Middle.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on source side, shoots preload, grabs the source close piece, shoots
  public static Command twoPieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("2 Piece Source.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("2 Piece Source.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on amp side, shoots preload, grabs the amp close piece, shoots
  public static Command twoPieceAmp() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("2 Piece Amp.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("2 Piece Amp.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on source side, shoots preload, grabs far piece on source side, shoots
  public static Command twoPieceSourceFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("2 Piece Far Source.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("2 Piece Far Source.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on amp side, shoots preload, grabs far piece on amp side, shoots
  public static Command twoPieceAmpFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("2 Piece Far Amp.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("2 Piece Far Amp.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on source side, shoots preload, grabs far, shoots, grabs far, shoots
  public static Command threePieceSourceFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Far Source.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Far Source.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Far Source.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Far Source.4"),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter());
  }

  // starts on amp side, shoots preload, grabs far, shoots, grabs far, shoots
  public static Command threePieceAmpFar() {
    return new SequentialCommandGroup(
      
    );
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs far piece, shoots
  public static Command threePieceSourceHalfFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Half Source.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Source.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Half Source.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Source.4"),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter());
  }

  // starts on amp side, shoots preload, grabs amp side close piece, shoots, grabs far piece, shoots
  public static Command threePieceAmpHalfFar() {
    SwervePosition.setPosition(new Vector2(100 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Half Amp.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Amp.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Half Amp.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Amp.4"),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter());
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs middle close piece, shoots
  public static Command threePieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Source.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Source.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Source.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Source.4"),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    );
  }

  // starts on amp side, shoots preload, grabs amp close piece, shoots, grabs far piece, shoots
  public static Command threePieceAmp() {
    SwervePosition.setPosition(new Vector2(100 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? -1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(.2),
      // shoot preload
      new ParallelCommandGroup(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Amp.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Amp.2"),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("3 Piece Amp.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Amp.4"),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter());
  }

  public static Command fourPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    Pigeon.setYaw(90);
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new ParallelCommandGroup(
        new SetShooterAngle(Math.toRadians(54)),
        new SetShooterVelocity(4200)
        ),
        new WaitCommand(0.2),
        new FireShooter(),

     new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("4Middle.1"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
        new ChoreoFollow("4Middle.2"),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(54)),
        new FireShooter(),

      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("4Middle.3"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
        new ChoreoFollow("4Middle.4"),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(54)),
        new FireShooter(),
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(
          new ChoreoFollow("4Middle.5"),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.6"),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(54)),
        new FireShooter()
    );
  }

  public static Command fourPieceMiddle2(){
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
    Pigeon.setYaw(90);
    
    return new SequentialCommandGroup(
      new SetShooterAngle(Math.toRadians(50.0)).alongWith(
      new SetShooterVelocity(4200),
      new SetIntakeFeedPos()),
      new ForceFire(0.4),
      new ChoreoFollow("4Middle.1").deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.2"),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).onlyIf(() -> Intake.reallyHasPiece),
      new ChoreoFollow("4Middle.3").deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.4"),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).onlyIf(() -> Intake.reallyHasPiece),
      new ChoreoFollow("4Middle.5").deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.6"),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).andThen(new InstantCommand(Shooter::disable))
    );
  }

  public static Command testCommand(){
    return new SequentialCommandGroup(
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelDeadlineGroup(
        new WaitCommand(10.0), 
        new RunCommand(() -> System.out.println("Running")).withTimeout(3.0)
        ),
      new InstantCommand(() -> System.out.println("Done"))
    );
  }
}