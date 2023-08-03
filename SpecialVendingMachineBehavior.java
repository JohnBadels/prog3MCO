import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the behavior of a special vending machine that implements the IVendingMachineBehavior interface.
 * It handles the products, prices, instances, and other operations related to the vending machine.
 */
public class SpecialVendingMachineBehavior implements IVendingMachineBehavior {
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
    private Map<String, Integer> nonSellableInstances;
    private String[] fruits = { "Banana", "Grapes", "Apple", "Melon", "Pear", "Watermelon" };
    private Set<String> selectedFruits;
    private double totalSales;
    private double collectedSales;
    private Scanner scanner;

    /**
     * Initializes a new instance of the SpecialVendingMachineBehavior class.
     * It sets up the arrays and collections to manage the vending machine's behavior.
     */
    public SpecialVendingMachineBehavior() {
        products = new String[NUM_SLOTS];
        prices = new double[NUM_SLOTS];
        instances = new int[NUM_SLOTS];
        calories = new int[NUM_SLOTS];
        changeDenominations = new int[NUM_DENOMINATIONS];
        initialInstances = new double[NUM_SLOTS];
        productsSold = new ArrayList<>();
        nonSellableInstances = new HashMap<>();
        selectedFruits = new HashSet<>();
        totalSales = 0.0;
        collectedSales = 0.0;
        scanner = new Scanner(System.in);
    }
    
