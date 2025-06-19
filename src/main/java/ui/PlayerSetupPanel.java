package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayerSetupPanel extends JPanel {
    private final JTextField player1Field;
    private final JTextField player2Field;
    private final JButton startButton;

    public PlayerSetupPanel(ActionListener startListener) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30,36,40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Enter Player Names");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        gbc.gridy++;
        JLabel player1Label = new JLabel("Player 1:");
        player1Label.setForeground(Color.WHITE);
        add(player1Label, gbc);

        gbc.gridx = 1;
        player1Field = new JTextField("Player 1", 15);
        add(player1Field, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel player2Label = new JLabel("Player 2:");
        player2Label.setForeground(Color.WHITE);
        add(player2Label, gbc);

        gbc.gridx = 1;
        player2Field = new JTextField("Player 2", 15);
        add(player2Field, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Start Game");
        add(startButton, gbc);

        startButton.addActionListener(startListener);
    }

    public String getPlayer1Name() {
        return player1Field.getText().trim().isEmpty() ? "Player 1" : player1Field.getText().trim();
    }

    public String getPlayer2Name() {
        return player2Field.getText().trim().isEmpty() ? "Player 2" : player2Field.getText().trim();
    }
}
