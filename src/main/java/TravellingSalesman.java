import java.io.*;
import java.util.*;

public class TravellingSalesman {
    public static ArrayList<Position> positions = new ArrayList<>();
    private static Position startpunkt;
    private static ArrayList<List<Position>> allPossibleRoutes = new ArrayList<>();
    private static ArrayList<Route> allCorrectPossibleRoutes = new ArrayList<>();

    public static double[][] distanceMatrix;
    public static ArrayList<Double> distanceMatrixResults = new ArrayList<>();

    public static void main(String[] args) {
        // --- Vorbereitungen ---

        // Filepfad von von Konsole lesen/anfordern
        boolean readFileSuccessful = false;
        while(!readFileSuccessful) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Pfad zum File angeben (absoluter Pfad): ");
            String fileLocation = scanner.nextLine();

            // Koordinaten von File einlesen und in ArrayList speichern
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    ArrayList<String> coordinatePairs = new ArrayList<>();
                    Collections.addAll(coordinatePairs, line.split(";"));
                    for (String coordinatePair : coordinatePairs) {
                        positions.add(new Position(Integer.parseInt(coordinatePair.substring(0, coordinatePair.indexOf(","))), Integer.parseInt(coordinatePair.substring(coordinatePair.indexOf(",") + 1))));
                    }
                    line = bufferedReader.readLine();
                }

                readFileSuccessful = true;
            } catch (IOException e) {
                System.out.println("Das File konnte entweder nicht gefunden werden, oder die Koordinaten haben ein falsches Format/Syntax.");
                readFileSuccessful = false;
            }
        }

        // als Startpunkt wird immer das erste Element der "positions" Liste genommen
        startpunkt = positions.get(0);

        // Distanzmatrix für späteren Gebrach initialisieren.
        distanceMatrix = new double[positions.size()][positions.size()];

        // --- Travelling Salesman lösen   ---

        // alle möglichen Kombinationen generieren (rekursive Methode)
        generateAllPossibleCombinations(new HashSet<>(positions), new ArrayList<>());

        // nur die Routen nehmen, welche mit dem Startpunkt beginen
        for (List<Position> route : allPossibleRoutes) {
            if (route.get(0).x == startpunkt.x && route.get(0).y == startpunkt.y) {
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
        allCorrectPossibleRoutes.sort(compareByTotalDistance);

        // top 3 Rangliste ausgeben und in File speichern
        StringBuilder newFileContent = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            // ausgeben
            System.out.println("Rang " + (i + 1));
            System.out.println("Route: " + allCorrectPossibleRoutes.get(i).getPositions());
            System.out.println("Totaldistanz: " + String.format("%.2f", allCorrectPossibleRoutes.get(i).getTotalDistance()));
            System.out.println();

            // Inhalt für File generieren
            newFileContent.append("Rang ").append(i + 1);
            newFileContent.append("\n");
            newFileContent.append("Route: ").append(allCorrectPossibleRoutes.get(i).getPositions());
            newFileContent.append("\n");
            newFileContent.append("Totaldistanz: ").append(String.format("%.2f", allCorrectPossibleRoutes.get(i).getTotalDistance()));
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

        // --- Distanzmatrix generieren zur manuellen Überprüfung ---

        // distanzen berechnen
        for (Position from : positions) {
            for (Position to : positions) {
                double x;
                double y;
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

        // Distanzmatrix generieren und ausgeben
        System.out.println("Distanzmatrix zum kontrollieren:");
        for (int row = 0; row < distanceMatrix.length; row++) {
            for (int col = 0; col < distanceMatrix[row].length; col++) {
                System.out.print(String.format("%.2f", distanceMatrix[row][col]) + " ");
            }
            System.out.println();
        }
    }

    private static void generateAllPossibleCombinations(Set<Position> todoPositions, List<Position> donePositions) {
        if (todoPositions.isEmpty()) {
            // Distanz berechnen
            double totalDistance = 0;
            for (int i = 0; i < donePositions.size(); i++) {
                totalDistance += donePositions.get(i).distance(donePositions.get((i + 1) % donePositions.size()));
            }

            // positionen klonen
            ArrayList<Position> clonedDonePositions = new ArrayList<>();
            for (Position position : donePositions) {
                clonedDonePositions.add(new Position(position.x, position.y));
            }
            // startpunkt dazufügen (zurück an die startposition)
            clonedDonePositions.add(new Position(startpunkt.x, startpunkt.y));

            // die fertiggestellte route hinzufügen
            allPossibleRoutes.add(clonedDonePositions);
        } else {
            for (Position nextPosition : todoPositions) {
                // Position hinzufügen
                donePositions.add(nextPosition);

                // rekursieren
                Set<Position> tmp = new HashSet<>(todoPositions);
                tmp.remove(nextPosition);
                generateAllPossibleCombinations(tmp, donePositions);

                // Position entfernen
                donePositions.remove(nextPosition);
            }
        }
    }
}
