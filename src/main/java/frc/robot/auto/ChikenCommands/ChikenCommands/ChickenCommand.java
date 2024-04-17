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
     * Decorator that makes the command print something when it is run
     * @param text The text to print
     * @return Returns a ParallelCommand of the command and a debug command with only an init print
     */
    public ChickenCommand printWhenRun(String text){
        return new ParallelCommand(this, new DebugCommand(text));
    }
}
