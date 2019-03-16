import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
//import java.time;

public class Player implements Serializable{
    public int xPos;
    public int yPos;
    public World world;
    public TETile[][] twoD;
    public TETile PLAYER = Tileset.PLAYER;
    public boolean GAMEWON = false;
    public boolean GAMEOVER = false;
    public boolean UNLOCKDOOR = false;
    public int COINS;
    public int HEARTS;
    public GUI gui;
    public Long baseTime;
    public TERenderer ter;
    public int mouseX;
    public int mouseY;

    public Player(World w, int xPos, int yPos, String extraMovements, GUI g) {
        ter = new TERenderer();
        ter.initialize(80, 40);
        COINS = w.numCoins;
        HEARTS = 0;
        baseTime = System.currentTimeMillis();
        gui = g;
        world = w;
        twoD = world.getWorld();
        this.xPos = xPos;
        this.yPos = yPos;
        drawPlayer();
        parseMovement(extraMovements);
        Move();
        //lets the player restart if wanted
    }


    public void drawPlayer() {
        twoD[xPos][yPos] = PLAYER;
        showWorld();
    }


    public void Move() {
        while (true) {
            //shows congratulatory message and ends game
            if (GAMEOVER) {
                StdDraw.setPenColor(StdDraw.BLACK);
                Font font = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(font);
                gui.drawFrame(40, 20,"You died.");
                StdDraw.text(40, 15, "(Press any key to return to the main screen)");
                StdDraw.show();
                gui.waitForInput();
                return;
            }

            if (GAMEWON) {
                StdDraw.setPenColor(StdDraw.BLACK);
                Font font = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(font);
                gui.drawFrame(40, 20,"Congratulations you win all of the stuffs!");
                StdDraw.text(40, 10, "(Press any key to return to the main screen)");
                time();
                gui.waitForInput();
                return;
            }

            if (COINS <= 0) {
                UNLOCKDOOR = true;
            }

            if (UNLOCKDOOR) {
                twoD[world.exitX][world.exitY] = Tileset.UNLOCKED_PORTAL;
            }


            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (c) {
                    case 'w':
                        moveUp();
                        break;
                    case 'a':
                        moveLeft();
                        break;
                    case 's':
                        moveDown();
                        break;
                    case 'd':
                        moveRight();
                        break;
                    case 'i':
                        clearTrap();
                    case ':':
                        char q = GUI.waitForInput();
                        if (q == 'q') {
                            gui.saveGame(this);
                            return;
                        } else {
                            break;
                        }
                    default:
                }
            }

            mouseX = (int) StdDraw.mouseX();
            mouseY = (int) StdDraw.mouseY();
            if (mouseX >= 80) {
                mouseX = 79;
            }
            if (mouseY >= 40) {
                mouseY = 39;
            }
            String tileDescription = twoD[mouseX][mouseY].description();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(4, 2, 3.7, 3);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(4, 3, tileDescription);
            StdDraw.text(4, 2, "Heart:" + HEARTS);
            StdDraw.text(4, 1, "Coin Left:" + COINS);
            StdDraw.show();

        }
    }


    public  void moveUp() {
        if (twoD[xPos][yPos + 1].equals(Tileset.TRAP) && (HEARTS == 0)) {
            GAMEOVER = true;
        }

        if (twoD[xPos][yPos + 1].equals(Tileset.TRAP) && (HEARTS != 0)) {
            HEARTS -= 1;
        }

        if (twoD[xPos][yPos + 1].equals(Tileset.HEART)) {
            HEARTS += 1;
        }

        if (twoD[xPos][yPos + 1].equals(Tileset.WALL)
                || twoD[xPos][yPos + 1].equals(Tileset.TREE)
                || twoD[xPos][yPos + 1].equals(Tileset.MOUNTAIN)
                || twoD[xPos][yPos + 1].equals(Tileset.SAND)
                || twoD[xPos][yPos + 1].equals(Tileset.LOCKED_PORTAL)) {
            return;
        }

        if (twoD[xPos][yPos + 1].equals(Tileset.COIN)) {
            COINS -= 1;
        }

        if (twoD[xPos][yPos + 1].equals(Tileset.UNLOCKED_PORTAL)) {
            GAMEWON = true;
        }

        //deletes previous player icon
        twoD[xPos][yPos] = Tileset.FLOOR;

        yPos += 1;
        //draws new player icon above current position
        twoD[xPos][yPos] = PLAYER;

        showWorld();
    }

    public  void moveDown() {
        if (twoD[xPos][yPos - 1].equals(Tileset.TRAP) && (HEARTS == 0)) {
            GAMEOVER = true;
        }

        if (twoD[xPos][yPos - 1].equals(Tileset.TRAP) && (HEARTS != 0)) {
            HEARTS -= 1;
        }

        if (twoD[xPos][yPos - 1].equals(Tileset.HEART)) {
            HEARTS += 1;
        }

        if (twoD[xPos][yPos - 1].equals(Tileset.WALL)
                || twoD[xPos][yPos - 1].equals(Tileset.TREE)
                || twoD[xPos][yPos - 1].equals(Tileset.MOUNTAIN)
                || twoD[xPos][yPos - 1].equals(Tileset.SAND)
                || twoD[xPos][yPos - 1].equals(Tileset.LOCKED_PORTAL)) {
            return;
        }

        if (twoD[xPos][yPos - 1].equals(Tileset.COIN)) {
            COINS -= 1;
        }

        if (twoD[xPos][yPos - 1].equals(Tileset.UNLOCKED_PORTAL)) {
            GAMEWON = true;
        }

        //deletes previous player icon
        twoD[xPos][yPos] = Tileset.FLOOR;

        yPos -= 1;
        //draws new player icon below the current position
        twoD[xPos][yPos] = PLAYER;

        showWorld();
    }

    public  void moveRight() {
        if (twoD[xPos + 1][yPos].equals( Tileset.TRAP) && (HEARTS == 0)) {
            GAMEOVER = true;
        }

        if (twoD[xPos + 1][yPos].equals( Tileset.TRAP) && (HEARTS != 0)) {
            HEARTS -= 1;
        }

        if (twoD[xPos + 1][yPos].equals( Tileset.HEART)) {
            HEARTS += 1;
        }

        if (twoD[xPos + 1][yPos].equals(Tileset.WALL)
                || twoD[xPos + 1][yPos].equals(Tileset.TREE)
                || twoD[xPos + 1][yPos].equals(Tileset.MOUNTAIN)
                || twoD[xPos + 1][yPos].equals(Tileset.SAND)
                || twoD[xPos + 1][yPos].equals(Tileset.LOCKED_PORTAL)) {
            return;
        }

        if (twoD[xPos + 1][yPos].equals(Tileset.COIN)) {
            COINS -= 1;
        }

        if (twoD[xPos + 1][yPos].equals(Tileset.UNLOCKED_PORTAL)) {
            GAMEWON = true;
        }

        //deletes previous player icon
        twoD[xPos][yPos] = Tileset.FLOOR;

        xPos += 1;
        //draws new player icon above current position
        twoD[xPos][yPos] = PLAYER;

        showWorld();
    }

    public  void moveLeft() {
        if (twoD[xPos - 1][yPos].equals(Tileset.TRAP) && (HEARTS == 0)) {
            GAMEOVER = true;
        }

        if (twoD[xPos - 1][yPos].equals(Tileset.TRAP) && (HEARTS != 0)) {
            HEARTS -= 1;
        }

        if (twoD[xPos - 1][yPos].equals(Tileset.HEART)) {
            HEARTS += 1;
        }

        if (twoD[xPos - 1][yPos].equals(Tileset.WALL)
                || twoD[xPos - 1][yPos].equals(Tileset.TREE)
                || twoD[xPos - 1][yPos].equals(Tileset.MOUNTAIN)
                || twoD[xPos - 1][yPos].equals(Tileset.SAND)
                || twoD[xPos - 1][yPos].equals(Tileset.LOCKED_PORTAL)) {
            return;
        }

        if (twoD[xPos - 1][yPos].equals(Tileset.COIN)) {
            COINS -= 1;
        }

        if (twoD[xPos - 1][yPos].equals(Tileset.UNLOCKED_PORTAL)) {
            GAMEWON = true;
        }

        //deletes previous player icon
        twoD[xPos][yPos] = Tileset.FLOOR;

        xPos -= 1;
        //draws new player icon above current position
        twoD[xPos][yPos] = PLAYER;

        showWorld();
    }

    public  void clearTrap() {
        if (twoD[xPos + 1][yPos].equals( Tileset.TRAP)) {
            twoD[xPos + 1][yPos] = Tileset.FLOOR;
        }

        if (twoD[xPos - 1][yPos].equals( Tileset.TRAP)) {
            twoD[xPos - 1][yPos] = Tileset.FLOOR;
        }

        if (twoD[xPos][yPos + 1].equals( Tileset.TRAP)) {
            twoD[xPos][yPos + 1] = Tileset.FLOOR;
        }

        if (twoD[xPos][yPos - 1].equals( Tileset.TRAP)) {
            twoD[xPos][yPos - 1] = Tileset.FLOOR;
        }
        showWorld();
    }

    public void time() {
        Long time = (System.currentTimeMillis() - baseTime) / 1000;
        if (time < 10) {
            StdDraw.text(40, 15, "You finished in " + time.toString() + "s. Great!" );
            StdDraw.show();
        } else if (time < 20) {
            StdDraw.text(40, 15, "You finished in " + time.toString() + "s. Not bad!" );
            StdDraw.show();
        } else {
            StdDraw.text(40, 15, "You finished in " + time.toString() + "s. Better luck next time!" );
            StdDraw.show();
        }
    }

    public  void showWorld() {
        ter.renderFrame(twoD);
    }

    public TETile[][] returntwoD() {
        return twoD;
    }

    public  void parseMovement(String movements) {
        //uses int[] top  return the input movements where
        //[0] is the change in x and [1] is the change in y
        for (int i = 0; i < movements.length(); i++) {
            char c = movements.charAt(i);
            switch (c) {
                case 'w':
                    moveUp();
                    break;
                case 'a':
                    moveLeft();
                    break;
                case 's':
                    moveDown();
                    break;
                case 'd':
                    moveRight();
                    break;
                default:
            }
        }
    }

    public void InitTer() {
        ter.initialize(80, 40);
    }

    public  int returnXPos() {
        return xPos;
    }

    public  int returnYPos() {
        return yPos;
    }
}
