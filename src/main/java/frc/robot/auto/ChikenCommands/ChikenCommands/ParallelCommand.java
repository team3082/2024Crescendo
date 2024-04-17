package frc.robot.auto.ChikenCommands.ChikenCommands;

/**
 * An Water Command that allows two commands to run parallel
 */
public class ParallelCommand extends ChickenCommand{
    /**An array of the commands to run parallel to eachother */
    private ChickenCommand[] commands;

    /**
     * Constructor for a ParallelCommand
     * @param commands The commands to run parallel to eachother
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
     * Finishs the ParallelCommand
     */
    @Override
    public void whenFinished(boolean interuppted) {
        if(!interuppted) return;
        for(ChickenCommand command : commands){
            if(!command.isFinished()){
                command.whenFinished(true);
            }
        } 
    }
}
