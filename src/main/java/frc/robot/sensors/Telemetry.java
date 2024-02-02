package frc.robot.sensors;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;

import edu.wpi.first.wpilibj.RobotBase;

// Class for monitoring and relaying real-time robot data
public class Telemetry {

    static final double IN_TO_M = 1 / 39.37;

    // Colors for logging
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Message severity options
     */
    public enum Severity {
        /**
         * Severity stating this message is purely informational.
         */
        INFO,
        /**
         * Severity stating this message contains general debugging information
         */
        DEBUG,
        /**
         * Severity stating this message is a warning.
         */
        WARNING, 
        /**
         * Severity stating this message conveys a critical/fatal error.
         */
        CRITICAL
    }

    // The Shuffleboard tabs that we use for monitoring data.
    // Each tab is isolated from one another.
    // This allows us to filter through only the ones we want to see,
    // and avoids any potential screwups.
    private static final ShuffleboardTab robotTab = Shuffleboard.getTab("SmartDashboard");
    private static final ShuffleboardTab pigeonTab = Shuffleboard.getTab("Pigeon");
    private static final ShuffleboardTab moveTab = Shuffleboard.getTab("Move PID");
    private static final ShuffleboardTab rotTab = Shuffleboard.getTab("Rot PID");
    private static final ShuffleboardTab pos = Shuffleboard.getTab("Positions");

    // NetworkTable entries
    // If we want granular control over our values via Glass (e.g, tuning PID),
    // We must use the NetworkTableEntry type, 
    // Since this enables live updates between Glass and our code. 

    // Field position
    private static final Field2d field = new Field2d();
    private static frc.robot.utils.Vector2 prevSimPos = new Vector2();
    private static Rotation2d prevSimRot = new Rotation2d();

    // Move PID
    private static final GenericEntry moveP = moveTab.add("Move P", SwervePID.moveP).getEntry();
    private static final GenericEntry moveI = moveTab.add("Move I", SwervePID.moveI).getEntry();
    private static final GenericEntry moveD = moveTab.add("Move D", SwervePID.moveD).getEntry();
    private static final GenericEntry moveDeadband = moveTab.add("Move Deadband", SwervePID.moveDead).getEntry();

    // Rot PID
    private static final GenericEntry rotP = rotTab.add("Rot P", SwervePID.rotP).getEntry();
    private static final GenericEntry rotI = rotTab.add("Rot I", SwervePID.rotI).getEntry();
    private static final GenericEntry rotD = rotTab.add("Rot D", SwervePID.rotD).getEntry();
    private static final GenericEntry rotDeadBand = rotTab.add("Rot Deadband", SwervePID.rotDead).getEntry();

    // SwervePosition
    private static final GenericEntry swervePos = pos.add("Swerve Position", SwervePosition.getPosition().toString()).getEntry();

    public static void init() {

        // Input other misc values into Shuffleboard.
        // pigeonTab.add("Pigeon", Pigeon.pigeon);
        robotTab.add("Field View", field);
    }

    /**
     * Log a message to the console. Same as a print statement, however this has color, and it is prefixed with a timestamp.
     * @param severity The severity of the message as a string. This changes the color of the message.
     * @param subsystem The subsystem of which this message was derived from.
     * @param message The message to send to the console.
     */
    public static void log(Severity severity, String subsystem, String message) {
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
        String fullMessage = "[" + RTime.createTimestamp() + "]" + " [" + severity + "] " + "(" + subsystem + "):" + " " + message;
        System.out.println(color + fullMessage + ANSI_RESET);
    }

    /**
     * Log a message to the console. Same as a print statement, however this has color, and it is prefixed with a timestamp.
     * The name of the class that called this function is automatically detected and printed as well.
     * @param severity The severity of the message as a string. This changes the color of the message.
     * @param message The message to send to the console.
     */
    public static void log(Severity severity, String message) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        log(severity, caller, message);
    }

    /**
     * Updates and reads from telemetry. Should be called each frame
     * @param compMode whether we should enable a lightweight version of telemetry for competition. 
     * This is just so we don't hog network bandwidth,
     * and it still gives us decently important information.
     */
    public static void update(boolean compMode) {

        Alliance alliance = RobotBase.isSimulation() ?  Alliance.Blue : DriverStation.getAlliance().get();

        // -1 if we're on the red alliance, 1 if we're on the blue alliance
        int allianceMultiplier = (alliance == DriverStation.Alliance.Red) ? -1 : 1;

        swervePos.setString(SwervePosition.getPosition().toString());

        if (RobotBase.isSimulation()) {
            // Allow the user to drag the robot around if we're in simulation mode
            Vector2 modifiedSimPos = new frc.robot.utils.Vector2(field.getRobotPose().getX(), field.getRobotPose().getY());
            if (prevSimPos.sub(modifiedSimPos).mag() > 0.001) {
                Vector2 modifiedSwervePos = modifiedSimPos
                        .div(IN_TO_M)
                        .sub(new Vector2(325.62, 157.75));
                SwervePosition.setPosition(new Vector2(-modifiedSwervePos.y, modifiedSwervePos.x * allianceMultiplier));
            }

            Rotation2d modifiedSimRot = field.getRobotPose().getRotation();
            if (Math.abs(prevSimRot.minus(modifiedSimRot).getRadians()) > 0.001)
                Pigeon.setSimulatedRot(modifiedSimRot.getRadians() + Math.PI / 2 * allianceMultiplier);
        }

        // Update field position and trajectory
        Vector2 fieldPosMeters = new Vector2(SwervePosition.getPosition().y * allianceMultiplier, -SwervePosition.getPosition().x)
                .add(new Vector2(325.62, 157.75))
                .mul(IN_TO_M);
        Rotation2d rotation = Rotation2d.fromRadians(Pigeon.getRotationRad() - Math.PI / 2 * allianceMultiplier);
        field.setRobotPose(fieldPosMeters.x, fieldPosMeters.y, rotation);

        // Store the field position for the next frame to check if it has been manually changed
        prevSimPos = fieldPosMeters;
        prevSimRot = rotation;

        // Move PID: X
        SwervePID.xPID.kP = moveP.getDouble(0);
        SwervePID.xPID.kI = moveI.getDouble(0);
        SwervePID.xPID.kD = moveD.getDouble(0);
        SwervePID.xPID.deadband = moveDeadband.getDouble(0);

        // Move PID: Y
        SwervePID.yPID.kP = moveP.getDouble(0);
        SwervePID.yPID.kI = moveI.getDouble(0);
        SwervePID.yPID.kD = moveD.getDouble(0);
        SwervePID.yPID.deadband = moveDeadband.getDouble(0);

        // Rot PID
        SwervePID.rotPID.kP = rotP.getDouble(0);
        SwervePID.rotPID.kI = rotI.getDouble(0);
        SwervePID.rotPID.kD = rotD.getDouble(0);
        SwervePID.rotPID.deadband = rotDeadBand.getDouble(0);


    }
}