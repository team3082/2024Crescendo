package frc.robot.auto.ChikenCommands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
        private ChikenCommandSupplier initMethod;

        /**
         * Constructor for a Sea Wrapper
         * @param sendableName The Name that represents the commands
         * @param commands The commands that should be run, when this is run
         * @param initMethod The method used to innit the auto rountine, 
         * this is meant to handel stuff such as reseting odometry
         */
        public AutoRoutine(String sendableName, ChikenCommandSupplier initMethod){
            this.sendableName=sendableName;
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
            this.commands = initMethod.getCommands();
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
        autoSelector.setDefaultOption(allRoutines.get(0).getName(), allRoutines.get(0).getName());
    }

    /**
     * This methods adds all Auto Routine Methods(@Routine) in the class to the CommandRunner
     * Note that if there is a Routine that 
     *  A. Needs Paramters 
     *  B. Dose not return ChikenCommand[] 
     *  C. Fails to run this method will throw a runtime error
     * A RuntimeException will be thrown
     * @param AutoBundle A generic for the class that has the Auto Routines in it
     * @param commandClass The class with the Auto Routines in it
     */
    public static <AutoBundle> void addRoutine(AutoBundle commandClass){
        //Loops through all the methods in the commandClass
        for(Method method : commandClass.getClass().getDeclaredMethods()){
            //If a method has an annotation of Routine, it will add it
            if(method.isAnnotationPresent(Routine.class)){
                addRoutine(method.getName(), ()->{
                    //This code turns the method into a ChikenCommandSupplier
                    try {
                        //Gets the result of the method
                        Object result = method.invoke(commandClass);
                        
                        //If the result is a ChickenCommand[] it will return it
                        if (result instanceof ChickenCommand[]) {
                          return (ChickenCommand[]) result;
                        } else {
                          throw new RuntimeException("Method doesn't return a ChikenCommand[]");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error invoking method", e);
                    }
                });
            }
        }
    }

    /**
     * Adds a Routine to the commandRunner class
     */
    public static void addRoutine(String sendableName, ChikenCommandSupplier initMethod){
        allRoutines.add(new AutoRoutine(sendableName, initMethod));
        autoSelector.addOption(allRoutines.get(allRoutines.size()-1).getName(), allRoutines.get(allRoutines.size()-1).getName());
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