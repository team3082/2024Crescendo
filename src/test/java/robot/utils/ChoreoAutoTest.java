package robot.utils;

import frc.robot.utils.auto.Auto;

import org.junit.jupiter.api.Test;

public class ChoreoAutoTest {
    

    @Test
    public void loadAutosTest(){
        try{
            Auto.loadAutos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
