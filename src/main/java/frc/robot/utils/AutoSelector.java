package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.auto.Auto;

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
    }

    /**
     * Run the auto selector. Gets the selected string from Glass,
     * and runs the auto that corresponds with the string chosen.
     */
    public static void run() {

        selectedAuto = autoChooser.getSelected();

        switch(autoChooser.getSelected()) {

            case "No Auto":
            
            break;

            case "4 Piece Amp":
                Auto.fourPieceAmpSide();
            break;

            case "4 Piece Source":
                Auto.fourPieceSourceSide();
            break;

            case "3 Piece Middle Amp":
                Auto.threePieceMiddleAmpSide();
            break;

            case "3 Piece Middle Source":
                Auto.threePieceMiddleSourceSide();
            break;

            case "Choreo Test":
                Auto.choreoTest();
            break;

            case "Bezier Curve Test":
                Auto.bezierCurveAutoTest();
            break;
        }
    }
}