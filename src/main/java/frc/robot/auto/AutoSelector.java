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
        autoChooser.addOption("4 Piece Amp", "4 Piece Amp");
        autoChooser.addOption("4 Piece Source", "4 Piece Source");
        autoChooser.addOption("3 Piece Middle Amp", "3 Piece Middle Amp");
        autoChooser.addOption("3 Piece Middle Source", "3 Piece Middle Source");
        autoChooser.addOption("Choreo Test", "Choreo Test");
        autoChooser.addOption("Bezier Curve Test", "Bezier Curve Test");
        autoChooser.addOption("Bounce Test", "Bounce Test");
        // autoChooser.addOption("4 Piece Middle", "4 Piece Middle");
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

            case "4 Piece Amp":
                CommandAuto.init(CommandAuto.fourPieceAmpSide());
            break;

            case "4 Piece Source":
                CommandAuto.init(CommandAuto.fourPieceSourceSide());
            break;

            case "3 Piece Middle Amp":
            
                CommandAuto.init(CommandAuto.threePieceMiddleAmpSide());
            break;

            case "3 Piece Middle Source":
                CommandAuto.init(CommandAuto.threePieceMiddleSourceSide());
            break;

            case "Choreo Test":
                //Auto.choreoTest();
                CommandAuto.init(CommandAuto.choreoTest());
            break;

            case "Bezier Curve Test":
                CommandAuto.init(CommandAuto.bezierCurveAutoTest());
            break;

            case "Bounce Test":
                CommandAuto.init(CommandAuto.bounceTest());
            break;
            
            case "4 Piece Middle":
               // CommandAuto.init(CommandAuto.fourPieceMiddle());
            break;

            case "Test":
                CommandAuto.init(CommandAuto.test());
            break;
        }
    }
}