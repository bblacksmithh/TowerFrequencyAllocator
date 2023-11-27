import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        List<Tower> towers = readTowersFromFile("towers.txt");

        // Create an instance of the Graph class
        Graph graph = new Graph(towers);

        // Call the method to establish connections between towers
        graph.establishConnections();

        // Display distances between tower pairs
        //displayDistances(graph);

        //Allocate Frequencies to each tower
        graph.assignFrequencies();

        //Display Connections made
        //graph.displayConnections();

        //Displays frequencies assigned
        graph.displayFrequencies();
    }

    private static List<Tower> readTowersFromFile(String fileName) {
        List<Tower> towers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String name = parts[0];
                double lat = Double.parseDouble(parts[1]);
                double lon = Double.parseDouble(parts[2]);

                towers.add(new Tower(name, lat, lon));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return towers;
    }

    private static void displayDistances(Graph graph) {
        List<Tower> towers = graph.getNodes();

        for (int i = 0; i < towers.size(); i++) {
            for (int j = i + 1; j < towers.size(); j++) {
                Tower tower1 = towers.get(i);
                Tower tower2 = towers.get(j);

                double distance = graph.calculateDistance(tower1, tower2);

                System.out.println("Distance between " + tower1.getName() + " and " + tower2.getName() + ": " + distance + " km");
            }
        }
    }
}