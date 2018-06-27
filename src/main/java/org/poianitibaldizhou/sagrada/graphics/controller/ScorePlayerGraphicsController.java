package org.poianitibaldizhou.sagrada.graphics.controller;

import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PlayerScoreWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ScorePlayerGraphicsController extends GraphicsController implements Initializable{


    @FXML
    public JFXTreeTableView<PlayerScoreWrapper> tableView;
    @FXML
    public Label winnerLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TreeTableColumn<PlayerScoreWrapper, String> playerColumn = new TreeTableColumn<>("Giocatore");
        playerColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("username"));
        playerColumn.setSortable(false);
        playerColumn.setEditable(false);
        playerColumn.setResizable(false);
        playerColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 2em");
        playerColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.49));
        TreeTableColumn<PlayerScoreWrapper, Integer> totalScoreColumn = new TreeTableColumn<>("Punteggio Totale");
        totalScoreColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalScore"));
        totalScoreColumn.setSortable(false);
        totalScoreColumn.setEditable(false);
        totalScoreColumn.setResizable(false);
        totalScoreColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 2em");
        totalScoreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5));
        tableView.setShowRoot(false);
        tableView.getColumns().add(playerColumn);
        tableView.getColumns().add(totalScoreColumn);
    }

    /**
     * Init method of the Score Scene
     *
     * @param winner the user who won the game
     * @param victoryPoints every final points of each player
     */
    public void initScoreScene(UserWrapper winner, Map<UserWrapper, Integer> victoryPoints) {
        TreeItem<PlayerScoreWrapper> itemRoot = new TreeItem<>();
        victoryPoints.forEach((key, value) -> {
            PlayerScoreWrapper playerScoreWrapper = new PlayerScoreWrapper(key.getUsername(), value);
            TreeItem<PlayerScoreWrapper> item = new TreeItem<>(playerScoreWrapper);
            itemRoot.getChildren().add(item);
            tableView.setRoot(itemRoot);
        });
        winnerLabel.setText(String.format(ClientMessage.THE_WINNER_IS, winner.getUsername()));
    }


    @FXML
    public void onBackButtonAction(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).setDisable(true);
        playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.popScene());
    }
}
