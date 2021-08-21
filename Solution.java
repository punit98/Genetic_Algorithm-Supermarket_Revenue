// import java.util.*;
import java.io.FileWriter;
import java.io.*;
import java.util.Random;
public class Solution{
    public static void main(String[] args) throws IOException{
        int numberOfGoods = 20;
        double bestRevenue = 0;
        
        int maxIter = 2000;

        double initialRevenue = 0;
        double[] initialPrices = new double[numberOfGoods];

        double currentRevenue1 = 0;
        double bestRevenue1 = 0;

        double currentRevenue2 = 0;
        double bestRevenue2 = 0;

        double temp = 0;

        int generation = 1;
        int[] generations = new int[maxIter];

        double[] bestRevenuesList = new double[maxIter];
        double[] bestPricesList = new double[20];

        FileWriter writer = new FileWriter("evolutionaryAlgoOutput.csv");

        writer.write("Generation"+","+"Revenue");


        Random rng = new Random(0);

        PricingProblem f = PricingProblem.courseworkInstance();

        double[] currentPrices1 = new double[numberOfGoods];
        double[] newPrices1 = new double[numberOfGoods];
        double[] bestPrices1 = new double[numberOfGoods];

        double[] currentPrices2 = new double[numberOfGoods];
        double[] newPrices2 = new double[numberOfGoods];
        double[] bestPrices2 = new double[numberOfGoods];

        //fill up arrays with the same initial solutions
        for (int i = 0; i < numberOfGoods; i++){
            currentPrices1[i] = rng.nextDouble() * 10;
            currentPrices2[i] = currentPrices1[i];
            initialPrices[i] = currentPrices1[i];
        }
        // reverse the second array **

        for(int i = 0; i < currentPrices2.length / 2; i++){
            temp = currentPrices2[i];
            currentPrices2[i] = currentPrices1[currentPrices1.length - i - 1];
            currentPrices2[currentPrices1.length - i - 1] = temp;
        }

        initialRevenue = f.evaluate(currentPrices1);

        //get revenue and best revenue for the first initial solution
        bestRevenue1 = f.evaluate(currentPrices1);
        if(bestRevenue1 >= initialRevenue){
            bestRevenue1 = currentRevenue1;
            for(int i = 0; i < numberOfGoods; i++){
                bestPrices1[i] = currentPrices1[i];
                newPrices1[i] = currentPrices1[i];
            }
        }
        
        //get revenue and best revenue for second initial solution
        bestRevenue2 = f.evaluate(currentPrices2);
        if(bestRevenue2 > initialRevenue){
            bestRevenue2 = currentRevenue2;
            for(int i = 0; i < numberOfGoods; i++){
                bestPrices2[i] = currentPrices2[i];
                newPrices2[i] = currentPrices2[i];
            }
        }

        newPrices1 = getNextGeneration(bestPrices1, bestPrices2, bestRevenue1, f);
        newPrices2 = getNextGeneration(bestPrices2, bestPrices1, bestRevenue2, f);
        writer.write("\n0"+","+initialRevenue);

        // iterate 100 times generating and comparing each generation
        for(int iter = 0; iter < maxIter; iter++){


            generations[iter] = generation;
            generation++;

            
            currentRevenue1 = f.evaluate(newPrices1);
            currentRevenue2 = f.evaluate(newPrices2);
            System.out.println("current Revenue 1: "+currentRevenue1);
            System.out.println("Current Revenue 2: "+currentRevenue2);

            //update best and current revenue1 and prices 1
            if(currentRevenue1 >= bestRevenue1){
                bestRevenue1 = currentRevenue1;
                bestRevenue = bestRevenue1;
                for(int i = 0; i < numberOfGoods; i++){
                    bestPrices1[i] = currentPrices1[i];
                    bestPricesList[i] = bestPrices1[i];
                }
                newPrices1 = getNextGeneration(bestPrices1, bestPrices2, bestRevenue1, f);
                bestRevenuesList[iter] = bestRevenue1;
                System.out.println("Best revenue so far: "+bestRevenue1);
            }

            //update best and current revenue2 and prices 2
            else if(currentRevenue2 >= bestRevenue2){
                bestRevenue2 = currentRevenue2;
                bestRevenue = bestRevenue2;
                for(int i = 0; i < numberOfGoods; i++){
                    bestPrices2[i] = currentPrices2[i];
                    bestPricesList[i] = bestPrices2[i];
                }
                newPrices2 = getNextGeneration(bestPrices2, bestPrices1, bestRevenue2, f);
                bestRevenuesList[iter] = bestRevenue2;
                System.out.println("Best revenue so far: "+bestRevenue2);
            }

            else{
                for(int i = 0; i < numberOfGoods; i++){
                    bestPrices1[i] = bestPrices1[i];
                    bestPrices2[i] = bestPrices2[i];
                    bestPricesList[i] = bestPricesList[i];
                }
                newPrices1 = getNextGeneration(bestPrices1, bestPrices2, bestRevenue1, f);
                newPrices2 = getNextGeneration(bestPrices2, bestPrices1, bestRevenue2, f);
                bestRevenuesList[iter] = bestRevenue;
                System.out.println("Best revenue so far: "+bestRevenue);
            }
            System.out.println("\n\n----------------------NextGeneration------------------\n\n");
            // writer.write("\n"+generations[iter]+","+bestRevenuesList[iter]);
        }
        for(int i = 1; i < maxIter; i++){
            writer.write("\n"+generations[i]+","+bestRevenuesList[i]);
        }
        writer.close();

        System.out.println("\nInitial Revenue was: "+initialRevenue+"\nInitial Prices were: \n");
        for(int i = 0; i < numberOfGoods; i++){
            System.out.print(initialPrices[i]+" ");
        }
        
        //print best revenue
        if(bestRevenue1 > bestRevenue2){
            System.out.println("\n\nBest Revenue: "+bestRevenue1+"\nBest Prices are:\n");
            for(int i = 0; i < numberOfGoods; i++){
                System.out.print(bestPrices1[i]+" ");
            }
        }
        else{
            System.out.println("\n\nBest Revenue: "+bestRevenue2+"\nBest Prices are:\n");
            for(int i = 0; i < numberOfGoods; i++){
                System.out.print(bestPrices2[i]+" ");
            }
        }
    }

