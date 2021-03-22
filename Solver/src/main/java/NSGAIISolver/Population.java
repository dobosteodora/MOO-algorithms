/*
 * This code / file / algorithm is completely free to use and modify as necessary.
 * Any attribution is welcome and highly appriciated.
 */
package NSGAIISolver;

import java.util.List;

//the set of solution generated in one iteration/a generation
public class Population {
    
    private final List<Chromosome> populace;
    
    public Population(final List<Chromosome> populace) {
        this.populace = populace;
    }

    public List<Chromosome> getPopulace() {
        return populace;
    }
}
