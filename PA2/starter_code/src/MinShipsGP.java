import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MinShipsGP {
    private final ArrayList<Integer> artifactsFound = new ArrayList<>();
    public ArrayList<Integer> getArtifactsFound() {
        return artifactsFound;
    }

    MinShipsGP(ArrayList<Integer> artifactsFound) {
        this.artifactsFound.addAll(artifactsFound);
    }

    public OptimalShipSolution optimalArtifactCarryingAlgorithm() throws FileNotFoundException {
        ArrayList<Integer> ships = new ArrayList<>();

        // Reverse sorted array
        ArrayList<Integer> tempList = new ArrayList<>(artifactsFound);
        tempList.sort(Collections.reverseOrder());

        for (int i = 0; i < tempList.size() - 1; i++) {
            int temp = findPlace(ships,tempList.get(i));

            // Find available place or create new one
            if (temp != -1) {
                ships.set(temp,ships.get(temp)+tempList.get(i));
            }else{
                ships.add(tempList.get(i));
            }
        }

        return new OptimalShipSolution(artifactsFound,ships.size());
    }

    public int findPlace(ArrayList<Integer> ships, int value){
        for(int i = 0; i < ships.size(); i++){
            if((100-ships.get(i))>=value){
                return i;
            }
        }
        return -1;
    }
}
