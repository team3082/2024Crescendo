package frc.robot.telemetry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.SwervePID;
import frc.robot.utils.Vector2;

public class Telemetry {
    
    // LOGGING
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    private enum Severtity {
        INFO,
        DEBUG,
        WARNING,
        CRITICAL
    }

    // SHUFFLEBOARD
    /*
     * Shuffle board tabs for monitoring robot values
     * Organizes values so that we can find them easier
     */
    private static final ShuffleboardTab robotTab = Shuffleboard.getTab("SmartDashboard");
    private static final ShuffleboardTab moveTab = Shuffleboard.getTab("Move PID");
    private static final ShuffleboardTab rotationTab = Shuffleboard.getTab("Rot PID");
    private static final ShuffleboardTab position = Shuffleboard.getTab("Positions");
    private static final ShuffleboardTab shooter = Shuffleboard.getTab("Shooter");
    private static final ShuffleboardTab climber = Shuffleboard.getTab("CLimber");

    // Field position
    private static final Field2d field = new Field2d();
    private static frc.robot.utils.Vector2 prevSimPos = new Vector2();
    private static Rotation2d prevSimRotation = new Rotation2d();

    // Trajectory state
    private static final Field2d trajField = new Field2d();
    public static Pose2d desiredPos;

    // Mechanism visualization
    private static Mechanism2d subsytems = new Mechanism2d(60, 60);
    private static MechanismRoot2d shooterRoot = subsytems.getRoot("Shooter", 20, 10);
    private static MechanismLigament2d shooterPivot = shooterRoot.append(new MechanismLigament2d("Shooter", 10, 180));

    // Swerve states
    private static Mechanism2d swerveMods = new Mechanism2d(40, 40);
    private static MechanismRoot2d swerveModRoot0 = swerveMods.getRoot("SwerveMod0", 0, 0);
    private static MechanismLigament2d swerveMod0 = swerveModRoot0.append(new MechanismLigament2d("SwerveMod0", 0, 0));

    private static MechanismRoot2d swerveModRoot1 = swerveMods.getRoot("SwerveMod1", 0, 0);
    private static MechanismLigament2d swerveMod1 = swerveModRoot1.append(new MechanismLigament2d("SwerveMod1", 0, 0));

    private static MechanismRoot2d swerveModRoot2 = swerveMods.getRoot("SwerveMod2", 0, 0);
    private static MechanismLigament2d swerveMod2 = swerveModRoot2.append(new MechanismLigament2d("SwerveMod2", 0, 0));

    private static MechanismRoot2d swerveModRoot3 = swerveMods.getRoot("SwerveMod3", 0, 0);
    private static MechanismLigament2d swerveMod3 = swerveModRoot3.append(new MechanismLigament2d("SwerveMod3", 0, 0));

    private static MechanismRoot2d swerveMovementRoot = swerveMods.getRoot("Movement Vector", 0, 0);
    private static MechanismLigament2d swerveMovement = swerveMovementRoot.append(new MechanismLigament2d("Movement Vector", 0, 0));

    // Move PID
    // TODO: Figure out how we're getting PID values
    // private static final GenericEntry moveP = moveTab.add("Move P", SwervePID.moveP).getEntry();

    // Rot PID
    // TODO: Same thing for rot PID values

    // Shooter
    // TODO: Figure out how we're getting the values for shooter, may have changed
    private static final GenericEntry topFlywheelRPM = shooter.add("Top Flywheel RPM", Shooter.getTopVel).getEntry();
    private static final GenericEntry bottomFlywheelRPM = shooter.add("Bottom Flywheel RPM", Shooter.getBottomVel).getEntry();
    private static final GenericEntry targetFlywheelRPM = shooter.add("Target Flywheel RPM", Shooter.getFlywheelTargetVel).getEntry();

    // Climber
    // TODO: Figure out how we're getting the values for climber states, may have changed
    private static final GenericEntry leftClimberState = climber.add("Left Climber State", ClimberManager.leftCLimber.getControlState()).getEntry();
    private static final GenericEntry rightClimberState = climber.add("Right Climber State", ClimberManager.rightClimber.getControlState()).getEntry();

    
}
