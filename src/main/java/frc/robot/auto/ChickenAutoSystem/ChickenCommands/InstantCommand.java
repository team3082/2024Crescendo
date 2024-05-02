package frc.robot.auto.ChickenAutoSystem.ChickenCommands;

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
