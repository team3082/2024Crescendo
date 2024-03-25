// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.auto.AutoSelector;
import frc.robot.subsystems.sensors.BannerLight;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.subsystems.sensors.Telemetry;
import frc.robot.subsystems.sensors.VisionManager;
import frc.robot.subsystems.climber.ClimberManager;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.trajectories.ChoreoTrajectoryGenerator;
import frc.robot.auto.CommandAuto;
import frc.robot.configs.Constants;

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
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Constants.init();
    Pigeon.init();
    Pigeon.zero();
    SwerveManager.init();
    SwervePosition.init();
    SwervePID.init();
    Pigeon.setYaw(90);
    VisionManager.init();
    ClimberManager.init();
    ChoreoTrajectoryGenerator.init();
    ChoreoTrajectoryGenerator.parseAll();
    Shooter.init();
    Intake.init();
    AutoSelector.setup();
    Telemetry.init();
    BannerLight.init();
    SwervePosition.enableVision();

    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
  }

  @Override
  public void robotPeriodic() {
    try {
    Pigeon.update();
    RTime.updateAbsolute();
    RTime.update();
    Telemetry.update(false);
    Intake.beambreak.update();
    } catch (Exception e) {
      System.out.println("oopsies" + e.toString());
      e.printStackTrace();
    }
  }

  @Override
  public void autonomousInit() {
    RTime.init();
    Pigeon.setYaw(90);
	  CommandScheduler.getInstance().enable();
    AutoSelector.run();
    SwervePosition.enableVision();
  }

  @Override
  public void autonomousPeriodic() {
    try {
    SwervePosition.update();
    CommandAuto.update();
    Shooter.update();
    } catch (Exception e) {
      System.out.println("oopsies" + e.toString());
      e.printStackTrace();
    }
  }

  @Override
  public void teleopInit() {
    OI.init();
    SwervePosition.setPosition(
        new Vector2(56.78 * (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red ? 1 : -1), -275));
  }

  @Override
  public void teleopPeriodic() {
    try {
      Shooter.update();
      SwervePosition.update();
      OI.userInput();
      BannerLight.updateTeleop();
    } catch (Exception e) {
      System.out.println("oopsies" + e.toString());
      e.printStackTrace();
    }
  }

  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().disable();
  }

  @Override
  public void disabledPeriodic() {
    // SwervePosition.updateAveragePosVision();
    // System.out.println(SwervePosition.getPosition().toString());
    // if(Robot.isReal())
    //   BannerLight.setTagInView(VisionManager.hasTarget());
  }

  @Override
  public void testInit() {
    Intake.disable();
    ShooterPivot.disable();
    Shooter.disable();
  }

  @Override
  public void testPeriodic() {

  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
