import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

/**
 * Represents the behavior of a regular vending machine that implements the IVendingMachineBehavior interface.
 */
public class RegularVendingMachineBehavior implements IVendingMachineBehavior {
    private static final int MAX_PRODUCTS_PER_SLOT = 10;
    private static final int NUM_SLOTS = 8;
    private static final int NUM_DENOMINATIONS = 9;
    private static final int MAX_STORED_DENOMINATION_INSTANCES = 20;

    private String[] products;
    private double[] prices;
    private int[] instances;
    private int[] calories;
    private int[] changeDenominations;
    private double[] initialInstances;
    private List<ProductSold> productsSold;
    private double totalSales;
    private double collectedSales;
    private Scanner scanner;

    /**
     * Constructs a RegularVendingMachineBehavior object with default values.
     * Initializes arrays and lists, and sets initial total sales and collected sales to 0.
     */
    public RegularVendingMachineBehavior() {
        products = new String[NUM_SLOTS];
        prices = new double[NUM_SLOTS];
        instances = new int[NUM_SLOTS];
        calories = new int[NUM_SLOTS];
        changeDenominations = new int[NUM_DENOMINATIONS];
        initialInstances = new double[NUM_SLOTS];
        productsSold = new ArrayList<>();
        totalSales = 0.0;
        collectedSales = 0.0;
        scanner = new Scanner(System.in);
    }
    
    /**
     * Initializes the regular vending machine by collecting product information from the user.
     * Asks the user for product name, price, quantity, and calories for each slot.
     */
    @Override
    public void initialize() {
        JOptionPane.showMessageDialog(null, "You have chosen the Regular Vending Machine.");
    
        products = new String[NUM_SLOTS];
        prices = new double[NUM_SLOTS];
        instances = new int[NUM_SLOTS];
        calories = new int[NUM_SLOTS];
        changeDenominations = new int[NUM_DENOMINATIONS];
        initialInstances = new double[NUM_SLOTS];
        productsSold = new ArrayList<>();
        totalSales = 0.0;
        collectedSales = 0.0;
    
        for (int i = 0; i < NUM_SLOTS; i++) {
            String productName;
            while (true) {
                productName = JOptionPane.showInputDialog("Enter product name for Slot " + (i + 1));
                if (productName != null && !productName.trim().isEmpty()) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Product name cannot be empty. Please enter a valid name.");
            }
    
            String priceInput;
            while (true) {
                priceInput = JOptionPane.showInputDialog("Enter the price for " + productName);
                if (priceInput != null && !priceInput.trim().isEmpty() && Double.parseDouble(priceInput) > 0) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Price cannot be empty or less than 1. Please enter a valid price.");
            }
            double price = Double.parseDouble(priceInput);
    
            String instancesInput;
            while (true) {
                instancesInput = JOptionPane.showInputDialog("Enter the quantity for " + productName + " (not exceeding 10)");
                if (instancesInput != null && !instancesInput.trim().isEmpty() && Integer.parseInt(instancesInput) > 0) {
                    int instances = Integer.parseInt(instancesInput);
                    if (instances > MAX_PRODUCTS_PER_SLOT) {
                        JOptionPane.showMessageDialog(null, "Quantity cannot exceed 10. Setting to 10.");
                        instances = MAX_PRODUCTS_PER_SLOT;
                    }
                    this.instances[i] = instances;
                    this.initialInstances[i] = instances;
                    break;
                }
                JOptionPane.showMessageDialog(null, "Quantity cannot be empty or less than 1. Please enter a valid quantity.");
            }
    
            String caloriesInput;
            while (true) {
                caloriesInput = JOptionPane.showInputDialog("Enter the calories for " + productName + ":");
                if (caloriesInput != null && !caloriesInput.trim().isEmpty() && Integer.parseInt(caloriesInput) >= 0) {
                    break;
                }
                JOptionPane.showMessageDialog(null, "Calories cannot be empty or a negative value. Please enter a valid value.");
            }
            int calories = Integer.parseInt(caloriesInput);
    
            this.products[i] = productName;
            this.prices[i] = price;
            this.calories[i] = calories;
    
            if (i == NUM_SLOTS - 1) {
                break; // Skip asking for additional slot on the 8th slot
            }
    
            int addAnotherProduct = JOptionPane.showConfirmDialog(
                    null,
                    "Add another product to the next slot?",
                    "Add Product",
                    JOptionPane.YES_NO_OPTION
            );
    
            if (addAnotherProduct != JOptionPane.YES_OPTION) {
                break;
            }
        }
    }

