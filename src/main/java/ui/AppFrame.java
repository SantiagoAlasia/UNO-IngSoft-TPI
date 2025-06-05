package ui;

import application.IGameAppService;
import ui.view.PlayerView;
import ui.view.TableView;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private final JPanel mainLayout;
    private final IGameAppService appService;

    public AppFrame(IGameAppService appService){
        this.appService = appService;
        mainLayout = new JPanel(new CardLayout());
        setupMenuPanel();
        showFrame();
    }

    private void showFrame() {
        setVisible(true);
        setResizable(false);
        setLocation(200, 100);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupMenuPanel() {
        MenuPanel menuPanel = new MenuPanel(e -> {
            setupPlayerSetupMenu();
        });
        menuPanel.setPreferredSize(new Dimension(960, 720));
        menuPanel.setBackground(new Color(30,36,40));
        mainLayout.add(menuPanel, "menu");
        add(mainLayout);
        CardLayout cl = (CardLayout) mainLayout.getLayout();
        cl.show(mainLayout, "menu");
    }

    private void setupPlayerSetupMenu() {
        final PlayerSetupPanel[] setupPanel = new PlayerSetupPanel[1];
        setupPanel[0] = new PlayerSetupPanel(e -> {
            String name1 = setupPanel[0].getPlayer1Name();
            String name2 = setupPanel[0].getPlayer2Name();
            appService.setPlayerNames(name1, name2); // You may need to implement this in your service
            showGame();
        });
        setupPanel[0].setPreferredSize(new Dimension(960, 720));
        setupPanel[0].setBackground(new Color(30,36,40));
        mainLayout.add(setupPanel[0], "setup");
        CardLayout cl = (CardLayout) mainLayout.getLayout();
        cl.show(mainLayout, "setup");
    }

    private void showGame() {
        mainLayout.removeAll();
        setupLayout();
        mainLayout.revalidate();
        mainLayout.repaint();
    }

    private void setupLayout() {
        mainLayout.setPreferredSize(new Dimension(960,720));
        mainLayout.setBackground(new Color(30,36,40));
        mainLayout.setLayout(new BorderLayout());

        java.util.List<application.dto.PlayerInfoDTO> players = appService.getPlayerInfos();
        PlayerView playerView1 = new PlayerView(players.get(0), appService);
        PlayerView playerView2 = new PlayerView(players.get(1), appService);

        TableView tableView = new TableView(appService);

        mainLayout.add(playerView1, BorderLayout.SOUTH);
        mainLayout.add(tableView, BorderLayout.CENTER);
        mainLayout.add(playerView2, BorderLayout.NORTH);
    }
}
