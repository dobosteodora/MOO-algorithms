package NSGAIISolver;

import start.GUI;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author  Debabrata Acharya <debabrata.acharya@icloud.com> -> only the skeleton code
 * @version 2.0
 * @since   2.0
 */
public class Chromosome {

    private List<Chromosome> dominatedChromosomes = new ArrayList<Chromosome>();

    private final List<Double> objectiveValues = new ArrayList<Double>();

    private double crowdingDistance = 0;

    //array of true and false values -> in the String representation are only 0s and 1s
    private final Allele[] geneticCode;

    //by how many solution this chromosome is dominated
    private int dominationCount = 0;

    //array with the assignments
    private int[] fitness = new int[GUI.students.size()];


    //the number of the Pareto Front to which the chromosome belongs
    private int rank;
    
    private Chromosome(final Chromosome chromosome) {
        
        this.geneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];
        
        for(int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
            this.geneticCode[i] = new Allele(chromosome.getGeneticCode()[i].getGene());

        for(int i = 0; i < Configuration.objectives.size(); i++)
            this.objectiveValues.add(chromosome.getObjectiveValues().get(i));

        this.fitness = ServiceNSGAII.calculateFitness(this.geneticCode);
    }
    
    public Chromosome(final Allele[] geneticCode) {
        
        this.geneticCode = geneticCode;
        this.fitness = ServiceNSGAII.calculateFitness(this.geneticCode);


        
        for(int index = 0; index < Configuration.objectives.size(); index++) {
            this.objectiveValues.add(index, Configuration.objectives.get(index).getObjectiveValue(this.fitness));
            //System.out.println(Configuration.objectives.get(index).getObjectiveValue(this.fitness));
        }
    }

    //this constructor is used when we create the first population that satisfies the constraints ->
    //we calculate the fitness only when we found a solution that satisfies the constraints
    // (after we apply the Mac, iDev, females, team size and student-exactly-one-team constraints)
    public Chromosome(final Allele[] geneticCode, boolean noFitness) {

        this.geneticCode = geneticCode;

        for(int i = 0; i < fitness.length; i++)
            this.fitness[i] = -1;
    }

    
    public Chromosome getCopy() {
        return new Chromosome(this);
    }
    
    public void setDominatedChromosome(final Chromosome chromosome) {
        this.dominatedChromosomes.add(chromosome);
    }
    
    public void incrementDominationCount(int incrementValue) {
        this.dominationCount += incrementValue;
    }

    public Allele[] getGeneticCode() {
        return geneticCode;
    }

    public List<Double> getObjectiveValues() {
        return objectiveValues;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public int getDominationCount() {
        return dominationCount;
    }

    public List<Chromosome> getDominatedChromosomes() {
        return dominatedChromosomes;
    }

    public int[] getFitness(){
        return fitness;
    }

    @Override
    public String toString() {
        return null;
    }
}
