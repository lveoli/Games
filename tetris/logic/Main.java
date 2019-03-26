import TileEngine.TERenderer;
import TileEngine.TETile;
import TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static boolean GAMEWON = false;
    private static boolean SHOULDSAVE = false;
    public static int[] currentCoord;
    private static TERenderer ter;

    public static void drawFrame(int xPos, int yPos, String s) {
        StdDraw.clear();
        StdDraw.text(xPos, yPos, s);
        StdDraw.show();
    }

    public static void startScreen() {
        ter = new TERenderer();
        StdDraw.setCanvasSize(World.WIDTH * TERenderer.TILE_SIZE, World.HEIGHT * TERenderer.TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 40);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        drawFrame(40, 25, "CS61B - A poorly made gAmE");
        StdDraw.text(40, 20, "(N) New Game");
//        StdDraw.text(40, 21, "(L) Load Game");
//        StdDraw.text(40, 19, "(S) Story");
        StdDraw.text(40, 18, "(Q) Quit");
        StdDraw.show();
        char keyPress = waitForInput();
        if (keyPress == 'n') {
            World world = new World();
            world.generateWorld();
        } else if (keyPress == 'q') {
            StdDraw.clear();
            StdDraw.text(40, 19, "See you next time!");
            StdDraw.show();
        }
    }

    //waits for input
    public static char waitForInput() {
        while (!StdDraw.hasNextKeyTyped()) {
        }
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

    public static void main(String[] args) {
        startScreen();
//        World hello = new World();
//        hello.drawShape();
    }
}
