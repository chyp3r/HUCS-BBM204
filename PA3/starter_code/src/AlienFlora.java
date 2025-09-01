import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class AlienFlora {
    private File xmlFile;
    ArrayList<GenomeCluster> clusters = new ArrayList<>();

    public AlienFlora(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void readGenomes() {
        System.out.println("##Start Reading Flora Genomes##");
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(this.xmlFile);
            NodeList genomes = doc.getElementsByTagName("genome");
            doc.getDocumentElement().normalize();
            for (int i = 0; i < genomes.getLength(); i++) {
                Node node = genomes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Genome genome = new Genome(element.getElementsByTagName("id").item(0).getTextContent(), Integer.parseInt(element.getElementsByTagName("evolutionFactor").item(0).getTextContent()));
                    NodeList links = element.getElementsByTagName("link");
                    for (int j = 0; j < links.getLength(); j++) {
                            Element link = (Element) links.item(j);
                            genome.addLink(link.getElementsByTagName("target").item(0).getTextContent(),Integer.parseInt(link.getElementsByTagName("adaptationFactor").item(0).getTextContent()));
                    }
                    GenomeCluster foundedCluser = null;

                    for (GenomeCluster cluster : clusters) {
                        for(Genome temp : cluster.genomeMap.values()) {
                            for(Genome.Link link : temp.links) {
                                if(Objects.equals(link.target, genome.id)) {
                                    foundedCluser = cluster;
                                    break;
                                }
                                if(foundedCluser != null) break;
                            }
                        }
                    }
                    if (foundedCluser == null) {
                        GenomeCluster cluster = new GenomeCluster();
                        cluster.addGenome(genome);
                        clusters.add(cluster);
                    }else{
                        foundedCluser.addGenome(genome);
                    }
                }
            }

            System.out.println("Number of Genome Clusters: " + clusters.size());
            System.out.print("For the Genomes: [");
            for (int i = 0; i < clusters.size(); i++) {
                System.out.print(clusters.get(i).genomeMap.keySet());
                if (i != clusters.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println("##Reading Flora Genomes Completed##");
        }
        catch (Exception ignored){
        }


    }

    public void evaluateEvolutions() {
        System.out.println("##Start Evaluating Possible Evolutions##");
        int certified = 0;
        ArrayList<Float> evolutionScores = new ArrayList<>();
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(this.xmlFile);
            NodeList evolutionPairsList = doc.getElementsByTagName("possibleEvolutionPairs");
            Element evolutionPairsElement = (Element) evolutionPairsList.item(0);
            NodeList pairList = evolutionPairsElement.getElementsByTagName("pair");

            for (int i = 0; i < pairList.getLength(); i++) {
                Node pairNode = pairList.item(i);
                if(pairNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) pairNode;
                    String first = element.getElementsByTagName("firstId").item(0).getTextContent();
                    String second = element.getElementsByTagName("secondId").item(0).getTextContent();
                    GenomeCluster firstCluster = null, secondCluster = null;
                    for(GenomeCluster cluster : clusters) {
                        if(findGenome(cluster,first) != null){
                            firstCluster = cluster;
                        }
                    }

                    for(GenomeCluster cluster : clusters) {
                        if(findGenome(cluster,second) != null){
                            secondCluster = cluster;
                        }
                    }

                    if(firstCluster == secondCluster){
                        evolutionScores.add((float) -1);

                    }
                    else{
                        certified++;
                        float temp = firstCluster.getMinEvolutionGenome().evolutionFactor + secondCluster.getMinEvolutionGenome().evolutionFactor;
                        evolutionScores.add(temp/2);
                    }
                }
            }
            System.out.println("Number of Possible Evolutions: " + evolutionScores.size());
            System.out.println("Number of Certified Evolution: "+certified);
            System.out.println("Evolution Factor for Each Evolution Pair: "+evolutionScores.toString());
            System.out.println("##Evaluated Possible Evolutions##");
        }catch (Exception ignored){
        }

    }

    public void evaluateAdaptations() {
        System.out.println("##Start Evaluating Possible Adaptations##");

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(this.xmlFile);
            NodeList adaptationPairsList = doc.getElementsByTagName("possibleAdaptationPairs");
            Element adaptationPairsElement = (Element) adaptationPairsList.item(0);
            NodeList pairList = adaptationPairsElement.getElementsByTagName("pair");
            int certified = 0;
            ArrayList<Integer> adaptationScores = new ArrayList<>();

            for (int i = 0; i < pairList.getLength(); i++) {
                Node pairNode = pairList.item(i);
                if(pairNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) pairNode;
                    String first = element.getElementsByTagName("firstId").item(0).getTextContent();
                    String second = element.getElementsByTagName("secondId").item(0).getTextContent();
                    GenomeCluster firstCluster = null, secondCluster = null;
                    for(GenomeCluster cluster : clusters) {
                        if(findGenome(cluster,first) != null){
                            firstCluster = cluster;
                        }
                    }
                    for(GenomeCluster cluster : clusters) {
                        if(findGenome(cluster,second) != null){
                            secondCluster = cluster;
                        }
                    }
                    if(firstCluster == secondCluster && firstCluster != null){
                        certified++;
                        adaptationScores.add((firstCluster.dijkstra(first,second)));
                    }
                    else if(firstCluster != secondCluster){
                        adaptationScores.add(-1);
                    }
                }
            }
            System.out.println("Number of Possible Adaptations: " + adaptationScores.size());
            System.out.println("Number of Certified Adaptations: " + certified);
            System.out.println("Adaptation Factor for Each Adaptation Pair: " + adaptationScores.toString());
            System.out.print("##Evaluated Possible Adaptations## ");
        }
        catch (Exception ignored){

        }
    }

    public GenomeCluster findGenome(GenomeCluster cluster,String id) {
        for (Genome genome: cluster.genomeMap.values()) {
           if (genome.id.equals(id)) {
               return cluster;
           }
        }
        return null;
    }

}
