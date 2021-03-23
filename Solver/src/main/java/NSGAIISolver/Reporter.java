package NSGAIISolver;

import start.GUI;

import static java.lang.System.exit;

public class Reporter {

    private Reporter() {}

    public static void reportAlgorithmStart() {

        System.out.println("\n\n=============================================================");
        System.out.println("RUNNING ALGORITHM");
        System.out.println("=============================================================\n\nDefine global constraints: ");
    }

    public static void reportGeneration(int generation) {
        
        System.out.println("\n\n=============================================================");
        System.out.println("GENERATION : " + generation);
        System.out.println("=============================================================\n\n");
    }

    public static void reportGeneralConstraintMinStudents() {

        System.out.println("\n\n=============================================================");
        System.out.println("Minimum number of students: ");
        System.out.println("There are " + GUI.students.size() + " students and " + GUI.teams.size() +
                " teams." + " The maximum possible value is " +
                               + GUI.students.size() / GUI.teams.size() + ". ");
        System.out.println("Value: ");
        System.out.println("=============================================================\n\n");
    }

    public static void reportGeneralConstraintMaxStudents() {

        System.out.println("\n\n=============================================================");
        System.out.println("Maximum number of students: ");
        System.out.println("=============================================================\n\n");
    }

    public static void reportGeneralConstraintMinMacs() {

        System.out.println("\n\n=============================================================");
        System.out.println("Minimum number of Macs: ");
        System.out.println("There are " + ServiceNSGAII.getStudentsWithMac(GUI.students).size() + " students with Macs. "
                + " The maximum possible value is " +
                + ServiceNSGAII.getStudentsWithMac(GUI.students).size() / GUI.teams.size() + ". ");
        System.out.println("=============================================================\n\n");
    }

    public static void reportGeneralConstraintMiniDev() {

        System.out.println("\n\n=============================================================");
        System.out.println("Minimum number of iDevices: ");
        System.out.println("There are " + ServiceNSGAII.getStudentsWithIDevice(GUI.students).size() + " students with iDevices. "
                + " The maximum possible value is " +
                + ServiceNSGAII.getStudentsWithIDevice(GUI.students).size() / GUI.teams.size() + ". ");
        System.out.println("=============================================================\n\n");
    }

    public static void reportGeneralConstraintMinFemales() {

        System.out.println("\n\n=============================================================");
        System.out.println("Minimum number of female students: ");
        System.out.println("There are " + ServiceNSGAII.getFemaleStudents(GUI.students).size() + " female students. "
                + " The maximum possible value is " +
                + ServiceNSGAII.getFemaleStudents(GUI.students).size() / GUI.teams.size() + ". ");
        System.out.println("=============================================================\n\n");
    }

    public static void reportIOException() {
        System.out.println("\n\nInput / Output Exception caught. Program will now exit!\n\n");
        exit(-1);
    }
}
