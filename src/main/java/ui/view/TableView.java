package ui.view;

import application.IGameAppService;
import ui.common.StyleUtil;

import javax.swing.*;

import domain.card.Card;
import domain.common.DomainEvent;
import domain.common.DomainEventPublisher;
import domain.common.DomainEventSubscriber;
import domain.game.events.CardPlayed;

import java.awt.*;

public class TableView extends JPanel implements DomainEventSubscriber {
    private final JPanel table;
    private final IGameAppService appService;

    public TableView(IGameAppService appService){
        this.appService = appService;

        setOpaque(false);
        setLayout(new GridBagLayout());
        table = new JPanel();
        table.setBackground(new Color(64,64,64));

        initTable();
        initInfoView();

        DomainEventPublisher.getInstance().subscribe(this);
    }

    private void initTable(){
        table.removeAll();

        table.setPreferredSize(new Dimension(500,200));
        table.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        Card topCard = appService.peekTopCard();
        Color background = StyleUtil.convertCardColor(topCard.getColor());

        var cardView = new CardView(topCard);
        table.add(cardView, c);

        table.setBackground(background);
        table.revalidate();
    }

    private void initInfoView() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 130, 0, 45);
        add(table,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 1, 0, 1);

        add(new GameStatusView(appService), c);
    }

    @Override
    public void handleEvent(DomainEvent event) {
        if(event instanceof CardPlayed) {
            initTable();
        }
    }

    // -----------------------------
    // Patrón Observer en la UI
    // -----------------------------
    // Esta vista de la mesa de juego se suscribe a los eventos del juego
    // implementando DomainEventSubscriber. Cuando se recibe un evento (por ejemplo,
    // una carta jugada), la vista se actualiza automáticamente.
    //
    // Así, la UI reacciona a los cambios en el estado del juego sin acoplamiento directo,
    // cumpliendo el patrón Observer.
}