    /**
     * Initializes the Special Vending Machine by collecting details for each product slot.
     * This method prompts the user to input product details such as name, price, quantity, and calories.
     * It also handles the special case of Fruit Salad and Slot 8, allowing the user to add custom products.
     */
    @Override
    public void initialize() {
        JOptionPane.showMessageDialog(null, "You have chosen the Special Vending Machine.");
    
        String addProductInSlot8;
    
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (i == 6) {
                // Compute and add the Fruit Salad details
                JOptionPane.showMessageDialog(null, "Input details for Fruit Salad");
                while (true) {
                    String priceInput = JOptionPane.showInputDialog(null, "Price:", "Fruit Salad", JOptionPane.QUESTION_MESSAGE);
                    if (priceInput == null || priceInput.trim().isEmpty() || Double.parseDouble(priceInput) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid price.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        prices[i] = Double.parseDouble(priceInput);
                        break;
                    }
                }
    
                products[i] = "Fruit Salad";
                this.calories[i] = computeFruitSaladCalories();
                this.instances[i] = fruitSaladIsNotAvailable() ? 0 : 10;
    
                addProductInSlot8 = JOptionPane.showInputDialog(null, "Do you want to add a product in the 8th slot? (y/n):", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                if (!addProductInSlot8.equalsIgnoreCase("y"))
                    i = 8;
            } else if (i == 7) {
                JOptionPane.showMessageDialog(null, "Input details for Slot 8");
                while (true) {
                    String productName = JOptionPane.showInputDialog(null, "Product name:", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                    if (productName == null || productName.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid product name.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        products[i] = productName;
                        break;
                    }
                }
    
                double price = 0;
                while (true) {
                    String priceInput = JOptionPane.showInputDialog(null, "Price:", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                    if (priceInput == null || priceInput.trim().isEmpty() || Double.parseDouble(priceInput) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid price.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        price = Double.parseDouble(priceInput);
                        break;
                    }
                }
                prices[i] = price;
    
                int quantity = 0;
                while (true) {
                    String quantityInput = JOptionPane.showInputDialog(null, "Quantity:", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                    if (quantityInput == null || quantityInput.trim().isEmpty() || Integer.parseInt(quantityInput) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid quantity.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        this.instances[i] = Integer.parseInt(quantityInput);
                        if (this.instances[i] > MAX_PRODUCTS_PER_SLOT) {
                            JOptionPane.showMessageDialog(null, "Quantity cannot exceed 10. Setting to 10.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            this.instances[i] = MAX_PRODUCTS_PER_SLOT;
                        }
                        break;
                    }
                }
    
                int calories = 0;
                while (true) {
                    String calorieInput = JOptionPane.showInputDialog(null, "Calories:", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                    if (calorieInput == null || calorieInput.trim().isEmpty() || Integer.parseInt(calorieInput) < 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid calorie value.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        this.calories[i] = Integer.parseInt(calorieInput);
                        break;
                    }
                }
            } else {
                String fruitName = fruits[i];
                JOptionPane.showMessageDialog(null, "Input details for " + fruitName);
    
                double price;
                while (true) {
                    String priceInput = JOptionPane.showInputDialog(null, "Price:", fruitName, JOptionPane.QUESTION_MESSAGE);
                    if (priceInput == null || priceInput.trim().isEmpty() || Double.parseDouble(priceInput) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid price.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        price = Double.parseDouble(priceInput);
                        break;
                    }
                }
    
                int quantity = 0;
                while (true) {
                    String quantityInput = JOptionPane.showInputDialog(null, "Quantity:", fruitName, JOptionPane.QUESTION_MESSAGE);
                    if (quantityInput == null || quantityInput.trim().isEmpty() || Integer.parseInt(quantityInput) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid quantity.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        this.instances[i] = Integer.parseInt(quantityInput);
                        if (this.instances[i] > MAX_PRODUCTS_PER_SLOT) {
                            JOptionPane.showMessageDialog(null, "Quantity cannot exceed 10. Setting to 10.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            this.instances[i] = MAX_PRODUCTS_PER_SLOT;
                        }
                        break;
                    }
                }
    
                int calories = 0;
                while (true) {
                    String calorieInput = JOptionPane.showInputDialog(null, "Calories:", "Slot 8", JOptionPane.QUESTION_MESSAGE);
                    if (calorieInput == null || calorieInput.trim().isEmpty() || Integer.parseInt(calorieInput) < 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid calorie value.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        this.calories[i] = Integer.parseInt(calorieInput);
                        break;
                    }
                }
            }
        }
        // Initialize non-sellable items
        initializeNonSellableItems();
    }

    /**
     * Prompts the user to input the number of instances for each denomination of change (maximum 20 instances).
     * This method is used to initialize the change denominations available in the vending machine.
     * It validates the user input and sets the appropriate number of instances for each denomination.
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
     * Handles the main vending features of the Special Vending Machine.
     * This method allows the user to select a product, specify the quantity, make payment,
     * and receive the product and change if applicable. It also handles the specific logic for Fruit Salad.
     * The method keeps running in a loop until the user decides to exit or go back to the main menu.
     * The selectedFruits set is used to store the fruits selected for the Fruit Salad (Slot 6).
     * Non-sellable items such as Cheese, Plastic Spoon, Condensed Milk, Evaporated Milk, and Paper Cup are updated accordingly.
     * The change denominations are also updated after each successful transaction.
     */
    @Override
    public void vendingFeatures() {
        while (true) {
            int[] selectedFruits = new int[3];
            boolean addCheese = false;
            displayProductList();
    
            String productChoiceStr = JOptionPane.showInputDialog(null, "Select a product (0 to go back):");
            if (productChoiceStr == null) {
                return;
            }
    
            int productChoice;
            try {
                productChoice = Integer.parseInt(productChoiceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                continue;
            }
    
            if (productChoice == 0) {
                return;
            } else if (productChoice >= 1 && productChoice <= NUM_SLOTS) {
                int selectedSlot = productChoice - 1;
                if (selectedSlot == 6 && fruitSaladIsNotAvailable()) {
                    JOptionPane.showMessageDialog(null, "Fruit Salad is not available. Please choose another product.");
                    continue;
                } else if (instances[selectedSlot] == 0) {
                    JOptionPane.showMessageDialog(null, "Slot #" + productChoice + " is empty. Please choose another product.");
                    continue;
                }
    
                JOptionPane.showMessageDialog(null, "Selected product: " + products[selectedSlot]
                        + "\nPrice: " + prices[selectedSlot]
                        + "\nCalories: " + calories[selectedSlot]);
    
                int quantityToBuy;
                double totalPrice;

                if (selectedSlot == 6) {
                    selectedFruits = new int[3];
                
                    for (int i = 0; i < 3; i++) {
                        int fruitChoice;
                        while (true) {
                            String fruitChoiceStr = JOptionPane.showInputDialog(null, "Pick fruit #" + (i + 1) + " (1-6):");
                            if (fruitChoiceStr == null) {
                                return; // Go back to product selection
                            } else if (fruitChoiceStr.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter a value between 1 and 6.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                                continue;
                            }
                
                            try {
                                fruitChoice = Integer.parseInt(fruitChoiceStr);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                                continue;
                            }
                
                            // Validate if the selected fruit is available and not selected previously
                            if (fruitChoice < 1 || fruitChoice > 6) {
                                JOptionPane.showMessageDialog(null, "Invalid input. Please pick a fruit between 1 and 6.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            } else if (fruitChoice < 1 || fruitChoice > NUM_SLOTS || instances[fruitChoice - 1] == 0) {
                                JOptionPane.showMessageDialog(null, "The selected fruit is not available. Please pick another fruit.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            } else if (selectedFruits[0] == fruitChoice || selectedFruits[1] == fruitChoice || selectedFruits[2] == fruitChoice) {
                                JOptionPane.showMessageDialog(null, "The selected fruit is already chosen. Please pick another fruit.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            } else {
                                selectedFruits[i] = fruitChoice;
                                break;
                            }
                        }
                    }
                
                    // Check if cheese should be added to the fruit salad
                    if (nonSellableInstances.get("Cheese") > 0) {
                        String choice;
                        while (true) {
                            choice = JOptionPane.showInputDialog(null, "Do you want to add cheese to your fruit salad? There will be an additional 25 fee to your total bill (yes/no):");
                            if (choice == null) {
                                return; // Go back to product selection
                            } else if (choice.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter 'yes' or 'no'.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            } else if (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
                                JOptionPane.showMessageDialog(null, "Please enter 'yes' or 'no'.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            } else {
                                break;
                            }
                        }
                        addCheese = choice.equalsIgnoreCase("yes");
                    }
                
                    // Adjust total price if cheese is added
                    totalPrice = prices[selectedSlot - 1];
                    if (addCheese) {
                        totalPrice += 25;
                    }
                
                    quantityToBuy = 1;
                
                    // Add selected fruits to the selectedFruits set
                    for (int fruit : selectedFruits) {
                        this.selectedFruits.add(products[fruit - 1]);
                    }
                } else {
                    // For regular products (non-fruit salad)
                    while (true) {
                        String quantityToBuyStr = JOptionPane.showInputDialog(null, "Enter the quantity you want to buy:");
                        if (quantityToBuyStr == null) {
                            return; // Go back to product selection
                        }
                
                        if (quantityToBuyStr.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                
                        try {
                            quantityToBuy = Integer.parseInt(quantityToBuyStr);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                
                        if (quantityToBuy <= 0 || quantityToBuy > instances[selectedSlot - 1]) {
                            JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a value between 1 and " + instances[selectedSlot - 1] + ".", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        } else {
                            break;
                        }
                    }
                
                    totalPrice = prices[selectedSlot - 1] * quantityToBuy;
                }

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
                
                    // Check if the machine can give sufficient change
                    if (!canGiveSufficientChange(change)) {
                        JOptionPane.showMessageDialog(null, "Cannot give sufficient change. Please provide a lower payment.\nReturning your payment of \u20B1" + amountPaid, "Insufficient Change", JOptionPane.WARNING_MESSAGE);
                        return; // Go back to main menu
                    }
                
                    // Special Vending Machine specific updates for fruit salad
                    if (selectedSlot == 6) {
                        // Display the dispensing messages for the fruit salad
                        JOptionPane.showMessageDialog(null, "Preparing your fruit salad...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                
                        for (int i = 0; i < 3; i++) {
                            JOptionPane.showMessageDialog(null, "Adding " + products[selectedFruits[i] - 1] + "...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                        }
                
                        JOptionPane.showMessageDialog(null, "Adding Condensed Milk...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                
                        JOptionPane.showMessageDialog(null, "Adding Evaporated Milk...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                
                        if (addCheese) {
                            JOptionPane.showMessageDialog(null, "Adding Cheese...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                            // Decrease instances of non-sellable products if cheese is added
                            if (nonSellableInstances.containsKey("Cheese")) {
                                nonSellableInstances.put("Cheese", nonSellableInstances.get("Cheese") - 1);
                            }
                        }
                
                        JOptionPane.showMessageDialog(null, "Mixing your Fruit Salad...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Doing some final touches on your Fruit Salad...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Your Fruit Salad is ready. Thank you for waiting.", "Product Ready", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // For regular products (non-fruit salad)
                        JOptionPane.showMessageDialog(null, "Dispensing your Product...\nPlease wait.", "Dispensing Product", JOptionPane.INFORMATION_MESSAGE);
                
                        JOptionPane.showMessageDialog(null, "Product Dispensed. Thank you!", "Product Dispensed", JOptionPane.INFORMATION_MESSAGE);
                    }               

                    updateChangeDenominations(change, amountPaid); 
                    updateSales(totalPrice, products[selectedSlot], quantityToBuy);

                    // Reduce instances and set slot to null if instances become 0
                    nonSellableInstances.put("Plastic Spoon", nonSellableInstances.get("Plastic Spoon") - 1); 
                    nonSellableInstances.put("Condensed Milk", nonSellableInstances.get("Condensed Milk") - 1);
                    nonSellableInstances.put("Evaporated Milk", nonSellableInstances.get("Evaporated Milk") - 1);
                    nonSellableInstances.put("Paper Cup", nonSellableInstances.get("Paper Cup") - 1);

                    if (selectedSlot == 6) {
                        for (int i = 0; i < 3; i++) {
                            instances[selectedFruits[i] - 1] -= 1;
                        }
                    } else {
                        instances[selectedSlot] -= quantityToBuy;

                        if (selectedSlot == 7){
                            if (instances[7] == 0) {
                                products[7] = null;
                                prices[7] = 0.0;
                                calories[7] = 0;
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Change: \u20B1" + change);
                    if (change > 0) {
                        displayChangeBreakdown(change);
                    }

                    continue; // Go back to product selection
                } else {
                    JOptionPane.showMessageDialog(null, "Insufficient payment. Transaction cancelled.");
                    return; // Go back to main menu
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid product or 0 to go back.");
            }
        }
    }

    /**
     * Handles the maintenance features of the Special Vending Machine.
     * This method displays a menu of maintenance options and allows the user to choose one.
     * The maintenance options include replenishing products, non-sellable items, and change denominations.
     * It also provides functionalities to add, remove, and edit products in the vending machine.
     * The user can also view the sales summary.
     * The method runs in a loop until the user decides to exit the maintenance menu.
     * Each option's specific functionality is implemented in separate methods.
     * The user can exit the maintenance menu by selecting option 0.
     */
    @Override
    public void maintenanceFeatures() {
        while (true) {
            StringBuilder message = new StringBuilder("\n----- Maintenance Features -----\n");
            message.append("1. Replenish Product\n");
            message.append("2. Replenish Non-sellable Items\n");
            message.append("3. Replenish Change\n");
            message.append("4. Add a Product\n");
            message.append("5. Remove a Product\n");
            message.append("6. Edit a Product\n");
            message.append("7. Sales Summary\n");
            message.append("0. Exit Maintenance Menu\n");
    
            String choiceString;
            do {
                choiceString = JOptionPane.showInputDialog(null, message.toString(), "Maintenance Features", JOptionPane.QUESTION_MESSAGE);
                if (choiceString == null) {
                    // User clicked "Cancel" or closed the dialog
                    return;
                } else if (choiceString.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid option.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } while (choiceString.trim().isEmpty());
    
            try {
                int choice = Integer.parseInt(choiceString);
                switch (choice) {
                    case 1:
                        replenishProduct();
                        break;
                    case 2:
                        replenishNonSellableItems();
                        break;
                    case 3:
                        replenishChange();
                        break;
                    case 4:
                        addProduct();
                        break;
                    case 5:
                        removeProduct();
                        break;
                    case 6:
                        editProduct();
                        break;
                    case 7:
                        salesSummary();
                        break;
                    case 0:
                        return;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid option.", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Checks if the given denomination is a valid coin or bill denomination accepted by the vending machine.
     *
     * @param denomination The denomination to be checked.
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
     * Updates the change denominations after a successful transaction.
     * This method calculates and updates the available instances of each denomination.
     *
     * @param change     The change to be given back to the customer.
     * @param amountPaid The amount paid by the customer.
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
     * Checks if the vending machine has sufficient change to provide for the given change amount.
     *
     * @param change The change amount to be given back to the customer.
     * @return true if the machine can provide sufficient change, false otherwise.
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
     * Displays the breakdown of change provided to the customer.
     * This method shows the number of instances for each denomination used in the change.
     *
     * @param change The change amount provided to the customer.
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
     * Replenishes the change denominations in the vending machine.
     * This method allows the user to select a denomination and replenish it with additional instances.
     * It checks for valid inputs, available instances, and the maximum replenishable instances.
     * The method runs in a loop until the user decides to go back to the maintenance menu.
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
     * Updates the sales records after a successful product purchase.
     * This method increments both the total sales and collected sales with the given total price.
     * It also adds the sold product with its details to the productsSold list.
     *
     * @param totalPrice   The total price of the purchased product(s).
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
     * Collects the sales amount from the vending machine and deducts it from the change denominations.
     * This method ensures that the collected sales amount is properly deducted from available denominations.
     * If there is an insufficient amount of denominations for the collected sales, a warning message is displayed.
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
     * Displays the breakdown of denominations used to provide change to the customer for the collected sales.
     * This method shows the number of instances for each denomination used in the change.
     *
     * @param collectedSales The total amount of sales collected from the vending machine.
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
     * Computes the total calories of the Fruit Salad by selecting the three highest-calorie fruits.
     *
     * @return The total calories of the Fruit Salad.
     */
    public int computeFruitSaladCalories() {
        // Calculate the calories of the Fruit Salad by selecting the three highest calorie fruits
        int[] copyOfCalories = Arrays.copyOf(calories, NUM_SLOTS - 1); // Copy calories array without the last element (Fruit Salad)
        Arrays.sort(copyOfCalories); // Sort in ascending order
        int fruitSaladCalories = 0;
        for (int i = NUM_SLOTS - 2; i >= NUM_SLOTS - 2 - 2; i--) { // Select the three highest calorie fruits
            fruitSaladCalories += copyOfCalories[i];
        }
        return fruitSaladCalories;
    }

    /**
     * Initializes the non-sellable items in the vending machine with the specified quantities.
     * This method allows the user to input the quantities of each non-sellable item.
     * It ensures that the input is valid, not exceeding the maximum allowed quantity, and stores the instances in the nonSellableInstances map.
     */
    public void initializeNonSellableItems() {
        StringBuilder message = new StringBuilder("\nInitialize Non-Sellable Items:\n");
        String[] nonSellableItems = { "Condensed Milk", "Evaporated Milk", "Paper Cup", "Plastic Spoon", "Cheese" };
        
        for (String item : nonSellableItems) {
            message.append("Item: ").append(item).append("\n");
    
            while (true) {
                String input = JOptionPane.showInputDialog(null, "Input Quantity (not exceeding 10) for " + item + ":", "Initialize Non-Sellable Items", JOptionPane.QUESTION_MESSAGE);
    
                if (input == null) {
                    return; // User clicked the 'X' button or pressed 'Cancel', return from the method
                } else if (input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid quantity for " + item + ".", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int instances = Integer.parseInt(input);
                        if (instances > MAX_PRODUCTS_PER_SLOT) {
                            instances = MAX_PRODUCTS_PER_SLOT;
                            JOptionPane.showMessageDialog(null, "Quantity cannot exceed 10. Setting to 10.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                        }
                        nonSellableInstances.put(item, instances);
                        break; // Break the loop when valid input is provided
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Displays the list of available products in the vending machine in a graphical user interface (GUI).
     * This method creates a table showing the products' details: slot number, name, price, stock, and calories.
     */
    public void displayProductList() {
        JFrame frame = new JFrame("Available Products");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        String[] columnNames = {"Slot", "Product Name", "Price", "Stock", "Calories"};
        Object[][] data = new Object[NUM_SLOTS][5];
    
        for (int i = 0; i < NUM_SLOTS; i++) {
            data[i][0] = (i + 1);
    
            if (i == 6) {
                data[i][1] = "Fruit Salad";
                data[i][2] = prices[i];
                if (fruitSaladIsNotAvailable()) {
                    data[i][3] = "Not Available";
                } else {
                    data[i][3] = "Available";
                }
                data[i][4] = computeFruitSaladCalories();
            } else {
                data[i][1] = products[i];
                data[i][2] = prices[i];
                data[i][3] = instances[i];
                data[i][4] = calories[i];
            }
        }
    
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
    
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Runs the graphical user interface (GUI) to display the list of available products in the vending machine.
     * This method is used to initiate the GUI display using SwingUtilities.
     */
    public void runGUI() {
        SwingUtilities.invokeLater(this::displayProductList);
    }

    /**
     * Checks if the Fruit Salad is not available for purchase in the vending machine.
     * The Fruit Salad will not be available if there are less than four fruit products with zero instances or if any non-sellable item has zero instances.
     *
     * @return true if the Fruit Salad is not available, false otherwise.
     */
    public boolean fruitSaladIsNotAvailable() {
        int numFruitProductsWithZeroInstances = 0;
        for (int i = 0; i < fruits.length; i++) { // Excluding the Fruit Salad slot (index NUM_SLOTS - 1)
            if (instances[i] == 0) {
                numFruitProductsWithZeroInstances++;
            }
        }
    
        int numNonSellableItemsWithZeroInstances = 0;
        for (int instances : nonSellableInstances.values()) {
            if (instances == 0) {
                numNonSellableItemsWithZeroInstances++;
            }
        }
    
        return numFruitProductsWithZeroInstances >= 4 || numNonSellableItemsWithZeroInstances > 0;
    }    
       
    /**
     * Allows the user to replenish a product's instances in the vending machine.
     * This method prompts the user to select a product from the available products list
     * and then asks for the number of instances to replenish. The selected product's instances
     * are updated accordingly.
     */
    public void replenishProduct() {
        System.out.println("\n----- Replenish Product -----");
    
        while (true) {
            displayProductList();
    
            String productChoiceString;
            do {
                productChoiceString = JOptionPane.showInputDialog(null, "Select a product to replenish (0 to go back):");
                if (productChoiceString == null) {
                    return; // Go back to maintenance menu
                } else if (productChoiceString.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid product or 0 to go back.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } while (productChoiceString.trim().isEmpty());
    
            int productChoice = Integer.parseInt(productChoiceString);
            if (productChoice == 0) {
                return; // Go back to maintenance menu
            } else if (productChoice >= 1 && productChoice <= NUM_SLOTS) {
                int selectedSlot = productChoice - 1;
    
                if (products[selectedSlot] == null) {
                    JOptionPane.showMessageDialog(null, "There is no product in Slot " + productChoice + ". Please choose another product.", "Product Not Found", JOptionPane.WARNING_MESSAGE);
                } else if (selectedSlot == 6) {
                    JOptionPane.showMessageDialog(null, "Fruit Salad cannot be replenished manually. Please choose another product.", "Invalid Product", JOptionPane.WARNING_MESSAGE);
                } else if (instances[selectedSlot] >= MAX_PRODUCTS_PER_SLOT) {
                    JOptionPane.showMessageDialog(null, products[selectedSlot] + " already has the maximum instances. Please choose another product.", "Maximum Instances Reached", JOptionPane.WARNING_MESSAGE);
                } else {
                    int currentInstances = instances[selectedSlot];
                    int maxReplenishable = MAX_PRODUCTS_PER_SLOT - currentInstances;
    
                    String message = "Current Instances: " + currentInstances + "\nMaximum Replenishable Instances: " + maxReplenishable;
                    String instancesToAddString;
                    do {
                        instancesToAddString = JOptionPane.showInputDialog(null, message + "\nEnter number of instances to replenish:");
                        if (instancesToAddString == null) {
                            return; // Go back to maintenance menu
                        } else if (instancesToAddString.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid number of instances to replenish.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (instancesToAddString.trim().isEmpty());
    
                    int instancesToAdd = Integer.parseInt(instancesToAddString);
                    if (instancesToAdd <= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive number of instances to replenish.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    } else if (instancesToAdd > maxReplenishable) {
                        instancesToAdd = maxReplenishable;
                        JOptionPane.showMessageDialog(null, "Exceeded the maximum replenishable instances. Setting to " + instancesToAdd + ".", "Maximum Replenishable Instances Exceeded", JOptionPane.WARNING_MESSAGE);
                    }
    
                    instances[selectedSlot] += instancesToAdd;
                    JOptionPane.showMessageDialog(null, "Successfully replenished " + instancesToAdd + " instances of " + products[selectedSlot] + ".", "Replenishment Successful", JOptionPane.INFORMATION_MESSAGE);
                    return; // Go back to maintenance menu after replenishment
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Please select a valid product or 0 to go back.", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Allows the user to replenish the quantity of non-sellable items in the vending machine.
     * This method displays a table of non-sellable items and their current quantities.
     * The user can select an item and add more instances up to the maximum allowed quantity.
     */
    public void replenishNonSellableItems() {
        StringBuilder message = new StringBuilder("\n----- Replenish Non-sellable Items -----\n");
    
        // Display non-sellable items in a table format
        message.append("--------------------------------------------------\n");
        message.append(String.format("| %-25s | %-4s |\n", "Item", "Qty"));
        message.append("--------------------------------------------------\n");
        for (String item : nonSellableInstances.keySet()) {
            message.append(String.format("| %-25s | %-4d |\n", item, nonSellableInstances.get(item)));
        }
        message.append("--------------------------------------------------\n");
    
        while (true) {
            String selecteditem = JOptionPane.showInputDialog(null, message + "\nEnter the item to replenish (0 to cancel):", "Replenish Non-sellable Item", JOptionPane.QUESTION_MESSAGE);
    
            if (selecteditem == null || selecteditem.equals("0")) {
                return;
            }
    
            if (!nonSellableInstances.containsKey(selecteditem)) {
                JOptionPane.showMessageDialog(null, "Invalid item. Please select a valid item or 0 to cancel.", "Invalid Item", JOptionPane.WARNING_MESSAGE);
                continue;
            }
    
            int currentQuantity = nonSellableInstances.get(selecteditem);
            int maxAvailableQuantity = MAX_PRODUCTS_PER_SLOT - currentQuantity;
    
            if (currentQuantity == MAX_PRODUCTS_PER_SLOT) {
                JOptionPane.showMessageDialog(null, "The item " + selecteditem + " already has the maximum quantity of instances (10). Please select another item.", "Maximum Quantity Reached", JOptionPane.WARNING_MESSAGE);
                continue;
            }
    
            String quantityToAddString;
            while (true) {
                quantityToAddString = JOptionPane.showInputDialog(null, "Enter the quantity to add (0 to cancel):", "Replenish Quantity", JOptionPane.QUESTION_MESSAGE);
                if (quantityToAddString == null || quantityToAddString.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No input detected. Please enter a valid quantity or 0 to cancel.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                } else {
                    break;
                }
            }
    
            int quantityToAdd = Integer.parseInt(quantityToAddString);
            if (quantityToAdd == 0) {
                JOptionPane.showMessageDialog(null, "Replenishment cancelled for " + selecteditem + ".", "Replenishment Cancelled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            if (quantityToAdd > maxAvailableQuantity) {
                quantityToAdd = maxAvailableQuantity;
                JOptionPane.showMessageDialog(null, "The maximum quantity that can be added for " + selecteditem + " is " + maxAvailableQuantity, "Exceeded Maximum Quantity", JOptionPane.WARNING_MESSAGE);
            }
    
            nonSellableInstances.put(selecteditem, currentQuantity + quantityToAdd);
            JOptionPane.showMessageDialog(null, "Quantity of " + selecteditem + " added: " + quantityToAdd + "\nUpdated Quantity: " + nonSellableInstances.get(selecteditem), "Replenishment Successful", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
    
    /**
     * Adds a new product to the vending machine in the specified slot.
     * This method prompts the user to enter details of the new product, including name, price, instances, and calories.
     * The new product is added to the specified slot, updating the product list accordingly.
     */
    public void addProduct() {
        StringBuilder message = new StringBuilder("----- Add a Product -----\n");
    
        // Display list of products and their current instances
        message.append("Current Product List:\n");
        displayProductList();
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
        displayProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Product Added Successfully", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Removes a product from the vending machine in the specified slot.
     * This method prompts the user to select a slot containing the product to remove.
     * The product is then removed from the selected slot, updating the product list accordingly.
     */
    public void removeProduct() {
        StringBuilder message = new StringBuilder("----- Remove a Product -----\n\n");
    
        // Display list of products and their current instances
        message.append("Product List:\n");
        displayProductList();
    
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
        displayProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Remove Product", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Edits the details of an existing product in the vending machine.
     * This method prompts the user to select a slot containing the product to edit.
     * The user can change the price and calories of the product, updating the product list accordingly.
     */
    public void editProduct() {
        StringBuilder message = new StringBuilder("\n----- Edit a Product -----\n");
    
        // Display list of products and their current instances
        message.append("\nCurrent Product List:\n");
        displayProductList();
    
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
        displayProductList();
        JOptionPane.showMessageDialog(null, message.toString(), "Product List Updated", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays a summary of the vending machine's sales and inventory status.
     * This method shows the starting inventory, ending inventory, products sold, total sales, and collected sales.
     * It also allows the user to collect the sales amount and displays the denomination breakdown for change.
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
        displayProductList();
    
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
