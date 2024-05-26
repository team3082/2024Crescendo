package frc.robot.auto.CAS;

import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;

@FunctionalInterface
public interface ChickenCommandSupplier {
    ChickenCommand[] getCommands();
}

