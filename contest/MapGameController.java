import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara moveChara;
    public GridPane mapGridPane;
    public GridPane itemGridPane;
    public Label scoreLabel;
    public Label timeLabel;
    public AudioClip mainAudioClip;
    public AudioClip itemAudioClip;
    public AudioClip coinAudioClip;
    public AudioClip portalAudioClip;
    public AudioClip bombAudioClip;
    public AudioClip hackAudioClip;
    public Timer timer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mainAudioClip == null) {
            mainAudioClip = new AudioClip(getClass().getResource("audio/main.mp3").toExternalForm());
            mainAudioClip.setCycleCount(AudioClip.INDEFINITE);
            mainAudioClip.setVolume(0.02);
            mainAudioClip.play();
        }
        if (itemAudioClip == null) {
            itemAudioClip = new AudioClip(getClass().getResource("audio/item.mp3").toExternalForm());
            itemAudioClip.setCycleCount(1);
            itemAudioClip.setVolume(0.02);
        }
        if (coinAudioClip == null) {
            coinAudioClip = new AudioClip(getClass().getResource("audio/coin.mp3").toExternalForm());
            coinAudioClip.setCycleCount(1);
            coinAudioClip.setVolume(0.02);
        }
        if (portalAudioClip == null) {
            portalAudioClip = new AudioClip(getClass().getResource("audio/portal.mp3").toExternalForm());
            portalAudioClip.setCycleCount(1);
            portalAudioClip.setVolume(0.02);
        }
        if (bombAudioClip == null) {
            bombAudioClip = new AudioClip(getClass().getResource("audio/bomb.mp3").toExternalForm());
            bombAudioClip.setCycleCount(1);
            bombAudioClip.setVolume(0.05);
        }
        if (hackAudioClip == null) {
            hackAudioClip = new AudioClip(getClass().getResource("audio/hack.mp3").toExternalForm());
            hackAudioClip.setCycleCount(1);
            hackAudioClip.setVolume(0.05);
        }
        mapData = new MapData(21, 15);
        moveChara = new MoveChara(1, 1, mapData);
        DrawMap(moveChara, mapData);
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    long remainingTime = mapData.GetRemainingTime();
                    timeLabel.setText(String.valueOf(remainingTime));
                    if (remainingTime <= 0) {
                        timer.cancel();
                        OverButtonAction();
                        return;
                    }
                });
            }
        }, 0, 500);
    }

    public void DrawMap(MoveChara moveChara, MapData mapData) {
        int moveCharaPositionX = moveChara.GetPositionX();
        int moveCharaPositionY = moveChara.GetPositionY();
        int itemType = mapData.GetItemType(moveCharaPositionX, moveCharaPositionY);
        switch (itemType) {
            case MapData.ITEM_TYPE_PORTAL:
                printAction("PORTAL");
                portalAudioClip.play();
                moveChara.Portal();
                break;
            default:
                if (itemType != MapData.ITEM_TYPE_NULL && itemType != MapData.ITEM_TYPE_GOAL) {
                    printAction("GET");
                    if (itemType == MapData.ITEM_TYPE_COIN) {
                        coinAudioClip.play();
                    } else {
                        itemAudioClip.play();
                    }
                    moveChara.AddItem(itemType);
                    mapData.SetItemType(moveCharaPositionX, moveCharaPositionY, MapData.ITEM_TYPE_NULL);
                }
                break;
        }

        moveCharaPositionX = moveChara.GetPositionX();
        moveCharaPositionY = moveChara.GetPositionY();
        mapGridPane.getChildren().clear();
        for (int y = 0; y < mapData.GetHeight(); y++) {
            for (int x = 0; x < mapData.GetWidth(); x++) {
                if (x == moveCharaPositionX && y == moveCharaPositionY) {
                    mapGridPane.add(moveChara.GetCharaImageView(), x, y);
                } else {
                    mapGridPane.add(mapData.GetMapItemImageView(x, y), x, y);
                }
            }
        }
        itemGridPane.getChildren().clear();
        ArrayList<Integer> itemInventory = moveChara.GetItemInventory();
        for (int i = 0; i < itemInventory.size(); i++) {
            itemGridPane.add(mapData.GetItemImageView(itemInventory.get(i)), i, 0);
        }
        scoreLabel.setText(String.valueOf(moveChara.GetScore()));

        if (itemType == MapData.ITEM_TYPE_GOAL) {
            if (moveChara.GetItemInventory().contains(MapData.ITEM_TYPE_KEY)) {
                printAction("CLEAR");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Clear!");
                alert.showAndWait();
                moveChara.AddScore(1000);
                RemapButtonAction();
            }
        }
    }

    public void KeyAction(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        System.out.println("KeyCode:" + keyCode);
        switch (keyCode) {
            case A:
                LeftButtonAction();
                break;
            case S:
                DownButtonAction();
                break;
            case W:
                UpButtonAction();
                break;
            case D:
                RightButtonAction();
                break;
            case B:
                BombButtonAction(null);
                break;
            case H:
                HackButtonAction(null);
                break;
            case R:
            case DELETE:
            case BACK_SPACE:
                RemapButtonAction();
                break;
            case ESCAPE:
                System.exit(0);
            default:
                break;
        }
    }

    public void UpButtonAction() {
        printAction("UP");
        moveChara.SetCharaDirection(MoveChara.TYPE_UP);
        moveChara.Move(MoveChara.TYPE_UP, 1);
        DrawMap(moveChara, mapData);
    }

    public void DownButtonAction() {
        printAction("DOWN");
        moveChara.SetCharaDirection(MoveChara.TYPE_DOWN);
        moveChara.Move(MoveChara.TYPE_DOWN, 1);
        DrawMap(moveChara, mapData);
    }

    public void LeftButtonAction() {
        printAction("LEFT");
        moveChara.SetCharaDirection(MoveChara.TYPE_LEFT);
        moveChara.Move(MoveChara.TYPE_LEFT, 1);
        DrawMap(moveChara, mapData);
    }

    public void RightButtonAction() {
        printAction("RIGHT");
        moveChara.SetCharaDirection(MoveChara.TYPE_RIGHT);
        moveChara.Move(MoveChara.TYPE_RIGHT, 1);
        DrawMap(moveChara, mapData);
    }

    public void RemapButtonAction() {
        printAction("REMAP");
        initialize(null, null);
    }

    public void BombButtonAction(ActionEvent actionEvent) {
        printAction("BOMB");
        if (moveChara.UseItem(MapData.ITEM_TYPE_BOMB)) {
            bombAudioClip.play();
        }
        DrawMap(moveChara, mapData);
    }

    public void HackButtonAction(ActionEvent actionEvent) {
        printAction("HACK");
        if (moveChara.UseItem(MapData.ITEM_TYPE_HACK)) {
            hackAudioClip.play();
        }
        DrawMap(moveChara, mapData);
    }

    public void OverButtonAction() {
        printAction("OVER");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Game Over!\n" + "Score : " + String.valueOf(moveChara.GetScore()));
        alert.showAndWait();
        moveChara.ResetScore();
        mapData.ResetTimeLimit();
        RemapButtonAction();
        mainAudioClip.stop();
        mainAudioClip.play();
    }

    public void func3ButtonAction(ActionEvent event) {
        System.out.println("func3: Nothing to do");
    }

    public void func4ButtonAction(ActionEvent event) {
        System.out.println("func4: Nothing to do");
    }

    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }
}
