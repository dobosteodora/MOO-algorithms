package NSGAIISolver;

import start.GUI;
import data.IObjectiveFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private Configuration() {}
    
    public static int POPULATION_SIZE = 15;
    public static int GENERATIONS = 100;
    public static int CHROMOSOME_LENGTH = GUI.students.size() * GUI.teams.size();// #students * #teams (20*5) 60 * 10

    public static final float CROSSOVER_PROBABILITY = 0.75f;
    public static final float MUTATION_PROBABILITY = 0.5f;

    public static List<IObjectiveFunction> objectives = null;
    
    public static void configure() {
        
        Configuration.buildObjectives();

        System.out.println();
        System.out.println("DATA NUMBERS: ");
        System.out.println();
        System.out.println("Number of students: " + GUI.students.size());
        System.out.println("Number of teams: " + GUI.teams.size());

        System.out.println("iDevice: " + ServiceNSGAII.getStudentsWithIDevice(GUI.students).size());
        System.out.println("Mac: " + ServiceNSGAII.getStudentsWithMac(GUI.students).size());
        System.out.println("Females : " + ServiceNSGAII.getFemaleStudents(GUI.students).size());
        System.out.println("Experienced Students: " + ServiceNSGAII.countExperiencedStudents(GUI.students));
        System.out.println("Advanced Students: " + ServiceNSGAII.countAdvancedStudents(GUI.students));
        System.out.println("Normal Students: " + ServiceNSGAII.countNormalStudents(GUI.students));
        System.out.println("Novice Students: " + ServiceNSGAII.countNoviceStudents(GUI.students));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        Reporter.reportAlgorithmStart();

        Reporter.reportGeneralConstraintMinStudents();

        int minStudents = 0;
        int maxStudents = 300;
        int minMac = 0;
        int miniDev = 0;
        int minFemales = 0;

        try{
            String minStudentsString = bufferedReader.readLine();

            try {
                minStudents = Integer.parseInt(minStudentsString.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

        } catch(IOException e) {
            Reporter.reportIOException();
        }


        Reporter.reportGeneralConstraintMaxStudents();

        try{
            String maxStudentsString = bufferedReader.readLine();

            try {
                maxStudents = Integer.parseInt(maxStudentsString.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

        } catch(IOException e) {
            Reporter.reportIOException();
        }


        Reporter.reportGeneralConstraintMinMacs();

        try{
            String minMacString = bufferedReader.readLine();

            try {
                minMac = Integer.parseInt(minMacString.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

        } catch(IOException e) {
            Reporter.reportIOException();
        }


        Reporter.reportGeneralConstraintMiniDev();

        try{
            String miniDevString = bufferedReader.readLine();

            try {
                miniDev = Integer.parseInt(miniDevString.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

        } catch(IOException e) {
            Reporter.reportIOException();
        }



        Reporter.reportGeneralConstraintMinFemales();

        try{
            String minFemalesString = bufferedReader.readLine();

            try {
                minFemales = Integer.parseInt(minFemalesString.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

        } catch(IOException e) {
            Reporter.reportIOException();
        }

        GUI.setGlobalConstraint(minStudents, maxStudents, minMac, miniDev, minFemales);


    }

    public static void setObjectives(List<IObjectiveFunction> objectives) {

       Configuration.objectives = objectives;
    }

    /**
     * this method sets the objective functions over which the algorithm is to operate.
     * it is a list of IObjectionFunction objects.
     */
    public static void buildObjectives() {
        
        List<IObjectiveFunction> newObjectives = new ArrayList<IObjectiveFunction>();
        
        newObjectives.add(0, new PriorityNSGAII());
        newObjectives.add(1, new ExperienceNSGAII());
        
        Configuration.setObjectives(newObjectives);
    }
}
