package frc.robot.openhouse;

public class OpenHouse {

    //An example auto
    public void exampleAuto(){
        robot.moveForward(36);
        robot.moveForward(36);
        robot.turnRight(120);
        robot.moveForward(72);
        robot.turnRight(120);
        robot.moveForward(72);
        robot.turnRight(120);
        robot.moveForward(36);
    }

    
    //Another example auto
    public void doASquare(){
        robot.moveForward(90);
        robot.turnRight(90);
        robot.moveForward(90);
        robot.turnRight(90);
        robot.moveForward(90);
        robot.turnRight(90);
        robot.moveForward(90);
        robot.turnRight(90);
    }

    //Make your own Auto Routine by naming it and coding it
    public void enterYourOwnNameIdea(){
        //The four method robot has is moveFoward(inches), turnRight(degrees), and turnLeft(degrees)
        robot.moveForward(50);
    }

}