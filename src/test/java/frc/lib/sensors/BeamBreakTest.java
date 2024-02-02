package frc.lib.sensors;

import org.junit.jupiter.api.Test;

import frc.robot.sensors.Beambreak;

import static org.junit.jupiter.api.Assertions.*;

public class BeamBreakTest {

    @Test
    public void tOFTooFarTest(){
        TimeOfFlight tof = new TimeOfFlight(){
            @Override
            public double getDist(){
                return 2;
            }

            @Override
            public boolean goodReading(){
                return true;
            }
        };

        BeamBreak bb = BeamBreakFactory.create(tof, 0,1);
        assertFalse(bb.isBroken());
    }

    @Test
    public void tOFTooCloseTest(){
        TimeOfFlight tof = new TimeOfFlight(){
            @Override
            public double getDist(){
                return 2;
            }

            @Override
            public boolean goodReading(){
                return true;
            }
        };

        BeamBreak bb = BeamBreakFactory.create(tof, 3,5);
        assertFalse(bb.isBroken());
    }

    @Test
    public void tOFBadSensorTest(){
        TimeOfFlight tof = new TimeOfFlight(){
            @Override
            public double getDist(){
                return 2;
            }

            @Override
            public boolean goodReading(){
                return true;
            }
        };
        BeamBreak bb = BeamBreakFactory.create(tof, 3,5);
        assertFalse(bb.isBroken());
    }

    @Test
    public void tOFGoodReadTest(){
        TimeOfFlight tof = new TimeOfFlight(){
            @Override
            public double getDist(){
                return 2.0;
            }

            @Override
            public boolean goodReading(){
                return true;
            }
        };
        BeamBreak bb = BeamBreakFactory.create(tof, 1,5);
        assertTrue(bb.isBroken());
    }

    @Test
    public void tOFCreateOverloadTest(){
        TimeOfFlight tof = new TimeOfFlight(){
            @Override
            public double getDist(){
                return 2;
            }

            @Override
            public boolean goodReading(){
                return true;
            }
        };
        BeamBreak bb = BeamBreakFactory.create(tof,5);
        assertTrue(bb.isBroken());
    }
    
}
