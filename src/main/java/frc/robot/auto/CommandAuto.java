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
import frc.robot.auto.commands.FollowBezierCurve;
import frc.robot.auto.commands.MoveTo;
import frc.robot.auto.commands.RotateTo;
import frc.robot.auto.commands.SetIntake;
import frc.robot.auto.commands.SetShooterAngle;
import frc.robot.auto.commands.SetShooterVelocity;
import frc.robot.auto.commands.StowShooter;
import frc.robot.auto.commands.TrajectoryFollow;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.PIDFollower;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.BezierCurve;
import frc.robot.utils.trajectories.ChoreoTrajectoryGenerator;
import frc.robot.utils.trajectories.DiscreteTraj;
import frc.robot.utils.trajectories.QuinticHermite;
import frc.robot.utils.trajectories.SwerveTrajectory;

public class CommandAuto {
  public static void init(Command command){
      new SequentialCommandGroup(command,stop()).schedule();//moveAndRotateTo(new Vector2(100,100), Math.PI/2), stop()).schedule();
  }
  
  public static void update(){
    CommandScheduler.getInstance().run();
  }

  public static Command fourPieceAmpSide() {
        // TODO test this
        BezierCurve curve1 = new BezierCurve(new Vector2(105, -295), new Vector2(100, -260), new Vector2(101, -248.3), new Vector2(109, -223.5));
        BezierCurve curve2 = new BezierCurve(new Vector2(109, -223.5), new Vector2(100, -250), new Vector2(65.8, -250.7), new Vector2(56.2, -222.6));
        BezierCurve curve3 = new BezierCurve(new Vector2(56.2, -222.6), new Vector2(50.2, -262), new Vector2(-8, -239), new Vector2(3.4, -223.7));

        SwervePosition.setPosition(curve1.a);
        return new SequentialCommandGroup(
          new RotateTo(2.5 * Math.PI / 2.0), 
          new frc.robot.auto.commands.SetShooterAngle((Math.PI / 3)), 
          new frc.robot.auto.commands.SetShooterVelocity(1000),
          followCurveAndShoot(curve1),
          followCurveAndShoot(curve2),
          followCurveAndShoot(curve3),
          new RotateTo(Math.PI/2)
          );
    }
    
    public static Command followCurveAndShoot(BezierCurve curve){
        double[] desiredShooterPos = Shooter.getDesiredShooterPos(SwervePosition.getPosition(),SwerveManager.getRobotDriveVelocity());
        double shooterAngle = desiredShooterPos[0], shooterSpeed = desiredShooterPos[1], swerveAngle = desiredShooterPos[2];
        System.out.println(swerveAngle);
            return new ParallelDeadlineGroup(
              new frc.robot.auto.commands.FollowBezierCurve(curve, 1.0),
              new frc.robot.auto.commands.RotateToPoint(new Vector2(-56.78, -327.1)).repeatedly(),
              //new frc.robot.auto.commands.SetIntake(),
              new frc.robot.auto.commands.SetShooterAngle(shooterAngle), 
              new frc.robot.auto.commands.SetShooterVelocity(shooterSpeed)
            );
            // new SetShoot(), // shoot current piece
    }

