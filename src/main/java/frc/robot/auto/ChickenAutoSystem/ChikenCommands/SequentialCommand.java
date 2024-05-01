package frc.robot.auto.ChickenAutoSystem.ChikenCommands;

/**
 * A ChickenCommand that has commands run sequentialy, 
 * useful for more complicated logic
 */
public class SequentialCommand extends ChickenCommand{
    /**An array of the commands to run sequentially*/
    private ChickenCommand[] commands;

    /**The index of the current command*/
    private static int currentCommandIndex;

    /**
     * Constructor for a SequentialCommand
     * @param commands The commands to run sequentialy
     */
    public SequentialCommand(ChickenCommand... commands){
        this.commands=commands;
    }

    /**
     * Inits the SequentialCommand 
     */
    public void init(){
        isFinished=false;
        currentCommandIndex=0;
        commands[0].init();
    }

    /**
     * Updates the SequentialCommand
     */
    public void update(){
        if(currentCommandIndex<commands.length){
            commands[currentCommandIndex].update();
            if(commands[currentCommandIndex].isFinished()){
                commands[currentCommandIndex].whenFinished(false);
                currentCommandIndex++;
                if(currentCommandIndex == commands.length){
                    isFinished=true;
                } else {
                    commands[currentCommandIndex].init();
                }
            }
        }
    }

    /**
     * Runs when the command is finished
     */
    public void whenFinished(boolean interuppted) {
       if(interuppted) commands[currentCommandIndex].whenFinished(true);
    }
}
