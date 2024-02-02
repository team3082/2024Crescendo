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
import frc.robot.utils.Vector2;
import frc.robot.utils.trajectories.ChoreoTrajectoryGenerator;

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
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Pigeon.init();
    Pigeon.zero();
    SwerveManager.init();
    SwervePosition.init();
    SwervePID.init();
    Pigeon.setYaw(270);
    //VisionManager.init();
    //Telemetry.init();
    ChoreoTrajectoryGenerator.init();
    SwervePosition.setPosition(new Vector2());
  }

  @Override
  public void robotPeriodic() {
    Pigeon.update();
    RTime.updateAbsolute();
    RTime.update();
    //Telemetry.update(false);
  }

  @Override
  public void autonomousInit() {
    RTime.init();
    Pigeon.setYaw(270);
    // Auto.bezierCurveAutoTest();
    // Auto.trajFollowerTest();
    Auto.choreoTest();
  }

  @Override
  public void autonomousPeriodic() {
    Auto.update();
  }

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
    //SwervePosition.updateAveragePosVision();
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
