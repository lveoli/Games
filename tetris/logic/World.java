import TileEngine.TETile;
import TileEngine.Tileset;
import TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.*;


public class World {
    public static TETile[][] world;

    //maps rooms to their coordinates
    public TileEngine.TERenderer ter;
    public TETile T = Tileset.WALL;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 160;

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
        System.out.println(Arrays.toString(newShape.coordinates[newShape.rotation]));
        for (int coords : newShape.coordinates[newShape.rotation]) {
            System.out.println(coords);
            System.out.println(Arrays.toString(Shape.convertCoord(coords)));
            int[] x_y = Shape.convertCoord(coords);
            addTiles(x_y[0], x_y[1], Tileset.GRASS, 1);
        }
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
        drawShape();





        ter.renderFrame(world);
    }

    public static TETile[][] getWorld() {
        return world;
    }
}
