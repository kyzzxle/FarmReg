import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * EditProductDialog.java
 *
 * Small addition beyond the five named GUI screens: "Edit" needs a form,
 * and none of the five screens is a good fit for embedding one. This dialog
 * fills that one gap. It never changes a product's category - only its own
 * fields.
 *
 * EXCEPTION HANDLING - two layers:
 *  1. Validate first, change second. Save builds a NEW temporary product from
 *     the typed values. The OOP classes check every rule while building it
 *     and throw InvalidInputException if anything is wrong. Only when that
 *     succeeds are the values copied onto the real product - so a failed
 *     save can never leave a product half-edited.
 *  2. ProductNotFoundException. Another window may have deleted this product
 *     while the dialog was open. farmer.updateProduct(id) throws in that case,
 *     and the dialog says "Product not found." instead of editing a ghost.
 */
public class EditProductDialog extends JDialog {

    private final Farmer farmer;
    private final Product product;

    private JTextField nameField, quantityField, unitField, priceField;
    private JCheckBox availabilityCheck;
    private JPanel specificPanel;

    private JTextField vegTypeField, soilTypeField, plantingDateField, harvestDateField, growthDurationField;
    private JTextField fruitTypeField, varietyField, harvestSeasonField, plantAgeField;
    private JComboBox<String> ripenessCombo;
    private JTextField animalTypeField, breedField, ageField;
    private JComboBox<String> sexCombo, healthCombo, purposeCombo;

    public EditProductDialog(JFrame parent, Farmer farmer, Product product) {
        super(parent, "Edit Product - " + product.getProductId(), true);
        this.farmer = farmer;
        this.product = product;

        setSize(480, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(Theme.PALE_GREEN);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 8, 12));

        JPanel commonPanel = Theme.card("Product Information");
        commonPanel.setLayout(new GridLayout(5, 2, 6, 8));

        nameField = new JTextField(product.getProductName());
        nameField.setName("editName");
        quantityField = new JTextField(String.valueOf(product.getQuantity()));
        quantityField.setName("editQuantity");
        unitField = new JTextField(product.getUnit());
        unitField.setName("editUnit");
        priceField = new JTextField(String.valueOf(product.getPrice()));
        priceField.setName("editPrice");
        availabilityCheck = new JCheckBox("Available", product.getAvailability());
        availabilityCheck.setOpaque(false);

        commonPanel.add(new JLabel("Product Name:"));
        commonPanel.add(nameField);
        commonPanel.add(new JLabel("Quantity:"));
        commonPanel.add(quantityField);
        commonPanel.add(new JLabel("Unit:"));
        commonPanel.add(unitField);
        commonPanel.add(new JLabel("Price:"));
        commonPanel.add(priceField);
        commonPanel.add(new JLabel("Availability:"));
        commonPanel.add(availabilityCheck);

        int rows = (product instanceof Animal) ? 6 : 5;
        specificPanel = Theme.card(product.getCategory() + " Information");
        specificPanel.setLayout(new GridLayout(rows, 2, 6, 8));
        buildSpecificFields();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton saveBtn = Theme.primaryButton("Save");
        JButton cancelBtn = Theme.secondaryButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(commonPanel, BorderLayout.NORTH);
        add(specificPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> saveChanges());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void addRow(String label, JComponent field) {
        specificPanel.add(new JLabel(label));
        specificPanel.add(field);
    }

    private void buildSpecificFields() {
        if (product instanceof Vegetable v) {
            vegTypeField = new JTextField(v.getVegetableType());
            soilTypeField = new JTextField(v.getSoilType());
            plantingDateField = new JTextField(v.getPlantingDate());
            plantingDateField.setName("editPlanting");
            harvestDateField = new JTextField(v.getHarvestDate());
            harvestDateField.setName("editHarvest");
            growthDurationField = new JTextField(String.valueOf(v.getGrowthDuration()));
            growthDurationField.setName("editGrowth");
            addRow("Vegetable Type:", vegTypeField);
            addRow("Soil Type:", soilTypeField);
            addRow("Planting Date (YYYY-MM-DD):", plantingDateField);
            addRow("Harvest Date (optional):", harvestDateField);
            addRow("Growth Duration (days):", growthDurationField);

        } else if (product instanceof Fruit f) {
            fruitTypeField = new JTextField(f.getFruitType());
            varietyField = new JTextField(f.getVariety());
            ripenessCombo = new JComboBox<>(Fruit.RIPENESS_LEVELS);
            ripenessCombo.setSelectedItem(f.getRipenessLevel());
            harvestSeasonField = new JTextField(f.getHarvestSeason());
            plantAgeField = new JTextField(String.valueOf(f.getPlantAge()));
            plantAgeField.setName("editPlantAge");
            addRow("Fruit Type:", fruitTypeField);
            addRow("Variety:", varietyField);
            addRow("Ripeness Level:", ripenessCombo);
            addRow("Harvest Season:", harvestSeasonField);
            addRow("Plant Age (months):", plantAgeField);

        } else if (product instanceof Animal a) {
            animalTypeField = new JTextField(a.getAnimalType());
            breedField = new JTextField(a.getBreed());
            ageField = new JTextField(String.valueOf(a.getAge()));
            ageField.setName("editAge");
            sexCombo = new JComboBox<>(Animal.SEXES);
            sexCombo.setSelectedItem(a.getSex());
            healthCombo = new JComboBox<>(Animal.HEALTH_STATUSES);
            healthCombo.setSelectedItem(a.getHealthStatus());
            purposeCombo = new JComboBox<>(Animal.PURPOSES);
            purposeCombo.setSelectedItem(a.getPurpose());
            addRow("Animal Type:", animalTypeField);
            addRow("Breed:", breedField);
            addRow("Age (months):", ageField);
            addRow("Sex:", sexCombo);
            addRow("Health Status:", healthCombo);
            addRow("Purpose:", purposeCombo);
        }
    }

