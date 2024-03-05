// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import eggshell.constructors.absoluteEncoder.CANCoderP6;
import eggshell.constructors.absoluteEncoder.AbsoluteEncoder.Direction;
import eggshell.constructors.gyro.Pigeon2P6;
import eggshell.constructors.limitswitch.ThriftyHallEffect;
import eggshell.constructors.motor.ctre.CTRETalonFX;
import eggshell.constructors.motor.rev.RevSparkMax;
import eggshell.constructors.swerve.SwerveConstants;
import eggshell.constructors.swerve.SwerveManager;
import eggshell.constructors.swerve.SwerveModule;
import eggshell.constructors.timeofflight.LaserCAN;
import eggshell.constructors.vision.PhotonCamera;
import eggshell.constructors.vision.VisionManager;
import eggshell.utils.Vector2;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.TimedRobot;
public class Robot extends TimedRobot {
	public static SwerveManager swerveManager;
	public static Shooter shooter;
	public static Intake intake;
	public static Climber climber;
	public static VisionManager visionManager;
	public static OI input;

	@Override
	public void robotInit() {
		// ensure everything starts up properly
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Swerve
		swerveManager = new SwerveManager(
			new SwerveModule(
				new CTRETalonFX(Constants.Swerve.FL_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new CTRETalonFX(Constants.Swerve.FL_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FL_STEER_ID, Constants.Swerve.FL_OFFSET, Direction.CLOCKWISE)
			), 
			new SwerveModule(
				new CTRETalonFX(Constants.Swerve.FR_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new CTRETalonFX(Constants.Swerve.FR_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FR_STEER_ID, Constants.Swerve.FR_OFFSET, Direction.CLOCKWISE)
			), 
			new SwerveModule(
				new CTRETalonFX(Constants.Swerve.BL_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new CTRETalonFX(Constants.Swerve.BL_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.BL_STEER_ID, Constants.Swerve.BL_OFFSET, Direction.CLOCKWISE)
			), 
			new SwerveModule(
				new CTRETalonFX(Constants.Swerve.BR_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new CTRETalonFX(Constants.Swerve.BR_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.BR_STEER_ID, Constants.Swerve.BR_OFFSET, Direction.CLOCKWISE)
			), 
			new Pigeon2P6(Constants.Swerve.GYRO_ID), 
			new SwerveConstants(new Vector2(26, 26), SwerveConstants.SwerveModule.MK4I_L3)
		);

		// Shooter
		shooter = new Shooter(
			new CTRETalonFX(Constants.Shooter.TOP_FLYWHEEL_ID, Constants.Shooter.TOP_FLYWHEEL_CONFIG),
			new CTRETalonFX(Constants.Shooter.BOTTOM_FLYWHEEL_ID, Constants.Shooter.BOTTOM_FLYWHEEL_CONFIG),
			new CTRETalonFX(Constants.Shooter.PIVOT_ID, Constants.Shooter.PIVOT_CONFIG),
			new CANCoderP6(Constants.Shooter.PIVOT_ID, Constants.Shooter.PIVOT_OFFSET, Direction.CLOCKWISE)
		);

		// Intake
		intake = new Intake(
			new RevSparkMax(0, null),
			new RevSparkMax(0, null),
			new CTRETalonFX(0, null),
			new LaserCAN()
		);

		// Climber
		climber = new Climber(
			new CTRETalonFX(0, null),
			new ThriftyHallEffect(0),
			new CTRETalonFX(0, null),
			new ThriftyHallEffect(1)
		);

		// Vision
		visionManager = new VisionManager(
			new PhotonCamera[] {}
		);
	}

	@Override
	public void robotPeriodic() {}

	@Override
	public void autonomousInit() {}

	@Override
	public void autonomousPeriodic() {}

	@Override
	public void teleopInit() {}

	@Override
	public void teleopPeriodic() {
		input.update();
	}

	@Override
	public void disabledInit() {}

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
