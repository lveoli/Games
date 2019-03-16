import TileEngine.TETile;
import TileEngine.Tileset;
import TileEngine.TERenderer;
import java.io.Serializable;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;


public class World implements Serializable {
    private static final long serialVersionUID = 45498234798734234L;
    public static TETile[][] world;
    private  int roomNum = 0;

    //maps rooms to their coordinates
    private  Random RANDOM;
    private  HashMap<Integer, Room> LISTOFROOMS;
    private  Set<Integer> CONNECTEDROOMS;
    public  TERenderer ter;
    private  int playerX;
    private  int playerY;
    public  int exitX;
    public  int exitY;
    public  int numCoins;
    public  TETile T = Tileset.WALL;
    public  Player p;
    public  final int WIDTH = 80;
    public  final int HEIGHT = 40;

    public World(Random r) {
        //ter = new TERenderer();
        //ter.initialize(WIDTH, HEIGHT);
        RANDOM = r;
        CONNECTEDROOMS = new HashSet<>();
        LISTOFROOMS = new HashMap<>();
    }

//    @Override
//    public String toString() {
//        String returnString = "" + playerX +  " "+ playerY +  " "+ exitY+  " " + exitX +  " "+ numCoins;
//        return returnString;
//    }

    //adds N tiles to a row at specified location
    public  void addTiles(int xPos, int yPos, TETile t, int N) {
        for (int x = 0; x < N; x++) {
            if (world[xPos + x][yPos] == Tileset.FLOOR) {
                continue;
            }
            world[xPos + x][yPos] = t;
        }
    }

