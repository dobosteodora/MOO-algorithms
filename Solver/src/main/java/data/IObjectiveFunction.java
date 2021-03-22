package data;

public interface IObjectiveFunction {

    String objectiveFunctionTitle();

    double getObjectiveValue(int[] fitness);
}
