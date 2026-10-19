import java.util.ArrayList;

/**
 * Farmer.java
 *
 * Regular class - NOT part of the Product inheritance tree. A farmer is not a
 * type of product, so Farmer neither extends nor is extended by Product's
 * classes. Farmer HAS/MANAGES a list of Product objects:
 *
 *     Farmer --- registers/manages ---> Product
 *
 * ADDITIONS BEYOND THE SUGGESTED METHOD LIST (no ProductManager class exists,
 * so Farmer is the only place these can live): the "products" list,
 * searchProduct(), filterByCategory(), filterByAvailability(),
 * isProductIdTaken() and isRegistered(). No new classes were added for them.
 *
 * EXCEPTION HANDLING:
 *  - InvalidInputException       - empty/negative farmer data, a product with no
 *                                  stock, or a Product ID that already exists.
 *  - ProductNotFoundException    - updating or removing a Product ID that is
 *                                  not in the list.
 * Farmer validates everything BEFORE changing anything, so a failed call
 * never leaves the record half-updated.
 */
public class Farmer {

    private String farmerId;
    private String farmerName;
    private String contactNumber;
    private String address;
    private String farmName;
    private String farmLocation;
    private double farmSize;
    private ArrayList<Product> products;

    public Farmer() {
        this.products = new ArrayList<>();
    }

    // ---------- Getters and setters (encapsulation + validation) ----------

    public String getFarmerId() {
        return farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public double getFarmSize() {
        return farmSize;
    }

    public void setFarmerId(String farmerId) throws InvalidInputException {
        this.farmerId = Validate.text(farmerId, "Farmer ID");
    }

    public void setFarmerName(String farmerName) throws InvalidInputException {
        this.farmerName = Validate.text(farmerName, "Farmer Name");
    }

    public void setContactNumber(String contactNumber) throws InvalidInputException {
        this.contactNumber = Validate.text(contactNumber, "Contact Number");
    }

    public void setAddress(String address) throws InvalidInputException {
        this.address = Validate.text(address, "Address");
    }

    public void setFarmName(String farmName) throws InvalidInputException {
        this.farmName = Validate.text(farmName, "Farm Name");
    }

    public void setFarmLocation(String farmLocation) throws InvalidInputException {
        this.farmLocation = Validate.text(farmLocation, "Farm Location");
    }

    public void setFarmSize(double farmSize) throws InvalidInputException {
        this.farmSize = Validate.nonNegative(farmSize, "Farm size");
    }

    /** True once the farmer has registered. */
    public boolean isRegistered() {
        return farmerId != null && !farmerId.isEmpty();
    }

    // ---------- Farmer information ----------

    public void registerFarmer(String farmerId, String farmerName, String contactNumber,
                               String address, String farmName, String farmLocation,
                               double farmSize) throws InvalidInputException {
        // Check everything first, then assign - all or nothing.
        String id = Validate.text(farmerId, "Farmer ID");
        String name = Validate.text(farmerName, "Farmer Name");
        String contact = Validate.text(contactNumber, "Contact Number");
        String addr = Validate.text(address, "Address");
        String farm = Validate.text(farmName, "Farm Name");
        String location = Validate.text(farmLocation, "Farm Location");
        double size = Validate.nonNegative(farmSize, "Farm size");

        this.farmerId = id;
        this.farmerName = name;
        this.contactNumber = contact;
        this.address = addr;
        this.farmName = farm;
        this.farmLocation = location;
        this.farmSize = size;
    }

    public void updateFarmer(String farmerName, String contactNumber, String address,
                             String farmName, String farmLocation,
                             double farmSize) throws InvalidInputException {
        String name = Validate.text(farmerName, "Farmer Name");
        String contact = Validate.text(contactNumber, "Contact Number");
        String addr = Validate.text(address, "Address");
        String farm = Validate.text(farmName, "Farm Name");
        String location = Validate.text(farmLocation, "Farm Location");
        double size = Validate.nonNegative(farmSize, "Farm size");

        this.farmerName = name;
        this.contactNumber = contact;
        this.address = addr;
        this.farmName = farm;
        this.farmLocation = location;
        this.farmSize = size;
    }

    public String getFarmerDetails() {
        if (!isRegistered()) {
            return "No farmer information has been registered yet.";
        }
        return "Farmer ID: " + farmerId
                + "\nName: " + farmerName
                + "\nContact Number: " + contactNumber
                + "\nAddress: " + address
                + "\nFarm Name: " + farmName
                + "\nFarm Location: " + farmLocation
                + "\nFarm Size: " + farmSize + " hectares";
    }

    // ---------- Product management (Farmer HAS/MANAGES Product) ----------

    /** Registers a product. A new product needs stock, and its ID must be unique. */
    public void addProduct(Product product) throws InvalidInputException {
        if (product == null) {
            throw new InvalidInputException("No product was given to register.");
        }
        if (product.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
        if (isProductIdTaken(product.getProductId())) {
            throw new InvalidInputException("Product ID already exists.");
        }
        products.add(product);
    }

    /**
     * Returns the live Product with this ID so the caller can edit it with
     * its setters. "products" stores references, so those changes show up
     * in this farmer's list immediately - no separate "save" step.
     */
    public Product updateProduct(String productId) throws ProductNotFoundException {
        for (Product p : products) {
            if (p.getProductId().equalsIgnoreCase(productId)) {
                return p;
            }
        }
        throw new ProductNotFoundException(productId);
    }

    public void removeProduct(String productId) throws ProductNotFoundException {
        boolean removed = products.removeIf(p -> p.getProductId().equalsIgnoreCase(productId));
        if (!removed) {
            throw new ProductNotFoundException(productId);
        }
    }

    public ArrayList<Product> viewProducts() {
        return products;
    }

    public boolean isProductIdTaken(String productId) {
        for (Product p : products) {
            if (p.getProductId().equalsIgnoreCase(productId)) {
                return true;
            }
        }
        return false;
    }

    /** Matches by product name or product ID, case-insensitive. */
    public ArrayList<Product> searchProduct(String keyword) {
        ArrayList<Product> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        for (Product p : products) {
            if (p.getProductName().toLowerCase().contains(lowerKeyword)
                    || p.getProductId().toLowerCase().contains(lowerKeyword)) {
                results.add(p);
            }
        }
        return results;
    }

    /** Pass "All" (or null) to get every product back. */
    public ArrayList<Product> filterByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return products;
        }
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                results.add(p);
            }
        }
        return results;
    }

    public ArrayList<Product> filterByAvailability(boolean available) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (p.getAvailability() == available) {
                results.add(p);
            }
        }
        return results;
    }
}
