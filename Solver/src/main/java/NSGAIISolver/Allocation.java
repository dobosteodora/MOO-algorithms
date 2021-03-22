package NSGAIISolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Allocation {

    private static Set<Chromosome> solutions = new HashSet();

    public void prepareMultipleDataset(final Population population, final int datasetIndex, final String key) {
        this.checkData(population);
    }

    private void checkData(final Population population) {

        List<Chromosome> chromosomes = population.getPopulace();

        for (Chromosome chromosome : chromosomes) {
            if (ServiceNSGAII.constraintsSatisfied(chromosome)) {
                boolean newChromosome = true;
                for (Chromosome sol : solutions) {
                    if (Synthesis.isGeneticCodeSimilar(sol.getGeneticCode(), chromosome.getGeneticCode())) {
                        newChromosome = false;
                    }
                }
                if (newChromosome) {
                    solutions.add(chromosome);

                }

                System.out.println(Configuration.objectives.get(0).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(0) + " | "
                        + Configuration.objectives.get(1).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(1));

            } else {
                System.out.println("Constraints are not satisfied for this solution.");
            }
        }
    }

    public static Set<Chromosome> computePareto(Set<Chromosome> solution) {
        Set<Chromosome> paretoSolutions = new HashSet();

        for (Chromosome c1 : solution){
            boolean ok = true;

            if(!ServiceNSGAII.constraintsSatisfied(c1)){
                ok = false;
            }

            for(Chromosome c2: solution){
                if(!Synthesis.isGeneticCodeSimilar(c1.getGeneticCode(), c2.getGeneticCode())){
                    if(c1.getObjectiveValues().get(0) > c2.getObjectiveValues().get(0) &&
                            c1.getObjectiveValues().get(1) > c2.getObjectiveValues().get(1)){
                        ok = false;
                    }
                }
            }
            if(ok){
                paretoSolutions.add(c1);
            }
        }

        return paretoSolutions;
    }

    public static Set<Chromosome> getSolutions() {
        return solutions;
    }

}
