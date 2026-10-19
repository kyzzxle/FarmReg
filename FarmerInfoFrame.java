import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * FarmerInfoFrame.java
 *
 * Screen 2 - Farmer Information. Register once with Save, edit later with
 * Update. The Farmer ID field locks after the first save.
 *
 * EXCEPTION HANDLING: this screen only turns the typed text into values.
 * The rules (required fields, farm size not negative) are enforced by the
 * Farmer class, which throws InvalidInputException; this screen catches it
 * and shows the message to the user.
 */
public class FarmerInfoFrame extends JFrame {

    private Farmer farmer;
    private JTextField idField, nameField, contactField, addressField,
            farmNameField, locationField, sizeField;

    public FarmerInfoFrame(Farmer farmer) {
        this.farmer = farmer;

        setTitle("Farmer Information");
        setSize(470, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(Theme.PALE_GREEN);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 8, 12));

        JPanel formPanel = Theme.card("Farmer Information");
        formPanel.setLayout(new GridLayout(7, 2, 8, 10));

        idField = new JTextField();
        idField.setName("farmerId");
        nameField = new JTextField();
        nameField.setName("farmerName");
        contactField = new JTextField();
        contactField.setName("contactNumber");
        addressField = new JTextField();
        addressField.setName("address");
        farmNameField = new JTextField();
        farmNameField.setName("farmName");
        locationField = new JTextField();
        locationField.setName("farmLocation");
        sizeField = new JTextField();
        sizeField.setName("farmSize");

        formPanel.add(new JLabel("Farmer ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Farmer Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Farm Name:"));
        formPanel.add(farmNameField);
        formPanel.add(new JLabel("Farm Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Farm Size (hectares):"));
        formPanel.add(sizeField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton saveBtn = Theme.primaryButton("Save");
        JButton updateBtn = Theme.secondaryButton("Update");
        JButton clearBtn = Theme.secondaryButton("Clear");
        buttonPanel.add(saveBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(clearBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadExistingData();

        saveBtn.addActionListener(e -> saveFarmer());
        updateBtn.addActionListener(e -> updateFarmer());
        clearBtn.addActionListener(e -> clearFields());
    }

    private void loadExistingData() {
        if (farmer.isRegistered()) {
            idField.setText(farmer.getFarmerId());
            idField.setEditable(false);
            nameField.setText(farmer.getFarmerName());
            contactField.setText(farmer.getContactNumber());
            addressField.setText(farmer.getAddress());
            farmNameField.setText(farmer.getFarmName());
            locationField.setText(farmer.getFarmLocation());
            sizeField.setText(String.valueOf(farmer.getFarmSize()));
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveFarmer() {
        if (farmer.isRegistered()) {
            JOptionPane.showMessageDialog(this, "Farmer is already registered. Use Update instead.",
                    "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            // Checked top-to-bottom, in the same order as the form, so the user hears
            // about the first problem they would see. Farmer validates again itself.
            String id = Validate.text(idField.getText(), "Farmer ID");
            String name = Validate.text(nameField.getText(), "Farmer Name");
            String contact = Validate.text(contactField.getText(), "Contact Number");
            String address = Validate.text(addressField.getText(), "Address");
            String farmName = Validate.text(farmNameField.getText(), "Farm Name");
            String location = Validate.text(locationField.getText(), "Farm Location");
            double size = Validate.number(sizeField.getText(), "Farm size");

            farmer.registerFarmer(id, name, contact, address, farmName, location, size);
            idField.setEditable(false);
            JOptionPane.showMessageDialog(this, "Farmer information saved successfully.");
        } catch (InvalidInputException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateFarmer() {
        if (!farmer.isRegistered()) {
            JOptionPane.showMessageDialog(this, "No farmer record exists yet. Use Save first.",
                    "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            String name = Validate.text(nameField.getText(), "Farmer Name");
            String contact = Validate.text(contactField.getText(), "Contact Number");
            String address = Validate.text(addressField.getText(), "Address");
            String farmName = Validate.text(farmNameField.getText(), "Farm Name");
            String location = Validate.text(locationField.getText(), "Farm Location");
            double size = Validate.number(sizeField.getText(), "Farm size");

            farmer.updateFarmer(name, contact, address, farmName, location, size);
            JOptionPane.showMessageDialog(this, "Farmer information updated successfully.");
        } catch (InvalidInputException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearFields() {
        if (!farmer.isRegistered()) {
            idField.setText("");
        }
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        farmNameField.setText("");
        locationField.setText("");
        sizeField.setText("");
    }
}
