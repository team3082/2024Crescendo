package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;

public class SetIntake extends Command {

  public SetIntake() {}

  @Override
  public void initialize() {
    Intake.autoSuck();
  }

  @Override
  public void execute() {
    Intake.autoSuck();
  }

  @Override
  public boolean isFinished() {
    return Intake.reallyHasPiece;
  }

  @Override
  public void end(boolean interrupted) {
    Intake.reallyHasPiece = false;
    Intake.suckTime = 0;
    Intake.setState(IntakeState.STOW);
    Intake.no();
  }
}
