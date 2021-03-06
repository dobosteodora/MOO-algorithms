package NSGAIISolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Debabrata Acharya <debabrata.acharya@icloud.com> -> only the skeleton code
 * @version 2.0
 * @since 2.0
 */
public class NSGAIIGenetics {

    private NSGAIIGenetics() {
    }

    private static final int DOMINANT = 1;
    private static final int INFERIOR = 2;
    private static final int NON_DOMINATED = 3;

    public static Population preparePopulation(final Population population) {

        Chromosome[] populace = population.getPopulace().toArray(new Chromosome[population.getPopulace().size()]);

        fastNonDominatedSort(populace);

        crowdingDistanceAssignment(populace);

        ServiceNSGAII.randomizedQuickSortForRank(population.getPopulace(), 0, populace.length - 1);

        return population;
    }

    public static Population getChildFromCombinedPopulation(final Population combinedPopulation) {

        int lastNonDominatedSetRank = combinedPopulation.getPopulace().get(Configuration.POPULATION_SIZE - 1).getRank();
        List<Chromosome> populace = new ArrayList<Chromosome>();

        ServiceNSGAII.sortForCrowdingDistance(combinedPopulation.getPopulace(), lastNonDominatedSetRank);

        for (int i = 0; i < Configuration.POPULATION_SIZE; i++) {
            populace.add(combinedPopulation.getPopulace().get(i));
        }


        return new Population(populace);
    }

    private static void fastNonDominatedSort(final Chromosome[] populace) {

        for (int i = 0; i < populace.length - 1; i++) {

            for (int j = i + 1; j < populace.length; j++) {

                switch (NSGAIIGenetics.dominates(populace[i], populace[j])) {

                    case NSGAIIGenetics.DOMINANT:

                        populace[i].setDominatedChromosome(populace[j]);
                        populace[j].incrementDominationCount(1);

                        break;

                    case NSGAIIGenetics.INFERIOR:

                        populace[i].incrementDominationCount(1);
                        populace[j].setDominatedChromosome(populace[i]);

                        break;

                    case NSGAIIGenetics.NON_DOMINATED:
                        break;
                }
            }

            if (populace[i].getDominationCount() == 0) populace[i].setRank(1);
        }

        if (populace[populace.length - 1].getDominationCount() == 0) populace[populace.length - 1].setRank(1);

        for (int i = 0; i < populace.length; i++) {

            for (Chromosome chromosome : populace[i].getDominatedChromosomes()) {

                chromosome.incrementDominationCount(-1);

                if (chromosome.getDominationCount() == 0) chromosome.setRank(populace[i].getRank() + 1);
            }
        }
    }

    private static void crowdingDistanceAssignment(final Chromosome[] nondominatedChromosomes) {

        int size = nondominatedChromosomes.length;

        for (int i = 0; i < Configuration.objectives.size(); i++) {

            ServiceNSGAII.sortAgainstObjective(nondominatedChromosomes, i);

            //assign infinity to boundary points (from the paper description)
            nondominatedChromosomes[0].setCrowdingDistance(Double.MAX_VALUE);
            nondominatedChromosomes[size - 1].setCrowdingDistance(Double.MAX_VALUE);

            double maxObjectiveValue = ServiceNSGAII.selectMaximumObjectiveValue(nondominatedChromosomes, i);
            double minObjectiveValue = ServiceNSGAII.selectMinimumObjectiveValue(nondominatedChromosomes, i);

            for (int j = 1; j < size - 1; j++)
                if (nondominatedChromosomes[j].getCrowdingDistance() < Double.MAX_VALUE)
                    nondominatedChromosomes[j].setCrowdingDistance(
                            nondominatedChromosomes[j].getCrowdingDistance() + (
                                    (nondominatedChromosomes[j + 1].getObjectiveValues().get(i) - nondominatedChromosomes[j - 1].getObjectiveValues().get(i)) / (maxObjectiveValue - minObjectiveValue)
                            )
                    );
        }
    }

    private static int dominates(final Chromosome chromosome1, final Chromosome chromosome2) {

        if (NSGAIIGenetics.isDominant(chromosome1, chromosome2)) return NSGAIIGenetics.DOMINANT;
        else if (NSGAIIGenetics.isDominant(chromosome2, chromosome1)) return NSGAIIGenetics.INFERIOR;
        else return NSGAIIGenetics.NON_DOMINATED;
    }

    //assume minimization for all objectives
    public static boolean isDominant(final Chromosome chromosome1, final Chromosome chromosome2) {

        if(chromosome1.getObjectiveValues().get(0) < chromosome2.getObjectiveValues().get(0) && chromosome1.getObjectiveValues().get(1) <= chromosome2.getObjectiveValues().get(1)
        ||chromosome1.getObjectiveValues().get(1) < chromosome2.getObjectiveValues().get(1) && chromosome1.getObjectiveValues().get(0) <= chromosome2.getObjectiveValues().get(0)){

            return true;
        }
        return false;
    }
}
