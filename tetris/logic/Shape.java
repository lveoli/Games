package logic;
import java.lang.Math;
import java.util.Random;

public class Shape {
    private Random RANDOM;
    public int[][] coordinates;
    public int rotation;
    public String name;

    public Shape(int shapeNumber) {
        switch (shapeNumber) {
            case 1:
                coordinates = new int[][]{{1, 5, 9, 13}, {4, 5, 6, 7}, {2, 6, 10, 14}, {9, 10, 11, 12}};
//        int[] two = new int[] {0x44C0, 0x8E00, 0x6440, 0x0E20};
//        int[] three = new int[] {0x4460, 0x0E80, 0xC440, 0x2E00};
//        int[] four = new int[] {0xCC00, 0xCC00, 0xCC00, 0xCC00};
//        int[] five = new int[] {0x06C0, 0x8C40, 0x6C00, 0x4620};
//        int[] six = new int[] {0x0E40, 0x4C40, 0x4E00, 0x4640};
//        int[] seven = new int[] {0x0C60, 0x4C80, 0xC600, 0x2640};
        }
    }

     //generates a random shape and rotation
     public static Shape generateShape() {
         int randomN = getRandomNumber() % 2;
         Shape newShape = new Shape(randomN);
         newShape.rotation = getRandomNumber() % 4;
         return newShape;
     }

    //generates random positive number
    private static int getRandomNumber() {
        return (int) Math.abs(Math.random());
    }

    public static int[] convertCoord(int num) {
        int[] coords = new int[2];
        coords[0] = num % 4;
        coords[1] = num / 4;
        return coords;
    }
}
