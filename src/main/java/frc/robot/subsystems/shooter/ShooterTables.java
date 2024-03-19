package frc.robot.subsystems.shooter;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import frc.robot.configs.ShooterSettings;

import java.util.List;
import static edu.wpi.first.units.Units.*;

/**
 * ShooterTables is an interpolating class meant for
 * calculating our desired speeds and angle for the
 * shooter, depending on our distance away from the target.
 * 
 * Both the RPM and the angle is calculated in this class,
 * pulling from an initialized ShooterSettings defined with
 * 15-20 points that we manually tested. Check out the Constants file
 * if you'd like to see that.
 * 
 * This also avoids amp, as we want that separate from the interpolation.
 */
public class ShooterTables {
    
    public static InterpolatingDoubleTreeMap speedsDistanceMap = new InterpolatingDoubleTreeMap(); // RPM
    public static InterpolatingDoubleTreeMap angleDistanceMap = new InterpolatingDoubleTreeMap(); // Degrees

    private static final ShooterSettings shooterSettings = new ShooterSettings();

    /**
     * Initialize with previously defined points.
     * This should only be called in Constants.
     */
    public static void init(List<ShooterSettings> settings) {
        for (ShooterSettings chosenSettings : settings) {
            double distance = chosenSettings.distance.in(Inches);
            speedsDistanceMap.put(distance, chosenSettings.velocity.in(RPM));
            angleDistanceMap.put(distance, chosenSettings.angle.in(Degrees));
        }
    }

    /**
    * Calculates the shooter velocity and angle by interpolating given our distance from the target.
    * @param distance our distance to the target
    * @return the desired shooter settings.
    */
    public static ShooterSettings calculate(double distance) {
        shooterSettings.distance.mut_replace(distance, Inches);
        shooterSettings.angle.mut_replace(angleDistanceMap.get(distance), Degrees);
        shooterSettings.velocity.mut_replace(speedsDistanceMap.get(distance), RPM);
        return shooterSettings;
    }
}