    public  void addSpaces() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    //checks if a newly created room is going to overlap with any existing room
    public  boolean overlap(Room r) {
        int rX = r.returnCurrentXPos();
        int rY = r.returnCurrentYPos();
        int rW = r.returnCurrentWidth();
        int rL = r.returnCurrentLength();

        for (Integer i : LISTOFROOMS.keySet()) {
            Room exRoom = LISTOFROOMS.get(i);

            int exRoomX = exRoom.returnCurrentXPos();
            int exRoomY = exRoom.returnCurrentYPos();
            int exRoomW = exRoom.returnCurrentWidth();
            int exRoomL = exRoom.returnCurrentLength();

            for (int x = rX; x < rX + rW; x++) {
                for (int y = rY; y < rY + rL; y++) {
                    if (exRoomX < x && x < exRoomX + exRoomW
                            && exRoomY < y && y < exRoomY + exRoomL) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //adds rectangles starting at the bottom left corner starting with position x and y
    public  void drawRectangle(int width, int length,
                                     int xPos, int yPos, TETile t) {
        int charPerRow = width;

        //adds in the tiles row by row starting at the bottom
        for (int row = 0; row < length; row++) {
            addTiles(xPos, yPos, t, charPerRow);
            yPos += 1;
        }
    }

    public  void drawRoom(int width, int length,
                                int xPos, int yPos, TETile t) {

        //if the room is out of bounds just don't draw it
        if (width + xPos >= WIDTH || length + yPos >= HEIGHT) {
            return;
        }

        //draws outline of room
        drawRectangle(width, length, xPos, yPos, t);
        //hollows out room by filling it in with FLOOR
        drawRectangle(width - 2, length - 2, xPos + 1, yPos + 1, Tileset.FLOOR);

        //generates room coordinates
        Room currentRoom = new Room(width, length, xPos, yPos);
        //adds room with its coordinates to list of rooms
        LISTOFROOMS.put(roomNum, currentRoom);

        roomNum += 1;
    }

    //hallways are width 3 rooms with no openings
    public  void drawVerticalHallway(int length,
                                           int xPos, int yPos, TETile t) {

        if (3 + xPos >= WIDTH || length + yPos >= HEIGHT) {
            return;
        }

        //draws the outline of the hallway
        drawRectangle(3, length, xPos, yPos, t);

        //empties out the hallway
        drawRectangle(1, length - 2, xPos + 1, yPos + 1, Tileset.FLOOR);
    }

    //hallways are width 3 rooms with no openings
    public  void drawHorizontalHallway(int length,
                                             int xPos, int yPos, TETile t) {
        if (length + xPos >= WIDTH || 3 + yPos >= HEIGHT) {
            return;
        }

        //draws the outline of the hallway
        drawRectangle(length, 3, xPos, yPos, t);

        //empties out the hallway
        drawRectangle(length - 2, 1, xPos + 1, yPos + 1, Tileset.FLOOR);
    }

    //takes in a room number and connects it to a random room in the list of rooms
    public  void connectRooms(TETile t) {
        int rN = roomNum - 1;
        //if there's only 1 room in existence don't connect anything
        if (CONNECTEDROOMS.size() < 1) {
            CONNECTEDROOMS.add(rN);
            return;
        }

        //get the coordinates of the unconnected room
        Room unconnectedRoom = LISTOFROOMS.get(rN);

        //@source stackoverflow converts set of connected rooms into array list
        List<Integer> connectedRoomsKey = new ArrayList<Integer>(CONNECTEDROOMS);
        //randomly gets one of the connected room #
        int randomRoomNum = getRandomNumber() % connectedRoomsKey.size();
        //get the coordinates of the connected room
        Room baseRoom = LISTOFROOMS.get(connectedRoomsKey.get(randomRoomNum));

        int x1 = unconnectedRoom.returnCurrentXPos() + unconnectedRoom.returnCurrentWidth() / 2;
        int y1 = unconnectedRoom.returnCurrentYPos() + unconnectedRoom.returnCurrentLength() / 2;

        int x2 = baseRoom.returnCurrentXPos() + baseRoom.returnCurrentWidth() / 2;
        int y2 = baseRoom.returnCurrentYPos() + baseRoom.returnCurrentLength() / 2;

        int minXPos = Math.min(x1, x2);
        int maxXPos = Math.max(x1, x2);
        int minYPos = Math.min(y1, y2);
        int maxYPos = Math.max(y1, y2);
        int horiLen = maxXPos - minXPos + 3;
        int verLen = maxYPos - minYPos + 3;

        /* some crazy ass stuff
           I made two ways to connect any two rooms,
           (first way is to draw hallways down-right or right-up
            second way is to draw hallways right-down and up-right)
           the getRandomNumber() % 2 acts as a random choice
           for either one of the two methods to give variety
        */
        if (getRandomNumber() % 2 == 1) {
            drawHorizontalHallway(horiLen, minXPos, minYPos - 1, t);
            //if one of the rooms has bigger x and y
            if ((x1 > x2 && y1 > y2) || (x2 > x1 && y2 > y1)) {
                drawVerticalHallway(verLen, maxXPos, minYPos - 1, t);
            } else {
                drawVerticalHallway(verLen, minXPos, minYPos - 1, t);
            }
        } else {
            drawHorizontalHallway(horiLen, minXPos, maxYPos - 1, t);
            //if one of the rooms has bigger x and y
            if ((x1 < x2 && y1 > y2) || (x2 < x1 && y2 > y1)) {
                drawVerticalHallway(verLen, maxXPos, minYPos - 1, t);
            } else {
                drawVerticalHallway(verLen, minXPos, minYPos - 1, t);
            }
        }

        //adds room to connected rooms
        CONNECTEDROOMS.add(rN);
    }

    //generates random positive number
    private  int getRandomNumber() {
        return Math.abs(RANDOM.nextInt());
    }

    //where all the drawing happens
    public void generateWorld() {


        world = new TETile[WIDTH][HEIGHT];
        addSpaces();

        int randomxPos = getRandomNumber() % 70;
        int randomyPos = getRandomNumber() % 30;
        int randomWidth = getRandomNumber() % 6 + 4;
        int randomLength = getRandomNumber() % 6 + 4;

        int randomN = getRandomNumber() % 4;

        if (randomN == 0) {
            T = Tileset.SAND;
        } else if (randomN == 1) {
            T = Tileset.MOUNTAIN;
        } else if (randomN == 2) {
            T = Tileset.TREE;
        }

        for (int i = 0; i < 20; i += 1) {
            //generate random coordinates
            randomxPos = getRandomNumber() % 66 + 10;
            randomyPos = getRandomNumber() % 26 + 10;
            //generate random width that is at most 9
            randomWidth = getRandomNumber() % 6 + 4;
            //generate random length that is at most 9
            randomLength = getRandomNumber() % 6 + 4;

            //save the coordinates of the first room to later put the player into
            if (roomNum == 0) {
                playerX = randomxPos + 1 + (getRandomNumber() % (randomWidth - 2));
                playerY = randomyPos + 1 + (getRandomNumber() % (randomLength - 2));
            }

            //store the current coordinates
            Room roomCoordinates = new Room(randomWidth, randomLength, randomxPos, randomyPos);

            //if the room overlaps with an existing room, don't draw it
            if (overlap(roomCoordinates)) {
                continue;
            }

            drawRoom(randomWidth, randomLength, randomxPos, randomyPos, T);

            connectRooms(T);

        }

        //@source stackoverflow converts set of connected rooms into array list
        List<Integer> connectedRoomsKey = new ArrayList<Integer>(CONNECTEDROOMS);
        //randomly gets one of the connected room #
        int randomRoomNum = getRandomNumber() % connectedRoomsKey.size();
        Room roomWithExit = LISTOFROOMS.get(connectedRoomsKey.get(randomRoomNum));

        drawExit(roomWithExit);

        //draws in all the traps
        Room roomWithTrap;
        int numTraps = getRandomNumber() % 20 + 20;
        for (int i = 0; i < numTraps; i++) {
            roomWithTrap = LISTOFROOMS.get(connectedRoomsKey.get(randomRoomNum));
            randomRoomNum = getRandomNumber() % connectedRoomsKey.size();
            drawTraps(roomWithTrap);
        }

        //draws in all the coins
        Room roomWithCoin;
        numCoins = getRandomNumber() % 3 + 3;
        for (int i = 0; i < numCoins; i++) {
            roomWithCoin = LISTOFROOMS.get(connectedRoomsKey.get(randomRoomNum));
            randomRoomNum = getRandomNumber() % connectedRoomsKey.size();
            drawCoins(roomWithCoin);
        }

        //draws in all the hearts
        Room roomWithHeart;
        int numHearts = getRandomNumber() % 2 + 1;
        for (int i = 0; i < numHearts; i++) {
            roomWithHeart = LISTOFROOMS.get(connectedRoomsKey.get(randomRoomNum));
            randomRoomNum = getRandomNumber() % connectedRoomsKey.size();
            drawHearts(roomWithHeart);
        }

        roomNum = 0;
    }

    public  void drawExit(Room r) {
        int i = 1;
        int x;
        int y;
        int roomW = r.returnCurrentWidth();
        int roomL = r.returnCurrentLength();
        int roomX = r.returnCurrentXPos();
        int roomY = r.returnCurrentYPos();
        int randomN = getRandomNumber();

        x = randomN % (roomW - 2) + 1;

        if (randomN % 2 == 0) {
            y = 0;
        } else {
            y = roomL - 1;
        }

        exitX = roomX + x;
        exitY = roomY + y;
        world[roomX + x][roomY + y] = Tileset.LOCKED_PORTAL;


        //iterates through the entire room space
//        while (randomN % (roomW * roomL) > i) {
//            //checks if we've traversed a whole row
//            if (i % (roomW) == 0) {
//                //if so reset the x and increment the y
//                x = 0;
//                y += 1;
//            }
//            x += 1;
//            i += 1;
//        }

        //if we happen to land on a stupid corner piece,
        //choose the same position to put the exit because too many cases
//        if ((x == 0 && y == 0)
//                || (x == roomW - 1 && y == 0)
//                || (x == 0 && y == roomL - 1)
//                || (x == roomW - 1 && y == roomL - 1)) {
//            world[roomX + 1][roomY] = Tileset.LOCKED_DOOR;
//            exitX = roomX + 1;
//            exitY = roomY;
//            return;
//        }
//
//        //if we happen to land on a floor tile,
//        // just move to the right and call it a day
//        if (world[roomX + x][roomY + y] == Tileset.FLOOR) {
//            x = roomW - 1;
//        }
//        exitX = roomX + x;
//        exitY = roomY + y;
//        world[roomX + x][roomY + y] = Tileset.LOCKED_DOOR;

//        System.out.println("bounds: " + randomN % (roomW * roomL));
//        System.out.println("i: " + i);
//        System.out.println("roomW: " + roomW);
//        System.out.println("roomL: " + roomL);
//        System.out.println("X: " + x);
//        System.out.println("Y: " + y);
//        System.out.println("roomX: "+ roomX);
//        System.out.println("ROOMy: " + roomY);
//        System.out.println("\n");

        //only draw the tile if we are next to a accessible floor tile
//        if (world[roomX + x + 1][roomY + y] == Tileset.FLOOR
//            || world[roomX + x - 1][roomY + y] == Tileset.FLOOR
//            || world[roomX + x][roomY + y + 1] == Tileset.FLOOR
//            || world[roomX + x][roomY + y - 1] == Tileset.FLOOR) {
//            world[roomX + x][roomY + y] = Tileset.LOCKED_DOOR;
//        } else {
    }

    public  void drawCoins(Room r) {
        int roomW = r.returnCurrentWidth();
        int roomL = r.returnCurrentLength();
        int roomX = r.returnCurrentXPos();
        int roomY = r.returnCurrentYPos();
        int randomN = getRandomNumber();

        int x = randomN % (roomW - 2);
        int y = randomN % (roomL - 2);

        world[roomX + 1 + x][roomY + 1 + y] = Tileset.COIN;
    }

    public  void drawHearts(Room r) {
        int roomW = r.returnCurrentWidth();
        int roomL = r.returnCurrentLength();
        int roomX = r.returnCurrentXPos();
        int roomY = r.returnCurrentYPos();
        int randomN = getRandomNumber();

        int x = randomN % (roomW - 2);
        int y = randomN % (roomL - 2);

        world[roomX + 1 + x][roomY + 1 + y] = Tileset.HEART;
    }

    public  void drawTraps(Room r) {
        int roomW = r.returnCurrentWidth();
        int roomL = r.returnCurrentLength();
        int roomX = r.returnCurrentXPos();
        int roomY = r.returnCurrentYPos();
        int randomN = getRandomNumber();

        int x = randomN % (roomW - 2);
        int y = randomN % (roomL - 2);

        world[roomX + 1 + x][roomY + 1 + y] = Tileset.TRAP;
    }

    public static TETile[][] getWorld() {
        return world;
    }

    public int returnPlayerX() {
        return playerX;
    }

    public int returnPlayerY() {
        return playerY;
    }

    public static TETile[][] returnTETile() {
        return world;
    }

}
