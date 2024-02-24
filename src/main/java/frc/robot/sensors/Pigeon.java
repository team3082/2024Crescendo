package frc.robot.sensors;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.swerve.SwerveManager;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector3;

@SuppressWarnings("removal")
public class Pigeon {

    public static Pigeon2 pigeon;
    private static double lastRot;
    private static double deltaRot;

    private static double simulatedRot = 0;

    public static void init() {
        pigeon = new Pigeon2(0, "CANivore");
        pigeon.configFactoryDefault();
    }

    public static void update() {
        if (RobotBase.isSimulation()) {
            simulatedRot += SwerveManager.getRotationalVelocity() * RTime.deltaTime();
        }
        deltaRot = (getRotationRad() - lastRot) / RTime.deltaTime();
        lastRot = getRotationRad();
    }

    public static void setSimulatedRot(double rad) {
        simulatedRot = rad;
    }

    public static void zero(){
        pigeon.setYaw(90);
    }

    public static void setYaw(double deg) {
        pigeon.setYaw(deg);
        simulatedRot = deg * Math.PI / 180;
    }

    public static void setYawRad(double rad) {
        setYaw(rad * 180.0 / Math.PI);
        simulatedRot = rad;
    }

    
    /**
     * Local to the robot, not the world
     * Pitch, rotates around the X, left to right, axis
     * Tilts forward and backward
     * @return Pitch in radians
     */
    public static double getPitchRad() {
        return Math.PI * pigeon.getPitch() / 180;
    }

    /**
     * Local to the robot, not the world
     * Yaw, rotates around the Y, up and down, axis
     * @return Yaw in radians
     */
    public static double getRotationRad() {
        if (RobotBase.isSimulation()) {
            return simulatedRot;
        }
        return Math.PI * pigeon.getYaw() / 180;
    }

    /**
     * Local to the robot, not the world
     * Roll, rotates around the Z, forward and backward, axis
     * Tilts left and right
     * @return Roll in radians
     */
    public static double getRollRad() {
        return Math.PI * pigeon.getRoll() / 180;
    }

    /**
     * Gets the rotation speed (yaw) of the robot in radians per second
     * @return Yaw in radians
     */
    public static double getDeltaRotRad() {
        return deltaRot;
    }

    /**
     * Returns the unit vector in the direction of the z-axis relative to the pigeon
     * This vector is in reference to the basis vectors of the pigeon at the start of the competition
     * @return The unit vector in the direction of the z-axis relative to the pigeon
     */
    public static Vector3 getKHat(){

        double yaw = getRotationRad();
        double roll = getRollRad();
        double pitch = getPitchRad();

        double x,y,z;

        x = Math.cos(yaw) * Math.sin(pitch) * Math.cos(roll) + (Math.sin(yaw) * Math.sin(roll));
        y = Math.cos(yaw) * Math.sin(roll) * -1  + (Math.sin(yaw) * Math.sin(pitch) * Math.cos(roll));
        z = Math.cos(pitch) * Math.cos(roll);
        return new Vector3(x,y,z);
        
    }
}
