package frc.robot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.openhouse.AutoBuilder;

/** Class for selecting autonomous routines. */
public class AutoSelector {

  // If you want to know what auto is running anywhere else in the code
  public static String selectedAuto;

  public static final SendableChooser<String> autoChooser = new SendableChooser<>();

  /*
   * Setup the Auto selector.
   */
  public static void setup() {
    autoChooser.setDefaultOption("No Auto", "No Auto");
    autoChooser.addOption("Test", "Test");
  }

  /**
   * Run the auto selector. Gets the selected string from Glass, and runs the auto that corresponds
   * with the string chosen.
   */
  public static void run() {

    selectedAuto = autoChooser.getSelected();

    switch (autoChooser.getSelected()) {
      case "No Auto":
        CommandAuto.init(Commands.none());
        break;
      case "Test":
        CommandAuto.init(AutoBuilder.myCommand());
    }
  }
}
