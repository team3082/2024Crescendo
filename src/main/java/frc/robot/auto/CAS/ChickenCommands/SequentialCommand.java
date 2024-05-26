package frc.robot.auto.CAS.ChickenCommands;

/**
 * A ChickenCommand that has commands run sequentially
 */
public class SequentialCommand extends ChickenCommand{
    private ChickenCommand[] commands;
    private static int currentCommandIndex;

    public SequentialCommand(ChickenCommand... commands){
        this.commands=commands;
    }

    public void init(){
        isFinished=false;
        currentCommandIndex=0;
        commands[0].init();
    }

    public void update(){
        if(!(currentCommandIndex<commands.length)) return;
    
        commands[currentCommandIndex].update();
        if(commands[currentCommandIndex].isFinished()){
            commands[currentCommandIndex].whenFinished(false);
            currentCommandIndex++;

            boolean wasLastCommand = currentCommandIndex == commands.length;
            if(wasLastCommand){
                isFinished=true;
                return;
            }
                
            commands[currentCommandIndex].init();
        }
    }

    public void whenFinished(boolean interrupted) {
       if(interrupted) commands[currentCommandIndex].whenFinished(true);
    }
}
