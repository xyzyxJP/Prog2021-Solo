import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara moveChara;
    public GridPane mapGridPane;
    public GridPane itemGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapData = new MapData(21, 15);
        moveChara = new MoveChara(1, 1, mapData);
        drawMap(moveChara, mapData);
    }

    public void drawMap(MoveChara moveChara, MapData mapData) {
        int moveCharaPositionX = moveChara.getPositionX();
        int moveCharaPositionY = moveChara.getPositionY();
        int itemType = mapData.getItemType(moveCharaPositionX, moveCharaPositionY);
        switch (itemType) {
            case MapData.ITEM_TYPE_GOAL:
                printAction("CLEAR");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Clear!");
                alert.showAndWait();
                remapButtonAction();
                break;
            case default:
                if (itemType != MapData.ITEM_TYPE_NULL) {
                    printAction("GET");
                    moveChara.addItem(itemType);
                    mapData.setItemType(moveCharaPositionX, moveCharaPositionY, MapData.ITEM_TYPE_NULL);
                }
                break;
        }
        mapData.setMapImageViews();
        mapGridPane.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (x == moveCharaPositionX && y == moveCharaPositionY) {
                    mapGridPane.add(moveChara.getCharaImageView(), x, y);
                } else {
                    mapGridPane.add(mapData.getMapImageView(x, y), x, y);
                }
            }
        }
        itemGridPane.getChildren().clear();
        ArrayList<Integer> itemInventory = moveChara.getItemInventory();
        for (int i = 0; i < itemInventory.size(); i++) {
            itemGridPane.add(mapData.getItemImageView(itemInventory.get(i)), i, 0);
        }
    }

    public void keyAction(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        System.out.println("keycode:" + keyCode);
        switch (keyCode) {
            case H:
            case A:
                leftButtonAction();
                break;
            case J:
            case S:
                downButtonAction();
                break;
            case K:
            case W:
                upButtonAction();
                break;
            case L:
            case D:
                rightButtonAction();
                break;
            case DELETE:
            case BACK_SPACE:
                remapButtonAction();
                break;
            default:
                break;
        }
    }

    public void upButtonAction() {
        printAction("UP");
        moveChara.setCharaDirection(MoveChara.TYPE_UP);
        moveChara.move(0, -1);
        drawMap(moveChara, mapData);
    }

    public void downButtonAction() {
        printAction("DOWN");
        moveChara.setCharaDirection(MoveChara.TYPE_DOWN);
        moveChara.move(0, 1);
        drawMap(moveChara, mapData);
    }

    public void leftButtonAction() {
        printAction("LEFT");
        moveChara.setCharaDirection(MoveChara.TYPE_LEFT);
        moveChara.move(-1, 0);
        drawMap(moveChara, mapData);
    }

    public void rightButtonAction() {
        printAction("RIGHT");
        moveChara.setCharaDirection(MoveChara.TYPE_RIGHT);
        moveChara.move(1, 0);
        drawMap(moveChara, mapData);
    }

    public void remapButtonAction() {
        printAction("REMAP");
        initialize(null, null);
    }

    public void func1ButtonAction(ActionEvent event) {
        System.out.println("func1: Nothing to do");
    }

    public void func2ButtonAction(ActionEvent event) {
        System.out.println("func2: Nothing to do");
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
