package frc.robot.auto.ChickenAutoSystem.ChikenCommands;

import java.util.function.BooleanSupplier;

/**
 * An if command that runs a true command if a condition is true 
 * and a false command of a condtion is not true. It can also only run
 * code if it is true and move on to the next command if the condition is not met
 */
public class GateCommand extends ChickenCommand {
    /**Condition to see if you need to run trueCommand or falseCommand */
    BooleanSupplier condition;

    /** A boolean to keep track of what the condition was at the start */
    boolean gate;

    /**The command to run if the condition is true */
    ChickenCommand trueCommand;

    /**The command to run if the condition is false */
    ChickenCommand falseCommand;

    /**
     * Creates a WaterGate with two paths
     * @param trueCommand WaterCommand to run if the condition is true
     * @param falseCommand  WaterCommand to run if the condition is false
     * @param condition Condition to found out what is run
     */
    public GateCommand(ChickenCommand trueCommand, ChickenCommand falseCommand, BooleanSupplier condition){
        this.condition=condition;
        this.trueCommand=trueCommand;
        this.falseCommand=falseCommand;
    }

    /**
     * Creates a WaterGate with one path
     * @param trueCommand WaterCommand to run if the condition is true
     * @param condition Condition to found out what is run
     */
    public GateCommand(ChickenCommand trueCommand, BooleanSupplier condition){
        this.condition=condition;
        this.trueCommand=trueCommand;
    }

    /**
     * Inits the WaterGate based on the condition
     */
    @Override
    public void init(){
        //IsFinshed is set to false
        isFinished=false;
        
        //Sees if the condition is true
        if(condition.getAsBoolean()){
            //
            gate=true;
            trueCommand.init();
        } else {
            if(falseCommand != null){
                gate=false;
                falseCommand.init();
            } else {
                isFinished=true;
            }
        }
    }

    /**
     * Updates the command
     */
    public void update(){
        if(isFinished) return;
        if(gate){
            trueCommand.update();
        } else {
            falseCommand.update();
        }
    }

    /**
     * Sees if the command is finished
     * @return A boolean that is true if the command is finsihed
     */
    public boolean isFinished(){ 
        if(isFinished) return true;
        if(gate) return(trueCommand.isFinished);
        return(falseCommand.isFinished);
    }

    /**
     * Final code to run when the command is finished
     */
    public void whenFinished(boolean interuppted){
        if(gate){
            trueCommand.whenFinished(interuppted);
        } else {
            if(falseCommand != null){
                falseCommand.whenFinished(interuppted);
            }
        }
    }
}
