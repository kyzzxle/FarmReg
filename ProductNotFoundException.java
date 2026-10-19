/**
 * ProductNotFoundException.java
 *
 * Thrown by Farmer when an operation names a Product ID that is not in the
 * farmer's list (for example, updating or deleting a product that was
 * already removed). Kept separate from InvalidInputException because the
 * input itself is fine - the product just is not there.
 */
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String productId) {
        super("Product not found.");
        this.productId = productId;
    }

    private final String productId;

    public String getProductId() {
        return productId;
    }
}
