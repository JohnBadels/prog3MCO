/**
 * VendingMachineGUI class is a graphical user interface for selecting a type of vending machine.
 * The user can choose between a Regular Vending Machine or a Special Vending Machine using a combo box.
 * When the "Confirm" button is clicked, the selected vending machine type is displayed in an alert dialog,
 * and the user is asked for confirmation to proceed. If the user confirms, the appropriate vending machine
 * behavior is initialized, and a new VendingMachine instance is created and started.
 *
 * Note: The "RegularVendingMachineBehavior" and "SpecialVendingMachineBehavior" classes are assumed to exist
 * and implement the IVendingMachineBehavior interface.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class VendingMachineGUI extends JPanel {
    private JButton jcomp1;
    private JLabel introText;
    private JComboBox<String> jcomp3;
    private JButton confirmButton;

    /**
     * Constructs a new VendingMachineGUI instance.
     */
    public VendingMachineGUI() {
        //construct preComponents
        String[] jcomp3Items = {"Regular Vending Machine", "Special Vending Machine"};

        //construct components
        jcomp1 = new JButton ("Button 2");
        introText = new JLabel ("Select a Vending Machine");
        jcomp3 = new JComboBox<>(jcomp3Items);
        confirmButton = new JButton ("Confirm");

        //adjust size and set layout
        setPreferredSize (new Dimension (287, 123));
        setLayout (null);

        //add components
        add (jcomp1);
        add (introText);
        add (jcomp3);
        add (confirmButton);

        //set component bounds 
        jcomp1.setBounds (300, 235, 100, 20);
        introText.setBounds (70, 5, 155, 35);
        jcomp3.setBounds (55, 40, 175, 25);
        confirmButton.setBounds (90, 80, 100, 25);

        // Add action listener for the confirmButton
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected vending machine choice from the combo box
                String selectedVendingMachine = (String) jcomp3.getSelectedItem();

                // Display the alert for the selected vending machine
                String alertMessage = "You have selected " + selectedVendingMachine + ".";
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(null, alertMessage, "Vending Machine Type", JOptionPane.PLAIN_MESSAGE);
                        // Ask for confirmation to proceed to initialize
                        int confirmResult = JOptionPane.showConfirmDialog(null, "Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmResult == JOptionPane.YES_OPTION) {
                            // Create the appropriate VendingMachineBehavior based on the user's choice
                            IVendingMachineBehavior behavior;
                            if (selectedVendingMachine.equals("Regular Vending Machine")) {
                                behavior = new RegularVendingMachineBehavior();
                            } else {
                                behavior = new SpecialVendingMachineBehavior();
                            }

                            // Create a new VendingMachine instance with the selected behavior and start it
                            VendingMachine vendingMachine = new VendingMachine(behavior);
                            vendingMachine.start();
                        }
                    }
                });
            }
        });
    }

    /**
     * The main method creates the GUI and sets it visible on the Event Dispatch Thread.
     *
     * @param args The command-line arguments (unused).
     */
    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame ("VendingMachineGUI");
                frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add (new VendingMachineGUI());
                frame.pack();
                frame.setVisible (true);
            }
        });
    }
}
