package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;

public class SetIntake extends ChickenCommand {
    double simDelay;
    frc.robot.auto.autoframe.SetIntake setIntake;

    public SetIntake(){
        setIntake = new frc.robot.auto.autoframe.SetIntake();
    }

    @Override
    public void init() {
        isFinished=false;
        setIntake.done=false;
        setIntake.start();
        Intake.reallyHasPiece = false;
    }

    @Override
    public void update() {        
        setIntake.update();
        System.out.println("set intake updating");
    }

    @Override
    public boolean isFinished(){
        return setIntake.done;
    }

    @Override
    public void whenFinished(boolean interrupted){
        System.out.println("set intake done");
        Intake.no();
        Intake.reallyHasPiece = false;
    }
}
