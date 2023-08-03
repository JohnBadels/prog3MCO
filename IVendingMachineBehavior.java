public interface IVendingMachineBehavior {
    void initialize();
    void inputChangeDenominations();
    void vendingFeatures();
    void maintenanceFeatures();
    boolean isValidDenomination(int denomination);
    void updateChangeDenominations(double change, double amountPaid);
    boolean canGiveSufficientChange(double change);
    void displayChangeBreakdown(double change);
    void replenishChange();
    void updateSales(double totalPrice, String product, int quantitySold);
    void collectSales();
    void displayDenominationBreakdown(double collectedSales);
}
