package org.poianitibaldizhou.sagrada.graphics.view;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.io.IOException;

public class GameView implements IGameView, IGameObserver{

    @Override
    public void ack(String ack) throws IOException {

    }

    @Override
    public void err(String err) throws IOException {

    }

    @Override
    public void onPlayersCreate(String players) throws IOException {

    }

    @Override
    public void onPublicObjectiveCardsDraw(String publicObjectiveCards) throws IOException {

    }

    @Override
    public void onToolCardsDraw(String toolCards) throws IOException {

    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) throws IOException {

    }

    @Override
    public void onPrivateObjectiveCardDraw(String privateObjectiveCards) throws IOException {

    }

    @Override
    public void onSchemaCardsDraw(String schemaCards) throws IOException {

    }
}
