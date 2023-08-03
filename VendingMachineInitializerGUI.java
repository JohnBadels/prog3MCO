/**
 * VendingMachineInitializerGUI is a simple graphical user interface that allows the user to choose the type of
 * Vending Machine (Regular or Special). It initializes the chosen Vending Machine behavior, inputs change denominations,
 * and proceeds to run vending features, collect sales, and display the sales summary.
 */
import java.util.*;
import javax.swing.JOptionPane;

public class VendingMachineInitializerGUI {
    public static void main(String[] args) {
        String[] vendingMachineChoices = { "Regular Vending Machine", "Special Vending Machine" };
        String selectedVendingMachine = (String) JOptionPane.showInputDialog(
                null,
                "Choose the type of Vending Machine:",
                "Vending Machine Type",
                JOptionPane.PLAIN_MESSAGE,
                null,
                vendingMachineChoices,
                vendingMachineChoices[0]
        );

        IVendingMachineBehavior behavior;
        if (selectedVendingMachine.equals("Regular Vending Machine")) {
            behavior = new RegularVendingMachineBehavior();
            behavior.initialize();
            behavior.inputChangeDenominations();
            behavior.vendingFeatures();
            behavior.collectSales();
            behavior.salesSummary();
        } else {
            behavior = new SpecialVendingMachineBehavior();
            behavior.initialize();
            behavior.inputChangeDenominations();
            behavior.vendingFeatures();
            behavior.collectSales();
            behavior.salesSummary();
        }
    }
}
