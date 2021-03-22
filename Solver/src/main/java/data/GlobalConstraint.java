package data;

public class GlobalConstraint {

    private int minSize;
    private int maxSize;
    private int minMac;
    private int minIDev;
    private int minFemales;

    public GlobalConstraint(int minSize, int maxSize, int minMac, int minIDev, int minFemales){
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minMac = minMac;
        this.minIDev = minIDev;
        this.minFemales = minFemales;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinMac() {
        return minMac;
    }

    public int getMinIDev() {
        return minIDev;
    }

    public int getMinFemales() {
        return minFemales;
    }

}
