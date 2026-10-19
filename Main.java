import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Main.java
 *
 * GUI entry point - opens the Main Menu.
 *
 * Safety net: every expected problem (bad input, product not found) is
 * already handled with try/catch right where it happens. This default
 * handler is only the last line of defense for something unexpected - the
 * user sees a message instead of the program silently misbehaving.
 */
public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Something unexpected went wrong:\n" + error.getMessage(),
                    "Unexpected Error", JOptionPane.ERROR_MESSAGE);
        });

        Theme.apply();      // light-green look - must be set before any window is created
        SwingUtilities.invokeLater(() -> new MainMenuFrame().setVisible(true));
    }
}
