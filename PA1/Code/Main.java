import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {
    public static void main(String args[]) throws IOException {
        int[] data = Utils.readCSVColumn("TrafficFlowDataset.csv",2).stream().mapToInt(Integer::valueOf).toArray();

        List<int[]> subsets = Utils.createSubsetList(data);
        Utils.startWarmUp(subsets);

        List<double[]> randomTimes = Utils.startTest(subsets,1);

        for (int[] subset : subsets) {
            Arrays.sort(subset);
        }

        List<double[]> orderedTimes = Utils.startTest(subsets,1);

        for (int[] subset : subsets) {
            Utils.reverseArray(subset);
        }

        List<double[]> reversedTimes = Utils.startTest(subsets,1);

        int[] inputAxis = {500,1000,2000,4000,8000,16000,32000,64000,128000,250000};

        String[] titles = {"Comb Sort","Insertion Sort","Shaker Sort","Shell Sort","Radix Sort"};
        String[] modes = {"Random Input Data","Sorted Input Data","Reversely Sorted Input Data"};
        List<List<double[]>> modeList = new ArrayList<>();
        modeList.add(randomTimes);
        modeList.add(orderedTimes);
        modeList.add(reversedTimes);

        for(int i = 0; i < titles.length; i++) {
            Utils.sortTable(titles[i]+" Time vs Input Size Graph",inputAxis,new double[][]{randomTimes.get(i),orderedTimes.get(i),reversedTimes.get(i)});
        }
        for(int i = 0; i < modes.length; i++) {
            var mode = modeList.get(i);
            Utils.allAlgoTable(modes[i]+" Time vs Input Size Graph",inputAxis,new double[][]{mode.get(0),mode.get(1),mode.get(2),mode.get(3),mode.get(4)});
        }
        Utils.randomFixer("Random Input Data Fixed"+" Time vs Input Size Graph",inputAxis,new double[][]{randomTimes.get(0),randomTimes.get(1),randomTimes.get(3),randomTimes.get(4)});
        Utils.sortedFixer("Sorted Input Data Fixed"+" Time vs Input Size Graph",inputAxis,new double[][]{orderedTimes.get(0),orderedTimes.get(1),orderedTimes.get(2),orderedTimes.get(3)});
        Utils.reverseFixed("Reversely Sorted Input Data Fixed"+" Time vs Input Size Graph",inputAxis,new double[][]{reversedTimes.get(0),reversedTimes.get(1),reversedTimes.get(3),reversedTimes.get(4)});

    }
}
