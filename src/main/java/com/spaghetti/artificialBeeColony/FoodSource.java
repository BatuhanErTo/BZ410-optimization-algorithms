package com.spaghetti.artificialBeeColony;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class FoodSource {

    private double[] solutionString;
    private double objectValue = 0;
    private double fitness = 0;
    private int trialCounter = 0;

    public FoodSource(int dimension) {
        this.solutionString = new double[dimension];
    }

    public FoodSource deepCopy() {
        FoodSource f1 = new FoodSource(this.solutionString.length);
        f1.objectValue = this.objectValue;
        f1.fitness = this.fitness;
        f1.trialCounter = this.trialCounter;
        System.arraycopy(this.solutionString, 0, f1.solutionString, 0, this.solutionString.length);
        return f1;
    }

    public void calculateFitness() {
        double sum = 0;
        for (double value : solutionString) {
            sum += value * value; // Example fitness calculation (to be replaced with actual problem logic)
        }
        this.objectValue = sum;

        double result = 0;
        if (this.objectValue >= 0)
            result = 1 / (1 + this.objectValue);
        else
            result = 1 + Math.abs(this.objectValue);
        this.fitness = result;
    }

    public void sendBee(FoodSource[] fSources,
                        int i,
                        Random random,
                        double LB,
                        double UB) {
        int k = random.nextInt(fSources.length);
        while (k == i) {
            k = random.nextInt(fSources.length);
        }

        int j = random.nextInt(solutionString.length);

        this.solutionString[j] = fSources[i].solutionString[j]
                + (-1 + 2 * random.nextDouble())
                * (fSources[i].solutionString[j] - fSources[k].solutionString[j]);

        if (this.solutionString[j] < LB)
            this.solutionString[j] = LB;
        if (this.solutionString[j] > UB)
            this.solutionString[j] = UB;
    }
}
