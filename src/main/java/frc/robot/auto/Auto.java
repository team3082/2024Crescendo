package frc.robot.auto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.auto.autoframe.*;
import frc.robot.sensors.Pigeon;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.PIDFollower;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.BezierCurve;
import frc.robot.utils.trajectories.ChoreoTrajectoryGenerator;
import frc.robot.utils.trajectories.DiscreteTraj;
import frc.robot.utils.trajectories.QuinticHermite;
import frc.robot.utils.trajectories.SwerveTrajectory;

public class Auto {
    public static void bezierCurveAutoTest() {
        BezierCurve curve1 = new BezierCurve(new Vector2(33, -149), new Vector2(101.6, -106), new Vector2(-87.5, -67), new Vector2(-17, -26));
        BezierCurve curve2 = new BezierCurve(new Vector2(-17, -26), new Vector2(-87.5, -67), new Vector2(101.6, -106), new Vector2(33, -149));

        SwervePosition.setPosition(curve1.a);

        Autoframe[] Frames = new Autoframe[] {
            new FollowBezierCurve(curve1, 1.0),
            new FollowBezierCurve(curve2, 1.0)
        };

        queueFrames(Frames);
    }

    public static void trajFollowerTest() {
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
        Autoframe[] frames = new Autoframe[]{
            new TrajectoryFollow(controller)
        };

        queueFrames(frames);
    }

