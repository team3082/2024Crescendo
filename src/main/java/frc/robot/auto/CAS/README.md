# Chicken Auto System

The Chicken Auto System(CAS for short) is Team3082 custom auto framework.

## Commands
### Chicken Command
This is the core class of the CAS. It is an abstract class from which all auto commands inherit. It has three main methods.
- init: This method inits the Command, it is required to be overridden in all subclasses of ChickenCommand
- update: This method updates the Command, it is not required to be overridden in all subclasses of ChickenCommand, but is highly recommend
- isFinished: This method tells the CommandRunner if the command is finished, unless overridden it will return the instance variable isFinished
- whenFinished: This method executes code needed for the command to be ended, you want to put your end code in here as this will also be run when the command is interrupted. The method takes in a boolean if this command was interrupted.

Futhermore, each Chicken Command has an instance variable called isFinished that is a boolean. This should be set to its current state at the end of the update method when you are overriding it

### Other Commands
CAS has many other helpful commands to make routines with

## Command Runner
Command Runner is the schedular for the CAS, it selects, adds, inits, updates, and finishes the auto routines. This part of the documentation will be going over its methods, and how you are intended to interact with it. 

### Methods for Adding Routines
<!-- #### addRoutine()
Add routine adds a new auto routine to the command runner routine list. It takes in a string that will be the name sent over telemetry to select the routine, it also's takes in a functional interface called ChickenCommandSupplier. You must provide a method that returns a ChickenCommand[] and also has an initialization code for the routine.
Example of addRoutine with the onePieceMiddle routine from the 2024 game season in the CommandAuto class
```java
public ChickenCommand[] onePieceMiddle(){
    //Code that initialization the routine by setting the intake and location
    new Vector2(56.78 * (DriverStation.getAlliance().      
        isPresent() && DriverStation.getAlliance().get() 
        == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    Intake.setState(IntakeState.FEED);

    //This is where your commands are, you must return it
    return new ChickenCommand[]{
        new SetIntakeFeedPos(),
        new WaitCommand(0.2),
        new ParallelCommand(
           new SetShooterAngle(Math.toRadians(57)),
           new SetShooterVelocity(3500)
        ),
        new FireShooter(),
        new WaitCommand(9.5),
        new ChoreoFollow("2 Piece Middle.1", 1.0)
    };
} 

//You would have an init method where you would add routines
public void init(){
    CommandRunner.addRoutine("onePieceMiddle", CommandAuto::onePieceMiddle)
}
``` -->

<!-- This way of adding routines however is inconvenient, you have to have multiple lines of addRoutines and it is tedious to do. The intended way of adding routines is with the methods shown below.

##### @AutoRoutine and addBundle -->
#### @AutoRoutine
This is an interface annotation that is used above a method that is an auto routine. It is used with the **addBundle** method of CommandRunner. It signifies that a method is an AutoRoutine and that CommandRunner should automatically add it to the list of auto routines. **Warning** this interface annotation will throw an runtime exception if two methods have the same name, the method needs parameters, or if it dose not return a ChickenCommand[]. Futhermore, the method must be public and should not be static.

#### addBundle
This is used to add auto routines to CommandRunner. It takes in a class(any type), it will look through all methods with @AutoRoutine and then add them to CommandRunner's list of routines

```java
@AutoRoutine
public ChickenCommand[] onePieceMiddle(){
    //Code that initialization the routine by setting the intake and location
    new Vector2(56.78 * (DriverStation.getAlliance().      
        isPresent() && DriverStation.getAlliance().get() 
        == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    Intake.setState(IntakeState.FEED);

    //This is where your commands are, you must return it
    return new ChickenCommand[]{
        new SetIntakeFeedPos(),
        new WaitCommand(0.2),
        new ParallelCommand(
           new SetShooterAngle(Math.toRadians(57)),
           new SetShooterVelocity(3500)
        ),
        new FireShooter(),
        new WaitCommand(9.5),
        new ChoreoFollow("2 Piece Middle.1", 1.0)
    };
} 

//You would have an init method for the class where you would make this method call
public void init(){
    CommandRunner.addBundle(new CommandAuto());
}
```
<!-- The benefits of this way of adding routines may not seem apparent in this example, but in large projects with dozens of routines, it rapidly saves on space and increases readability. -->


#### routineInit()
Starts the CommandRunner class to run the selected auto routine, this would be called in auto init

#### update()
It updates and runs through all the commands in the current auto routine sequentially. It firsts update the command that CommandRunner is on, then it checks if that command has been finished, if it is finished it moves to the next command. This would be called in Auto Periodic. 
#### getSelector()
Gets the SendableChooser of the auto routines, this SendableChooser is how you select auto routines.
