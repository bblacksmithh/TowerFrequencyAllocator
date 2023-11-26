import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
    // Constants for frequency range and maximum distance threshold
    private int minFreq = 110;
    private int maxFreq = 115;
    private double maxDistanceThreshold = 0.269002;

    // List to store tower nodes and their connections
    private List<Tower> nodes;
    private List<List<Tower>> connections;

    // Constructor initializes the Graph with a list of Tower nodes
    public Graph(List<Tower> nodes) {
        this.nodes = nodes;
        this.connections = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            connections.add(new ArrayList<>());
        }
    }

    // Getter methods for frequency range and Tower Nodes
    public int getMinFreq() {
        return minFreq;
    }

    public int getMaxFreq() {
        return maxFreq;
    }

    public List<Tower> getNodes() {
        return nodes;
    }

    //Method to calculate the Haversine distance between two towers to take into consideration the distance of 2 points on a sphere
    protected double calculateDistance(Tower tower1, Tower tower2) {
        double lat1 = Math.toRadians(tower1.getLat());
        double lat2 = Math.toRadians(tower2.getLat());
        double lon1 = Math.toRadians(tower1.getLon());
        double lon2 = Math.toRadians(tower2.getLon());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radius = 6371; //Radius of the earth in KM

        //Returns Distance in KM
        return radius * c;
    }

    //Method to establish connections between towers based on the maximum distance threshold
    public void establishConnections() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Tower tower1 = nodes.get(i);
                Tower tower2 = nodes.get(j);

                double distance = calculateDistance(tower1, tower2);

                if (distance <= maxDistanceThreshold) {
                    connections.get(i).add(tower2);
                    connections.get(j).add(tower1);
                }
            }
        }
    }

    //Method to display the connections for each tower
    public void displayConnections() {
        for (int i = 0; i < nodes.size(); i++) {
            Tower tower = nodes.get(i);
            List<Tower> connectedTowers = connections.get(i);

            System.out.print("Connections for " + tower.getName() + ": ");
            for (Tower connectedTower : connectedTowers) {
                System.out.print(connectedTower.getName() + " ");
            }
            System.out.println();
        }
    }

    //Method to display the frequencies assigned to each tower
    public void displayFrequencies() {
        for (int i = 0; i < nodes.size(); i++) {
            Tower tower = nodes.get(i);
            System.out.println("Node: " + tower.getName() + ", Frequency: " + tower.getFrequency());
        }
    }

    //Method to assign frequencies to towers based on the average distance to neighbors
    public void assignFrequencies() {
        List<Tower> sortedNodes = new ArrayList<>(nodes);
        Collections.sort(sortedNodes, (node1, node2) -> Double.compare(calculateAverageDistance(node1), calculateAverageDistance(node2)));

        Set<Integer> usedNumbers = new HashSet<>();

        for (Tower currentTower : sortedNodes) {
            Set<Integer> neighborNumbers = getNeighborFrequencies(currentTower);

            int assignedNumber = findAvailableNumber(neighborNumbers, usedNumbers);
            currentTower.setFrequency(assignedNumber);

            usedNumbers.add(assignedNumber);

            // If all numbers are used, clear the hash set of used numbers
            if (usedNumbers.size() == (maxFreq - minFreq + 1)) {
                usedNumbers.clear();
            }
        }
    }

    //Method to get frequencies assigned to neighbors of a tower
    private Set<Integer> getNeighborFrequencies(Tower currentTower) {
        Set<Integer> neighborFrequencies = new HashSet<>();
        List<Tower> connectedTowers = connections.get(nodes.indexOf(currentTower));

        for (Tower neighbor : connectedTowers) {
            neighborFrequencies.add(neighbor.getFrequency());
        }

        return neighborFrequencies;
    }

    private int findAvailableNumber(Set<Integer> neighborNumbers, Set<Integer> usedNumbers) {
        // Find the smallest available number not used by neighbors
        int candidateNumber = minFreq;

        while (neighborNumbers.contains(candidateNumber) || usedNumbers.contains(candidateNumber)) {
            candidateNumber++;
            if (candidateNumber > maxFreq) {
                candidateNumber = minFreq;
            }
        }

        return candidateNumber;
    }

    //Method to calculate the average distance of a tower to its neighbors
    private double calculateAverageDistance(Tower node) {
        List<Tower> connectedTowers = connections.get(nodes.indexOf(node));

        double totalDistance = 0;
        for (Tower neighbor : connectedTowers) {
            totalDistance += calculateDistance(node, neighbor);
        }

        return totalDistance / connectedTowers.size();
    }
}