    public static void test() {

        SwervePosition.setPosition(new Vector2(56.78 * (DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));

        Autoframe[] frames = new Autoframe[] {
            // Shoot preloaded from subwoofer
            new SetShooterAngle(Math.toRadians(58.8)),
            new SetShooterVelocity(3500),
            new FireShooter(),
            new ClearActive(),

            // Set intake to ground, intake for 3 seconds
            // while driving to piece, go back to subwoofer,
            // wait till Choreo is finished and then shoot.
            new SetIntake(),
            new ChoreoFollow("2 Piece Middle.1"),
            new ChoreoFollow("2 Piece Middle.2"),
            new SetShooterAngle(Math.toRadians(58.8)),
            new SetShooterVelocity(2650),
            new FireShooter(),
            new ClearActive(),
        };
        queueFrames(frames);
    }

    public static void choreoTest() {
        
        DiscreteTraj traj1 = ChoreoTrajectoryGenerator.getChoreo("Circle.1");
        SwerveState start = traj1.startState();
        SwervePosition.setPosition(start.getPos());;
        Pigeon.setYawRad(start.theta);
        PIDFollower f = new PIDFollower();
        f.setTrajectory(traj1);

        queueFrames(new TrajectoryFollow(f));
    }

    public static void bounceTest(){
        SwerveTrajectory traj = ChoreoTrajectoryGenerator.getChoreo("Loopties.1");
        SwerveState start = traj.startState();
        SwervePosition.setPosition(start.getPos());;
        Pigeon.setYawRad(start.theta);

        queueFrames(new ChoreoFollow("Loopties.1"));
    }

    public static void fourPieceMiddle() {
        SwerveTrajectory traj = ChoreoTrajectoryGenerator.getChoreo("4 Piece Center.1");
        SwerveState start = traj.startState();
        SwervePosition.setPosition(start.getPos());;
        Pigeon.setYawRad(start.theta);

        queueFrames(
            new SetIntake(),
            new ChoreoFollow("4 Piece Center.1"),
            new SetShooterAngle(Math.PI/4),
            // new SetShoot(),
            new ChoreoFollow("4 Piece Center.2"),
            new SetIntake(),
            new ChoreoFollow("4 Piece Center.3"),
            new SetShooterAngle(Math.PI/4),
            // new SetShoot(),
            new ChoreoFollow("4 Piece Center.4"),
            new SetIntake(),
            new ChoreoFollow("4 Piece Center.5"),
            new SetShooterAngle(Math.PI/4),
            // new SetShoot(),
            new ChoreoFollow("4 Piece Center.6"),
            new ChoreoFollow("4 Piece Center.7")
        );
    }

    public static void fourPieceAmpSide() {
        // TODO test this
        BezierCurve curve1 = new BezierCurve(new Vector2(105, -295), new Vector2(100, -260), new Vector2(101, -248.3), new Vector2(109, -223.5));
        BezierCurve curve2 = new BezierCurve(new Vector2(109, -223.5), new Vector2(100, -250), new Vector2(65.8, -250.7), new Vector2(56.2, -222.6));
        BezierCurve curve3 = new BezierCurve(new Vector2(56.2, -222.6), new Vector2(50.2, -262), new Vector2(-8, -239), new Vector2(3.4, -223.7));

        SwervePosition.setPosition(curve1.a);

        Autoframe[] Frames = new Autoframe[] {
            new RotateTo(2.5 * Math.PI / 2.0, 0.5),
            new SetShooterAngle(Math.PI / 3), // add angle
            new SetShooterVelocity(1000), // add velocity
            // new SetShoot(),

            new SetIntake(), // puts down intake until the piece is grabbed
            new SetShooterAngle(Math.PI / 4), // add angle
            new SetShooterVelocity(2000), // add velocity
            // go to second piece
            new RotateTo(2.5 * Math.PI / 2.0, 0.5), // add angle
            new FollowBezierCurve(curve1, 1.0),
            // new SetShoot(), // shoot current piece

            new SetIntake(), // puts down intake until the piece is grabbed
            new SetShooterAngle(Math.PI / 5), // add angle
            new SetShooterVelocity(3000), // add velocity
            // // go to third piece
            new RotateTo(3.0 * Math.PI / 2.0, 0.5), // add angle
            new FollowBezierCurve(curve2, 1.0),
            // new SetShoot(), // shoot current piece

            new SetIntake(), // puts down intake until the piece is grabbed
            new SetShooterAngle(Math.PI / 6), // add angle
            new SetShooterVelocity(4000), // add velocity
            // go to fourth piece
            new RotateTo(3.5 * Math.PI / 2.0, 0.5), // add angle
            new FollowBezierCurve(curve3, 1.0),
            // new SetShoot() // shoot current piece
        };

        queueFrames(Frames);
    }

    public static void fourPieceSourceSide() {
        // TODO make this
        Autoframe[] Frames = new Autoframe[] {

        };
        queueFrames(Frames);
    }

    public static void threePieceMiddleAmpSide() {
        // TODO make this
        Autoframe[] Frames = new Autoframe[] {

        };
        queueFrames(Frames);
    }

    public static void threePieceMiddleSourceSide() {
        // TODO make
        Autoframe[] Frames = new Autoframe[] {

        };
        queueFrames(Frames);
    }

    // AUTO SYSTEM =================================================
    public static Queue<Autoframe> queuedFrames;
    public static HashSet<Autoframe> activeFrames = new HashSet<>();

    public static double startTimestamp = 0;


    private static void queueFrames(Autoframe... frames) {
        activeFrames.clear();
        queuedFrames = new LinkedList<>(Arrays.asList(frames));
        startTimestamp = RTime.now();
    }

    public static void update() {
        boolean advanceFrame = true;
        HashSet<Autoframe> finishedFrames = new HashSet<>();

        for (Autoframe frame : activeFrames) {

            // For debugging, print the classes of all of the active frames
            System.out.print(frame.getClass().getSimpleName() + " ");

            // Update and/or finish the frame according to whether or not its "done" boolean has been set to true
            frame.update();
            if (frame.done){
                finishedFrames.add(frame);
                frame.finish();
            }

            // If there's any blocking frame active that didn't just end, don't advance to the next frame
            else if (frame.blocking)
                advanceFrame = false;
        }

        // Remove all of the finished frames from the set of active frames
        activeFrames.removeAll(finishedFrames);

        System.out.println(); // Newline for debugging readability
        System.out.println(activeFrames.size());

        // Conditions for advancing the frame:
        // AdvanceFrame must be true, meaning there are no active, blocking frames
        // QueuedFrames must have items remaining
        if (advanceFrame && !queuedFrames.isEmpty()) {
            Autoframe newFrame = queuedFrames.remove();
            newFrame.start();
            activeFrames.add(newFrame);
        }

        // Print "Done" to signify Auto has finished
        if (queuedFrames.isEmpty() && activeFrames.isEmpty()) {
            System.out.println("Done");
        }
    }

}
