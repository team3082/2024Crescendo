package frc.robot.configs;

import edu.wpi.first.units.*;
import static edu.wpi.first.units.Units.*;

/**
 * ShooterSettings is a class that "holds" the desired shooter
 * settings at any given point. This contains our distance away from the target,
 * the speeds the flywheels should be spinning at, and the angle the pivot should be at.
 * 
 * This class should be only one object, constantly mutated during each call.
 * This reduces complexity as its easy to track down one wrong class,
 * rather than several made. It also cuts down on RAM usage for the roboRIO.
 */
public class ShooterSettings {

  public final MutableMeasure<Distance> distance = MutableMeasure.zero(Inches);
  public final MutableMeasure<Velocity<Angle>> velocity = MutableMeasure.zero(RPM);
  public final MutableMeasure<Angle> angle = MutableMeasure.zero(Degrees);

  /** Sets the distance of this table. */
  public ShooterSettings distance(Measure<Distance> distance) {
    this.distance.mut_replace(distance);
    return this;
  }

  /** Sets the angle for this table. */
  public ShooterSettings angle(Measure<Angle> angle) {
    this.angle.mut_replace(angle);
    return this;
  }

  /** Sets the shooter velocity, in RPM, for this table. */
  public ShooterSettings velocity(Measure<Velocity<Angle>> velocity) {
    this.velocity.mut_replace(velocity);
    return this;
  }

  /** Gets the shooter angle calculated from this table. */
  public Measure<Angle> getAngle() {
    return angle;
  }

  /** Gets the robot's distance to the target. */
  public Measure<Distance> getDistance() {
    return distance;
  }

  /** Gets the shooter velocity calculated from this table. */
  public Measure<Velocity<Angle>> getVelocity() {
    return velocity;
  }
}