import java.io.Serializable;
import java.util.*;

public class CampusNavigatorApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HashMap<Station, Station> predecessors = new HashMap<>();
    public HashMap<Set<Station>, Double> times = new HashMap<>();

    public CampusNavigatorNetwork readCampusNavigatorNetwork(String filename) {
        CampusNavigatorNetwork network = new CampusNavigatorNetwork();
        network.readInput(filename);
        return network;
    }

    /**
     * Calculates the fastest route from the user's selected starting point to 
     * the desired destination, using the campus golf cart network and walking paths.
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(CampusNavigatorNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<Station> allStations = new ArrayList<>();
        allStations.add(network.startPoint);
        allStations.add(network.destinationPoint);

        for (int i=0; i<network.lines.size(); i++) {
            CartLine line = network.lines.get(i);
            for (int j = 0; j < line.cartLineStations.size(); j++) {
                allStations.add(line.cartLineStations.get(j));
            }
        }

        for (int i=0; i<allStations.size(); i++) {
            for (int j = i + 1; j < allStations.size(); j++) {
                Station current = allStations.get(i);
                Station next = allStations.get(j);
                Set<Station> stationSet = new HashSet<>();
                stationSet.add(current);
                stationSet.add(next);
                times.put(stationSet, current.coordinates.getDistance(next.coordinates) / network.averageWalkingSpeed);
            }
        }

        for (CartLine line : network.lines) {
            List<Station> stations = line.cartLineStations;
            for (int i = 0; i < stations.size() - 1; i++) {
                Station current = stations.get(i);
                Station next = stations.get(i + 1);
                Set<Station> stationSet = new HashSet<>();
                stationSet.add(current);
                stationSet.add(next);
                times.put(stationSet, current.coordinates.getDistance(next.coordinates) / network.averageCartSpeed);
            }
        }

        dijkstra(network.startPoint,allStations);

        LinkedList<Station> path = new LinkedList<>();
        Station step = network.destinationPoint;
        while (step != null) {
            path.addFirst(step);
            step = predecessors.get(step);
        }

        for (int i=0; i<path.size()-1; i++) {
            Station from = path.get(i);
            Station to = path.get(i + 1);
            Set<Station> stationSet = new HashSet<>(List.of(from, to));
            boolean isRide = false;
            for (CartLine line : network.lines) {
                List<Station> s = line.cartLineStations;
                for (int j=0; j<s.size()-1; j++) {
                    if ((s.get(j).equals(to) && s.get(j + 1).equals(from)) || (s.get(j).equals(from) && s.get(j + 1).equals(to))) {
                        isRide = true;
                        break;
                    }
                }
                if (isRide) {
                    break;
                }
            }
            routeDirections.add(new RouteDirection(from.description, to.description, times.getOrDefault(stationSet, Double.POSITIVE_INFINITY), isRide));
        }
        return routeDirections;
    }

    public void dijkstra(Station startPoint, List<Station> allStations) {
        Map<Station, Double> mins = new HashMap<>();
        Set<Station> visited = new HashSet<>();
        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(mins::get));

        for (int i = 0; i < allStations.size(); i++) {
            Station s = allStations.get(i);
            mins.put(s, Double.POSITIVE_INFINITY);
        }

        mins.put(startPoint, 0.0);
        pq.add(startPoint);

        while (!pq.isEmpty()) {
            Station current = pq.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Station neighbor : current.getAdjacent(times)) {
                double time = mins.get(current) + Station.getTime(current, neighbor,times);
                if (time < mins.get(neighbor)) {
                    predecessors.put(neighbor, current);
                    mins.put(neighbor, time);
                    pq.add(neighbor);
                }
            }
        }
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double totalDuration = 0.0;
        for (int i = 0; i < directions.size(); i++) {
            RouteDirection dir = directions.get(i);
            totalDuration += dir.duration;
        }
        var temp = Math.round(totalDuration);
        System.out.printf("The fastest route takes %d minute(s).\n",temp);
        System.out.println("Directions");
        System.out.println("----------");
        for (int i = 0; i < directions.size(); i++) {
            RouteDirection dir = directions.get(i);
            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.\n", i + 1, dir.cartRide ? "Ride the cart" : "Walk", dir.startStationName, dir.endStationName, dir.duration);
        }
    }
}
