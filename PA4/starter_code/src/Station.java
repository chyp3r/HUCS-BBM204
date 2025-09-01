import java.io.Serializable;
import java.util.*;

public class Station implements Serializable {
    static final long serialVersionUID = 55L;

    public Point coordinates;
    public String description;

    public Station(Point coordinates, String description) {
        this.coordinates = coordinates;
        this.description = description;
    }

    public String toString() {
        return this.description;
    }


    public List<Station> getAdjacent(HashMap<Set<Station>, Double> times) {
        List<Station> adj = new ArrayList<>();
        for (Set<Station> stationSet : times.keySet()) {
            if (stationSet.contains(this)){
                for (Station s : stationSet){
                    if (!s.equals(this)) adj.add(s);
                }
            }
        }
        return adj;
    }

    public static double getTime(Station first, Station second, HashMap<Set<Station>, Double> times) {
        Set<Station> stationSet = new HashSet<>();
        stationSet.add(first);
        stationSet.add(second);
        return times.getOrDefault(stationSet, Double.POSITIVE_INFINITY);
    }
}