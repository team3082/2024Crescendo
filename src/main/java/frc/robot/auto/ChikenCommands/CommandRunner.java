package frc.robot.auto.ChikenCommands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;

public abstract class CommandRunner{
    //Auto Selector
    private static SendableChooser<String> autoSelector = new SendableChooser<String>();

    /**
     * Index of the the current command
     */
    private static int currentCommandIndex;

    /**
     * Index of the AutoRoutine chosen
     */
    private static int currentRoutineIndex;

    /**
     * All of the auto commands stored in a Sea objects 
     * so they can be selected over network tables
     */
    private static ArrayList<AutoRoutine> allRoutines = new ArrayList<AutoRoutine>();

    /**
     * This is a wrapper class for a WaterCommand array, 
     * it is meant to simplify selecting autos in telemetry, 
     * and initing command sequences
     */
    private static class AutoRoutine {
        private String sendableName;
        private ChickenCommand[] commands;
        private Runnable initMethod;

        /**
         * Constructor for a Sea Wrapper
         * @param sendableName The Name that represents the commands
         * @param commands The commands that should be run, when this is run
         * @param initMethod The method used to innit the auto rountine, 
         * this is meant to handel stuff such as reseting odometry
         */
        public AutoRoutine(String sendableName, ChickenCommand[] commands, Runnable initMethod){
            this.sendableName=sendableName;
            this.commands=commands;
            this.initMethod=initMethod;
        }

        public AutoRoutine(){
            this.sendableName="No Auto";
        }
        
        /**
         * Inits the autoRoutine
         */
        public void initRoutine(){
            if(this.sendableName=="No Auto") 
                return;
            initMethod.run();
            commands[0].init();
        }

        /**
         * Gets the name of the Routine
         * @return A string of the name
         */
        public String getName(){
            return sendableName;
        }

        /**
         * Gets the commands of the Routine
         * @return ChickenCommand[] of the commands
         */
        public ChickenCommand[] getCommands(){
            return commands;
        }
    }
    
    /**
     * Inits the CommandRunner class
     */
    public static void Init(){
        allRoutines.add(new AutoRoutine());
    }

    /**
     * Inits the CommandRunner class
     */
    public static void addRoutine(String sendableName, ChickenCommand[] commands, Runnable initMethod){
        allRoutines.add(new AutoRoutine(sendableName, commands, initMethod));
        updateSelector();
    }

    /**
     * Updates the auto selector and adds the routines sendable names as options
     */
    private static void updateSelector(){
        autoSelector.setDefaultOption(allRoutines.get(0).getName(), allRoutines.get(0).getName());
        for(int index = 1; index<allRoutines.size(); index++){
            autoSelector.addOption(allRoutines.get(index).getName(), allRoutines.get(index).getName());
        }
    }

    /**
     * Gets the index of the auto routine selected
     * @return A int of the auto routine
     */
    private static int getSelectedAuto(){
        String selected = autoSelector.getSelected();
        for(int index = 0; index<allRoutines.size(); index++){
            if(selected.equals(allRoutines.get(index).getName())) return index;
        }
        return -1;
    }   

    /**
     * Gets an autoSelector
     * @return SendableChooser<String> that is the autoSelector
     */
    public static SendableChooser<String> getSelector(){
        return autoSelector;
    }

    /**
     * Starts the CommandRunner class to run an auto
     */
    public static void RoutineInit(){
        currentCommandIndex=0;
        currentRoutineIndex=getSelectedAuto();
        allRoutines.get(currentRoutineIndex).initRoutine();
    }

    /**
     * Updates and runs through all the commands sequentially. It firsts update the command that the robot is on, 
     * then it checks if that command has been finshed, if it is finished it moves to the next command. 
     * If there is no more commands it sets the robot speed to zero, and prints that the auto is finished
     */
    public static void update(){
        if(allRoutines.get(currentRoutineIndex).getName().equals("No Auto"))
            return;
        ChickenCommand[] commands = allRoutines.get(currentRoutineIndex).getCommands();
        if(currentCommandIndex<commands.length){
            commands[currentCommandIndex].update();
            if(commands[currentCommandIndex].isFinished()){
                commands[currentCommandIndex].whenFinished(false);
                currentCommandIndex++;
                if(currentCommandIndex == commands.length){
                    System.out.println(allRoutines.get(currentRoutineIndex).getName()+" is finished");
                } else {
                    commands[currentCommandIndex].init();
                }
            }
        }
    }
}