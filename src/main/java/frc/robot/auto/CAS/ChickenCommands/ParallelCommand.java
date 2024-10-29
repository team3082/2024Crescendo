package frc.robot.auto.CAS.ChickenCommands;

/**
 * An Command that allows commands to run parallel
 */
public class ParallelCommand extends ChickenCommand{
    private ChickenCommand[] commands;

    public ParallelCommand(ChickenCommand... commands){
        this.commands = commands;
    }

    @Override
    public void init(){
        isFinished=false;
        for(ChickenCommand command : commands) command.init();
    }

    @Override
    public void update(){
        isFinished = true;
        for(ChickenCommand command : commands){
            if(!command.isFinished()){
                command.update();
                isFinished=false;
            }
        } 
    }
    
    @Override
    public void whenFinished(boolean interrupted) {
        if(!interrupted) return;
        for(ChickenCommand command : commands){
            if(!command.isFinished()){
                command.whenFinished(true);
            }
        } 
    }
}
