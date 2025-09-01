import java.io.*;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList<Integer>> safeSet = new ArrayList<>();
        ArrayList<Integer> artifactSet = new ArrayList<>();

        /** Safe-lock Opening Algorithm Below **/
        System.out.println("##Initiate Operation Safe-lock##");

        // Read Safes file
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(",");
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(Integer.parseInt(numbers[0].trim()));
                pair.add(Integer.parseInt(numbers[1].trim()));
                safeSet.add(pair);
            }
        }

        // Solve
        MaxScrollsDP dpSolver = new MaxScrollsDP(safeSet);
        OptimalScrollSolution solution = dpSolver.optimalSafeOpeningAlgorithm();
        solution.printSolution(solution);

        System.out.println("##Operation Safe-lock Completed##");

        /** Operation Artifact Algorithm Below **/
        System.out.println("##Initiate Operation Artifact##");

        // Read arg Artifacts file
        try (BufferedReader reader = new BufferedReader(new FileReader(args[1]))) {
            for (String num : reader.readLine().split(","))
                artifactSet.add(Integer.parseInt(num.trim()));
        }

        // Solve
        MinShipsGP greedySolver = new MinShipsGP(artifactSet);
        OptimalShipSolution solution2 = greedySolver.optimalArtifactCarryingAlgorithm();
        solution2.printSolution(solution2);

        System.out.println("##Operation Artifact Completed##");

    }
}