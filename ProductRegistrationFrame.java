import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * ProductRegistrationFrame.java
 *
 * Screen 3 - Product Registration. A category combo box swaps the visible
 * field group (Vegetable / Fruit / Animal) using CardLayout, so only the
 * fields relevant to the selected category are shown. On Register, this is
 * where the GUI creates the OOP objects:
 *
 *     new Vegetable(...)   or   new Fruit(...)   or   new Animal(...)
 *
 * and stores the result through farmer.addProduct(Product) - the common
 * parent type.
 *
 * EXCEPTION HANDLING: the screen only turns text into values and builds the
 * object. The rules (empty fields, negative numbers, real dates, allowed
 * choices, duplicate ID, no stock) are enforced by the OOP classes, which
 * throw InvalidInputException. registerProduct() catches it and shows the
 * message, and the form stays filled so the user can fix the mistake.
 */
public class ProductRegistrationFrame extends JFrame {

    private Farmer farmer;

    private JTextField productIdField, productNameField, quantityField, unitField, priceField;
    private JCheckBox availabilityCheck;
    private JComboBox<String> categoryCombo;

    private CardLayout cardLayout;
    private JPanel categoryCardPanel;

    private JTextField vegTypeField, soilTypeField, plantingDateField, harvestDateField, growthDurationField;
    private JTextField fruitTypeField, varietyField, harvestSeasonField, plantAgeField;
    private JComboBox<String> ripenessCombo;
    private JTextField animalTypeField, breedField, ageField;
    private JComboBox<String> sexCombo, healthCombo, purposeCombo;

