package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class MenuPanel extends JPanel {
    private Image cardImage;

    public MenuPanel(ActionListener onPlayClicked) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30,36,40));
        try {
            cardImage = new ImageIcon(getClass().getResource("/cards.png")).getImage();
        } catch (Exception e) {
            cardImage = null;
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int w = 350, h = 520;
                int x = (getWidth() - w) / 2;
                int y = (getHeight() - h) / 2;
                int border = 25;

                // --- DIBUJA SOLO LA CARTA CENTRAL ---
                // 1. Dibuja la carta UNO (o fondo negro)
                if (cardImage != null) {
                    g2.drawImage(cardImage, x, y, w, h, this);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillRoundRect(x, y, w, h, 40, 40);
                    // 2. Dibuja el óvalo rojo encima de la carta, pero debajo del borde blanco
                    int ovalW = w + 120;
                    int ovalH = h - 270;
                    int centerX = x + w/2;
                    int centerY = y + h/2;
                    int ovalX = centerX - ovalW/2;
                    int ovalY = centerY - ovalH/2;
                    AffineTransform old2 = g2.getTransform();
                    g2.rotate(Math.toRadians(-38), centerX, centerY);
                    g2.setColor(new Color(200, 0, 0));
                    g2.fillOval(ovalX, ovalY, ovalW, ovalH);
                    // --- TEXTO UNO! EN EL CENTRO DEL OVALO ---
                    String text = "UNO!";
                    int fontSize = 105;
                    Font font = new Font("Arial Black", Font.BOLD, fontSize);
                    g2.setFont(font);
                    FontMetrics fm = g2.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    int textX = centerX - textWidth/2;
                    int textY = centerY + textHeight/2 - 30;
                    for (int i = 10; i > 0; i -= 2) {
                        g2.setColor(new Color(0,0,0, 200 - i*10));
                        g2.drawString(text, textX + i, textY + i);
                    }
                    java.awt.font.GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), text);
                    Shape outline = gv.getOutline(textX, textY);
                    g2.setStroke(new BasicStroke(8f));
                    g2.setColor(Color.WHITE);
                    g2.draw(outline);
                    g2.setColor(Color.YELLOW);
                    g2.fill(outline);
                    g2.setTransform(old2);
                }
                // 3. Dibuja el borde blanco por encima del óvalo
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(border));
                g2.drawRoundRect(x - border/2, y - border/2, w + border, h + border, 60, 60);
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(400 + 76, 540 + 76); // suma el borde
            }
            private void drawUnoCard(Graphics2D g2, int w, int h, int border, Image cardImage) {
                // Dibuja borde blanco
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(border));
                g2.drawRoundRect(0, 0, w, h, 60, 60);
                // Dibuja carta UNO (o fondo negro)
                if (cardImage != null) {
                    g2.drawImage(cardImage, 0, 0, w, h, null);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillRoundRect(0, 0, w, h, 40, 40);
                    // Dibuja óvalo rojo
                    int ovalW = w + 120;
                    int ovalH = h - 270;
                    int centerX = w/2;
                    int centerY = h/2;
                    int ovalX = centerX - ovalW/2;
                    int ovalY = centerY - ovalH/2;
                    AffineTransform old = g2.getTransform();
                    g2.rotate(Math.toRadians(-38), centerX, centerY);
                    g2.setColor(new Color(200, 0, 0));
                    g2.fillOval(ovalX, ovalY, ovalW, ovalH);
                    g2.setTransform(old);
                }
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);

        // Botón centrado en la pantalla, más abajo y de la mitad del tamaño
        JButton playButton = new JButton("INICIAR PARTIDA");
        playButton.setFont(new Font("Arial", Font.BOLD, 18)); // más pequeño
        playButton.setBackground(new Color(255,255,255,0)); // fondo transparente
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        playButton.setOpaque(false);
        // Más abajo y más pequeño
        int buttonW = 170, buttonH = 35;
        int buttonX = (cardPanel.getPreferredSize().width - buttonW) / 2;
        int buttonY = (int)(cardPanel.getPreferredSize().height * 0.83); // 83% hacia abajo
        playButton.setBounds(buttonX, buttonY, buttonW, buttonH);
        playButton.addActionListener(onPlayClicked);
        cardPanel.add(playButton);

        add(cardPanel, gbc);
    }
}
