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
    public static boolean GAMELOST = false;
    private static boolean SHOULDSAVE = false;
    public static int[] currentCoord;
    public static World WORLD;
    private static TERenderer ter;

    public static void drawFrame(int xPos, int yPos, String s) {
        WORLD = new World();
        currentCoord = new int[]{5, 15};
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
        while (true) {
            if (startScreenInput() == 'w' || startScreenInput() == 'q') {
                break;
            }
        }
    }

    public static char startScreenInput(){
        char keyPress = waitForInput();
        if (keyPress == 'w') {
            WORLD.generateWorld();
            return 'w';
        } else if (keyPress == 'q') {
            StdDraw.clear();
            StdDraw.text(40, 19, "See you next time!");
            StdDraw.show();
            return 'q';
        } else {
            return 's';
        }
    }

    //waits for input
    public static char waitForInput() {
        while (!StdDraw.hasNextKeyTyped()) {
        }
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

    public static void interpretKey(char key) {
        switch (key) {
            case 'a':
                if (currentCoord[0] <= 0) {
                    return;
                }
                currentCoord[0] = currentCoord[0] - 1;
                break;
            case 'd':
                if (currentCoord[0] >= 7) {
                    return;
                }
                currentCoord[0] += 1;
                break;
        }
    }


    public static void main(String[] args) throws InterruptedException {
//        if (startScreen() == 'w') {
        startScreen();
            while(!GAMELOST) {
                while (currentCoord[1] != 0) {
                    for (int i = 1; i < 10; i++) {
                        Thread.sleep(20);
                        if (StdDraw.hasNextKeyTyped()) {
                            interpretKey(StdDraw.nextKeyTyped());
                        }
                        ter.renderFrame(World.getWorld());
                    }
                    Shape currentShape = Shape.generateShape();
                    WORLD.drawShape(currentShape);
                    Thread.sleep(300);
                    currentCoord[1] -= 1;
                    System.out.println("current x coord: " + currentCoord[0]);
                    WORLD.generateWorld();
                }
            }
//        } else {
//            System.out.println("u quit af");
//        }
    }
}
