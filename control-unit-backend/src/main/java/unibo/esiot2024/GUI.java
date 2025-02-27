package unibo.esiot2024;

import java.awt.GridLayout;
import java.awt.Toolkit;

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

    /**
     * Instantiates a simple graphical interface to collect database login data.
     */
    public GUI() {
        final var frame = new JFrame("Database login");
        frame.setLayout(new GridLayout(3, 1));

        final var userPanel = new JPanel();
        final var username = new JTextField(TEXT_FIELD_COLUMNS);
        userPanel.add(new JLabel("Username: "));
        userPanel.add(username);

        final var passPanel = new JPanel();
        final var password = new JPasswordField(TEXT_FIELD_COLUMNS);
        passPanel.add(new JLabel("Password: "));
        passPanel.add(password);

        final var submitPanel = new JPanel();
        final var submit = new JButton("Login");
        submit.addActionListener(e -> {
            frame.dispose();
            Application.launch(username.getText(), new String(password.getPassword()));
        });
        submitPanel.add(submit);

        frame.add(userPanel);
        frame.add(passPanel);
        frame.add(submitPanel);

        frame.setSize(
            (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3,
            (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3
        );
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
