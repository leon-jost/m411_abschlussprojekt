import java.util.*;

public class TravellingSalesman {
    public static ArrayList<Position> positions = new ArrayList<>(Arrays.asList(new Position(1, 1), new Position(3, 2), new Position(3, 3), new Position(2, 2)));
    public static double[][] distanceMatrix = new double[positions.size()][positions.size()];
    public static ArrayList<Double> results = new ArrayList<>();

    public static void main(String[] args) {
        // distanzen berechnen
        for (Position from : positions) {
            for (Position to : positions) {
                int x;
                int y;
                // x
                if (from.x > to.x) {
                    x = from.x - to.x;
                } else {
                    x = to.x - from.x;
                }
                // y
                if (from.y > to.y) {
                    y = from.y - to.y;
                } else {
                    y = to.y - from.y;
                }

                double distance;
                distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                results.add(distance);

                from.print();
                to.print();
                System.out.println(x + " => " + y);
                System.out.println("distance: " + distance);
            }
        }

        // distanzen in matrix füllen
        int counter = 0;
        for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                distanceMatrix[row][col] = results.get(counter);
                counter++;
            }
        }

        // print out matrix
        for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                System.out.print(distanceMatrix[row][col] + " ");
            }
            System.out.println();
        }

        // calculate all possible routes

        // TODO: continue (Test2.java)
        // step 1: find out amount of combinations

        // step 2: calculate all possible combinations

        // classic loop for reference
        /*for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                System.out.print(distanceMatrix[row][col] + " ");
            }
            System.out.println();
        }*/
    }
}
