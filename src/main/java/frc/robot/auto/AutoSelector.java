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
        autoChooser.addOption("4 Piece Middle", "4 Piece Middle");
        autoChooser.addOption("Three Piece Middle", "Three Piece Middle");
        autoChooser.addOption("Three Piece Right", "Three Piece Right");
        autoChooser.addOption("Three Piece Left", "Three Piece Left");
        autoChooser.addOption("Two Piece Middle", "Two Piece Middle");
        autoChooser.addOption("Two Piece Right", "Two Piece Right");
        autoChooser.addOption("Two Piece Left", "Two Piece Left");
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
                CommandAuto.init(CommandAuto.twoPiece("2 Piece Middle"));
            break;

            case "Two Piece Right":
                CommandAuto.init(CommandAuto.twoPiece("2 Piece Right"));
            break;

            case "Two Piece Left":
                CommandAuto.init(CommandAuto.twoPiece("2 Piece Left"));
            break;

            case "Three Piece Middle":
                CommandAuto.init(CommandAuto.ThreePiece("3 Piece Middle"));
            break;

            case "Three Piece Right":
                CommandAuto.init(CommandAuto.ThreePiece("3 Piece Right"));
            break;

            case "Three Piece Left":
                CommandAuto.init(CommandAuto.ThreePiece("3 Piece Left"));
            break;
            case "One Piece":
                CommandAuto.init(CommandAuto.onePieceStationary());
            break;
        }
    }
}