    public static Command test2() {
      // SwervePosition.setPosition(new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
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
            new ChoreoFollow("2 Piece Middle.1"),
            new SetShooterVelocity(3200)
          ),
          new ChoreoFollow("2 Piece Middle.2"),
          new WaitCommand(0.1),
          new SetShooterAngle(Math.toRadians(57)),
          new FireShooter()
      );
  }

  public static Command fourMiddle() {
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

    public static Command test() {
      SwervePosition.setPosition(new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
      return new SequentialCommandGroup(
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(58.8)),
          new SetShooterVelocity(3500)
        ),
        new FireShooter(),
        new ParallelCommandGroup(
          new SetIntake(IntakeState.GROUND),
          new ChoreoFollow("3 Piece Middle.1")
        ),
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(59.8)),
          new ChoreoFollow("3 Piece Middle.2"),
          new SetShooterVelocity(3500)
        ),
        new FireShooter(),
        new ParallelCommandGroup(
          new SetIntake(IntakeState.GROUND),
          new ChoreoFollow("3 Piece Middle.3")
        ),
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(59.8)),
          new ChoreoFollow("3 Piece Middle.4"),
          new SetShooterVelocity(3500)
        ),
        new FireShooter()
        
        // new SetShooterVelocity(0)
      );
    }

    public static Command fourPieceMiddle() {
      SwervePosition.setPosition(new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red && RobotBase.isReal() ? 1 : -1), -275));
      return new SequentialCommandGroup(
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(59.8)),
          new SetShooterVelocity(3500)
        ),
        new FireShooter(),
        new StowShooter(),
        new ParallelCommandGroup(
          intakePiece(),
          new ChoreoFollow("4 Piece Middle.1")
        ),
        new ParallelCommandGroup(
          new ChoreoFollow("4 Piece Middle.2"),
          new SetShooterVelocity(3500)
        ),
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(60.3)),
          new FireShooter()
        ),
        new StowShooter(),
        new ParallelCommandGroup(
          new ChoreoFollow("4 Piece Middle.3"),
          intakePiece()
        ),
        new ParallelCommandGroup(
          new ChoreoFollow("4 Piece Middle.4"),
          new SetShooterVelocity(3500)
        ),
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(60.3)),
          new FireShooter()
        ),
        new StowShooter(),
        new ParallelCommandGroup(
          new ChoreoFollow("4 Piece Middle.5"),
          intakePiece()
        ),
        new ParallelCommandGroup(
          new ChoreoFollow("4 Piece Middle.6"),
          new SetShooterVelocity(3500)
        ),
        new ParallelCommandGroup(
          new SetShooterAngle(Math.toRadians(60.3)),
          new FireShooter()
        ),
        new StowShooter()

        // new SetShoot(),
      );
  }

    public static Command choreoTest() {
        
        DiscreteTraj traj1 = ChoreoTrajectoryGenerator.getChoreo("Circle.1");
        SwerveState start = traj1.startState();
        SwervePosition.setPosition(start.getPos());;
        Pigeon.setYawRad(start.theta);
        PIDFollower f = new PIDFollower();
        f.setTrajectory(traj1);

        return new TrajectoryFollow(f);
    }

    public static Command rotateTo(double rad){
      SwervePID.setDestRot(rad);
      System.out.println("Rot " + SwervePID.updateOutputRot());
      return Commands.run(() -> SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), new Vector2())).until(() -> SwervePID.atRot());
    }

    public static Command trajFollowerTest() {
        SwervePosition.setPosition(new Vector2(0, 0));
        Pigeon.setYawRad(0);
        SwerveState[] anchors = new SwerveState[]{
            new SwerveState(0,0, 0,0,0,0), 
            new SwerveState(0, -75, 0, 0,0,0), 
            new SwerveState(60, -75,0,0,0,0), 
            new SwerveState(60,0,0,0,0,0)
        };
        QuinticHermite traj = new QuinticHermite(anchors, 3);
        PIDFollower controller = new PIDFollower();
        controller.setTrajectory(traj);
        return new TrajectoryFollow(controller);

    }

    public static Command moveAndRotateTo(Vector2 pos, double rad){
      return new ParallelCommandGroup(new MoveTo(pos), new RotateTo(rad));
    }

    public static Command stop(){
      return Commands.runOnce(() -> SwerveManager.rotateAndDrive(0, new Vector2()));
    }

    public static Command bezierCurveAutoTest() {
        BezierCurve curve1 = new BezierCurve(new Vector2(33, -149), new Vector2(101.6, -106), new Vector2(-87.5, -67), new Vector2(-17, -26));
        BezierCurve curve2 = new BezierCurve(new Vector2(-17, -26), new Vector2(-87.5, -67), new Vector2(101.6, -106), new Vector2(33, -149));

        SwervePosition.setPosition(curve1.a);

        return new SequentialCommandGroup(
            new FollowBezierCurve(curve1, 1.0),
            new FollowBezierCurve(curve2, 1.0)
        );
    }
    
    public static Command bounceTest(){
        SwerveTrajectory traj = ChoreoTrajectoryGenerator.getChoreo("Loopties.1");
        SwerveState start = traj.startState();
        SwervePosition.setPosition(start.getPos());;
        Pigeon.setYawRad(start.theta);

       return new ChoreoFollow("Loopties.1");
    }

    public static Command fourPieceSourceSide() {
      // TODO make this
      return new SequentialCommandGroup(Commands.none());
  }

  public static Command threePieceMiddleAmpSide() {
      // TODO make this
      return new SequentialCommandGroup(Commands.none());
  }

  public static Command threePieceMiddleSourceSide() {
      // TODO make
      return new SequentialCommandGroup(Commands.none());
  }

  public static Command intakePiece(){
    return new SequentialCommandGroup(
      new InstantCommand(() -> Intake.setState(IntakeState.GROUND)),
      new RunCommand(Intake::suck).repeatedly().until(Intake.beambreak::isBroken),
      new InstantCommand(Intake::no).alongWith(new InstantCommand(() ->Intake.setState(IntakeState.STOW)))
    );
  }
}

