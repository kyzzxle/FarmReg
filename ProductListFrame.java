import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * ProductListFrame.java
 *
 * Screen 4 - Product List. Displays every registered product in a JTable
 * with only the shared columns (category-specific detail belongs in "View
 * Details", not the table). "View Details" calls product.getProductDetails()
 * on the selected row's actual object - polymorphism driven from the GUI.
 *
 * EXCEPTION HANDLING: because this program can have several windows open at
 * once, a product shown in this table may already have been deleted from
 * another window. Delete (and the Edit dialog) therefore handle
 * ProductNotFoundException: the user gets "Product not found." and the table
 * refreshes, instead of the program crashing or editing a ghost.
 */
public class ProductListFrame extends JFrame {

    private static final String[] COLUMNS = {
            "Product ID", "Product Name", "Category", "Quantity", "Unit", "Price", "Availability"
    };

    private Farmer farmer;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Product> displayedProducts;

    public ProductListFrame(Farmer farmer) {
        this.farmer = farmer;

        setTitle("Product List");
        setSize(760, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(Theme.PALE_GREEN);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 8, 12));

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;       // read-only - edits go through the Edit button/dialog
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Theme.styleTable(table);
        table.setDefaultRenderer(Object.class, new Theme.ZebraRenderer());
        Theme.setColumnWidths(table, 110, 160, 105, 90, 65, 100, 115);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton viewDetailsBtn = Theme.secondaryButton("View Details");
        JButton editBtn = Theme.primaryButton("Edit");
        JButton deleteBtn = Theme.dangerButton("Delete");
        JButton refreshBtn = Theme.secondaryButton("Refresh");
        JButton backBtn = Theme.secondaryButton("Back");
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        JPanel tableCard = Theme.card("Registered Products");
        tableCard.setLayout(new BorderLayout());
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        viewDetailsBtn.addActionListener(e -> viewDetails());
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> dispose());

        refreshTable();
    }

    private void refreshTable() {
        populateTable(new ArrayList<>(farmer.viewProducts()));
    }

    private void populateTable(ArrayList<Product> products) {
        this.displayedProducts = products;
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductId(), p.getProductName(), p.getCategory(),
                    p.getQuantity(), p.getUnit(), p.getPrice(),
                    p.getAvailability() ? "Available" : "Sold Out"
            });
        }
    }

    private Product getSelectedProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return displayedProducts.get(row);
    }

    private void viewDetails() {
        Product p = getSelectedProduct();
        if (p == null) return;
        // p.getProductDetails() is overridden differently in Vegetable, Fruit,
        // and Animal - this one call shows the right details either way.
        JOptionPane.showMessageDialog(this, p.getProductDetails(),
                "Product Details - " + p.getProductId(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void editSelected() {
        Product p = getSelectedProduct();
        if (p == null) return;
        new EditProductDialog(this, farmer, p).setVisible(true);
        refreshTable();
    }

    private void deleteSelected() {
        Product p = getSelectedProduct();
        if (p == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete product " + p.getProductId() + " (" + p.getProductName() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            farmer.removeProduct(p.getProductId());          // throws if it is already gone
            JOptionPane.showMessageDialog(this, "Product deleted successfully.");
        } catch (ProductNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }
}
