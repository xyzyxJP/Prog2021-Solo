import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int MAP_TYPE_SPACE = 0;
    public static final int MAP_TYPE_WALL = 1;
    public static final int MAP_TYPE_OTHERS = 2;
    private static final String mapImagePaths[] = {
            "png/SPACE.png",
            "png/WALL.png"
    };

    public static final int ITEM_TYPE_NULL = -1;
    public static final int ITEM_TYPE_GOAL = 0;
    public static final int ITEM_TYPE_BOMB = 1;
    public static final int ITEM_TYPE_KEY = 2;
    private static final String itemImagePaths[] = {
            "png/GOAL.png",
            "png/BOMB.png",
            "png/KEY.png"
    };

    private Image[] mapImages;
    private Image[] itemImages;
    private int[][] mapTypes;
    private int[][] itemTypes;
    private int width;
    private int height;

    MapData(int x, int y) {
        width = x;
        height = y;

        mapImages = new Image[mapImagePaths.length];
        for (int i = 0; i < mapImagePaths.length; i++) {
            mapImages[i] = new Image(mapImagePaths[i]);
        }
        mapTypes = new int[y][x];

        itemImages = new Image[itemImagePaths.length];
        for (int i = 0; i < itemImagePaths.length; i++) {
            itemImages[i] = new Image(itemImagePaths[i]);
        }
        itemTypes = new int[y][x];

        fillMapType(MAP_TYPE_WALL);
        digMap(1, 3);

        fillItemType(ITEM_TYPE_NULL);
        setItemType(x - 2, y - 2, ITEM_TYPE_GOAL);
        SetItemTypeRandom(3, ITEM_TYPE_BOMB);
        SetItemTypeRandom(1, ITEM_TYPE_KEY);
    }

    private void SetItemTypeRandom(int itemCount, int itemType) {
        for (int i = 0; i < itemCount; i++) {
            int tempX = (int) (Math.random() * width);
            int tempY = (int) (Math.random() * height);
            if (getMapType(tempX, tempY) == MAP_TYPE_SPACE && getItemType(tempX, tempY) == ITEM_TYPE_NULL
                    && !(tempX == 1 && tempY == 1)) {
                setItemType(tempX, tempY, itemType);
            } else {
                i--;
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMapType(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return mapTypes[y][x];
    }

    public ImageView getMapItemImageView(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return null;
        }
        if (itemTypes[y][x] == MapData.ITEM_TYPE_NULL) {
            return new ImageView(mapImages[mapTypes[y][x]]);
        } else {
            return new ImageView(itemImages[itemTypes[y][x]]);
        }
    }

    public void setMapType(int x, int y, int mapType) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        mapTypes[y][x] = mapType;
    }

    public int getItemType(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return itemTypes[y][x];
    }

    public void setItemType(int x, int y, int itemType) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        itemTypes[y][x] = itemType;
    }

    public ImageView getItemImageView(int itemType) {
        return new ImageView(itemImages[itemType]);
    }

    public void fillMapType(int mapType) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapTypes[y][x] = mapType;
            }
        }
    }

    public void fillItemType(int itemType) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                itemTypes[y][x] = itemType;
            }
        }
    }

    public void digMap(int x, int y) {
        setMapType(x, y, MAP_TYPE_SPACE);
        int[] temp;
        int[][] tempVectors = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int i = 0; i < tempVectors.length; i++) {
            int j = (int) (Math.random() * tempVectors.length);
            temp = tempVectors[i];
            tempVectors[i] = tempVectors[j];
            tempVectors[j] = temp;
        }

        for (int i = 0; i < tempVectors.length; i++) {
            int dx = tempVectors[i][0];
            int dy = tempVectors[i][1];
            if (getMapType(x + dx * 2, y + dy * 2) == MAP_TYPE_WALL) {
                setMapType(x + dx, y + dy, MAP_TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);
            }
        }
    }
}