    public static double[] getNextGeneration(double[] prices1, double[] prices2, double currRev, PricingProblem f){
        double[] crossedPrices1 = new double[prices1.length];
        double[] mutatedPrices1 = new double[prices1.length];

        double[] crossedPrices2 = new double[prices1.length];
        double[] mutatedPrices2 = new double[prices1.length];

        double[] selectedPrices = new double[prices1.length];
        // crossover
        crossedPrices1 = crossover(prices1, prices2, f);
        crossedPrices2 = crossover(prices2, prices1, f);
        // mutate
        mutatedPrices1 = mutate(crossedPrices1, crossedPrices2);
        mutatedPrices2 = mutate(crossedPrices2, crossedPrices1);
        // select
        selectedPrices = selection(mutatedPrices1, mutatedPrices2, currRev, f);
        return selectedPrices;
    }

    //crossing over the two passed arrays
    private static double[] crossover(double[] prices1, double[] prices2, PricingProblem f){
        double[] crossedPrices = new double[prices1.length];
        for(int i = 0; i < prices1.length; i++){
            crossedPrices[i] = prices1[i];
        }

        double rev1 = 0;
        double rev2 = 0;
        double temp = 0;
        //crossover code
        rev1 = f.evaluate(prices1);
        rev2 = f.evaluate(prices2);
        if(rev2 > rev1){
            //do the crossover from 2 to 1
            for(int i = (prices1.length / 5)+10; i < prices1.length; i++){
                temp = prices1[i];
                // System.out.print("prices1[i]: "+prices1[i]+" temp: "+temp);
                prices1[i] = prices2[i];
                prices2[i] = temp;
                // System.out.print("\n");
                crossedPrices[i] = prices1[i];
            }
        }
        else{
            for(int i = 0; i < prices1.length; i++){
                crossedPrices[i] = prices1[i];
            }
        }
        return crossedPrices;
    }

    //mutating the two passed arrays
    private static double[] mutate(double[] prices1, double[] prices2){
        double[] mutatedPrices = new double[prices1.length];
        // double temp = 0;
        Random rand = new Random();

        //mutation code
        for(int j = 0; j < prices1.length / 5; j++){
            int r1 = rand.nextInt(prices1.length / 2);
            int r2 = rand.nextInt(prices1.length / 2) + 10;
            // double rndm1 = rand.nextDouble() * 10;
            // double rndm2 = rand.nextDouble() *10;
            double mutationProb = rand.nextDouble();
            if(mutationProb > 0.85){
                prices1 = swap(prices1, r1, r2);
                // prices1[r1] = rndm1;
                // prices1[r2] = rndm2;
            }
            else{
                prices1[r1] = prices1[r1];
            }
        }
        
        for(int i = 0; i < prices1.length; i++){
            mutatedPrices[i] = prices1[i];
        }

        return mutatedPrices;
    }

    private static double[] swap(double[] prices, int r1, int r2){
        double temp = prices[r1];
        prices[r1] = prices[r2];
        prices[r2] = temp;
        return prices;

    }
    //selection of a particular population
    private static double[] selection(double[] prices1, double[] prices2, double bestRev, PricingProblem f){
        double[] selected = new double[prices1.length];

        if(f.evaluate(prices1) >= f.evaluate(prices2)){
            for(int i = 0; i < prices1.length; i++){
                selected[i] = prices1[i];
            }
        }
        else if(f.evaluate(prices1) <= f.evaluate(prices2)){
            for(int i = 0; i < prices2.length; i++){
                selected[i] = prices2[i];
            }
        }
        else{
            // for(int i = 0; i < prices2.length; i++){
            //     selected[i] = prices1[i];
            // }
            prices1 = getNextGeneration(prices1, prices2, bestRev, f);
            prices2 = getNextGeneration(prices2, prices1, bestRev, f);
        }
        return selected;
    } 
}
