package frc.robot.auto.CAS.ChickenCommands;

/**
 * A command that runs other commands parallel until a deadline command finishes
 */
public class ParallelDeadlineCommand extends ChickenCommand{
    /**An array of the commands to run parallel to each other */
    private ChickenCommand[] commands;

    /**The deadline command */
    private ChickenCommand deadline;

    /**
     * Constructor for a ParallelDeadlineCommand
     * @param commands The commands to run parallel to each other
     */
    public ParallelDeadlineCommand(ChickenCommand deadline, ChickenCommand... commands){
        this.deadline = deadline;
        this.commands = commands;
    }

    /**
     * Inits the ParallelDeadlineCommand 
     */
    @Override
    public void init(){
        isFinished=false;
        deadline.init();
        for(ChickenCommand command : commands) command.init();
    }

    /**
     * Updates the ParallelDeadlineCommand
     */
    @Override
    public void update(){
        if(deadline.isFinished()){
            deadline.whenFinished(false);
            isFinished=true;
            for(ChickenCommand command : commands){
                if(!command.isFinished()){
                    command.whenFinished(true);
                }
            }
        } else {
            deadline.update();
            for(ChickenCommand command : commands){
                if(!command.isFinished()){
                    command.update();
                    if(command.isFinished()){
                        command.whenFinished(false);
                    }
                }
            }
        }
        
    }

    /**
     * Finishes the ParallelDeadlineCommand
     */
    @Override
    public void whenFinished(boolean interrupted) {
        if(!interrupted) return;
        if(!deadline.isFinished) deadline.whenFinished(true);
        
        for(ChickenCommand command : commands){
            if(!command.isFinished){
                command.whenFinished(true);
            }
        } 
    }
}
