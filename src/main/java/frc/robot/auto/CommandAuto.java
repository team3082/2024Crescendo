package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.auto.ChickenAutoSystem.AutoRoutine;
import frc.robot.auto.ChickenAutoSystem.CommandRunner;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.ChickenCommand;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.InstantCommand;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.ParallelCommand;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.ParallelDeadlineCommand;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.SequentialCommand;
import frc.robot.auto.ChickenAutoSystem.ChikenCommands.WaitCommand;
import frc.robot.auto.commands.Aim;
import frc.robot.auto.commands.ChoreoFollow;
import frc.robot.auto.commands.FireShooter;
import frc.robot.auto.commands.ForceFire;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetIntakeFeedPos;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.auto.commands.UseVision;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class CommandAuto{
  // starts in middle, shoots preload
  @AutoRoutine
  public ChickenCommand[] onePieceMiddle(){
    SwervePosition.setPosition(
    new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    Intake.setState(IntakeState.FEED);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelCommand(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),
      new WaitCommand(9.5),
      new ChoreoFollow("2 Piece Middle.1", 1.0)
    };
  } 

  // starts on source side, shoots preload
  @AutoRoutine
  public ChickenCommand[] onePieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    new ParallelCommand(
          // new SetShooterAngle(Math.toRadians(57)),
          new SetShooterVelocity(3500)
        ),
      new FireShooter(),
    new ParallelCommand(
      new ChoreoFollow("1 Piece Source.1", 1.0),
      new SetIntake()
    )
    };
  }

  // starts on amp side, shoots preload
  @AutoRoutine
  public ChickenCommand[] onePieceAmp() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      new ParallelCommand(
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),
      new ParallelCommand(
        new ChoreoFollow("1 Piece Amp.1", 1.0),
        new SetIntake()
      )
    };
  }

  // starts in middle, shoots preload, grabs the middle close piece, shoots
  @AutoRoutine
  public ChickenCommand[] twoPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      // new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

    // grab piece
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("2 Piece Middle.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(3500)
    ),
    new ChoreoFollow("2 Piece Middle.2", 1.0),

    // shoot
    new WaitCommand(0.1),
    // new SetShooterAngle(Math.toRadians(57)),
    new FireShooter()
  };
  }

  // starts on source side, shoots preload, grabs the source close piece, shoots
  @AutoRoutine
  public ChickenCommand[] twoPieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      // new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

    // grab piece
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("2 Piece Source.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(3500)
    ),
    new ChoreoFollow("2 Piece Source.2", 1.0),

    // shoot
    new WaitCommand(0.1),
    // new SetShooterAngle(Math.toRadians(57)),
    new FireShooter()
  };
  }

  // starts on amp side, shoots preload, grabs the amp close piece, shoots
  @AutoRoutine
  public ChickenCommand[] twoPieceAmp() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      // new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

    // grab piece
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("2 Piece Amp.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(3500)
    ),
    new ChoreoFollow("2 Piece Amp.2", 1.0),

    // shoot
    new WaitCommand(0.1),
    // new SetShooterAngle(Math.toRadians(57)),
    new FireShooter()
  };
 }

  // starts on source side, shoots preload, grabs far piece on source side, shoots
  @AutoRoutine
  public ChickenCommand[] twoPieceSourceFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      // new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

    // grab piece
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("2 Piece Far Source.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(3500)
    ),
    new ChoreoFollow("2 Piece Far Source.2", 1.0),

    // shoot
    new WaitCommand(0.1),
    // new SetShooterAngle(Math.toRadians(57)),
    new FireShooter()
  };
 }

  // starts on amp side, shoots preload, grabs far piece on amp side, shoots
  @AutoRoutine
  public ChickenCommand[] twoPieceAmpFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      // new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

    // grab piece
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("2 Piece Far Amp.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(3500)
    ),
    new ChoreoFollow("2 Piece Far Amp.2", 1.0),

    // shoot
    new WaitCommand(0.1),
    // new SetShooterAngle(Math.toRadians(57)),
    new FireShooter()
  };
  }

  // starts on source side, shoots preload, grabs far, shoots, grabs far, shoots
  @AutoRoutine
  public ChickenCommand[] threePieceSourceFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new WaitCommand(0.2),
    // shoot preload
    new ParallelCommand(
      new SetShooterAngle(Math.toRadians(57)),
      new SetShooterVelocity(3500)
    ),
    new FireShooter(),

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

    // shoot
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
  }

  @AutoRoutine
  public ChickenCommand[] amp145(){
    SwervePosition.setPosition(new Vector2(96 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
    new ParallelCommand(
      new SetShooterAngle(Math.toRadians(54.0)),
      new SetShooterVelocity(4000)
    ),
    new FireShooter(),
    new ParallelDeadlineCommand(
      new ChoreoFollow("amp145.1", 1.0),
      new SetIntake()
    ),
    new WaitCommand(0.25),
    new ParallelDeadlineCommand(
      new FireShooter(), new UseVision()),
      
    new ParallelDeadlineCommand(
      new ChoreoFollow("amp145.2", 1.0),
      new SetIntake()),
    new ChoreoFollow("amp145.3", 1.0),
    new ParallelDeadlineCommand(
      new FireShooter(), new UseVision()),

    new ParallelDeadlineCommand(
      new ChoreoFollow("amp145.4", 1.0),
      new SetIntake()),
    new ChoreoFollow("amp145.5", 1.0),
    new ParallelDeadlineCommand(
      new FireShooter(), new UseVision())
  };
  }

  @AutoRoutine
  public ChickenCommand[] threeSourceCitrus() {
    SwervePosition.setPosition(new Vector2(-100 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -267));
    Pigeon.setYaw(90);
    return new ChickenCommand[]{
      new ParallelDeadlineCommand(
        new ChoreoFollow("source_citrus.1", 1.0).alongWith(new SetShooterAngle(Math.toRadians(30.0))).alongWith(new SetShooterVelocity(4000.0)).andThen(new ChoreoFollow("source_citrus.2", 1.0)), 
        new SetIntake()),
      new ParallelDeadlineCommand(new FireShooter(), new UseVision()),

      new ParallelDeadlineCommand(
        new SequentialCommand(
         new ChoreoFollow("source_citrus.3", 1.0), new ChoreoFollow("source_citrus.4", 1.0)
        ),
        new SetIntake()),
      new ParallelDeadlineCommand(new FireShooter(), new UseVision()),

      new ParallelDeadlineCommand(
        new ChoreoFollow("source_citrus.5", 1.0).andThen(new ChoreoFollow("source_citrus.6", 1.0)),
        new SetIntake()),
      new ParallelDeadlineCommand(new FireShooter(), new UseVision())
    };
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs far piece, shoots
  @AutoRoutine
  public ChickenCommand[] threePieceSourceHalfFar() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommand(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Half Source.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Source.2", 1.0),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Half Source.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Source.4", 1.0),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    };
  }

  // starts on amp side, shoots preload, grabs amp side close piece, shoots, grabs far piece, shoots
  @AutoRoutine
  public ChickenCommand[] threePieceAmpHalfFar() {
    SwervePosition.setPosition(new Vector2(100 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommand(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Half Amp.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Amp.2", 1.0),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Half Amp.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Half Amp.4", 1.0),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    };
  }

  // starts on source side, shoots preload, grabs source close piece, shoots, grabs middle close piece, shoots
  @AutoRoutine
  public ChickenCommand[] threePieceSource() {
    SwervePosition.setPosition(new Vector2(16 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 30 : 150);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(0.2),
      // shoot preload
      new ParallelCommand(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Source.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Source.2", 1.0),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Source.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Source.4", 1.0),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    };
  }

  // starts on amp side, shoots preload, grabs amp close piece, shoots, grabs far piece, shoots
  @AutoRoutine
  public ChickenCommand[] threePieceAmp() {
    SwervePosition.setPosition(new Vector2(100 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -300));
    Pigeon.setYaw(DriverStation.getAlliance().get() == Alliance.Blue ? 150 : 30);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new WaitCommand(.2),
      // shoot preload
      new ParallelCommand(
        // new SetShooterAngle(Math.toRadians(57)),
        new SetShooterVelocity(3500)
      ),
      new FireShooter(),

      // grab piece
      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Amp.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Amp.2", 1.0),

      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter(),

       new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("3 Piece Amp.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(3500)
      ),
      new ChoreoFollow("3 Piece Amp.4", 1.0),
      // shoot
      new WaitCommand(0.1),
      // new SetShooterAngle(Math.toRadians(57)),
      new FireShooter()
    };
  }

  @AutoRoutine
  public ChickenCommand[] fourPieceMiddle() {
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    return new ChickenCommand[]{
    new SetIntakeFeedPos(),
    new ParallelCommand(
      new SetShooterAngle(Math.toRadians(54)),
      new SetShooterVelocity(4200)
    ),
    new WaitCommand(0.2),
    new FireShooter(),

    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("4Middle.1", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(4200)
    ),
    new ChoreoFollow("4Middle.2", 1.0),
    new WaitCommand(0.1),
    new SetShooterAngle(Math.toRadians(54)),
    new FireShooter(),

    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("4Middle.3", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(4200)
    ),
    new ChoreoFollow("4Middle.4", 1.0),
    new WaitCommand(0.1),
    new SetShooterAngle(Math.toRadians(57.5)),
    new FireShooter(),
    new ParallelDeadlineCommand(
      new SequentialCommand(
        new ChoreoFollow("4Middle.5", 1.0),
        new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
      ),
      new SetIntake(),
      new SetShooterVelocity(4200)
    ),
    new ChoreoFollow("4Middle.6", 1.0),
    new WaitCommand(0.1),
    new SetShooterAngle(Math.toRadians(54)),
    new FireShooter()
  };
  }

  @AutoRoutine
  public ChickenCommand[] middle03215(){
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new ParallelCommand(
        new SetShooterAngle(Math.toRadians(54)),
        new SetShooterVelocity(4200)
        ),
        new WaitCommand(0.2),
        new FireShooter(),

     new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("4Middle.1", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
        new ChoreoFollow("4Middle.2", 1.0),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(54)),
        new FireShooter(),

      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("4Middle.3", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
        new ChoreoFollow("4Middle.4", 1.0),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(57.5)),
        new FireShooter(),

      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("4Middle.5", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.6", 1.0),
        new WaitCommand(0.1),
        new SetShooterAngle(Math.toRadians(54)),
        new FireShooter(),

      new ParallelDeadlineCommand(
        new SequentialCommand(
          new ChoreoFollow("4Middle.7", 1.0),
          new WaitCommand(.5).onlyIf(() -> !Intake.reallyHasPiece)
        ),
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),

      new ChoreoFollow("4Middle.8",1.0),
      new ParallelDeadlineCommand(
        new WaitCommand(0.1).andThen(new FireShooter()),
        new UseVision()
      )
    };
  }

  @AutoRoutine()
  public ChickenCommand[] fourPieceMiddle2(){
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    
    return new ChickenCommand[]{
      new SetShooterAngle(Math.toRadians(50.0)).alongWith(
      new SetShooterVelocity(4200),
      new SetIntakeFeedPos()),
      new ForceFire(0.4),
      new ChoreoFollow("4Middle.1", 1.0).deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.2", 1.0),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).onlyIf(() -> Intake.reallyHasPiece),
      new ChoreoFollow("4Middle.3", 1.0).deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.4", 1.0),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).onlyIf(() -> Intake.reallyHasPiece),
      new ChoreoFollow("4Middle.5", 1.0).deadlineWith(
        new SetIntake(),
        new SetShooterVelocity(4200)
      ),
      new ChoreoFollow("4Middle.6", 1.0),
      new SetShooterAngle(Math.toRadians(50)),
      new ForceFire(0.2).andThen(new InstantCommand(Shooter::disable))
    };
  }

  @AutoRoutine
  public ChickenCommand[] fourMiddleFast(){
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    return new ChickenCommand[]{
      new SetIntakeFeedPos(),
      new ParallelCommand(
        new SetShooterAngle(Math.toRadians(54)),
        new SetShooterVelocity(4200)
        ),
        new WaitCommand(0.2),
        new FireShooter(), // Shoot first piece

        new SetIntakeFeedPos(),
        new ChoreoFollow("4MidFast.1", 1.0),
        new ParallelCommand(
          new ChoreoFollow("4MidFast.2", 0.5),
          new Aim().onlyIf(() -> Intake.reallyHasPiece) // Shoot second piece
        ),
        new WaitCommand(.5),

        new SetIntakeFeedPos(),
        new ChoreoFollow("4MidFast.3", 1.0),
        new ParallelCommand(
          new ChoreoFollow("4MidFast.4", 0.5),
          new Aim().onlyIf(() -> Intake.reallyHasPiece)
        ), // Shoot third piece
    };
  }

  public static void init(){
    CommandRunner.Init();
    CommandRunner.addBundle(new CommandAuto());
  }

  public static void routineInit() {
    CommandRunner.RoutineInit();
  }

  public static void update() {
    CommandRunner.update();
  }
}