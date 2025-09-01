import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MaxScrollsDP {
    private ArrayList<ArrayList<Integer>> safesDiscovered = new ArrayList<>();

    public MaxScrollsDP(ArrayList<ArrayList<Integer>> safesDiscovered) {
        this.safesDiscovered = safesDiscovered;
    }

    public ArrayList<ArrayList<Integer>> getSafesDiscovered() {
        return safesDiscovered;
    }

    public OptimalScrollSolution optimalSafeOpeningAlgorithm() {
        int size = safesDiscovered.size();

        int max = size*5;
        int[][] dp = new int[size+1][max+1];
        for(int i = 1; i < size+1; i++) {
            for(int w=0; w < max+1; w+=5) {
                int generate,maintain,open;

                //Generate knowledge
                try{
                    generate = dp[i-1][w-5];
                } catch (Exception e) {
                    generate = 0;
                }

                //Maintain state
                try{
                    maintain = dp[i-1][w];
                } catch (Exception e) {
                    maintain = 0;
                }

                //Open safe
                try{
                    int temp = w+safesDiscovered.get(i-1).get(0);
                    if((i-1)*5>= temp){
                        open = dp[i-1][temp] + safesDiscovered.get(i-1).get(1);
                    }
                    else{
                        open = 0;
                    }
                } catch (Exception e) {
                    open = 0;
                }

                // Take max
                int max1 = Math.max(generate, maintain);
                dp[i][w] = Math.max(open, max1);
            }
        }

        // Find max in dp
        int ans = dp[size][0];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                if (dp[i][j] > ans) {
                    ans = dp[i][j];
                }
            }
        }

        return new OptimalScrollSolution(safesDiscovered, ans);
    }

}
