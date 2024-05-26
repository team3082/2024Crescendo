package frc.robot.auto.CAS.ChickenCommands;

/**Runs a Runnable for the command */
public class InstantCommand extends ChickenCommand{
    Runnable command;

    public InstantCommand(Runnable command){
        this.command=command;
    }

    @Override
    public void init(){
        this.isFinished=false;
        command.run();
    }

    public void update(){
        this.isFinished=true;
    }
}
