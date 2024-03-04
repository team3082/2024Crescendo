// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import eggshell.constructors.absoluteEncoder.CANCoderP6;
import eggshell.constructors.gyro.Pigeon2P6;
import eggshell.constructors.motor.TalonFXP6;
import eggshell.constructors.swerve.SwerveConstants;
import eggshell.constructors.swerve.SwerveManager;
import eggshell.constructors.swerve.SwerveModule;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.TimedRobot;
public class Robot extends TimedRobot {
	public SwerveManager swerveManager;
	public Shooter shooter;
	public Intake intake;
	public Climber climber;

	@Override
	public void robotInit() {
		try {
		Thread.sleep(5000);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}

		// Swerve
		this.swerveManager = new SwerveManager(
			new SwerveModule(
				new TalonFXP6(Constants.Swerve.FL_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new TalonFXP6(Constants.Swerve.FL_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FL_STEER_ID)
			), 
			new SwerveModule(
				new TalonFXP6(Constants.Swerve.FR_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new TalonFXP6(Constants.Swerve.FR_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FL_STEER_ID)
			), 
			new SwerveModule(
				new TalonFXP6(Constants.Swerve.BL_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new TalonFXP6(Constants.Swerve.BL_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FL_STEER_ID)
			), 
			new SwerveModule(
				new TalonFXP6(Constants.Swerve.BR_STEER_ID, Constants.Swerve.STEER_CONFIG),
				new TalonFXP6(Constants.Swerve.BR_DRIVE_ID, Constants.Swerve.DRIVE_CONFIG),
				new CANCoderP6(Constants.Swerve.FL_STEER_ID)
			), 
			new Pigeon2P6(), 
			new SwerveConstants(26, 26, SwerveConstants.SwerveModule.MK4I_L3)
		);

		// Shooter


		// Intake


		// Climber
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
	public void teleopPeriodic() {}

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
