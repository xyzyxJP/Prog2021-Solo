import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;

public class MoveChara {
    public static final int[][] VECTORS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    public static final int TYPE_UP = 0;
    public static final int TYPE_DOWN = 1;
    public static final int TYPE_LEFT = 2;
    public static final int TYPE_RIGHT = 3;

    private final String[] directions = { "Up", "Down", "Left", "Right" };
    private final String[] animationNumbers = { "1", "2", "3" };
    private final String imagePathCat = "png/cat";
    private final String imagePathExt = ".png";

    private int positionX;
    private int positionY;

    private ArrayList<Integer> itemInventory = new ArrayList<Integer>();

    private MapData mapData;

    private Image[][] charaImages;
    private ImageView[] charaImageViews;
    private ImageAnimation[] charaImageAnimations;

    private int charaDirection;

    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i = 0; i < 4; i++) {
            charaImages[i] = new Image[3];
            for (int j = 0; j < 3; j++) {
                charaImages[i][j] = new Image(imagePathCat + directions[i] + animationNumbers[j] + imagePathExt);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation(charaImageViews[i], charaImages[i]);
        }

        positionX = startX;
        positionY = startY;

        setCharaDirection(TYPE_RIGHT);
    }

    public void setCharaDirection(int charaDirection) {
        this.charaDirection = charaDirection;
        for (int i = 0; i < 4; i++) {
            if (i == charaDirection) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    public boolean canMove(int charaDirection) {
        if (mapData.getMapType(positionX + VECTORS[charaDirection][1],
                positionY + VECTORS[charaDirection][0]) == MapData.MAP_TYPE_SPACE) {
            return true;
        }
        return false;
    }

    public boolean move(int charaDirection) {
        if (canMove(charaDirection)) {
            positionX += VECTORS[charaDirection][1];
            positionY += VECTORS[charaDirection][0];
            return true;
        } else {
            return false;
        }
    }

    public ImageView getCharaImageView() {
        return charaImageViews[charaDirection];
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void addItem(int itemType) {
        itemInventory.add(itemType);
    }

    public ArrayList<Integer> getItemInventory() {
        return itemInventory;
    }

    public boolean useItem(int itemType) {
        if (!itemInventory.contains(itemType)) {
            return false;
        }
        switch (itemType) {
            case MapData.ITEM_TYPE_BOMB:
                if (!canMove(charaDirection)) {
                    itemInventory.remove(itemInventory.indexOf(itemType));
                    mapData.setMapType(positionX + VECTORS[charaDirection][1],
                            positionY + VECTORS[charaDirection][0], MapData.MAP_TYPE_SPACE);
                    return true;
                }
        }
        return false;
    }

    private class ImageAnimation extends AnimationTimer {
        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;

        private long duration = 500 * 1000000L;
        private long startTime = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation(ImageView charaView, Image[] images) {
            this.charaView = charaView;
            this.charaImages = images;
            this.index = 0;
        }

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }

            preCount = count;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus;
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