    public ProductRegistrationFrame(Farmer farmer) {
        this.farmer = farmer;

        setTitle("Product Registration");
        setSize(510, 670);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(Theme.PALE_GREEN);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 8, 12));

        // ---- Common fields (shared by every category) ----
        JPanel commonPanel = Theme.card("Product Information");
        commonPanel.setLayout(new GridLayout(7, 2, 6, 8));

        productIdField = new JTextField();
        productIdField.setName("productId");
        productNameField = new JTextField();
        productNameField.setName("productName");
        quantityField = new JTextField();
        quantityField.setName("quantity");
        unitField = new JTextField();
        unitField.setName("unit");
        priceField = new JTextField();
        priceField.setName("price");
        availabilityCheck = new JCheckBox("Available", true);
        availabilityCheck.setOpaque(false);
        categoryCombo = new JComboBox<>(new String[]{"Vegetable", "Fruit", "Animal"});
        categoryCombo.setName("category");

        commonPanel.add(new JLabel("Product ID:"));
        commonPanel.add(productIdField);
        commonPanel.add(new JLabel("Product Name:"));
        commonPanel.add(productNameField);
        commonPanel.add(new JLabel("Quantity:"));
        commonPanel.add(quantityField);
        commonPanel.add(new JLabel("Unit:"));
        commonPanel.add(unitField);
        commonPanel.add(new JLabel("Price:"));
        commonPanel.add(priceField);
        commonPanel.add(new JLabel("Availability:"));
        commonPanel.add(availabilityCheck);
        commonPanel.add(new JLabel("Category:"));
        commonPanel.add(categoryCombo);

        // ---- Category-specific fields (only one card visible at a time) ----
        cardLayout = new CardLayout();
        categoryCardPanel = Theme.card("Category-Specific Information");
        categoryCardPanel.setLayout(cardLayout);
        categoryCardPanel.add(buildVegetablePanel(), "Vegetable");
        categoryCardPanel.add(buildFruitPanel(), "Fruit");
        categoryCardPanel.add(buildAnimalPanel(), "Animal");

        categoryCombo.addActionListener(e ->
                cardLayout.show(categoryCardPanel, (String) categoryCombo.getSelectedItem()));

        // ---- Buttons ----
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton registerBtn = Theme.primaryButton("Register");
        JButton clearBtn = Theme.secondaryButton("Clear");
        JButton backBtn = Theme.secondaryButton("Back");
        buttonPanel.add(registerBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);

        add(commonPanel, BorderLayout.NORTH);
        add(categoryCardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        registerBtn.addActionListener(e -> registerProduct());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> dispose());
    }

    private JPanel buildVegetablePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 6, 8));
        panel.setOpaque(false);
        vegTypeField = new JTextField();
        vegTypeField.setName("vegType");
        soilTypeField = new JTextField();
        soilTypeField.setName("soilType");
        plantingDateField = new JTextField();
        plantingDateField.setName("plantingDate");
        harvestDateField = new JTextField();
        harvestDateField.setName("harvestDate");
        growthDurationField = new JTextField();
        growthDurationField.setName("growthDuration");
        panel.add(new JLabel("Vegetable Type:"));
        panel.add(vegTypeField);
        panel.add(new JLabel("Soil Type:"));
        panel.add(soilTypeField);
        panel.add(new JLabel("Planting Date (YYYY-MM-DD):"));
        panel.add(plantingDateField);
        panel.add(new JLabel("Harvest Date (optional):"));
        panel.add(harvestDateField);
        panel.add(new JLabel("Growth Duration (days):"));
        panel.add(growthDurationField);
        return panel;
    }

    private JPanel buildFruitPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 6, 8));
        panel.setOpaque(false);
        fruitTypeField = new JTextField();
        fruitTypeField.setName("fruitType");
        varietyField = new JTextField();
        varietyField.setName("variety");
        ripenessCombo = new JComboBox<>(Fruit.RIPENESS_LEVELS);
        ripenessCombo.setName("ripeness");
        harvestSeasonField = new JTextField();
        harvestSeasonField.setName("harvestSeason");
        plantAgeField = new JTextField();
        plantAgeField.setName("plantAge");
        panel.add(new JLabel("Fruit Type:"));
        panel.add(fruitTypeField);
        panel.add(new JLabel("Variety:"));
        panel.add(varietyField);
        panel.add(new JLabel("Ripeness Level:"));
        panel.add(ripenessCombo);
        panel.add(new JLabel("Harvest Season:"));
        panel.add(harvestSeasonField);
        panel.add(new JLabel("Plant Age (months):"));
        panel.add(plantAgeField);
        return panel;
    }

    private JPanel buildAnimalPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 6, 8));
        panel.setOpaque(false);
        animalTypeField = new JTextField();
        animalTypeField.setName("animalType");
        breedField = new JTextField();
        breedField.setName("breed");
        ageField = new JTextField();
        ageField.setName("age");
        sexCombo = new JComboBox<>(Animal.SEXES);
        sexCombo.setName("sex");
        healthCombo = new JComboBox<>(Animal.HEALTH_STATUSES);
        healthCombo.setName("health");
        purposeCombo = new JComboBox<>(Animal.PURPOSES);
        purposeCombo.setName("purpose");
        panel.add(new JLabel("Animal Type:"));
        panel.add(animalTypeField);
        panel.add(new JLabel("Breed:"));
        panel.add(breedField);
        panel.add(new JLabel("Age (months):"));
        panel.add(ageField);
        panel.add(new JLabel("Sex:"));
        panel.add(sexCombo);
        panel.add(new JLabel("Health Status:"));
        panel.add(healthCombo);
        panel.add(new JLabel("Purpose:"));
        panel.add(purposeCombo);
        return panel;
    }

    // =====================================================================
    //  Register
    // =====================================================================

    private void registerProduct() {
        try {
            Product product = buildProductFromForm();        // validated by the OOP classes
            product.setAvailability(availabilityCheck.isSelected());
            farmer.addProduct(product);                      // may throw: no stock / duplicate ID
            JOptionPane.showMessageDialog(this, "Product registered successfully.");
            clearFields();
        } catch (InvalidInputException ex) {
            showError(ex.getMessage());                      // the form stays filled so the user can fix it
        }
    }

    /** Creates the right subclass for the selected category: new Vegetable / Fruit / Animal. */
    private Product buildProductFromForm() throws InvalidInputException {
        // Checked top-to-bottom, in the same order as the form, so the user always hears
        // about the FIRST problem they would see. (The classes validate again in their own
        // constructors - that is their job - this just fixes the order of the messages.)
        String id = Validate.text(productIdField.getText(), "Product ID");
        String name = Validate.text(productNameField.getText(), "Product Name");
        double quantity = Validate.nonNegative(
                Validate.number(quantityField.getText(), "Quantity"), "Quantity");
        String unit = Validate.text(unitField.getText(), "Unit");
        double price = Validate.nonNegative(
                Validate.number(priceField.getText(), "Price"), "Price");
        String date = LocalDate.now().toString();
        String category = (String) categoryCombo.getSelectedItem();

        if (category.equals("Vegetable")) {
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
        } else if (category.equals("Fruit")) {
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        productIdField.setText("");
        productNameField.setText("");
        quantityField.setText("");
        unitField.setText("");
        priceField.setText("");
        availabilityCheck.setSelected(true);

        vegTypeField.setText("");
        soilTypeField.setText("");
        plantingDateField.setText("");
        harvestDateField.setText("");
        growthDurationField.setText("");

        fruitTypeField.setText("");
        varietyField.setText("");
        ripenessCombo.setSelectedIndex(0);
        harvestSeasonField.setText("");
        plantAgeField.setText("");

        animalTypeField.setText("");
        breedField.setText("");
        ageField.setText("");
        sexCombo.setSelectedIndex(0);
        healthCombo.setSelectedIndex(0);
        purposeCombo.setSelectedIndex(0);
    }
}
