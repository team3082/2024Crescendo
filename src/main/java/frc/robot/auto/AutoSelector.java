package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
        autoChooser.addOption("4 Piece Middle", "4 Piece Middle");
        autoChooser.addOption("Three Piece Middle", "Three Piece Middle");
        autoChooser.addOption("Three Piece Source", "Three Piece Source");
        autoChooser.addOption("Two Piece Middle", "Two Piece Middle");
        autoChooser.addOption("Two Piece Source", "Two Piece Source");
        autoChooser.addOption("Two Piece Amp", "Two Piece Amp");
        autoChooser.addOption("One Piece", "One Piece");
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

            case "4 Piece Middle":
               CommandAuto.init(CommandAuto.fourPieceMiddle());
            break;

            case "Two Piece Middle":
                CommandAuto.init(CommandAuto.twoPieceMid());
            break;

            case "Two Piece Source":
                CommandAuto.init(CommandAuto.twoPieceSource());
            break;

            case "Two Piece Amp":
                CommandAuto.init(CommandAuto.twoPieceAmp());
            break;

            case "Three Piece Middle":
                CommandAuto.init(CommandAuto.threePieceMiddle());
            break;

            case "Three Piece Source":
                CommandAuto.init(CommandAuto.threePieceSource());
            break;
            
            case "One Piece":
                CommandAuto.init(CommandAuto.onePieceStationary());
            break;
        }
    }
}