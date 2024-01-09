package frc.robot.utils;

public class RMath {
    /** 
     * Converts an target angle within a certain range to an absolute target angle, based
     * on the best path the robot should rotate to get to this angle.
     * @param currentAngle The robot's current angle
     * @param targetAngle The angle the robot should rotate to, bounded by angleRange
     * @param angleRange The range of one full rotation, usually 360
     * @return The absolute angle the robot should rotate to 
     * */ 
    public static double targetAngleAbsolute(double currentAngle, double targetAngle, double angleRange) {

        targetAngle = targetAngle % angleRange;
        if(targetAngle < 0)
            targetAngle += angleRange;

        // The number of full rotations the bot has made
        int numRot = (int) Math.floor(currentAngle / angleRange);

        // The target pigeon angle
        double target = numRot * angleRange + targetAngle;
        double targetPlus = target + angleRange;
        double targetMinus = target - angleRange;

        // The true destination for the bot to rotate to
        double destination;

        // Determine if, based on the current angle, it should stay in the same
        // rotation, enter the next, or return to the previous.
        if (Math.abs(target - currentAngle) <= Math.abs(targetPlus - currentAngle)
         && Math.abs(target - currentAngle) <= Math.abs(targetMinus - currentAngle)) {
            destination = target;
        } else if (Math.abs(targetPlus - currentAngle) < Math.abs(targetMinus - currentAngle)) {
            destination = targetPlus;
        } else {
            destination = targetMinus;
        }

        return destination;
    }


    public static double smoothJoystick1(double val) {
        //return val * val * val;
        return (val*val) * Math.signum(val);

    }
    public static Vector2 smoothJoystick2(Vector2 dir) {
        double mag = dir.mag();
        mag = mag * mag;
        return dir.norm().mul(mag);
    }

    public static double clamp(double in, double min, double max){
        return Math.min(Math.max(in, min), max);
    }
}
