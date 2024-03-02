package frc.robot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Commands;

/**
 * Class for selecting autonomous routines.
 */
public class AutoSelector {

    // If you want to know what auto is running anywhere else in the code
    public static String selectedAuto;

    public static final SendableChooser<String> autoChooser = new SendableChooser<>();

    /*
     * Setup the Auto selector.
     */
    public static void setup() {
        autoChooser.setDefaultOption("No Auto", "No Auto");
        autoChooser.addOption("1 Piece Middle", "1 Piece Middle");
        autoChooser.addOption("1 Piece Source", "1 Piece Source");
        autoChooser.addOption("1 Piece Amp", "1 Piece Amp");
        autoChooser.addOption("2 Piece Middle", "2 Piece Middle");
        autoChooser.addOption("2 Piece Source", "2 Piece Source");
        autoChooser.addOption("2 Piece Amp", "2 Piece Amp");
        autoChooser.addOption("2 Piece Source Far", "2 Piece Source Far");
        autoChooser.addOption("2 Piece Amp Far", "2 Piece Amp Far");
        autoChooser.addOption("3 Piece Source", "3 Piece Source");
        autoChooser.addOption("3 Piece Amp", "3 Piece Amp");
        autoChooser.addOption("3 Piece Source Half Far", "3 Piece Source Half Far");
        autoChooser.addOption("3 Piece Amp Half Far", "3 Piece Amp Half Far");
        autoChooser.addOption("3 Piece Source Far", "3 Piece Source Far");
        // TODO
        autoChooser.addOption("3 Piece Amp Far", "3 Piece Amp Far");
        autoChooser.addOption("4 Piece Middle", "4 Piece Middle");
        autoChooser.addOption("Test", "Test");
    }

    /**
     * Run the auto selector. Gets the selected string from Glass,
     * and runs the auto that corresponds with the string chosen.
     */
    public static void run() {

        selectedAuto = autoChooser.getSelected();

        switch(autoChooser.getSelected()) {
            case "No Auto":
                CommandAuto.init(Commands.none());     
            break;

            case "1 Piece Middle":
                CommandAuto.init(CommandAuto.onePieceMiddle());
            break;

            case "1 Piece Source":
                CommandAuto.init(CommandAuto.onePieceSource());
            break;

            case "1 Piece Amp":
                CommandAuto.init(CommandAuto.onePieceAmp());
            break;

            case "2 Piece Middle":
                CommandAuto.init(CommandAuto.twoPieceMiddle());
            break;

            case "2 Piece Source":
                CommandAuto.init(CommandAuto.twoPieceSource());
            break;

            case "2 Piece Amp":
                CommandAuto.init(CommandAuto.twoPieceAmp());
            break;

            case "2 Piece Source Far":
                CommandAuto.init(CommandAuto.twoPieceSourceFar());
            break;

            case "2 Piece Amp Far":
                CommandAuto.init(CommandAuto.twoPieceAmpFar());
            break;

            case "3 Piece Source":
                CommandAuto.init(CommandAuto.threePieceSource());
            break;

            case "3 Piece Amp":
                CommandAuto.init(CommandAuto.threePieceAmp());
            break;

            case "3 Piece Source Half Far":
                CommandAuto.init(CommandAuto.threePieceSourceHalfFar());
            break;

            case "3 Piece Amp Half Far":
                CommandAuto.init(CommandAuto.threePieceAmpHalfFar());
            break;

            case "3 Piece Source Far":
                CommandAuto.init(CommandAuto.threePieceSourceFar());
            break;
            
            case "3 Piece Amp Far":
                CommandAuto.init(CommandAuto.threePieceAmpFar());
            break;
            
            case "4 Piece Middle":
               CommandAuto.init(CommandAuto.fourPieceMiddle());
            break;
            case "Test":
                CommandAuto.init(CommandAuto.testCommand());
        }
    }
}