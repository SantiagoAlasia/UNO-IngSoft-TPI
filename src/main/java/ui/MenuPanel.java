package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    public MenuPanel(ActionListener onPlayClicked) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30,36,40));
        JButton playButton = new JButton("Jugar una partida");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));
        playButton.addActionListener(onPlayClicked);
        add(playButton);
    }
}
