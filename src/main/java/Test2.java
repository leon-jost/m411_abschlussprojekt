import java.util.*;

class Test2 {
    private static ArrayList<List<Position>> allPossibleRoutes = new ArrayList<>();
    private static ArrayList<List<Position>> allCorrectPossibleRoutes = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<Position> positions = new ArrayList<>(Arrays.asList(new Position(1, 1), new Position(3, 2), new Position(3, 3), new Position(2, 2)));

        generateAllPossibleCombinations(new HashSet<>(positions), new ArrayList<>());

        for (List<Position> route : allPossibleRoutes) {
            if (route.get(0).x == 1 && route.get(0).y == 1) {
                allCorrectPossibleRoutes.add(route);
            }
        }

        for (List<Position> route : allCorrectPossibleRoutes) {
            // route ausgeben
            System.out.println(route);
            // totale distanz aller möglichen routen berechnen
            double totalDistance = 0;
            for (int i = 0; i < route.size(); i++) {
                totalDistance += route.get(i).distance(route.get((i + 1) % route.size()));
            }

            // total ausgeben
            System.out.println(totalDistance);
        }


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
