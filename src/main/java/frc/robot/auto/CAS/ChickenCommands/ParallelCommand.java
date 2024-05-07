package frc.robot.auto.CAS.ChickenCommands;

/**
 * An Water Command that allows two commands to run parallel
 */
public class ParallelCommand extends ChickenCommand{
    /**An array of the commands to run parallel to each other */
    private ChickenCommand[] commands;

    /**
     * Constructor for a ParallelCommand
     * @param commands The commands to run parallel to each other
     */
    public ParallelCommand(ChickenCommand... commands){
        this.commands = commands;
    }

    /**
     * Inits the ParallelCommand 
     */
    @Override
    public void init(){
        isFinished=false;
        for(ChickenCommand command : commands) command.init();
    }

    /**
     * Updates the ParallelCommand
     */
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

    /**
     * Finishes the ParallelCommand
     */
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
