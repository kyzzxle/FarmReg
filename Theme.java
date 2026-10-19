import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Theme.java
 *
 * GUI helper only - not a business-logic class like the Admin/ProductManager
 * ones the proposal ruled out. It holds the light-green color scheme and a
 * few styling helpers in one place, so the five GUI classes do not each
 * repeat the same color codes.
 */
public final class Theme {

    private Theme() { }

    public static final Color DARK_GREEN  = new Color(0x2F, 0x5D, 0x2A);
    public static final Color MED_GREEN   = new Color(0x4F, 0x8A, 0x3A);
    public static final Color LIGHT_GREEN = new Color(0xD9, 0xEB, 0xCE);
    public static final Color PALE_GREEN  = new Color(0xF1, 0xF8, 0xEC);
    public static final Color WHITE       = Color.WHITE;
    public static final Color TEXT_DARK   = new Color(0x26, 0x2B, 0x24);
    public static final Color RED         = new Color(0xB0, 0x3A, 0x2E);

    public static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 13);

    /** Installs Nimbus (built into the JDK) and recolors it green. */
    public static void apply() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Falls back to the default look - not a fatal problem.
        }
        UIManager.put("nimbusBase", MED_GREEN);
        UIManager.put("nimbusBlueGrey", new Color(0xA9, 0xC4, 0x9A));
        UIManager.put("control", PALE_GREEN);
        UIManager.put("nimbusLightBackground", WHITE);
        UIManager.put("text", TEXT_DARK);
        UIManager.put("nimbusSelectionBackground", MED_GREEN);
        UIManager.put("nimbusFocus", MED_GREEN);
        UIManager.put("defaultFont", new FontUIResource(FONT_BODY));
    }

    // ---------- Buttons ----------

    private static JButton makeButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BUTTON);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setMargin(new Insets(4, 6, 4, 6));
        return b;
    }

    public static JButton primaryButton(String text)   { return makeButton(text, MED_GREEN, WHITE); }
    public static JButton secondaryButton(String text) { return makeButton(text, LIGHT_GREEN, DARK_GREEN); }
    public static JButton dangerButton(String text)    { return makeButton(text, RED, WHITE); }

    // ---------- Panels and labels ----------

    public static TitledBorder sectionBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MED_GREEN, 1), " " + title + " ");
        border.setTitleColor(DARK_GREEN);
        border.setTitleFont(FONT_HEADER);
        return border;
    }

    /** A white rounded-feel card with a green titled border. */
    public static JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                sectionBorder(title), new EmptyBorder(6, 10, 10, 10)));
        return p;
    }

    /** Small green label sitting above an input field. */
    public static JPanel labeled(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(FONT_SMALL);
        l.setForeground(DARK_GREEN);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    /** 50 -> "50", 2.5 -> "2.5" (no pointless ".0"). */
    public static String formatNumber(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e12) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }

    // ---------- Table styling ----------

    /** Draws every cell with alternating white / pale-green rows. */
    public static class ZebraRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                       boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, false, row, col);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            if (sel) {
                setBackground(MED_GREEN);
                setForeground(WHITE);
            } else {
                setBackground(row % 2 == 0 ? WHITE : PALE_GREEN);
                setForeground(TEXT_DARK);
            }
            return this;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                       boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, v, false, false, row, col);
            setOpaque(true);
            setBackground(DARK_GREEN);
            setForeground(WHITE);
            setFont(FONT_BUTTON);
            setBorder(new EmptyBorder(6, 8, 6, 8));
            return this;
        }
    }

    /** Gives each table column a sensible starting width so headers are not cut off. */
    public static void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(WHITE);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
    }
}
