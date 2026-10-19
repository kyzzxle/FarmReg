/**
 * Product.java
 *
 * Abstract parent class for every agricultural product in the system.
 *
 * WHY THIS CLASS IS ABSTRACT:
 * A "Product" that is not a Vegetable, a Fruit, or an Animal has no real
 * meaning here - a farmer only ever registers one of those three kinds.
 * Product holds what every product shares, and its abstract methods force
 * each child class to supply its own version of the rest (abstraction).
 *
 * EXCEPTION HANDLING:
 * Bad data is stopped at the source. The constructor and every setter
 * validate their input and throw InvalidInputException, so a Product object
 * can never hold a blank name, a negative price, and so on. This is the
 * practical reason the fields are private: the only way to change them is
 * through code that checks the value first (encapsulation).
 */
public abstract class Product {

    private String productId;
    private String productName;
    private double quantity;
    private String unit;
    private double price;
    private boolean availability;
    private String registrationDate;

    public Product(String productId, String productName, double quantity, String unit,
                   double price, String registrationDate) throws InvalidInputException {
        this.productId = Validate.text(productId, "Product ID");
        this.productName = Validate.text(productName, "Product Name");
        this.quantity = Validate.nonNegative(quantity, "Quantity");
        this.unit = Validate.text(unit, "Unit");
        this.price = Validate.nonNegative(price, "Price");
        this.registrationDate = Validate.text(registrationDate, "Registration Date");
        this.availability = this.quantity > 0;
    }

    // ---------- Getters and setters (encapsulation + validation) ----------

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) throws InvalidInputException {
        this.productId = Validate.text(productId, "Product ID");
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) throws InvalidInputException {
        this.productName = Validate.text(productName, "Product Name");
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) throws InvalidInputException {
        this.quantity = Validate.nonNegative(quantity, "Quantity");
        this.availability = this.quantity > 0;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) throws InvalidInputException {
        this.unit = Validate.text(unit, "Unit");
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidInputException {
        this.price = Validate.nonNegative(price, "Price");
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) throws InvalidInputException {
        this.registrationDate = Validate.text(registrationDate, "Registration Date");
    }

    // ---------- Abstract methods (abstraction + polymorphism) ----------

    /**
     * Each child class returns its own formatted description. The same call,
     * product.getProductDetails(), prints different details depending on
     * whether "product" really is a Vegetable, a Fruit, or an Animal.
     */
    public abstract String getProductDetails();

    /** Each child class returns its own category name. */
    public abstract String getCategory();

    // ---------- Shared behavior (inherited as-is by every subclass) ----------

    public double calculateTotalValue() {
        return quantity * price;
    }

    public boolean checkAvailability() {
        return availability && quantity > 0;
    }

    /**
     * Adds (positive) or removes (negative) stock, e.g. after a harvest or a
     * sale. Removing more than what is in stock is an error, not a silent
     * reset to zero.
     */
    public void updateQuantity(double amount) throws InvalidInputException {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new InvalidInputException("Amount must be a valid number.");
        }
        double newQuantity = this.quantity + amount;
        if (newQuantity < 0) {
            throw new InvalidInputException("Cannot remove more than the current stock ("
                    + quantity + " " + unit + ").");
        }
        this.quantity = newQuantity;
        this.availability = this.quantity > 0;
    }

    /**
     * The part of getProductDetails() every product shares. "protected" so
     * only Product and its subclasses can call it - each subclass adds its
     * own lines after this one instead of repeating the same formatting.
     */
    protected String getBaseDetails() {
        return "Product ID: " + productId
                + "\nProduct Name: " + productName
                + "\nCategory: " + getCategory()
                + "\nQuantity: " + quantity + " " + unit
                + "\nPrice: " + price
                + "\nAvailability: " + (availability ? "Available" : "Sold Out")
                + "\nRegistration Date: " + registrationDate;
    }
}
