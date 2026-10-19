/**
 * Fruit.java
 *
 * Child class - extends Product.
 *
 * INHERITED FROM Product (not redeclared): productId, productName, quantity,
 * unit, price, availability, registrationDate, calculateTotalValue(),
 * checkAvailability(), updateQuantity().
 *
 * UNIQUE TO Fruit: fruitType, variety, ripenessLevel, harvestSeason,
 * plantAge, checkRipeness(), getHarvestSeason().
 */
public class Fruit extends Product {

    public static final String[] RIPENESS_LEVELS = {"Unripe", "Ripe", "Overripe"};

    private String fruitType;
    private String variety;
    private String ripenessLevel;
    private String harvestSeason;
    private int plantAge;           // in months

    public Fruit(String productId, String productName, double quantity, String unit,
                 double price, String registrationDate, String fruitType, String variety,
                 String ripenessLevel, String harvestSeason, int plantAge)
            throws InvalidInputException {
        super(productId, productName, quantity, unit, price, registrationDate);
        this.fruitType = Validate.text(fruitType, "Fruit Type");
        this.variety = Validate.text(variety, "Variety");
        this.ripenessLevel = Validate.oneOf(ripenessLevel, "Ripeness Level", RIPENESS_LEVELS);
        this.harvestSeason = Validate.text(harvestSeason, "Harvest Season");
        this.plantAge = Validate.nonNegative(plantAge, "Plant age");
    }

    // ---------- Getters and setters for the fields unique to Fruit ----------

    public String getFruitType() {
        return fruitType;
    }

    public void setFruitType(String fruitType) throws InvalidInputException {
        this.fruitType = Validate.text(fruitType, "Fruit Type");
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) throws InvalidInputException {
        this.variety = Validate.text(variety, "Variety");
    }

    public String getRipenessLevel() {
        return ripenessLevel;
    }

    public void setRipenessLevel(String ripenessLevel) throws InvalidInputException {
        this.ripenessLevel = Validate.oneOf(ripenessLevel, "Ripeness Level", RIPENESS_LEVELS);
    }

    public void setHarvestSeason(String harvestSeason) throws InvalidInputException {
        this.harvestSeason = Validate.text(harvestSeason, "Harvest Season");
    }

    public int getPlantAge() {
        return plantAge;
    }

    public void setPlantAge(int plantAge) throws InvalidInputException {
        this.plantAge = Validate.nonNegative(plantAge, "Plant age");
    }

    // ---------- Methods unique to Fruit ----------

    public String checkRipeness() {
        switch (ripenessLevel) {
            case "Unripe":
                return "Unripe - not yet ready";
            case "Ripe":
                return "Ripe - ready for harvest or sale";
            default:
                return "Overripe - sell or use soon";
        }
    }

    // Also serves as the standard getter for harvestSeason - a separate
    // getter would just be a duplicate method.
    public String getHarvestSeason() {
        return harvestSeason;
    }

    // ---------- Overridden from Product ----------

    @Override
    public String getProductDetails() {
        return getBaseDetails()
                + "\nFruit Type: " + fruitType
                + "\nVariety: " + variety
                + "\nRipeness Level: " + ripenessLevel
                + "\nHarvest Season: " + harvestSeason
                + "\nPlant Age: " + plantAge + " month(s)";
    }

    @Override
    public String getCategory() {
        return "Fruit";
    }
}
