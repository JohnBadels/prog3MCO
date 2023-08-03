/**
 * VendingMachine class represents a vending machine that operates with different behaviors based on the chosen
 * IVendingMachineBehavior implementation. It allows users to select between Regular Vending Machine and Special
 * Vending Machine types, initializes the chosen behavior, and provides a main menu to access vending and maintenance
 * features or exit the program.
 */

import javax.swing.JOptionPane;
import java.util.*;

public class VendingMachine {
    private IVendingMachineBehavior vendingMachineBehavior;

    /**
     * Constructs a new VendingMachine instance with the specified behavior.
     *
     * @param vendingMachineBehavior The behavior to be used for the vending machine.
     */
    public VendingMachine(IVendingMachineBehavior vendingMachineBehavior) {
        this.vendingMachineBehavior = vendingMachineBehavior;
    }

    /**
     * Starts the vending machine by initializing the behavior and processing the main menu options.
     * The user can select between Vending Features, Maintenance Features, or Exit Program.
     */
    public void start() {
        vendingMachineBehavior.initialize();
        vendingMachineBehavior.inputChangeDenominations();

        boolean exitProgram = false;
        while (!exitProgram) {
            String[] mainMenuOptions = { "Vending Features", "Maintenance Features", "Exit Program" };

            int mainMenuChoice = JOptionPane.showOptionDialog(null, "Main Menu:", "Main Menu", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, mainMenuOptions, mainMenuOptions[0]);

            switch (mainMenuChoice) {
                case 0:
                    vendingMachineBehavior.vendingFeatures();
                    break;
                case 1:
                    vendingMachineBehavior.maintenanceFeatures();
                    break;
                case 2:
                    exitProgram = true;
                    JOptionPane.showMessageDialog(null, "Exiting the program. Goodbye!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * The main method that initiates the vending machine factory.
     * It displays a welcome message and prompts the user to select a type of vending machine.
     * Based on the selection, it creates the appropriate IVendingMachineBehavior and starts the VendingMachine.
     *
     * @param args The command-line arguments (unused).
     */
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to the Vending Machine Factory!");

        String[] vendingMachineChoices = {"Regular Vending Machine", "Special Vending Machine"};
        String selectedVendingMachine = (String) JOptionPane.showInputDialog(
                null,
                "Select a Vending Machine",
                "Vending Machine Type",
                JOptionPane.PLAIN_MESSAGE,
                null,
                vendingMachineChoices,
                vendingMachineChoices[0]
        );

        IVendingMachineBehavior behavior;
        if (selectedVendingMachine.equals("Regular Vending Machine")) {
            behavior = new RegularVendingMachineBehavior();
        } else {
            behavior = new SpecialVendingMachineBehavior();
        }

        if (behavior != null) {
            VendingMachine vendingMachine = new VendingMachine(behavior);
            vendingMachine.start();
        }
    }
}
