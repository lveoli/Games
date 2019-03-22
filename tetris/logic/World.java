package logic;
import TileEngine.TETile;
import TileEngine.Tileset;
import TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.*;


public class World implements Serializable {
    public static TETile[][] world;

    //maps rooms to their coordinates
    public TileEngine.TERenderer ter;
    public TETile T = Tileset.WALL;
    public final int WIDTH = 12;
    public final int HEIGHT = 22;

    public World() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        world = new TETile[WIDTH][HEIGHT];
    }

    //adds N tiles to a row at specified location
    public void addTiles(int xPos, int yPos, TETile t, int N) {
        for (int x = 0; x < N; x++) {
            world[xPos + x][yPos] = t;
        }
    }

    public void drawShape() {
        Shape newShape = Shape.generateShape();
        System.out.println(newShape.coordinates);
        System.out.println(Arrays.toString(newShape.coordinates[0]));
        System.out.println(newShape.coordinates[1]);
        System.out.println(newShape.coordinates[2]);
        System.out.println(newShape.coordinates[3]);

//        for (int[] coords : newShape.coordinates[newShape.rotation]) {

//        }
    }

    public void addSpaces() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    //adds rectangles starting at the bottom left corner starting with position x and y
    public void drawRectangle(int width, int length,
                              int xPos, int yPos, TETile t) {
        int charPerRow = width;
        //adds in the tiles row by row starting at the bottom
        for (int row = 0; row < length; row++) {
            addTiles(xPos, yPos, t, charPerRow);
            yPos += 1;
        }
    }

    public  void drawRoom(int width, int height,
                          int xPos, int yPos, TETile t) {
        //if the room is out of bounds just don't draw it
        if (width + xPos > WIDTH || height + yPos > HEIGHT) {
            System.out.println(width);
            System.out.println(xPos);
            System.out.println(WIDTH);
            System.out.println(height);
            System.out.println(yPos);
            System.out.println(HEIGHT);
            System.out.println("the hi");
            return;
        }
        //draws outline of room
        drawRectangle(width, height, xPos, yPos, t);
        //hollows out room by filling it in with FLOOR
        drawRectangle(width - 2, height - 2, xPos + 1, yPos + 1, Tileset.FLOOR);
        //generates room coordinates
    }


    //where all the drawing happens
    public void generateWorld() {
        addSpaces();
        drawRoom(WIDTH, HEIGHT, 0, 0, Tileset.FLOWER);
        ter.renderFrame(world);
        System.out.println(WIDTH);
        System.out.println(HEIGHT);
        addTiles(20, 20, Tileset.WALL, 5);
//        drawRectangle(20, 20, 20, 20, Tileset.WALL);
//        ter.renderFrame(world);
    }

    public static TETile[][] getWorld() {
        return world;
    }
}
