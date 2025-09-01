import java.util.*;

public class GenomeCluster {
    public Map<String, Genome> genomeMap = new HashMap<>();

    public void addGenome(Genome genome) {
        genomeMap.put(genome.id,genome);
    }

    public boolean contains(String genomeId) {
        return genomeMap.containsKey(genomeId);
    }

    public Genome getMinEvolutionGenome() {
        Genome minEvolutionGenome = null;
        for (Genome genome : genomeMap.values()) {
            if (minEvolutionGenome == null) {
                minEvolutionGenome = genome;
                continue;
            }
            if(minEvolutionGenome.evolutionFactor > genome.evolutionFactor){
                minEvolutionGenome = genome;
            }
        }
        return minEvolutionGenome;
    }

    public int dijkstra(String startId, String endId) {
        Genome start = genomeMap.get(startId);
        Genome end = genomeMap.get(endId);
        Map<Genome, Integer> dist = new HashMap<>();
        PriorityQueue<Genome> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        for (Genome genome : genomeMap.values()) {
            dist.put(genome, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        pq.add(start);
        while (!pq.isEmpty()) {
            Genome current = pq.poll();
            if(current == end){
                return dist.get(current);
            }
            for(Genome.Link link : current.links){
                int temp = dist.get(current) + link.adaptationFactor;
                if(temp < dist.get(genomeMap.get(link.target))){
                    dist.put(genomeMap.get(link.target), temp);
                    pq.add(genomeMap.get(link.target));
                }
            }
        }

        return -1;
    }
}
