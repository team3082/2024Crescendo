package frc.robot.auto.ChikenCommands;

import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;

/**
 * Supplies a ChikenCommand[] array
 */
@FunctionalInterface
public interface ChikenCommandSupplier {
    ChickenCommand[] getCommands();
}