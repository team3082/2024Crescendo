package frc.robot.swerve;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.RobotConfig;
import frc.robot.sensors.Pigeon;
import frc.robot.utils.PIDController;
import frc.robot.utils.RMath;
import frc.robot.utils.Vector2;

public class SwervePID {

    public static PIDController xPID, yPID, rotPID;

    public static double moveP = RobotConfig.moveP;
    public static double moveI = RobotConfig.moveI;
    public static double moveD = RobotConfig.moveD;
    public static double moveDead = RobotConfig.moveDead;
    public static double moveVelDead = RobotConfig.moveVelDead;
    public static double moveSpeedMax = RobotConfig.moveSpeedMax;

    public static double rotP = RobotConfig.rotP;
    public static double rotI = RobotConfig.rotI;
    public static double rotD = RobotConfig.rotD;
    public static double rotDead = RobotConfig.rotDead;
    public static double rotVelDead = RobotConfig.rotVelDead;
    public static double rotSpeedMax = RobotConfig.rotSpeedMax;

    public static void init() {
        xPID = new PIDController(moveP, moveI, moveD, moveDead, moveVelDead, moveSpeedMax);
        yPID = new PIDController(moveP, moveI, moveD, moveDead, moveVelDead, moveSpeedMax);
        rotPID = new PIDController(rotP, rotI, rotD, rotDead, rotVelDead, rotSpeedMax);
    }

    public static void setDestState(Vector2 dest, double destRot) {
        setDestPt(dest);
        setDestRot(destRot);
    }

    public static void setDestPt(Vector2 dest) {
        setDestX(dest.x);
        setDestY(dest.y);
    }

    public static void setDestX(double dest) {
        xPID.setDest(dest);
    }

    public static void setDestY(double dest) {
        yPID.setDest(dest);
    }

    public static void setDestRot(double dest) {
        rotPID.setDest(RMath.targetAngleAbsolute(Pigeon.getRotationRad(), dest, 2*Math.PI));
    }
    
    public static double updateOutputX() {
        return (xPID.atSetpoint() ? 0 : (DriverStation.getAlliance().get() == Alliance.Red ? -1 : 1) * xPID.updateOutput(SwervePosition.getPosition().x));
    }

    public static double updateOutputY() {
        return yPID.atSetpoint()? 0 : yPID.updateOutput(SwervePosition.getPosition().y);
    }

    public static double updateOutputRot() {
        return rotPID.updateOutput(Pigeon.getRotationRad());
    }

    public static Vector2 updateOutputVel() {
        return new Vector2(updateOutputX(), updateOutputY());
    }

    public static SwerveInstruction updateAll() {
        return new SwerveInstruction(updateOutputRot(), updateOutputVel());
    }

    public static Vector2 getDest() {
        return new Vector2(xPID.getDest(), yPID.getDest());
    }

    public static boolean atDest() {
        return xPID.atSetpoint() && yPID.atSetpoint();
    }
    
    public static boolean atRot() {
        return rotPID.atSetpoint();
    }

}
