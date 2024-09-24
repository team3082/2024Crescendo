package frc.robot.subsystems.sensors;

import java.util.Optional;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class VisionTable {
    public static InterpolatingDoubleTreeMap angles = new InterpolatingDoubleTreeMap(); // Degrees

    public static void init() {
        // (Y Val (px), Angle)
        angles.put(0.0, 40.0);
        angles.put(-10.0, 40.0);
        angles.put(10.0, 40.0);
    }

    public static Optional<Double> getAngle(Optional<Double> y) {
        if (y.isPresent()) {
            return Optional.of(angles.get(y.get()));
        } else {
            return Optional.empty();
        }
    }
}
