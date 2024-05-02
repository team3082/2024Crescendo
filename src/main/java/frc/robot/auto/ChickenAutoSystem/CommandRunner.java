package frc.robot.auto.ChickenAutoSystem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;

/**
 * Command Runner is the schedular for the CAS, it inits, 
 * updates, and finishes the commands. It also handles the 
 * selection of AutoRoutines.
 */
public abstract class CommandRunner{
    /** Auto Selector */
    private static SendableChooser<String> autoSelector = new SendableChooser<String>();

    /** Index of the the current command */
    private static int currentCommandIndex;

    /** Index of the AutoRoutine chosen */
    private static int currentRoutineIndex;

    /**
     * All of the auto routines stored in AutoShell
     * so they can be selected over network tables
     */
    private static ArrayList<AutoShell> allRoutines = new ArrayList<AutoShell>();

    /**
     * This is a wrapper class for a ChickenCommand[] array, 
     * it is meant to simplify selecting autos in telemetry, 
     * and starting command sequences
    */
    private static class AutoShell {
        /**The name that will be sent to the end user */
        private String sendableName;

        /**An array of all the ChickenCommands */
        private ChickenCommand[] commands;
        
        /**A method that inits the routine and supplies the commands */
        private ChickenCommandSupplier initMethod;

        /**
         * Constructor for a AutoShell
         * @param sendableName The Name that represents the commands
         * @param commands The commands that should be run, when this is run
         * @param initMethod The method used to innit the auto routine
         */
        public AutoShell(String sendableName, ChickenCommandSupplier initMethod){
            this.sendableName=sendableName;
            this.initMethod=initMethod;
        }

        /**
         * Constructor for a AutoShell set to No Auto
         */
        public AutoShell(){
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
        allRoutines.add(new AutoShell());
        autoSelector.setDefaultOption(allRoutines.get(0).getName(), allRoutines.get(0).getName());
    }

    /**
     * This methods adds all Auto Routine Methods(@Routine) in the class to the CommandRunner
     * Note that if there is a Routine that 
     *  A. Needs Parameters 
     *  B. Dose not return ChickenCommand[] 
     *  C. Fails to run this method will throw a runtime error
     * A RuntimeException will be thrown
     * @param AutoBundle A generic for the class that has the Auto Routines in it
     * @param commandClass The class with the Auto Routines in it
     */
    public static <AutoBundle> void addBundle(AutoBundle commandClass){
        //Loops through all the methods in the commandClass
        for(Method method : commandClass.getClass().getDeclaredMethods()){
            //If a method has an annotation of Routine, it will add it
            if(method.isAnnotationPresent(AutoRoutine.class)){
                addRoutine(method.getName(), ()->{
                    //This code turns the method into a ChickenCommandSupplier
                    try {
                        //Gets the result of the method
                        Object result = method.invoke(commandClass);
                        
                        //If the result is a ChickenCommand[] it will return it
                        if (result instanceof ChickenCommand[]) {
                          return (ChickenCommand[]) result;
                        } else {
                          throw new RuntimeException("Method doesn't return a ChickenCommand[]");
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
    public static void addRoutine(String sendableName, ChickenCommandSupplier initMethod){
        //Checks to see if the Routine dose not share a name with any other routine
        for(AutoShell shell:allRoutines){
            if(shell.getName().equals(sendableName))
                throw new RuntimeException(sendableName+" is already a routine");
        }

        //Adds the routine
        allRoutines.add(new AutoShell(sendableName, initMethod));
        autoSelector.addOption(sendableName, sendableName);
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
     * then it checks if that command has been finished, if it is finished it moves to the next command. 
     * If there is no more commands it sets the robot speed to zero, and prints that the auto is finished
     */
    public static void update(){
        //If there is No Auto, end the method
        if(allRoutines.get(currentRoutineIndex).getName().equals("No Auto"))
            return;

        //Get the commands from the current routine
        ChickenCommand[] commands = allRoutines.get(currentRoutineIndex).getCommands();

        //Checks to see if the currentCommandIndex is valid
        if(currentCommandIndex<commands.length){
            //Updates the current command
            commands[currentCommandIndex].update();

            //Checks to see if the current command is finished
            if(commands[currentCommandIndex].isFinished()){
                commands[currentCommandIndex].whenFinished(false);
                currentCommandIndex++;

                //Sees if the next command index is valid
                if(currentCommandIndex == commands.length){
                    System.out.println(allRoutines.get(currentRoutineIndex).getName()+" is finished");
                } else {
                    //Inits the next command
                    commands[currentCommandIndex].init();
                }
            }
        }
    }
}