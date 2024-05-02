# Chicken Auto System

The Chicken Auto System(CAS for short) is Team3082 custom auto framework.

## CommandRunner

Command Runner is the schedular for the CAS, it inits, updates, and finishes the commands. It also handles the selection of AutoRoutines.

### Methods
##### update()
Updates and runs through all the commands in the current auto routine sequentially. It firsts update the command that the robot is on, then it checks if that command has been finished, if it is finished it moves to the next command. 
If there is no more commands it prints that the auto is finished

##### getSelector()
Gets the SendableChooser of the auto routines

#### Adding Routines
##### @AutoRoutine
This is an interface annotation that is used above a method that is an auto routine.
It is used with the **addBundle** method of CommandRunner. It signifies that a method is an
AutoRoutine and that CommandRunner should automatically add it to the list of auto routines. 
**Warning** this interface annotation will throw an runtime exception if two methods have the same name, 
the method needs parameters, or if it dose not return a ChickenCommand[].

Example of @AutoRoutine with the onePieceMiddle routine from the 2024 game season
```java
@AutoRoutine
public ChickenCommand[] onePieceMiddle(){
    SwervePosition.setPosition(
    new Vector2(56.78 * (DriverStation.getAlliance().      
        isPresent() && DriverStation.getAlliance().get() 
        == Alliance.Red ? 1 : -1), -275));
    Pigeon.setYaw(90);
    Intake.setState(IntakeState.FEED);
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
```
