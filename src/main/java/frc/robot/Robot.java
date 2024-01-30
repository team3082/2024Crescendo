// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.sensors.Pigeon;
import frc.robot.sensors.Telemetry;
import frc.robot.sensors.VisionManager;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;

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
  @Override
  public void robotInit() {
    // Blue
    System.out.println(VisionManager.getTagPos(1).toString());
    System.out.println(VisionManager.getTagPos(8).toString());
    // Red
    System.out.println(VisionManager.getTagPos(3).toString());
    System.out.println(VisionManager.getTagPos(13).toString());

    Pigeon.init();
    Pigeon.zero();
    SwerveManager.init();
    SwervePosition.init();
    SwervePID.init();
    Pigeon.setYaw(270);
    VisionManager.init();
    Telemetry.init();
  }

  @Override
  public void robotPeriodic() {
    Pigeon.update();
    RTime.updateAbsolute();
    SwervePosition.update();
    Telemetry.update(false);
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    OI.init();
    SwervePosition.enableVision();
  }

  @Override
  public void teleopPeriodic() {
    RTime.update();
    SwervePosition.update();
    OI.useInput();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    SwervePosition.updateAveragePosVision();
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
