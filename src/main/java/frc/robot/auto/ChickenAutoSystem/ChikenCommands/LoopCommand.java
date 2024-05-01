package frc.robot.auto.ChickenAutoSystem.ChikenCommands;
import java.util.function.BooleanSupplier;

/** 
 * An WaterCommand that loops the commands 
 * it gets a set number of times or until
 * a condition is met. It will run the commands
 * sequentially. In the case it is interuppted, 
 * it will only call a whenFinished on the command running
 */
public class LoopCommand extends ChickenCommand{
    /**An array of the commands to run sequentialy to loop*/
    private ChickenCommand[] commands;      

    //These are variables to handle looping a set amount of times
        /**The amount of times a full loop has been done */
        private int loopPhaseOn;
        /**The amount of times to loop */
        private int loopAmount;

    /**Condition that must be false to loop*/
    private BooleanSupplier condition;

    /**Boolean that is true if it stoped based on a conditiomn */
    private boolean isConditionStop;

    /**The index of the current command */
    private int currentCommandIndex;

    /**
     * Creates a LoopCommand that runs a set number of times
     * @param loopAmount The amount of times to repeate
     * @param commands WaterCommand[] of the commands to loop
     */
    public LoopCommand(int loopAmount, ChickenCommand... commands){
        this.commands=commands;
        this.loopAmount = loopAmount;
        this.isConditionStop=false;
    }

    /**
     * Creates a LoopCommand that runs until a condition
     * @param condition The conditon that must be finished to end looping
     * @param commands WaterCommand[] of the commands to loop
     */
    public LoopCommand(BooleanSupplier condition, ChickenCommand... commands){
        this.condition=condition;
        this.isConditionStop=true;
    }

    /**
     * Inits the loopWave coomand
     */
    @Override
    public void init(){
        isFinished=false;
        loopPhaseOn=0;
    }

    /**
     * Updates the loopWave command
     */
    @Override
    public void update(){
        //Sees if it is based on a condition
        if(isConditionStop==true){
            //If the conditon is true, end the last command and set is finished to true
            if(condition.getAsBoolean()){
                commands[currentCommandIndex].whenFinished(true);
                isFinished=true;
                return;
            }
            
            //Update the commands, and move through them sequentially
            if(currentCommandIndex<commands.length){
                commands[currentCommandIndex].update();
                if(commands[currentCommandIndex].isFinished()){
                    commands[currentCommandIndex].whenFinished(false);
                    currentCommandIndex++;
                    if(currentCommandIndex == commands.length){
                        currentCommandIndex=0;
                        return;
                    }
                    commands[currentCommandIndex].init();
                }
            }
            return;
        }

        //looping a set amount of times, it updates the commands
        commands[currentCommandIndex].update();
        if(commands[currentCommandIndex].isFinished()){
            commands[currentCommandIndex].whenFinished(false);
            currentCommandIndex++;
            if(currentCommandIndex == commands.length){
                currentCommandIndex=0;
                loopPhaseOn++;
                if(loopPhaseOn==loopAmount){
                    isFinished=true;
                    return;
                }
            }
        }
    }
}

