package logic;

import byog.TileEngine.TETile;
import java.io.File;


import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable {
    /* Feel free to change the width and height. */
    private static final long serialVersionUID = 423123123123123L;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static boolean GAMEWON = false;
    private static boolean SHOULDSAVE = false;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */


    public void playWithKeyboard() {
        /**
         * Method used for autograding and testing the game code. The input string will be a series
         * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
         * behave exactly as if the user typed these characters into the game after playing
         * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
         * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
         * world. However, the behavior is slightly different.
         * After playing with "n123sss:q", the game
         * should save, and thus if we then called
         * playWithInputString with the string "l", we'd expect
         * to get the exact same world back again, since this corresponds to loading the saved game.
         * @param input the input string to feed to your program
         * @return the 2D TETile[][] representing the state of the world
         */

        TETile[][] finalWorldFrame = null;

        GUI gui = new GUI();
        //asks for user input and parses the input into a seed

        gui.startScreen();

    }

    public static boolean returnSHOULDSAVE() {
        return SHOULDSAVE;
    }
}
