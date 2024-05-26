package frc.robot.auto.CAS.ChickenCommands;

import java.util.function.BooleanSupplier;

/**
 * An if command that runs a true command if a condition is true 
 * and a false command of a condition is not true. It can also only run
 * code if it is true and move on to the next command if the condition is not met
 */
public class GateCommand extends ChickenCommand {
    BooleanSupplier condition;
    boolean gate;

    ChickenCommand trueCommand;
    ChickenCommand falseCommand;

    /**
     * Creates a GateCommand with two paths
     * @param trueCommand ChickenCommand to run if the condition is true
     * @param falseCommand ChickenCommand to run if the condition is false
     * @param condition Condition to found out what is run
     */
    public GateCommand(ChickenCommand trueCommand, ChickenCommand falseCommand, BooleanSupplier condition){
        this.condition=condition;
        this.trueCommand=trueCommand;
        this.falseCommand=falseCommand;
    }

    /**
     * Creates a GateCommand with one path
     * @param trueCommand ChickenCommand to run if the condition is true
     * @param condition Condition to found out what is run
     */
    public GateCommand(ChickenCommand trueCommand, BooleanSupplier condition){
        this.condition=condition;
        this.trueCommand=trueCommand;
    }


    @Override
    public void init(){
        isFinished=false;
        
        if(condition.getAsBoolean()){
            gate=true;
            trueCommand.init();
            return;
        }

        if(falseCommand != null){
            gate=false;
            falseCommand.init();
            return;
        }

        isFinished=true;
    }

  
    public void update(){
        if(isFinished) return;
        if(gate){
            trueCommand.update();
            return;
        }

        falseCommand.update();
    }

    public boolean isFinished(){ 
        if(isFinished) return true;
        if(gate) return(trueCommand.isFinished);
        return(falseCommand.isFinished);
    }

 
    public void whenFinished(boolean interrupted){
        if(gate){
            trueCommand.whenFinished(interrupted);
            return;
        }

        if(falseCommand != null){
            falseCommand.whenFinished(interrupted);
        }
    }
}
