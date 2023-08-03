/**
 * Represents a product that has been sold in the vending machine.
 * It includes information about the product's name, price, quantity sold, and total price.
 */
public class ProductSold {
    private String product;
    private double price;
    private int quantitySold;
    private double totalPrice;

    /**
     * Constructs a new instance of the ProductSold class.
     *
     * @param product      The name of the product.
     * @param price        The price of the product.
     * @param quantitySold The quantity of the product sold.
     */
    public ProductSold(String product, double price, int quantitySold) {
        this.product = product;
        this.price = price;
        this.quantitySold = quantitySold;
        this.totalPrice = price * quantitySold;
    }

    /**
     * Get the name of the product.
     *
     * @return The name of the product.
     */
    public String getProduct() {
        return product;
    }

    /**
     * Get the price of the product.
     *
     * @return The price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Get the quantity of the product sold.
     *
     * @return The quantity of the product sold.
     */
    public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * Get the total price of the product sold (price * quantitySold).
     *
     * @return The total price of the product sold.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns a string representation of the ProductSold object.
     *
     * @return A formatted string containing the product name, price, quantity sold, and total price.
     */
    @Override
    public String toString() {
        return String.format("%-17s| %-6.2f| %-8d| %-6.2f", product, price, quantitySold, totalPrice);
    }
}
