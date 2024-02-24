package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;

import static frc.robot.subsystems.shooter.Intake.IntakeState.*;
import frc.robot.utils.RTime;

public class SetIntake extends Command {
    double simDelay;
    frc.robot.auto.autoframe.SetIntake setIntake;

    public SetIntake(IntakeState state){
        setIntake = new frc.robot.auto.autoframe.SetIntake(state);
    }

    @Override
    public void initialize() {
        setIntake.start();
    }

    @Override
    public void execute() {        
        setIntake.update();
    }

    @Override
    public boolean isFinished(){
        return setIntake.done;
    }
}
