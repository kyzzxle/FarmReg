import java.time.LocalDate;

/**
 * Vegetable.java
 *
 * Child class - extends Product.
 *
 * INHERITED FROM Product (not redeclared): productId, productName, quantity,
 * unit, price, availability, registrationDate, calculateTotalValue(),
 * checkAvailability(), updateQuantity().
 *
 * UNIQUE TO Vegetable: vegetableType, soilType, plantingDate, harvestDate,
 * growthDuration, calculateExpectedHarvest(), checkHarvestStatus().
 *
 * harvestDate is optional: a crop that is still growing has no harvest date
 * yet (stored as ""). When it is given it must be a real date and cannot be
 * earlier than the planting date.
 */
public class Vegetable extends Product {

    private String vegetableType;
    private String soilType;
    private String plantingDate;
    private String harvestDate;     // "" while still growing
    private int growthDuration;     // in days

    public Vegetable(String productId, String productName, double quantity, String unit,
                     double price, String registrationDate, String vegetableType, String soilType,
                     String plantingDate, String harvestDate, int growthDuration)
            throws InvalidInputException {
        super(productId, productName, quantity, unit, price, registrationDate);
        this.vegetableType = Validate.text(vegetableType, "Vegetable Type");
        this.soilType = Validate.text(soilType, "Soil Type");
        LocalDate planting = Validate.date(plantingDate, "Planting date");
        LocalDate harvest = checkHarvest(harvestDate, planting);
        this.plantingDate = planting.toString();
        this.harvestDate = harvest == null ? "" : harvest.toString();
        this.growthDuration = Validate.nonNegative(growthDuration, "Growth duration");
    }

    /** Blank harvest date = not harvested yet (returns null). Otherwise it must be valid and not before planting. */
    private static LocalDate checkHarvest(String harvestText, LocalDate planting) throws InvalidInputException {
        if (harvestText == null || harvestText.trim().isEmpty()) {
            return null;
        }
        LocalDate harvest = Validate.date(harvestText, "Harvest date");
        if (harvest.isBefore(planting)) {
            throw new InvalidInputException("Harvest date cannot be before the planting date.");
        }
        return harvest;
    }

    // ---------- Getters and setters for the fields unique to Vegetable ----------

    public String getVegetableType() {
        return vegetableType;
    }

    public void setVegetableType(String vegetableType) throws InvalidInputException {
        this.vegetableType = Validate.text(vegetableType, "Vegetable Type");
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) throws InvalidInputException {
        this.soilType = Validate.text(soilType, "Soil Type");
    }

    public String getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(String plantingDate) throws InvalidInputException {
        setDates(plantingDate, this.harvestDate);
    }

    public String getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(String harvestDate) throws InvalidInputException {
        setDates(this.plantingDate, harvestDate);
    }

    /** Changes both dates together so the "harvest is not before planting" rule is checked on the pair. */
    public void setDates(String plantingDate, String harvestDate) throws InvalidInputException {
        LocalDate planting = Validate.date(plantingDate, "Planting date");
        LocalDate harvest = checkHarvest(harvestDate, planting);
        this.plantingDate = planting.toString();
        this.harvestDate = harvest == null ? "" : harvest.toString();
    }

    public int getGrowthDuration() {
        return growthDuration;
    }

    public void setGrowthDuration(int growthDuration) throws InvalidInputException {
        this.growthDuration = Validate.nonNegative(growthDuration, "Growth duration");
    }

    // ---------- Methods unique to Vegetable ----------

    public String calculateExpectedHarvest() {
        return "Planted on " + plantingDate + "; expected to be ready in about "
                + growthDuration + " day(s).";
    }

    public String checkHarvestStatus() {
        if (harvestDate.isEmpty()) {
            return "Still growing - not yet harvested";
        }
        return "Harvested on " + harvestDate;
    }

    // ---------- Overridden from Product ----------

    @Override
    public String getProductDetails() {
        return getBaseDetails()
                + "\nVegetable Type: " + vegetableType
                + "\nSoil Type: " + soilType
                + "\nPlanting Date: " + plantingDate
                + "\nHarvest Date: " + (harvestDate.isEmpty() ? "Not yet harvested" : harvestDate)
                + "\nGrowth Duration: " + growthDuration + " day(s)";
    }

    @Override
    public String getCategory() {
        return "Vegetable";
    }
}
