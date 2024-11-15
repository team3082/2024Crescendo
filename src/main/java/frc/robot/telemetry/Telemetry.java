package frc.robot.telemetry;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;

public class Telemetry {
    
    // LOGGING
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    private enum Severity {
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
    // private static final GenericEntry moveI = moveTab.add("Move I", SwervePID.moveI).getEntry();
    // private static final GenericEntry moveD = moveTab.add("Move D", SwervePID.moveD).getEntry();
    // private static final GenericEntry moveDeadband = moveTab.add("Move Deadband", SwervePID.moveDead).getEntry();

    // Rot PID
    // TODO: Same thing for rot PID values
    // private static final GenericEntry rotP = moveTab.add("Rot P", SwervePID.rotP).getEntry();
    // private static final GenericEntry rotI = moveTab.add("Rot I", SwervePID.rotI).getEntry();
    // private static final GenericEntry rotD = moveTab.add("Rot D", SwervePID.rotD).getEntry();
    // private static final GenericEntry rotDeadband = moveTab.add("Rot Deadband", SwervePID.rotDead).getEntry();

    // Shooter
    // TODO: Figure out how we're getting the values for shooter, may have changed
    // private static final GenericEntry topFlywheelRPM = shooter.add("Top Flywheel RPM", Shooter.getTopVel).getEntry();
    // private static final GenericEntry bottomFlywheelRPM = shooter.add("Bottom Flywheel RPM", Shooter.getBottomVel).getEntry();
    // private static final GenericEntry targetFlywheelRPM = shooter.add("Target Flywheel RPM", Shooter.getFlywheelTargetVel).getEntry();

    // private static final GenericEntry topVector = shooter.add("Top Flywheel", OI.getTopVector()).getEntry();
    // private static final GenericEntry bottomVector = shooter.add("Bottom Flywheel", OI.getBottomVector()).getEntry();

    // private static final GenericEntry pivotAngle = shooter.add("Pivot Angle", Shooter.getPivotAngle()).getEntry();
    // private static final GenericEntry pivotTargetAngle = shooter.add("Pivot Target Angle", Shooter.getTargePivotAngle()).getEntry();


    // Climber
    // TODO: Figure out how we're getting the values for climber states, may have changed
    // private static final GenericEntry leftClimberState = climber.add("Left Climber State", ClimberManager.leftCLimber.getControlState()).getEntry();
    // private static final GenericEntry rightClimberState = climber.add("Right Climber State", ClimberManager.rightClimber.getControlState()).getEntry();

    // Swerve position
    // private static final GenericEntry swervePos = position.add("Swerve Position", SwervePosition.getPosition().toString()).getEntry();

    public static void init() {

        // Misc values
        robotTab.add("Field View", field);
        robotTab.add("Swerve", swerveMods);
        // robotTab.add("Auto Selector", AutoSelector.autoChooser); // TODO: Implement AutoSelector

    }

    public static void log(Severity severity, String subsytem, String message) {
        String color;

        switch (severity) {
            case INFO:
                color = ANSI_RESET;
            break;
            case DEBUG:
                color = ANSI_GREEN;
            break;
            case WARNING:
                color = ANSI_YELLOW;
            break;
            case CRITICAL:
                color = ANSI_RED;
            break;
            default:
                return;
        }
        String fullMessage = "[" + RTime.createTimestamp() + "]" + "[" + severity + "]" + "(" + subsytem + "): " + message;
        System.out.println(color + fullMessage + ANSI_RESET);
    }

    public static void updateField() {
        // -1 if we're on red alliance, 1 if on blue alliance
        Optional<Alliance> alliance  = DriverStation.getAlliance();
        int allianceMultiplier = (alliance.isEmpty() || alliance.get() == Alliance.Red ? -1 : 1);

        // TODO: This might be different this year depending on how swerve works
        // swervePos.setString(SwervePosition.getPosition().toString());

        // Update field position and trajectory
        // TODO: Implement correct methods to get swerve x, y. Might also have to convert units DO NOT USE AS IS
        // Vector2 fieledPosMeters = new Vector2(SwervePosition.getPosition().x, SwervePosition.getPosition().y);

        // TODO: Pigeon,java needs to be created
        // Rotation2d rotation = Rotation2d.fromRadians(Pigeon.getRotationRad() - Math.PI / 2 * allianceMultiplier);
        // field.setRobotPose(fieledPosMeters.x, fieledPosMeters.y, rotation);
    }

    public static void updateShooter() {
        // topFlywheelRPM.setDouble(Shooter.getTopVel());
        // bottomFlywheelRPM.setDouble(Shooter.getBottomVel());
        // targetFlywheelRPM.setDouble(Shooter.getFlywheelTargetVel()); // Might have to convert to RPM. Check Shooter code

        // topVector.setDouble(OI.getTopVector());
        // bottomVector.setDouble(OI.getBottomVector());

        // pivotAngle.setDouble(Shooter.getPivotAngle());
        // pivotTargetAngle.setDouble(Shooter.getTargePivotAngle());
    }

    public static void updateClimber() {
        // TODO: Figure out how we're getting the values for climber states, may have changed
        // leftClimberState.setString(ClimberManager.leftClimber.getControlState());
        // rightClimberState.setString(ClimberManager.rightClimber.getControlState());
    }

    public static void updateSwerve() {

        // TODO: These probably changed, update them with the new method/variable names

        // // Move PID: X
        // SwervePID.xPID.kP = moveP.getDouble(0);
        // SwervePID.xPID.kI = moveI.getDouble(0);
        // SwervePID.xPID.kD = moveD.getDouble(0);
        // SwervePID.xPID.deadband = moveDeadband.getDouble(0);

        // // Move PID: Y
        // SwervePID.yPID.kP = moveP.getDouble(0);
        // SwervePID.yPID.kI = moveI.getDouble(0);
        // SwervePID.yPID.kD = moveD.getDouble(0);
        // SwervePID.yPID.deadband = moveDeadband.getDouble(0);

        // // Rot PID
        // SwervePID.rotPID.kP = rotP.getDouble(0);
        // SwervePID.rotPID.kI = rotI.getDouble(0);
        // SwervePID.rotPID.kD = rotD.getDouble(0);
        // SwervePID.rotPID.deadband = rotDeadBand.getDouble(0);

        // If we really feel like it, we can add Kade's cool swerve visualization here

        // swerveMovementRoot.setPosition(20, 20);
        // swerveMovement.setAngle(Math.toDegrees(SwerveManager.getRobotDriveVelocity()));
        
    }
}
