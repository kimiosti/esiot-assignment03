package unibo.esiot2024;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Graphical User Interface to allow user to input database credentials.
 */
public final class GUI {

    private static final int TEXT_FIELD_COLUMNS = 10;
    private static final int MAIN_GUI_ROWS = 6;
    private static final int MAIN_GUI_COLS = 1;

    private final JFrame frame;

    /**
     * Instantiates a simple graphical interface to collect database login data.
     * @param msgs messages to be shown by the user interface.
     */
    public GUI(final String... msgs) {
        this.frame = new JFrame("Controller setup");
        this.frame.setLayout(new GridLayout(MAIN_GUI_ROWS, MAIN_GUI_COLS));

        final var messagePanel = new JPanel();
        Stream.of(msgs).forEach(msg -> messagePanel.add(new JLabel(msg)));
        this.frame.add(messagePanel);

        final var dbUser = this.singleInput("DB username:");
        final var dbPassword = this.singlePassword("DB");
        final var serialPort = this.singleInput("Serial port:");
        final var mqttBroker = this.singleInput("MQTT broker:");

        final var buttonPanel = new JPanel();
        final var button = new JButton("Start");
        button.addActionListener(e -> {
            Application.launch(
                dbUser.getText(),
                String.valueOf(dbPassword.getPassword()),
                serialPort.getText(),
                mqttBroker.getText()
            );
            this.frame.dispose();
        });
        buttonPanel.add(button);

        this.frame.add(buttonPanel);
        this.frame.setSize(new Dimension(
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2),
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2)
        ));
        this.frame.getRootPane().setDefaultButton(button);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
    }

    private JTextField singleInput(final String label) {
        final var panel = new JPanel();
        panel.add(new JLabel(label));
        final var field = new JTextField(TEXT_FIELD_COLUMNS);
        panel.add(field);
        this.frame.add(panel);
        return field;
    }

    private JPasswordField singlePassword(final String prefix) {
        final var panel = new JPanel();
        panel.add(new JLabel(prefix + " password:"));
        final var field = new JPasswordField(TEXT_FIELD_COLUMNS);
        panel.add(field);
        this.frame.add(panel);
        return field;
    }
}
