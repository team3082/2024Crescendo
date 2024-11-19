// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.auto.Auto;
import frc.robot.auto.AutoSelector;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.SwerveManager;
import frc.robot.utils.sim.SimDevices;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  public boolean isReal;
  @Override
  public void robotInit() {
    // initialize all subsystems
    // SwerveManager.init();
    // Shooter.init();
    Intake.init();
    // Climber.init();

    OI.init();

    // initialize auto routines
  }

  @Override
  public void robotPeriodic() {
    // SimDevices.update();
    // SwerveManager.update();
    // Shooter.update();
    Intake.update();
    // Climber.update();
  }

  @Override
  public void autonomousInit() {
    // AutoSelector.select();
    // Auto.init();
  }

  @Override
  public void autonomousPeriodic() {
    // Auto.update();
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    OI.update();
  }

  @Override
  public void disabledInit() {
    // SwerveManager.disable();
    // Shooter.disable();
    Intake.disable();
    // Climber.disable();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
