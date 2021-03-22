/*
 * This code / file / algorithm is completely free to use and modify as necessary.
 * Any attribution is welcome and highly appriciated.
 */
package NSGAIISolver;

public class Allele {
    
    private final boolean gene;
    
    public Allele(final boolean gene) {
        this.gene = gene;
    }
    
    public boolean getGene() {
        return gene;
    }
    
    @Override
    public String toString() {
        return (this.gene ? "1" : "0");
    }
}
