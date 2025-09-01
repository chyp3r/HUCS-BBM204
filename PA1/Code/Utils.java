import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<Integer> readCSVColumn(String filename, int column) {
        List<Integer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int data = Integer.parseInt(values[column]);
                list.add(data);
            }
        }
        catch (IOException e){
        }
        return list;
    }

    public static List<int[]> createSubsetList(int[] data){
        int[] dataValues = {500,1000,2000,4000,8000,16000,32000,64000,128000,250000};

        List<int[]> subsets = new ArrayList<>();
        for (int dataValue : dataValues) {
            subsets.add(getRandomSubset(data, dataValue));
        }
        return subsets;
    }

    public static int[] getRandomSubset(int[] arr, int x) {
        return Arrays.copyOfRange(arr, 0, x);
    }

    public static int findDiff(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void printData(List<double[]> times){
        int[] dataValues = {500,1000,2000,4000,8000,16000,32000,64000,128000,250000};

        String[] headers = {"xxx", "Comb", "Insert", "Shaker", "Shell", "Radix"};

        System.out.println("+------------+------------+------------+------------+------------+------------+");

        for (String header : headers) {
            System.out.printf("| %-10s ", header);
        }
        System.out.println("|");

        System.out.println("+------------+------------+------------+------------+------------+------------+");

        for (int i = 1; i < 11; i++) {
            System.out.printf("| %-10d ", dataValues[i-1]);
            for (int j = 1; j < 6; j++) {
                System.out.printf("| %-10s ", times.get(j-1)[i-1]);
            }
            System.out.println("|");
            System.out.println("+------------+------------+------------+------------+------------+------------+");
        }
    }

    public static void startWarmUp(List<int[]> subsets){
        int[] subset = subsets.get(1);
        for (int i = 0; i < 10000; i++) {
            SortAlgorithms.combSort(subset);
            SortAlgorithms.insertSort(subset);
            SortAlgorithms.shellSort(subset);
            SortAlgorithms.shakerSort(subset);
            SortAlgorithms.radixSort(subset,9);
        }
    }

    public static List<double[]> startTest(List<int[]> subsets,int count){
        double[] combSortTimes = new double[10];
        double[]  insertSortTimes = new double[10];
        double[]  shakerSortTimes = new double[10];
        double[] shellSortTimes = new double[10];
        double[]  radixSortTimes = new double[10];

        List<double[]> times = new ArrayList<>();

        double startTime = 0;
        double estimatedTime = 0;

        for(int i = 0;i<count;i++){
            for(int index = 0; index < 10; index++) {
                int[] subset = subsets.get(index);

                startTime = System.nanoTime();
                int[] combSortSorted = SortAlgorithms.combSort(subset);
                estimatedTime = (System.nanoTime() - startTime)/1000000;

                combSortTimes[index] += estimatedTime;

                int maxDigit = String.valueOf(combSortSorted[combSortSorted.length-1]).length();
                startTime = System.nanoTime();
                int[] insertSortSorted = SortAlgorithms.insertSort(subset);
                estimatedTime = (System.nanoTime() - startTime)/1000000;

                insertSortTimes[index] += estimatedTime;

                startTime = System.nanoTime();
                int[] shakerSortSorted = SortAlgorithms.shakerSort(subset);
                estimatedTime = (System.nanoTime() - startTime)/1000000;

                shakerSortTimes[index] += estimatedTime;

                startTime = System.nanoTime();
                int[] shellSortSorted = SortAlgorithms.shellSort(subset);
                estimatedTime = (System.nanoTime() - startTime)/1000000;

                shellSortTimes[index] += estimatedTime;

                startTime = System.nanoTime();
                int[] radixSortSorted = SortAlgorithms.radixSort(subset,maxDigit);
                estimatedTime = (System.nanoTime() - startTime)/1000000;

                radixSortTimes[index] += estimatedTime;

            }
        }

        times.add(Arrays.stream(combSortTimes).map(n -> BigDecimal.valueOf(n / count).setScale(2, RoundingMode.HALF_UP).doubleValue()).toArray());
        times.add(Arrays.stream(insertSortTimes).map(n -> BigDecimal.valueOf(n / count).setScale(2, RoundingMode.HALF_UP).doubleValue()).toArray());
        times.add(Arrays.stream(shakerSortTimes).map(n -> BigDecimal.valueOf(n / count).setScale(2, RoundingMode.HALF_UP).doubleValue()).toArray());
        times.add(Arrays.stream(shellSortTimes).map(n -> BigDecimal.valueOf(n / count).setScale(2, RoundingMode.HALF_UP).doubleValue()).toArray());
        times.add(Arrays.stream(radixSortTimes).map(n -> BigDecimal.valueOf(n / count).setScale(2, RoundingMode.HALF_UP).doubleValue()).toArray());

        Utils.printData(times);

        return times;
    }

    public static void sortTable(String title, int[] xAxis, double[][] yAxis) throws IOException {
        XYChart chart = new XYChartBuilder().width(1280).height(720).title(title)
                .yAxisTitle("Time in Milliseconds (ms)").xAxisTitle("Input Size").build();
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisDecimalPattern("0");
        chart.getStyler().setYAxisDecimalPattern("0");


        chart.addSeries("Random", doubleX, yAxis[0]);
        chart.addSeries("Sorted", doubleX, yAxis[1]);
        chart.addSeries("Reversed", doubleX, yAxis[2]);

        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        new SwingWrapper(chart).displayChart();
    }

    public static void allAlgoTable(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1280).height(720).title(title)
                .yAxisTitle("Time in Milliseconds (ms)").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisDecimalPattern("0");
        chart.getStyler().setYAxisDecimalPattern("0");


        // Add a plot for a sorting algorithm
        chart.addSeries("CombSort", doubleX, yAxis[0]);
        chart.addSeries("InsertionSort", doubleX, yAxis[1]);
        chart.addSeries("ShakerSort", doubleX, yAxis[2]);
        chart.addSeries("ShellSort", doubleX, yAxis[3]);
        chart.addSeries("RadixSort", doubleX, yAxis[4]);


        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    public static void randomFixer(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1280).height(720).title(title)
                .yAxisTitle("Time in Milliseconds (ms)").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisDecimalPattern("0");
        chart.getStyler().setYAxisDecimalPattern("0");

        // Add a plot for a sorting algorithm
        chart.addSeries("Comb Sort", doubleX, yAxis[0]);
        chart.addSeries("Insertion Sort", doubleX, yAxis[1]);
        chart.addSeries("Shell Sort", doubleX, yAxis[2]);
        chart.addSeries("Radix Sort", doubleX, yAxis[3]);


        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
    public static void sortedFixer(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1280).height(720).title(title)
                .yAxisTitle("Time in Milliseconds (ms)").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisDecimalPattern("0");
        chart.getStyler().setYAxisDecimalPattern("0");

        // Add a plot for a sorting algorithm

        chart.addSeries("Comb Sort", doubleX, yAxis[0]);
        chart.addSeries("Insertion Sort", doubleX, yAxis[1]);
        chart.addSeries("Shaker Sort", doubleX, yAxis[2]);
        chart.addSeries("Shell Sort", doubleX, yAxis[3]);


        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
    public static void reverseFixed(String title, int[] xAxis, double[][] yAxis) throws IOException {
        XYChart chart = new XYChartBuilder().width(1280).height(720).title(title)
                .yAxisTitle("Time in Milliseconds (ms)").xAxisTitle("Input Size").build();

        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisDecimalPattern("0");
        chart.getStyler().setYAxisDecimalPattern("0");

        chart.addSeries("Comb Sort", doubleX, yAxis[0]);
        chart.addSeries("Insertion Sort", doubleX, yAxis[1]);
        chart.addSeries("Shell Sort", doubleX, yAxis[2]);
        chart.addSeries("Radix Sort", doubleX, yAxis[3]);

        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        new SwingWrapper(chart).displayChart();
    }

    public static void reverseArray(int[] array) {
        int start = 0;
        int end = array.length - 1;
        while (start < end) {
            SortAlgorithms.swap(array, start, end);
            start++;
            end--;
        }
    }
}