    // =====================================================================
    //  Save
    // =====================================================================

    private void saveChanges() {
        try {
            Product target = farmer.updateProduct(product.getProductId());   // ProductNotFoundException if deleted
            Product candidate = buildCandidate();                            // InvalidInputException if anything is wrong
            candidate.setAvailability(availabilityCheck.isSelected() && candidate.getQuantity() > 0);
            copyChanges(candidate, target);                                  // only reached when everything passed
            JOptionPane.showMessageDialog(this, "Product updated successfully.");
            dispose();

        } catch (ProductNotFoundException ex) {
            showError(ex.getMessage());
            dispose();                                                       // nothing left to edit
        } catch (InvalidInputException ex) {
            showError(ex.getMessage());                                      // dialog stays open for correction
        }
    }

    /** A new temporary object built from the typed values - this is where every rule is checked. */
    private Product buildCandidate() throws InvalidInputException {
        // Checked top-to-bottom, in the same order as the form.
        String name = Validate.text(nameField.getText(), "Product Name");
        double quantity = Validate.nonNegative(
                Validate.number(quantityField.getText(), "Quantity"), "Quantity");
        String unit = Validate.text(unitField.getText(), "Unit");
        double price = Validate.nonNegative(
                Validate.number(priceField.getText(), "Price"), "Price");
        String id = product.getProductId();
        String date = product.getRegistrationDate();

        if (product instanceof Vegetable) {
            // Checked in form order (the class validates again, and also checks that the
            // harvest date is not before the planting date).
            Validate.text(vegTypeField.getText(), "Vegetable Type");
            Validate.text(soilTypeField.getText(), "Soil Type");
            Validate.date(plantingDateField.getText(), "Planting date");
            if (!harvestDateField.getText().trim().isEmpty()) {
                Validate.date(harvestDateField.getText(), "Harvest date");
            }
            int growth = Validate.wholeNumber(growthDurationField.getText(), "Growth duration");
            return new Vegetable(id, name, quantity, unit, price, date,
                    vegTypeField.getText(), soilTypeField.getText(),
                    plantingDateField.getText(), harvestDateField.getText(), growth);
        } else if (product instanceof Fruit) {
            Validate.text(fruitTypeField.getText(), "Fruit Type");
            Validate.text(varietyField.getText(), "Variety");
            Validate.text(harvestSeasonField.getText(), "Harvest Season");
            int plantAge = Validate.wholeNumber(plantAgeField.getText(), "Plant age");
            return new Fruit(id, name, quantity, unit, price, date,
                    fruitTypeField.getText(), varietyField.getText(),
                    (String) ripenessCombo.getSelectedItem(), harvestSeasonField.getText(), plantAge);
        } else {
            Validate.text(animalTypeField.getText(), "Animal Type");
            Validate.text(breedField.getText(), "Breed");
            int age = Validate.wholeNumber(ageField.getText(), "Age");
            return new Animal(id, name, quantity, unit, price, date,
                    animalTypeField.getText(), breedField.getText(), age,
                    (String) sexCombo.getSelectedItem(), (String) healthCombo.getSelectedItem(),
                    (String) purposeCombo.getSelectedItem());
        }
    }

    /** Copies the already-validated values from the temporary product onto the real one. */
    private void copyChanges(Product source, Product target) throws InvalidInputException {
        target.setProductName(source.getProductName());
        target.setUnit(source.getUnit());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAvailability(source.getAvailability());      // after setQuantity, which also updates it

        if (target instanceof Vegetable t && source instanceof Vegetable s) {
            t.setVegetableType(s.getVegetableType());
            t.setSoilType(s.getSoilType());
            t.setDates(s.getPlantingDate(), s.getHarvestDate());
            t.setGrowthDuration(s.getGrowthDuration());
        } else if (target instanceof Fruit t && source instanceof Fruit s) {
            t.setFruitType(s.getFruitType());
            t.setVariety(s.getVariety());
            t.setRipenessLevel(s.getRipenessLevel());
            t.setHarvestSeason(s.getHarvestSeason());
            t.setPlantAge(s.getPlantAge());
        } else if (target instanceof Animal t && source instanceof Animal s) {
            t.setAnimalType(s.getAnimalType());
            t.setBreed(s.getBreed());
            t.setAge(s.getAge());
            t.setSex(s.getSex());
            t.setHealthStatus(s.getHealthStatus());
            t.setPurpose(s.getPurpose());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}
