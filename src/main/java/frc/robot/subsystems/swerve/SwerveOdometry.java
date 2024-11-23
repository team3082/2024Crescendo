package frc.robot.subsystems.swerve;

import frc.robot.utils.Vector2;

import edu.wpi.first.wpilibj.Timer;

// TODO: SwerveManager.mods not implemented yet
import frc.robot.subsystems.swerve.SwerveManager.mods;

public class SwerveOdometry {
    
    // Positon of the robot relative to and arbitrary positon of (0,0)
    private static Vector2 position;
    // A lock prevents the positon from being updated and fetched at the same time
    private static Object positionLock = new Object();

    private static double lastLoopTimeStamp;

    private static double[] previousDrivePositions = new double[mods.length];
    private static double previousPigeonAngle;

    public static void init() {
        position = new Vector2(0.0, 0.0);

        // TODO: Method getAngle() not implemented yet. Need to get angle from Pigeon in radians
        previousPigeonAngle = SwerveManager.getAngle();

        odomThread.setDaemon(true);
        odomThread.start();
    }

    private static Thread odomThread = new Thread() {
        @Override
        public void run() {
            while (!isInterrupted()) {
                // How far has the robot moved since the last loop
                Vector2 robotDisp = new Vector2();

                // Get positions from all of the swerve mods
                for (int i = 0; i < mods.length; i++) {

                    double position = mods[i].getDriverPosition();
                    double disp = position - previousDrivePositions[i];
                    previousDrivePositions[i] = position;

                    double angle = mods[i].getSteerAngle();

                    robotDisp = robotDisp.add(Vector2.fromPolar(angle, disp));
                }

                Vector2 meanDisp = robotDisp.div(mods.length);

                // TODO: Method getAngle() not implemented yet. Need to get angle from Pigeon in radians
                double pigeonAngle = SwerveManager.getAngle();
                double deltaAngle = 0.0;
                if (previousPigeonAngle != Double.NaN) {
                    deltaAngle = pigeonAngle - previousPigeonAngle;
                }

                // Devon's lil fix goes here
                Vector2 innovation = poseExponentiation(meanDisp, previousPigeonAngle, deltaAngle);

                previousPigeonAngle = pigeonAngle;

                synchronized(positionLock) {
                    position = position.add(innovation);
                }

                try {
                    sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        }
    };

    public static Vector2 getPosition() {
        synchronized(positionLock) {
            return position;
        }
    }


    /**
     * returns an estimate of innovation using pose exponentiation
     * @param deltaPos the linear displacement of the system wrt the robot
     * @param theta0 the initial angle of the velocity
     * @param deltaTheta the change in theta throughout the timestep
     * @return a Vector2 object containing the displacement of the system after the timestep assuming constant drive and steer velocities
     */
    public static Vector2 poseExponentiation(double deltaPos, double theta0, double deltaTheta){
        if(deltaTheta == 0.0){
            return Vector2.fromPolar(theta0, deltaPos);
        }

        double deltax = (Math.cos(theta0 + deltaTheta) - Math.cos(theta0)) / deltaTheta * deltaPos;
        double deltay = (Math.sin(theta0 + deltaTheta) - Math.sin(theta0)) / deltaTheta * deltaPos;

        return new Vector2(deltax, deltay);

    }

    public static Vector2 poseExponentiation(Vector2 deltaPos, double theta0, double deltaTheta){
        return poseExponentiation(deltaPos.mag(), theta0 + deltaPos.atan2(), deltaTheta);
    }
}
