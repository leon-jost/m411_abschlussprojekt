import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TravellingSalesman {
    public static ArrayList<Position> positions = new ArrayList<>(Arrays.asList(new Position(1, 1), new Position(3, 2), new Position(3, 3), new Position(2, 2)));
    private static ArrayList<List<Position>> allPossibleRoutes = new ArrayList<>();
    private static ArrayList<Route> allCorrectPossibleRoutes = new ArrayList<>();

    public static double[][] distanceMatrix = new double[positions.size()][positions.size()];
    public static ArrayList<Double> distanceMatrixResults = new ArrayList<>();

    public static void main(String[] args) {
        // --- Travelling Salesman lösen ---
        // alle möglichen Kombinationen generieren (rekursive Methode)
        generateAllPossibleCombinations(new HashSet<>(positions), new ArrayList<>());

        // nur die Routen nehmen, welche mit dem Startpunkt beginen (als Startpunkt wird immer das erste Element der "positions" Liste genommen
        for (List<Position> route : allPossibleRoutes) {
            if (route.get(0).x == 1 && route.get(0).y == 1) {
                allCorrectPossibleRoutes.add(new Route(route));
            }
        }

        // totale Distanz zu den Routen ausrechnen
        for (Route route : allCorrectPossibleRoutes) {
            // totale distanz der route berechnen
            double totalDistance = 0;
            for (int i = 0; i < route.getPositions().size(); i++) {
                totalDistance += route.getPositions().get(i).distance(route.getPositions().get((i + 1) % route.getPositions().size()));
            }

            // totale distanz speichern
            route.setTotalDistance(totalDistance);
        }

        // sortierte Rangliste erstellen
        Comparator<Route> compareByTotalDistance = Comparator.comparing(Route::getTotalDistance);
        allCorrectPossibleRoutes.sort(compareByTotalDistance.reversed());

        // top 3 Rangliste ausgeben und in File speichern
        StringBuilder newFileContent = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            // ausgeben
            System.out.println("Rang " + (i + 1));
            System.out.println("Route: " + allCorrectPossibleRoutes.get(i).getPositions());
            System.out.println("Totaldistanz: " + allCorrectPossibleRoutes.get(i).getTotalDistance());
            System.out.println();

            // Inhalt für File generieren
            newFileContent.append("Rang ").append(i + 1);
            newFileContent.append("\n");
            newFileContent.append("Route: ").append(allCorrectPossibleRoutes.get(i).getPositions());
            newFileContent.append("\n");
            newFileContent.append("Totaldistanz: ").append(allCorrectPossibleRoutes.get(i).getTotalDistance());
            newFileContent.append("\n\n");
        }

        // generierten Inhalt nach File schreiben
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/src/main/resources/top3Routes.txt"))) {
            String fileContent = newFileContent.toString();
            bufferedWriter.write(fileContent);
        } catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }

        // --- Distanzmatrix generieren zum manuellen Überprüfen ---
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
                distanceMatrixResults.add(distance);

                // zum Debuggen
                /*from.print();
                to.print();
                System.out.println(x + " => " + y);
                System.out.println("distance: " + distance);*/
            }
        }

        // distanzen in matrix füllen
        int counter = 0;
        for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                distanceMatrix[row][col] = distanceMatrixResults.get(counter);
                counter++;
            }
        }

        // Distanzmatrix generieren


        // zum debuggen
        /*for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                System.out.print(distanceMatrix[row][col] + " ");
            }
            System.out.println();
        }*/
    }

    private static void generateAllPossibleCombinations(Set<Position> todoPositions, List<Position> donePositions) {
        if (todoPositions.isEmpty()) {
            // measure distance
            double totalDistance = 0;
            for (int i = 0; i < donePositions.size(); i++) {
                totalDistance += donePositions.get(i).distance(donePositions.get((i + 1) % donePositions.size()));
            }
            //System.out.println("Solution found");
            //System.out.println("\t" + donePositions);

            // positionen klonen
            ArrayList<Position> clonedDonePositions = new ArrayList<>();
            for (Position position : donePositions) {
                clonedDonePositions.add(new Position(position.x, position.y));
            }
            // startpunkt dazufügen (zurück an die startposition)
            clonedDonePositions.add(new Position(1, 1));

            // die fertiggestellte route hinzufügen
            allPossibleRoutes.add(clonedDonePositions);
            //System.out.println("\t" + totalDistance);
        } else {
            for (Position nextPosition : todoPositions) {
                // add position
                donePositions.add(nextPosition);

                // recurse
                Set<Position> tmp = new HashSet<>(todoPositions);
                tmp.remove(nextPosition);
                generateAllPossibleCombinations(tmp, donePositions);

                // remove position
                donePositions.remove(nextPosition);
            }
        }
    }
}
