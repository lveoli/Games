package logic;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;

import java.awt.*;
import java.util.Random;

public class GUI implements Serializable {
    private static final long serialVersionUID = 1231231223123L;
    private static boolean LOADGAME = false;
    private TERenderer ter;

    public GUI() {
    }

    public static void drawFrame(int xPos, int yPos, String s) {
        StdDraw.clear();

        StdDraw.text(xPos, yPos, s);

        StdDraw.show();

    }


    public void startScreen() {
         //will wait until either "n", "l", "s", or "q" is entered
         while (true) {
             ter = new TERenderer();
             StdDraw.setCanvasSize(80 * 16, 40 * 16);
             Font font = new Font("Monaco", Font.BOLD, 30);
             StdDraw.setFont(font);
             StdDraw.setXscale(0, 80);
             StdDraw.setYscale(0, 40);
             StdDraw.clear(Color.BLACK);
             StdDraw.enableDoubleBuffering();
             drawFrame(40, 25, "CS61B - A poorly made gAmE");
             StdDraw.text(40, 23, "(N) New Game");
             StdDraw.text(40, 21, "(L) Load Game");
             StdDraw.text(40, 19, "(S) Story");
             StdDraw.text(40, 17, "(Q) Quit");
             StdDraw.show();
             char firstInput = waitForInput();
             Long seed = null;
             GUI gui = new GUI();
             if (firstInput == 'n' || firstInput == 'N') {
                 //stores the keyboard input
                 String input = null;
                 while (input == null) {
                     input = solicitSeed("please input a seed:");
                     //gets the seed after parsing
                     seed = Long.parseLong(input);
                 }
                 Random random = new Random(seed);
                 World world = new World(random);
                 world.generateWorld();
                 Player p = new Player(world, world.returnPlayerX(),
                         world.returnPlayerY(), "", gui);

             } else if (firstInput == 'l' || firstInput == 'L') {
                 Player p = loadGame();
                 p.InitTer();
                 p.showWorld();
                 p.Move();
             } else if (firstInput == 's' || firstInput == 'S') {
                 showLore();
                 continue;
             } else if (firstInput == 'q'|| firstInput == 'Q') {
                 System.exit(0);
             } else {
                 startScreen();
             }
         }
    }


    // Read n letters of player input
    public String solicitSeed(String message) {
        drawFrame(40, 25, message);
        String input = "";
        //ensures the first thing typed is n
//        while (true) {
//            if(waitForInput() == 'n') {
//                break;
//            }
//        }
        //shows input
//        input += 'n';
        StdDraw.text(40, 20, input);
        StdDraw.show();
        //gets rest of inputs up to s
        while (true) {
            //stores keyboard input
            char c = Character.toLowerCase(waitForInput());
            //if user inputs s, return the actual seed
            if (c == 's') {
                return input;
            }
            //only store the numbers after n
            if (Character.isDigit(c)) {
                input += c;
            }
            //prints out the input
            StdDraw.clear();
            StdDraw.text(40, 25, message);
            StdDraw.text(40, 20, input);
            StdDraw.show();
        }
    }


    //waits for input
    public static char waitForInput() {
        while (!StdDraw.hasNextKeyTyped()) {
        }
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

    //returns string[] where [0] is the numeric seed
    //and [1] is the additional player movements
    public static String[] parseInput(String input) {

        int i = 1;
        String seedString = "";
        String movement = "";

        //parses the first part of the function
        if (input.charAt(0) == 'n') {
            char c;
            while (i < input.length()) {
                c = input.charAt(i);
                //break the loop once we see s
                if (c == 's') {
                    i += 1;
                    break;
                }
                //only store the numbers after n
                if (Character.isDigit(c)) {
                    seedString += c;
                }
                i += 1;
            }
        }

        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isAlphabetic(c) && c != 'q') {
                movement += c;
            }
            i += 1;
        }

        String[] seedPlusMovement = new String[2];
        seedPlusMovement[0] = seedString;
        seedPlusMovement[1] = movement;

        return seedPlusMovement;
    }


    public void saveGame(Player p) {
        File f = new File("./save.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(p);
            os.flush();
            os.close();
            fs.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public Player loadGame() {
        File f = new File("./save.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Player loadWorld = (Player) os.readObject();
                os.close();
                fs.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }

        }
        /* In the case no World has been saved yet, we return a new one. */
        return null;
    }
//
//    public void HUD() {
//        //StdDraw.show();
//        ter.initialize(4, 4);
//
//        while (true) {
//            int mouseX = (int) StdDraw.mouseX();
//            int mouseY = (int) StdDraw.mouseY();
//            String tileDescription = World.getWorld()[mouseX][mouseY].description();
//            StdDraw.setPenColor(StdDraw.WHITE);
//            StdDraw.text(2, 2, tileDescription);
//            StdDraw.show();
//        }
//    }

    public void showLore() {
        File loreFile = new File("./byog/Core/lore.txt");
        FileReader loreReader = null;
        //attempt to read the lore file
        try {
            loreReader = new FileReader(loreFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader xPosBufferedReader = new BufferedReader(loreReader);
        String lore = null;
        //read and print out the lore
        try {
            Font font = new Font("Monaco", Font.BOLD, 25);
            StdDraw.setFont(font);
            int y = 38;
            drawFrame(40, y, "");
            while (true) {
                lore = xPosBufferedReader.readLine();
                if (lore == null) {
                    break;
                }

                y -= 3;
                StdDraw.text(40, y, lore);
                StdDraw.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //waits until input
        while (!StdDraw.hasNextKeyTyped()) {
        }
        StdDraw.nextKeyTyped();
    }

    public boolean returnLOADGAME() {
        return LOADGAME;
    }

}
