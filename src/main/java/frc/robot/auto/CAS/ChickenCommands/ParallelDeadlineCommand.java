package frc.robot.auto.CAS.ChickenCommands;

/**
 * A command that runs other commands parallel until a deadline command finishes
 */
public class ParallelDeadlineCommand extends ChickenCommand{
    private ChickenCommand deadline;
    private ChickenCommand[] commands;

    public ParallelDeadlineCommand(ChickenCommand deadline, ChickenCommand... commands){
        this.deadline = deadline;
        this.commands = commands;
    }

    @Override
    public void init(){
        isFinished=false;
        deadline.init();
        for(ChickenCommand command : commands) command.init();
    }

    @Override
    public void update(){
        deadline.update();
        for(ChickenCommand command : commands){
            if(!command.isFinished()){
                command.update();
                if(command.isFinished()){
                    command.whenFinished(false);
                }
            }
        }
    
        if(deadline.isFinished()){
            deadline.whenFinished(false);
            isFinished=true;
            for(ChickenCommand command : commands){
                if(!command.isFinished()){
                    command.whenFinished(true);
                }
            }
        }  
    }

    @Override
    public void whenFinished(boolean interrupted) {
        if(!interrupted) return;
        if(!deadline.isFinished()) deadline.whenFinished(true);
        
        for(ChickenCommand command : commands){
            if(!command.isFinished){
                command.whenFinished(true);
            }
        } 
    }
}
