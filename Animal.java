/**
 * Animal.java
 *
 * Child class - extends Product.
 *
 * INHERITED FROM Product (not redeclared): productId, productName, quantity,
 * unit, price, availability, registrationDate, calculateTotalValue(),
 * checkAvailability(), updateQuantity().
 *
 * UNIQUE TO Animal: animalType, breed, age, sex, healthStatus, purpose,
 * checkHealthStatus(), getAnimalDetails().
 */
public class Animal extends Product {

    public static final String[] SEXES = {"Male", "Female"};
    public static final String[] HEALTH_STATUSES = {"Healthy", "Under Treatment", "Sick", "Recovering"};
    public static final String[] PURPOSES = {"Meat", "Milk", "Eggs", "Breeding"};

    private String animalType;
    private String breed;
    private int age;                // in months
    private String sex;
    private String healthStatus;
    private String purpose;

    public Animal(String productId, String productName, double quantity, String unit,
                  double price, String registrationDate, String animalType, String breed,
                  int age, String sex, String healthStatus, String purpose)
            throws InvalidInputException {
        super(productId, productName, quantity, unit, price, registrationDate);
        this.animalType = Validate.text(animalType, "Animal Type");
        this.breed = Validate.text(breed, "Breed");
        this.age = Validate.nonNegative(age, "Age");
        this.sex = Validate.oneOf(sex, "Sex", SEXES);
        this.healthStatus = Validate.oneOf(healthStatus, "Health Status", HEALTH_STATUSES);
        this.purpose = Validate.oneOf(purpose, "Purpose", PURPOSES);
    }

    // ---------- Getters and setters for the fields unique to Animal ----------

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) throws InvalidInputException {
        this.animalType = Validate.text(animalType, "Animal Type");
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) throws InvalidInputException {
        this.breed = Validate.text(breed, "Breed");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws InvalidInputException {
        this.age = Validate.nonNegative(age, "Age");
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) throws InvalidInputException {
        this.sex = Validate.oneOf(sex, "Sex", SEXES);
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) throws InvalidInputException {
        this.healthStatus = Validate.oneOf(healthStatus, "Health Status", HEALTH_STATUSES);
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) throws InvalidInputException {
        this.purpose = Validate.oneOf(purpose, "Purpose", PURPOSES);
    }

    // ---------- Methods unique to Animal ----------

    public String checkHealthStatus() {
        return healthStatus;
    }

    /**
     * Just the animal-specific lines. getProductDetails() below calls this and
     * puts it after the shared base details, so the two do not duplicate each other.
     */
    public String getAnimalDetails() {
        return "Animal Type: " + animalType
                + "\nBreed: " + breed
                + "\nAge: " + age + " month(s)"
                + "\nSex: " + sex
                + "\nHealth Status: " + healthStatus
                + "\nPurpose: " + purpose;
    }

    // ---------- Overridden from Product ----------

    @Override
    public String getProductDetails() {
        return getBaseDetails() + "\n" + getAnimalDetails();
    }

    @Override
    public String getCategory() {
        return "Animal";
    }
}
