import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainMenuFrame.java
 *
 * Screen 1 - Main Menu / Dashboard. One big button per task; each opens its
 * own window. The Exit button is red so it stands apart from the green
 * action buttons.
 *
 * This is the only class that creates the Farmer object. Every other screen
 * receives that same Farmer instance through its constructor, so there is
 * one shared list of products instead of several disconnected copies - no
 * static fields or singletons needed.
 */
public class MainMenuFrame extends JFrame {

    private Farmer farmer;

    public MainMenuFrame() {
        farmer = new Farmer();

        setTitle("FarmReg - Farmer Product Registration System");
        setSize(440, 480);
        // Both the Exit button and the window's X button ask first (see confirmExit()).
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.PALE_GREEN);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildButtons(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JLabel title = new JLabel("FarmReg");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Theme.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Farmer Product Registration System");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Theme.LIGHT_GREEN);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Theme.DARK_GREEN);
        header.setBorder(new EmptyBorder(18, 20, 16, 20));
        header.add(title);
        header.add(Box.createVerticalStrut(2));
        header.add(subtitle);
        return header;
    }

    private JPanel buildButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 12, 12));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(26, 70, 30, 70));

        JButton farmerInfoBtn = menuButton(Theme.primaryButton("Farmer Information"));
        JButton registerProductBtn = menuButton(Theme.primaryButton("Register Product"));
        JButton viewProductsBtn = menuButton(Theme.primaryButton("View Products"));
        JButton searchProductsBtn = menuButton(Theme.primaryButton("Search Products"));
        JButton exitBtn = menuButton(Theme.dangerButton("Exit"));         // red
        exitBtn.setName("exitBtn");

        farmerInfoBtn.addActionListener(e -> new FarmerInfoFrame(farmer).setVisible(true));
        registerProductBtn.addActionListener(e -> new ProductRegistrationFrame(farmer).setVisible(true));
        viewProductsBtn.addActionListener(e -> new ProductListFrame(farmer).setVisible(true));
        searchProductsBtn.addActionListener(e -> new SearchFilterFrame(farmer).setVisible(true));
        exitBtn.addActionListener(e -> confirmExit());

        buttonPanel.add(farmerInfoBtn);
        buttonPanel.add(registerProductBtn);
        buttonPanel.add(viewProductsBtn);
        buttonPanel.add(searchProductsBtn);
        buttonPanel.add(exitBtn);
        return buttonPanel;
    }

    /** Makes a menu button a little bigger than a normal one. */
    private JButton menuButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        return button;
    }

    /** Products are kept in memory only, so warn the user that closing the program discards them. */
    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit FarmReg?\nRegistered products are not saved when the program closes.",
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
