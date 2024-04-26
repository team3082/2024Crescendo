package frc.robot.auto.ChikenCommands.ChikenCommands;

import java.util.function.BooleanSupplier;

/**
 * Super class of all ChikenCommands
 */
public abstract class ChickenCommand {
    protected boolean isFinished;
    
    /**
     * Constructor for the ChikenCommand
     */
    public ChickenCommand(){}

    /**
     * Inits the command 
     */
    public void init(){};
    /**
     * Updates the command
     */
    public void update(){};

    /**
     * Sees if the command is finished
     * @return A boolean that is true if the command is finsihed
     */
    public boolean isFinished(){ 
        return isFinished; 
    }

    /**
     * Final code to run when the command is finished
     * @param interuppted If the command was ending without finishing
     */
    public void whenFinished(boolean interuppted){};

    /**
     * Decorator that makes the command run only if a conditon is met
     * @param Condition A BooleanSupplier that will make the command only run if it is true
     * @return Returns a 1 path GateCommand
     */
    public ChickenCommand onlyIf(BooleanSupplier Condition){
        return new GateCommand(this, Condition);
    }

    /**
     * Decorator that makes a sequential commands with the current command and others you specifiy
     * @param commands The commands to run after
     * @return Returns a SequentialCommand of all the commands given
     */
    public ChickenCommand andThen(ChickenCommand... commands){
        ChickenCommand[] allCommands = new ChickenCommand[commands.length+1];
        allCommands[0] = this;
        for(int index = 1; index<allCommands.length; index++){
            allCommands[index] = commands[index-1];
        }
        return new SequentialCommand(allCommands);
    }

    /**
     * Decorator that makes the command run with other commands
     * @param text The commands to run with
     * @return Returns a ParallelCommand of the command and all commands given
     */
    public ChickenCommand alongWith(ChickenCommand... commands){
        ChickenCommand[] allCommands = new ChickenCommand[commands.length+1];
        allCommands[0] = this;
        for(int index = 1; index<allCommands.length; index++){
            allCommands[index] = commands[index-1];
        }
        return new ParallelCommand(allCommands);
    }

    /**
     * Decorator that makes the command print something when it is run
     * @param text The text to print
     * @return Returns a ParallelCommand of the command and a debug command with only an init print
     */
    public ChickenCommand printWhenRun(String text){
        return new ParallelCommand(this, new DebugCommand(text));
    }
}