    /**
     * Asks the user to input the number of instances for each denomination to use as change.
     */
    @Override
    public void inputChangeDenominations() {
        JOptionPane.showMessageDialog(null, "Input the number of instances for each denomination (maximum 20 instances)");
        int[] denominations = { 1, 5, 10, 20, 50, 100, 200, 500, 1000 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            while (true) {
                String input = JOptionPane.showInputDialog(null, "Enter the number of instances for ₱" + denominations[i],
                        "Change Denomination", JOptionPane.PLAIN_MESSAGE);
    
                // Check if the input is empty
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input provided. Please enter a value.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // Go back to the beginning of the loop to ask for input again
                }
    
                try {
                    int numInstances = Integer.parseInt(input);
                    if (numInstances >= 0 && numInstances <= 20) {
                        changeDenominations[i] = numInstances;
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid number of instances. Please enter a non-negative value and not exceeding 20.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer value.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Implements the vending features of the regular vending machine.
     * Allows users to select products, buy them, and receive change.
     */
    @Override
    public void vendingFeatures() {
        while (true) {
            String message = "\n----- Vending Features -----\n";
            showProductList();
            message += "Select a product (0 to go back):\n";
    
            String[] productOptions = new String[NUM_SLOTS + 1];
            productOptions[0] = "0";
            for (int i = 0; i < NUM_SLOTS; i++) {
                if (products[i] != null) {
                    productOptions[i + 1] = Integer.toString(i + 1);
                    message += (i + 1) + ". " + products[i] + "\n";
                }
            }
    
            String productChoice = (String) JOptionPane.showInputDialog(null, message, "Product Selection",
                    JOptionPane.PLAIN_MESSAGE, null, productOptions, productOptions[0]);
    
            if (productChoice == null) {
                return; // User clicked cancel or closed the dialog
            }
    
            int selectedSlot = Integer.parseInt(productChoice);
            if (selectedSlot == 0) {
                return;
            } else if (selectedSlot >= 1 && selectedSlot <= NUM_SLOTS) {
                selectedSlot--; // Convert to zero-based index
    
                if (instances[selectedSlot] == 0) {
                    JOptionPane.showMessageDialog(null, "Slot #" + (selectedSlot + 1) + " is empty. Please choose another product.");
                    continue;
                }
    
                String productInfo = "Selected product: " + products[selectedSlot] + "\n"
                                    + "Price: " + prices[selectedSlot] + "\n"
                                    + "Calories: " + calories[selectedSlot] + "\n";
    
                int quantityToBuy;
                while (true) {
                    String quantityInput = JOptionPane.showInputDialog(null, productInfo + "Enter the quantity you want to buy:", "Quantity",
                            JOptionPane.PLAIN_MESSAGE);

                    if (quantityInput == null) {
                        return; // User clicked cancel or closed the dialog
                    }

                    if (quantityInput.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Quantity cannot be blank. Please enter a value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    try {
                        quantityToBuy = Integer.parseInt(quantityInput);
                        if (quantityToBuy <= 0 || quantityToBuy > instances[selectedSlot]) {
                            JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a value between 1 and " + instances[selectedSlot] + ".",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
    
                double totalPrice = prices[selectedSlot] * quantityToBuy;
                String totalPriceMsg = "Total Price: " + totalPrice;
                JOptionPane.showMessageDialog(null, totalPriceMsg);
    
                String denominationMsg = "Accepted denomination: 1, 5, 10, 20, 50, 100, 200, 500, 1000";
                double amountPaid = 0;
                while (amountPaid < totalPrice) {
                    String denominationInput = JOptionPane.showInputDialog(null, totalPriceMsg + "\n" + denominationMsg
                            + "\nEnter a coin/bill (0 to cancel):", "Payment", JOptionPane.PLAIN_MESSAGE);

                    if (denominationInput == null) {
                        return; // User clicked cancel or closed the dialog
                    }

                    if (denominationInput.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Denomination cannot be blank. Please enter a value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    try {
                        int denomination = Integer.parseInt(denominationInput);
                        if (denomination == 0) {
                            break;
                        } else if (!isValidDenomination(denomination)) {
                            JOptionPane.showMessageDialog(null, "Invalid denomination. Please enter a valid coin/bill.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        amountPaid += denomination;
                        JOptionPane.showMessageDialog(null, "Remaining Balance: " + (totalPrice - amountPaid));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
    
                if (amountPaid >= totalPrice) {
                    double change = amountPaid - totalPrice;
    
                    if (!canGiveSufficientChange(change)) {
                        JOptionPane.showMessageDialog(null, "Cannot give sufficient change. Please provide a lower payment.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Returning your payment of \u20B1" + amountPaid);
                        return; // Go back to main menu
                    }
    
                    JOptionPane.showMessageDialog(null, "Dispensing your Product...");
                    try {
                        Thread.sleep(5000); // 5-second delay to simulate product dispensing
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
    
                    updateChangeDenominations(change, amountPaid);
                    updateSales(totalPrice, products[selectedSlot], quantityToBuy);
    
                    instances[selectedSlot] -= quantityToBuy;
                    if (instances[selectedSlot] == 0) {
                        products[selectedSlot] = null;
                        prices[selectedSlot] = 0.0;
                        calories[selectedSlot] = 0;
                    }
    
                    JOptionPane.showMessageDialog(null, "Product Dispensed. Thank you!");
                    JOptionPane.showMessageDialog(null, "Change: \u20B1" + change);
    
                    if (change > 0) {
                        displayChangeBreakdown(change);
                    }
    
                    continue;
                }
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid product or 0 to go back.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Implements maintenance features of the regular vending machine.
     * Allows maintenance personnel to replenish products and change, add, remove, or edit products, and view sales summary.
     */
    @Override
    public void maintenanceFeatures() {
        while (true) {
            StringBuilder message = new StringBuilder("\n----- Maintenance Features -----\n");
            message.append("\nSelect an option:\n");
            message.append("1. Replenish Product\n");
            message.append("2. Replenish Change\n");
            message.append("3. Add a Product\n");
            message.append("4. Remove a Product\n");
            message.append("5. Edit a Product\n");
            message.append("6. Sales Summary\n");
            message.append("0. Go back to main menu\n");
    
            String choiceString;
            do {
                choiceString = JOptionPane.showInputDialog(null, message.toString(), "Maintenance Features", JOptionPane.QUESTION_MESSAGE);
                if (choiceString == null) {
                    // User clicked "Cancel" or closed the dialog
                    return;
                } else if (choiceString.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid option or 0 to go back.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } while (choiceString.trim().isEmpty());
    
            try {
                int maintenanceChoice = Integer.parseInt(choiceString);
                switch (maintenanceChoice) {
                    case 1:
                        replenishProduct();
                        break;
                    case 2:
                        replenishChange();
                        break;
                    case 3:
                        addProduct();
                        break;
                    case 4:
                        removeProduct();
                        break;
                    case 5:
                        editProduct();
                        break;
                    case 6:
                        salesSummary();
                        break;
                    case 0:
                        return;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid option or 0 to go back.", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Checks if the given denomination is a valid denomination.
     *
     * @param denomination The denomination to check.
     * @return true if the denomination is valid, false otherwise.
     */
    @Override
    public boolean isValidDenomination(int denomination) {
        int[] validDenominations = { 1, 5, 10, 20, 50, 100, 200, 500, 1000 };
        for (int valid : validDenominations) {
            if (denomination == valid) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the change denominations based on the amount of change provided by the user.
     *
     * @param change     The amount of change to be given to the user.
     * @param amountPaid The total amount paid by the user.
     */
    @Override
    public void updateChangeDenominations(double change, double amountPaid) {
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            int numDenominationUsed = (int) (change / denominations[i]);
            if (numDenominationUsed > changeDenominations[i]) {
                numDenominationUsed = changeDenominations[i];
            }
            changeDenominations[i] -= numDenominationUsed;
            change -= numDenominationUsed * denominations[i];
    
            // Add the instances for the denominations used by the user as payment
            int numDenominationPaid = (int) (amountPaid / denominations[i]);
            changeDenominations[i] += numDenominationPaid;
            amountPaid -= numDenominationPaid * denominations[i];
        }
    }
    
    /**
     * Checks if the regular vending machine can give sufficient change for the specified amount of change.
     *
     * @param change The amount of change required.
     * @return true if sufficient change can be given, false otherwise.
     */
    @Override
    public boolean canGiveSufficientChange(double change) {
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            int numDenominationNeeded = (int) (change / denominations[i]);
            if (numDenominationNeeded > changeDenominations[i]) {
                return false;
            }
            change -= numDenominationNeeded * denominations[i];
        }
        return true;
    }

    /**
     * Displays the breakdown of change given to the user in terms of denominations.
     *
     * @param change The amount of change given to the user.
     */
    @Override
    public void displayChangeBreakdown(double change) {
        StringBuilder message = new StringBuilder("Change Breakdown:\n");
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            int numDenominationUsed = (int) (change / denominations[i]);
            if (numDenominationUsed > 0) {
                message.append("  ").append(numDenominationUsed).append(" x \u20B1").append(denominations[i]).append("\n");
            }
            change -= numDenominationUsed * denominations[i];
        }
    
        JOptionPane.showMessageDialog(null, message.toString(), "Change Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Replenishes the change denominations with additional instances of each denomination.
     */
    @Override
    public void replenishChange() {
        System.out.println("\n----- Replenish Change -----");
    
        while (true) {
            StringBuilder message = new StringBuilder("\nCurrent Denomination Instances:\n");
            int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
            for (int i = 0; i < NUM_DENOMINATIONS; i++) {
                message.append((i + 1)).append(". \u20B1").append(denominations[i]).append(" (Remaining: ").append(changeDenominations[i]).append(")\n");
            }
    
            String denominationChoiceString;
            while (true) {
                denominationChoiceString = JOptionPane.showInputDialog(null, message + "\nSelect a denomination to replenish (0 to go back to maintenance features menu):");
                if (denominationChoiceString == null) {
                    // User clicked "Cancel" or closed the dialog
                    return; // Go back to maintenance menu
                }
    
                // Check for empty input
                if (denominationChoiceString.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid denomination or 0 to go back.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            }
    
            try {
                int denominationChoice = Integer.parseInt(denominationChoiceString);
    
                if (denominationChoice == 0) {
                    return; // Go back to maintenance menu
                } else if (denominationChoice >= 1 && denominationChoice <= NUM_DENOMINATIONS) {
                    int selectedDenomination = denominationChoice - 1;
                    int currentInstances = changeDenominations[selectedDenomination];
                    int maxReplenishable = MAX_STORED_DENOMINATION_INSTANCES - currentInstances;
    
                    String maxReplenishableMessage = "Maximum Replenishable Instances: " + maxReplenishable;
                    String instancesToAddString;
                    while (true) {
                        instancesToAddString = JOptionPane.showInputDialog(null, "Current Instances: " + currentInstances + "\n" + maxReplenishableMessage + "\nEnter number of instances to replenish:");
                        if (instancesToAddString == null) {
                            // User clicked "Cancel" or closed the dialog
                            return; // Go back to maintenance menu
                        }
    
                        // Check for empty input
                        if (instancesToAddString.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid number of instances to replenish.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    }
    
                    int instancesToAdd = Integer.parseInt(instancesToAddString);
                    if (instancesToAdd <= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive number of instances to replenish.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    } else if (instancesToAdd > maxReplenishable) {
                        instancesToAdd = maxReplenishable;
                        JOptionPane.showMessageDialog(null, "Exceeded the maximum replenishable instances. Setting to " + instancesToAdd + ".", "Maximum Replenishable Instances Exceeded", JOptionPane.WARNING_MESSAGE);
                    }
    
                    changeDenominations[selectedDenomination] += instancesToAdd;
                    JOptionPane.showMessageDialog(null, "Successfully replenished " + instancesToAdd + " instances of \u20B1" + denominations[selectedDenomination] + ".", "Replenishment Successful", JOptionPane.INFORMATION_MESSAGE);
                    return; // Go back to maintenance menu after replenishment
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid denomination or 0 to go back.", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Updates the total sales and collected sales based on the sale of a product.
     *
     * @param totalPrice   The total price of the product sold.
     * @param product      The name of the product sold.
     * @param quantitySold The quantity of the product sold.
     */
    @Override
    public void updateSales(double totalPrice, String product, int quantitySold) {
        // Increment both total sales and collected sales
        totalSales += totalPrice;
        collectedSales += totalPrice;

        // Add the sold product to the sales salesSummary
        ProductSold soldProduct = new ProductSold(product, totalPrice / quantitySold, quantitySold);
        productsSold.add(soldProduct);
    }
    
    /**
     * Collects the sales and deducts the collected sales from the change denominations.
     */
    @Override
    public void collectSales() {
        // Deduct collected sales from change denominations
        double remainingCollectedSales = collectedSales;
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            int numDenominationUsed = (int) (remainingCollectedSales / denominations[i]);
            if (numDenominationUsed > changeDenominations[i]) {
                numDenominationUsed = changeDenominations[i];
            }
            changeDenominations[i] -= numDenominationUsed;
            remainingCollectedSales -= numDenominationUsed * denominations[i];
        }
        if (remainingCollectedSales > 0) {
            System.out.println("Insufficient change denominations for the remaining collected sales: \u20B1" + remainingCollectedSales);
        }
    }
    
    /**
     * Displays the breakdown of collected sales in terms of denominations.
     *
     * @param collectedSales The total amount of collected sales.
     */
    @Override
    public void displayDenominationBreakdown(double collectedSales) {
        StringBuilder message = new StringBuilder("Denomination Breakdown:\n");
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < NUM_DENOMINATIONS; i++) {
            int numDenominationUsed = (int) (collectedSales / denominations[i]);
            if (numDenominationUsed > 0) {
                message.append("  ").append(numDenominationUsed).append(" x \u20B1").append(denominations[i]).append("\n");
                collectedSales -= numDenominationUsed * denominations[i];
            }
        }
        JOptionPane.showMessageDialog(null, message.toString(), "Denomination Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the list of available products in a graphical table.
     */
    public void showProductList() {
        JFrame frame = new JFrame("Available Products");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {"Slot", "Product Name", "Price", "Stock", "Calories"};
        Object[][] data = new Object[NUM_SLOTS][5];

        for (int i = 0; i < NUM_SLOTS; i++) {
            data[i][0] = (i + 1);
            data[i][1] = products[i];
            data[i][2] = prices[i];
            data[i][3] = instances[i];
            data[i][4] = calories[i];
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    // Add this method to launch the GUI
    public void runGUI() {
        SwingUtilities.invokeLater(this::showProductList);
    }

    /**
     * Displays a dialog with a list of available products and their instances. Allows the user to select a product and replenish its instances.
     * The user can input the number of instances to replenish, and the method updates the product's instances accordingly.
     * If the user chooses to go back or cancels the operation, the method returns to the maintenance menu.
     */
    public void replenishProduct() {
        String message;
        while (true) {
            String availableProducts = "\nAvailable Products:\n";
            for (int i = 0; i < NUM_SLOTS; i++) {
                if (products[i] != null) {
                    availableProducts += (i + 1) + ". " + products[i] + " (Current Instances: " + instances[i] + ")\n";
                }
            }
    
            String inputChoice;
            int productChoice = 0;
            boolean validInput = false;
    
            while (!validInput) {
                inputChoice = JOptionPane.showInputDialog(null, availableProducts + "Select a product to replenish (0 to go back to maintenance features menu):");
    
                if (inputChoice == null) {
                    // The user pressed cancel or closed the dialog
                    return; // Go back to maintenance menu
                } else if (inputChoice.trim().isEmpty()) {
                    message = "No input. Please enter a product number.";
                    JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        productChoice = Integer.parseInt(inputChoice);
                        validInput = true;
                    } catch (NumberFormatException e) {
                        message = "Invalid input. Please enter a valid product number.";
                        JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
    
            if (productChoice == 0) {
                return; // Go back to maintenance menu
            } else if (productChoice >= 1 && productChoice <= NUM_SLOTS) {
                int selectedSlot = productChoice - 1;
                if (products[selectedSlot] == null) {
                    message = "There is no product in Slot " + productChoice + ". Please choose another product.";
                } else if (instances[selectedSlot] >= MAX_PRODUCTS_PER_SLOT) {
                    message = products[selectedSlot] + " already has the maximum instances. Please choose another product.";
                } else {
                    int currentInstances = instances[selectedSlot];
                    int maxReplenishable = MAX_PRODUCTS_PER_SLOT - currentInstances;
    
                    message = "Current Instances: " + currentInstances + "\nMaximum Replenishable Instances: " + maxReplenishable;
    
                    String inputInstances;
                    int instancesToAdd = 0;
                    validInput = false;
    
                    while (!validInput) {
                        inputInstances = JOptionPane.showInputDialog(null, message + "\nEnter number of instances to replenish:");
    
                        if (inputInstances == null) {
                            // The user pressed cancel or closed the dialog
                            return; // Go back to maintenance menu
                        } else if (inputInstances.trim().isEmpty()) {
                            message = "No input. Please enter the number of instances to replenish.";
                            JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.WARNING_MESSAGE);
                        } else {
                            try {
                                instancesToAdd = Integer.parseInt(inputInstances);
                                validInput = true;
                            } catch (NumberFormatException e) {
                                message = "Invalid input. Please enter a valid number of instances.";
                                JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
    
                    if (instancesToAdd <= 0) {
                        message = "Invalid input. Please enter a positive number of instances to replenish.";
                    } else if (instancesToAdd > maxReplenishable) {
                        instancesToAdd = maxReplenishable;
                        message = "Exceeded the maximum replenishable instances. Setting to " + instancesToAdd + ".";
                    } else {
                        instances[selectedSlot] += instancesToAdd;
                        message = "Successfully replenished " + instancesToAdd + " instances of " + products[selectedSlot] + ".";
                        JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.INFORMATION_MESSAGE);
                        return; // Go back to maintenance menu after replenishment
                    }
                }
            } else {
                message = "Invalid choice. Please select a valid product or 0 to go back.";
                JOptionPane.showMessageDialog(null, message, "Replenish Product", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Displays a dialog with a list of current products and their instances.
     * Asks the user to select a slot for a new product and provides input dialogs to add the new product's details such as name, price, instances, and calories.
     * After adding the product, the updated product list is displayed in a message dialog.
     */
    public void addProduct() {
        StringBuilder message = new StringBuilder("----- Add a Product -----\n");
    
        // Display list of products and their current instances
        message.append("Current Product List:\n");
        showProductList();
        message.append("\n");
    
        // Ask the user to select a slot for the new product
        int selectedSlot;
        while (true) {
            String slotInput = JOptionPane.showInputDialog(null, "Select a slot for the new product (1 to 8, 0 to go back to maintenance features menu):");
            if (slotInput == null) {
                return; // User canceled, go back to maintenance menu
            }
    
            try {
                selectedSlot = Integer.parseInt(slotInput);
            } catch (NumberFormatException e) {
                selectedSlot = -1;
            }
    
            if (selectedSlot == 0) {
                return; // Go back to maintenance menu
            } else if (selectedSlot < 1 || selectedSlot > NUM_SLOTS) {
                JOptionPane.showMessageDialog(null, "Invalid slot. Please select a slot number between 1 and " + NUM_SLOTS + ".", "Invalid Slot", JOptionPane.ERROR_MESSAGE);
            } else if (products[selectedSlot - 1] != null) {
                JOptionPane.showMessageDialog(null, "Slot " + selectedSlot + " is already occupied by " + products[selectedSlot - 1] + ". Please choose another slot.", "Slot Occupied", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
    
        // Ask for product details using input dialogs
        String productName = JOptionPane.showInputDialog(null, "Enter the product name:");
        if (productName == null || productName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product name cannot be empty. Please enter a valid name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return; // Go back to maintenance menu
        }
    
        int productInstances;
        while (true) {
            String instancesInput = JOptionPane.showInputDialog(null, "Enter the number of instances (not exceeding 10):");
            try {
                productInstances = Integer.parseInt(instancesInput);
                if (productInstances <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive number of instances.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else if (productInstances > MAX_PRODUCTS_PER_SLOT) {
                    productInstances = MAX_PRODUCTS_PER_SLOT;
                    JOptionPane.showMessageDialog(null, "Instances cannot exceed " + MAX_PRODUCTS_PER_SLOT + ". Setting to " + MAX_PRODUCTS_PER_SLOT + ".", "Exceeded Maximum Instances", JOptionPane.WARNING_MESSAGE);
                    break;
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                // Handle non-integer input
                productInstances = -1;
            }
        }
    
        String priceInput = JOptionPane.showInputDialog(null, "Enter the price:");
        double productPrice;
        try {
            productPrice = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            productPrice = 0.0;
        }
    
        String caloriesInput = JOptionPane.showInputDialog(null, "Enter the calories:");
        int productCalories;
        try {
            productCalories = Integer.parseInt(caloriesInput);
        } catch (NumberFormatException e) {
            productCalories = 0;
        }
    
        // Update the product details in the selected slot
        products[selectedSlot - 1] = productName;
        prices[selectedSlot - 1] = productPrice;
        instances[selectedSlot - 1] = productInstances;
        calories[selectedSlot - 1] = productCalories;
    
        // Display the updated product list in a message dialog
        message.append("\n\nUpdated Product List:\n");
        showProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Product Added Successfully", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog with a list of current products and their instances.
     * Asks the user to select a slot to remove the product from, and then asks for confirmation.
     * If confirmed, the selected product is removed from the list and its details are reset.
     * The updated product list is then displayed in a message dialog.
     */
    public void removeProduct() {
        StringBuilder message = new StringBuilder("----- Remove a Product -----\n\n");
    
        // Display list of products and their current instances
        message.append("Product List:\n");
        showProductList();
    
        // Ask the user to select a slot to remove the product from
        int selectedSlot;
        while (true) {
            String slotInput = JOptionPane.showInputDialog(null, "Select a slot to remove the product from (1 to 8, 0 to go back to maintenance features menu):");
            if (slotInput == null || slotInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No input. Please enter a slot number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                continue;
            }
    
            try {
                selectedSlot = Integer.parseInt(slotInput);
            } catch (NumberFormatException e) {
                selectedSlot = -1;
            }
    
            if (selectedSlot == 0) {
                return; // Go back to maintenance menu
            } else if (selectedSlot < 1 || selectedSlot > NUM_SLOTS) {
                JOptionPane.showMessageDialog(null, "Invalid slot. Please select a slot number between 1 and " + NUM_SLOTS + ".", "Invalid Slot", JOptionPane.WARNING_MESSAGE);
            } else if (products[selectedSlot - 1] == null) {
                JOptionPane.showMessageDialog(null, "Slot " + selectedSlot + " is vacant. Please choose another slot.", "Slot Vacant", JOptionPane.WARNING_MESSAGE);
            } else {
                break;
            }
        }
    
        // Ask the user if they are sure to remove the product
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove " + products[selectedSlot - 1] + " from Slot " + selectedSlot + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Remove the product from the list
            products[selectedSlot - 1] = null;
            prices[selectedSlot - 1] = 0.0;
            instances[selectedSlot - 1] = 0;
            calories[selectedSlot - 1] = 0;
            message.append("Product has been successfully removed from Slot ").append(selectedSlot).append(".");
        } else {
            message.append("Process canceled. Returning to maintenance features menu.");
        }
    
        // Display the updated product list
        message.append("\n\nUpdated Product List:\n");
        showProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Remove Product", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog with a list of current products and their instances.
     * Asks the user to select a slot to edit the product details and then provides input dialogs to change the product's price and calories.
     * The updated product list is displayed in a message dialog.
     */
    public void editProduct() {
        StringBuilder message = new StringBuilder("\n----- Edit a Product -----\n");
    
        // Display list of products and their current instances
        message.append("\nCurrent Product List:\n");
        showProductList();
    
        // Ask the user to select a slot to edit the product details
        int selectedSlot;
        while (true) {
            String slotInput = JOptionPane.showInputDialog(null,
                    "Select a slot to edit the product details (0 to go back to maintenance features menu):",
                    "Edit a Product", JOptionPane.PLAIN_MESSAGE);
    
            if (slotInput == null) {
                return; // User clicked cancel or closed the dialog
            }
    
            if (slotInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid slot number.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
    
            try {
                selectedSlot = Integer.parseInt(slotInput);
            } catch (NumberFormatException e) {
                selectedSlot = -1;
            }
    
            if (selectedSlot == 0) {
                return; // Go back to maintenance menu
            } else if (selectedSlot < 1 || selectedSlot > NUM_SLOTS) {
                JOptionPane.showMessageDialog(null,
                        "Invalid slot. Please select a slot number between 1 and " + NUM_SLOTS + ".", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (products[selectedSlot - 1] == null) {
                JOptionPane.showMessageDialog(null,
                        "Slot " + selectedSlot + " is vacant. Please choose another slot.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
    
        // Ask the user to change the price
        String priceInput;
        while (true) {
            priceInput = JOptionPane.showInputDialog(null,
                    "Editing details for " + products[selectedSlot - 1] + " (Slot " + selectedSlot + ").\n\n" +
                            "Enter the new price (or type '0' for no changes):",
                    "Edit Price", JOptionPane.PLAIN_MESSAGE);
    
            if (priceInput == null) {
                return; // User clicked cancel or closed the dialog
            }
    
            if (priceInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid price or '0' for no changes.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
    
        double newPrice = prices[selectedSlot - 1];
        if (!priceInput.equals("0")) {
            try {
                newPrice = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid price format. Please enter a valid price (a numeric value) or '0' for no changes.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    
        // Ask the user to change the calories
        String caloriesInput;
        while (true) {
            caloriesInput = JOptionPane.showInputDialog(null,
                    "Enter the new calories (or type '0' for no changes):", "Edit Calories",
                    JOptionPane.PLAIN_MESSAGE);
    
            if (caloriesInput == null) {
                return; // User clicked cancel or closed the dialog
            }
    
            if (caloriesInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid calorie count or '0' for no changes.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
    
        int newCalories = calories[selectedSlot - 1];
        if (!caloriesInput.equals("0")) {
            try {
                newCalories = Integer.parseInt(caloriesInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid calories format. Please enter a valid calorie count (an integer value) or '0' for no changes.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    
        // Update the product details
        prices[selectedSlot - 1] = newPrice;
        calories[selectedSlot - 1] = newCalories;
    
        // Display the updated product list
        message.append("\nUpdated Product List:\n");
        showProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Product List Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the sales summary which includes the starting inventory, ending inventory, list of products sold, total sales, and collected sales.
     * The method also allows the user to collect the sales, deducting the collected amount from the change denominations.
     * If the user chooses not to collect the sales, the method will not deduct the amount.
     */
    public void salesSummary() {
        StringBuilder message = new StringBuilder("\n----- Sales Summary -----\n");
    
        // Display the starting inventory (initialInstances)
        message.append("Starting Inventory:\n");
        message.append("Slot # | Product           | Price | Initial | Calories\n");
        for (int i = 0; i < NUM_SLOTS; i++) {
            message.append(String.format("%-6d| %-17s| %-6.2f| %-8.0f| %-8d%n", (i + 1), products[i], prices[i], initialInstances[i], calories[i]));
        }
    
        // Display the ending inventory (updated instances)
        message.append("\nEnding Inventory:\n");
        showProductList();
    
        // Display the list of products sold
        message.append("\nProducts Sold:\n");
        message.append("Product           | Price | Qty Sold | Total Price\n");
        for (ProductSold soldProduct : productsSold) {
            message.append(soldProduct).append("\n");
        }
    
        message.append("\nTotal Sales: \u20B1").append(totalSales);
        message.append("\nCollected Sales: \u20B1").append(collectedSales);
    
        // Check if there are any sales to collect
        if (collectedSales == 0.0) {
            message.append("\n\nNo sales to collect.");
        } else {
            String collectSalesChoice;
            do {
                collectSalesChoice = JOptionPane.showInputDialog(null, "Do you want to collect the sales? (y/n)", "Sales Collection", JOptionPane.QUESTION_MESSAGE);
                if (collectSalesChoice == null) {
                    return; // User clicked the 'X' button or pressed 'Cancel', return from the method
                } else if (collectSalesChoice.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter 'y' or 'n'.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            } while (collectSalesChoice.trim().isEmpty());
    
            if (collectSalesChoice.equalsIgnoreCase("y")) {
                message.append("\n\nTotal amount collected: \u20B1").append(collectedSales);
                displayDenominationBreakdown(collectedSales);
                collectSales(); // Deduct collected sales from change denominations
                collectedSales = 0.0; // Reset collected sales to 0 after collecting
                message.append("\n\nCollected sales have been released.");
            } else if (collectSalesChoice.equalsIgnoreCase("n")) {
                message.append("\n\nSales collection canceled.");
            } else {
                message.append("\n\nInvalid input. Sales collection canceled.");
            }
        }
    
        JOptionPane.showMessageDialog(null, message.toString(), "Sales Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
