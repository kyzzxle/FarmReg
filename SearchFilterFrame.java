import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * SearchFilterFrame.java
 *
 * Screen 5 - Search / Filter. Combines a name/ID keyword search with a
 * category filter, using Farmer's searchProduct() and filterByCategory().
 *
 * EXCEPTION HANDLING: nothing here is typed in as a number or date, so there
 * is nothing that can throw. An empty result is not an error - it is shown
 * as a plain "Product not found." message.
 */
public class SearchFilterFrame extends JFrame {

    private static final String[] COLUMNS = {
            "Product ID", "Product Name", "Category", "Quantity", "Unit", "Price", "Availability"
    };

    private Farmer farmer;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public SearchFilterFrame(Farmer farmer) {
        this.farmer = farmer;

        setTitle("Search / Filter Products");
        setSize(760, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(Theme.PALE_GREEN);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 8, 12));

        JPanel topPanel = Theme.card("Search & Filter");
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchField = new JTextField(15);
        searchField.setName("search");
        categoryCombo = new JComboBox<>(new String[]{"All", "Vegetable", "Fruit", "Animal"});
        categoryCombo.setName("filterCategory");
        JButton searchBtn = Theme.primaryButton("Search");
        JButton clearBtn = Theme.secondaryButton("Clear");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryCombo);
        topPanel.add(searchBtn);
        topPanel.add(clearBtn);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.setDefaultRenderer(Object.class, new Theme.ZebraRenderer());
        Theme.setColumnWidths(table, 110, 160, 105, 90, 65, 100, 115);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.WHITE);
        JPanel tableCard = Theme.card("Matching Products");
        tableCard.setLayout(new BorderLayout());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton backBtn = Theme.secondaryButton("Back");
        bottomPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> performSearch());
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            categoryCombo.setSelectedItem("All");
            populateTable(farmer.viewProducts());
        });
        backBtn.addActionListener(e -> dispose());

        populateTable(farmer.viewProducts());
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();

        // Farmer does the searching and filtering; this screen only combines the two results.
        ArrayList<Product> filtered = new ArrayList<>(
                keyword.isEmpty() ? farmer.viewProducts() : farmer.searchProduct(keyword));
        filtered.retainAll(farmer.filterByCategory(category));

        populateTable(filtered);
        if (filtered.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product not found.", "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateTable(ArrayList<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductId(), p.getProductName(), p.getCategory(),
                    p.getQuantity(), p.getUnit(), p.getPrice(),
                    p.getAvailability() ? "Available" : "Sold Out"
            });
        }
    }
}
