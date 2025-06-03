package ui.view;

import application.IGameAppService;
import application.dto.PlayerInfoDTO;
import ui.common.StyleUtil;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Dimension;

import domain.card.Card;
import domain.common.DomainEvent;
import domain.common.DomainEventPublisher;
import domain.common.DomainEventSubscriber;
import domain.game.events.CardPlayed;
import domain.game.events.GameOver;

public class PlayerView extends JPanel implements DomainEventSubscriber {
    private JLayeredPane handCardsView;
    private Box controlPanel;

    private JLabel nameLabel;
    private JButton drawButton;
    private JButton sayUnoButton;
    private boolean hasSaidUno = false;

    private final PlayerInfoDTO player;

    private final IGameAppService appService;

    public PlayerView(PlayerInfoDTO player, IGameAppService appService) {
        this.player = player;
        this.appService = appService;

        initView();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        DomainEventPublisher.subscribe(this);
    }

    private void initView() {
        Box layout = Box.createHorizontalBox();

        initHandCardsView();
        initControlPanel();

        layout.add(handCardsView);
        layout.add(Box.createHorizontalStrut(20));
        layout.add(controlPanel);
        add(layout);

        setOpaque(false);

        refresh();
    }

    private void initHandCardsView() {
        handCardsView = new JLayeredPane();
        handCardsView.setPreferredSize(new java.awt.Dimension(600, 175));
        handCardsView.setOpaque(false);
    }

    private void renderHandCardsView() {
        handCardsView.removeAll();

        java.util.List<Card> handCards = appService.getHandCards(player.getId()).collect(java.util.stream.Collectors.toList());

        int width = handCardsView.getWidth() == 0 ? handCardsView.getPreferredSize().width : handCardsView.getWidth();
        int offset = calculateOffset(width, handCards.size());
        java.awt.Point originPoint = getFirstCardPoint(handCards.size());
        int i = 0;
        for (Card card : handCards) {
            CardView cardView = new CardView(card, this::playCard);
            cardView.setBounds(originPoint.x, originPoint.y,
                cardView.getDimension().width, cardView.getDimension().height);
            handCardsView.add(cardView, i++);
            handCardsView.moveToFront(cardView);
            originPoint.x += offset;
        }
        handCardsView.revalidate();
    }

    private java.awt.Point getFirstCardPoint(int totalCards) {
        java.awt.Point p = new java.awt.Point(0, 20);
        if (totalCards < domain.game.DealerService.TOTAL_INITIAL_HAND_CARDS) {
            int width = handCardsView.getWidth() == 0 ? handCardsView.getPreferredSize().width : handCardsView.getWidth();
            int offset = calculateOffset(width, totalCards);
            p.x = (width - offset * totalCards) / 2;
        }
        return p;
    }

    private int calculateOffset(int width, int totalCards) {
        if (totalCards <= domain.game.DealerService.TOTAL_INITIAL_HAND_CARDS) {
            return 71;
        } else {
            return (width - 100) / (totalCards - 1);
        }
    }

    private void initControlPanel() {
        initDrawButton();
        initSayNoButton();
        initNameLabel();

        controlPanel = Box.createVerticalBox();
        controlPanel.add(nameLabel);
        controlPanel.add(drawButton);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(sayUnoButton);
    }

    private void toggleControlPanel() {
        boolean isMyTurn = appService.getCurrentPlayer().getId().equals(player.getId());

        if (appService.isGameOver()) {
            isMyTurn = false;
        }

        drawButton.setVisible(isMyTurn);
        sayUnoButton.setVisible(isMyTurn);

        controlPanel.revalidate();
    }

    private void initNameLabel() {
        nameLabel = new JLabel(player.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font(StyleUtil.DEFAULT_FONT, Font.BOLD, 15));
    }

    private void initSayNoButton() {
        sayUnoButton = new JButton("Say UNO");
        sayUnoButton.setBackground(new Color(149, 55, 53));
        sayUnoButton.setFont(new Font(StyleUtil.DEFAULT_FONT, Font.BOLD, 20));
        sayUnoButton.setFocusable(false);

        sayUnoButton.addActionListener(e -> hasSaidUno = true);
    }

    private void initDrawButton() {
        drawButton = new JButton("Draw");

        drawButton.setBackground(new Color(79, 129, 189));
        drawButton.setFont(new Font(StyleUtil.DEFAULT_FONT, Font.BOLD, 20));
        drawButton.setFocusable(false);

        drawButton.addActionListener(e -> appService.drawCard(player.getId()));
    }

    private void playCard(Card selectedCard) {
        Card cardToPlay = selectedCard;

        if (selectedCard.getType() == domain.card.CardType.WILD_COLOR || selectedCard.getType() == domain.card.CardType.WILD_DRAW_FOUR) {
            domain.card.CardColor chosenColor = ColorPicker.getInstance().show();
            cardToPlay = new domain.card.WildCard(selectedCard.getType(), chosenColor);
        }

        appService.playCard(player.getId(), cardToPlay, hasSaidUno);
        hasSaidUno = false;
    }

    private void refresh() {
        renderHandCardsView();
        toggleControlPanel();

        repaint();
    }

    @Override
    public void handleEvent(DomainEvent event) {
        if (event instanceof CardPlayed
            || event instanceof GameOver) {
            refresh();
        }
    }
}
