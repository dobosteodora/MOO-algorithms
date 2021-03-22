/*
 * This code / file / algorithm is completely free to use and modify as necessary.
 * Any attribution is welcome and highly appriciated.
 */
package NSGAIISolver;

import start.GUI;
import data.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class ServiceNSGAII {

    private ServiceNSGAII() {
    }

    //some helper functions

    public static int countMacs(Chromosome chromosome, int teamIndex) {
        int result = 0;

        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[GUI.teams.size() * i + teamIndex].getGene()) {
                if (GUI.students.get(i).isHasMac()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int countIDevice(Chromosome chromosome, int teamIndex) {
        int result = 0;

        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[GUI.teams.size() * i + teamIndex].getGene()) {
                if (GUI.students.get(i).isHasIDevice()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int countFemales(Chromosome chromosome, int teamIndex) {
        int result = 0;

        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[GUI.teams.size() * i + teamIndex].getGene()) {
                if (GUI.students.get(i).isFemale()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int countStudentsInTeam(Chromosome chromosome, int teamIndex) {
        int result = 0;

        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[GUI.teams.size() * i + teamIndex].getGene()) {
                result++;
            }
        }
        return result;
    }

    public static int countExperiencedStudents(List<Student> students) {
        int result = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getInstructorRating() == 1) {
                result++;
            }
        }
        return result;
    }

    public static int countAdvancedStudents(List<Student> students) {
        int result = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getInstructorRating() == 2) {
                result++;
            }
        }
        return result;
    }

    public static int countNormalStudents(List<Student> students) {
        int result = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getInstructorRating() == 3) {
                result++;
            }
        }
        return result;
    }

    public static int countNoviceStudents(List<Student> students) {
        int result = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getInstructorRating() == 4) {
                result++;
            }
        }
        return result;
    }

    public static boolean constraintMacSatisfied(Chromosome chromosome, int teamIndex) {
        return countMacs(chromosome, teamIndex) >= GUI.globalConstraint.getMinMac();
    }

    public static boolean constraintIDevSatisfied(Chromosome chromosome, int teamIndex) {
        return countIDevice(chromosome, teamIndex) >= GUI.globalConstraint.getMinIDev();
    }

    public static boolean constraintFemaleSatisfied(Chromosome chromosome, int teamIndex) {
        return countFemales(chromosome, teamIndex) >= GUI.globalConstraint.getMinFemales();
    }

    public static boolean constraintTeamSizeSatisfied(Chromosome chromosome, int teamIndex) {
        return countStudentsInTeam(chromosome, teamIndex) >= GUI.globalConstraint.getMinSize() && countStudentsInTeam(chromosome, teamIndex) <= GUI.globalConstraint.getMaxSize();
    }

    public static boolean constraintExactlyOneTeamForEachStudent(Chromosome chromosome) {

        for (int i = 0; i < GUI.students.size(); i++) {
            boolean assigned = false;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (chromosome.getGeneticCode()[i * GUI.teams.size() + t].getGene()) {
                    if (!assigned) {
                        assigned = true;
                    } else {
                        return false;//student is assigned to more than one team
                    }
                }
            }
            if (!assigned) {
                return false;
            }
        }
        return true;
    }

    public static boolean constraintsSatisfiedByTeam(Chromosome chromosome, int teamIndex) {
        return constraintMacSatisfied(chromosome, teamIndex) && constraintIDevSatisfied(chromosome, teamIndex) && constraintFemaleSatisfied(chromosome, teamIndex) && constraintTeamSizeSatisfied(chromosome, teamIndex);
    }

    public static boolean teamConstraintsSatisfied(Chromosome chromosome) {

        for (int t = 0; t < GUI.teams.size(); t++) {
            if (!constraintsSatisfiedByTeam(chromosome, t)) {
                return false;
            }
        }
        return true;
    }

    public static boolean constraintsSatisfied(Chromosome chromosome) {
        return teamConstraintsSatisfied(chromosome) && constraintExactlyOneTeamForEachStudent(chromosome);
    }


    public static List<Student> getStudentsWithMac(List<Student> students) {
        List<Student> result = new ArrayList<>();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isHasMac()) {
                result.add(students.get(i));
            }
        }
        return result;
    }

    public static List<Student> getStudentsWithIDevice(List<Student> students) {
        List<Student> result = new ArrayList<Student>();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isHasIDevice()) {
                result.add(students.get(i));
            }
        }
        return result;
    }

    public static List<Student> getFemaleStudents(List<Student> students) {
        List<Student> result = new ArrayList<Student>();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isFemale()) {
                result.add(students.get(i));
            }
        }
        return result;
    }

    //returns the team index to which the student is assigned or -1 if the student is not assigned
    public static int studentIsAssigned(Chromosome chromosome, int index) {
        int result = -1;
        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.teams.size(); i++) {
            if (allele[index * GUI.teams.size() + i].getGene()) {
                result = i;
            }
        }
        return result;
    }

    public static List<Integer> getStudentsAssignedToTeam(Chromosome chromosome, int index) {
        List<Integer> students = new ArrayList<Integer>();
        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[i * GUI.teams.size() + index].getGene()) {
                students.add(i);
            }
        }
        return students;
    }

    public static List<Integer> getUnassignedStudents(Chromosome chromosome) {
        List<Integer> students = new ArrayList<Integer>();
        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            boolean assigned = false;
            for (int j = 0; j < GUI.teams.size(); j++) {
                if (allele[GUI.teams.size() * i + j].getGene()) {
                    assigned = true;
                }
            }
            if (!assigned) {
                students.add(i);
            }
        }
        return students;
    }

    public static List<Student> getStudentsOfTeam(Chromosome chromosome, int teamIndex) {
        List<Student> result = new ArrayList<Student>();

        Allele[] allele = chromosome.getGeneticCode();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (allele[GUI.teams.size() * i + teamIndex].getGene()) {
                result.add(GUI.students.get(i));
            }
        }
        return result;
    }

    /**
     * @param geneticCode the genetic code whose fitness is to be calculated
     * @return the corresponding calculated fitness
     */

    //returns the assignment solution
    public static int[] calculateFitness(Allele[] geneticCode) {

        return decodeGeneticCode(geneticCode);
    }

    //sort the population based on rank
    public static void randomizedQuickSortForRank(final List<Chromosome> populace, final int head, final int tail) {

        if (head < tail) {

            int pivot = ServiceNSGAII.randomizedPartitionForRank(populace, head, tail);

            ServiceNSGAII.randomizedQuickSortForRank(populace, head, pivot - 1);
            ServiceNSGAII.randomizedQuickSortForRank(populace, pivot + 1, tail);
        }
    }

    public static void sortForCrowdingDistance(final List<Chromosome> populace, final int lastNonDominatedSetRank) {

        int rankStartIndex = -1;
        int rankEndIndex = -1;

        for (int i = 0; i < populace.size(); i++)
            if ((rankStartIndex < 0) && (populace.get(i).getRank() == lastNonDominatedSetRank)) rankStartIndex = i;
            else if ((rankStartIndex >= 0) && (populace.get(i).getRank() == lastNonDominatedSetRank)) rankEndIndex = i;

        ServiceNSGAII.randomizedQuickSortForCrowdingDistance(populace, rankStartIndex, rankEndIndex);
    }

    public static void sortAgainstObjective(final Chromosome[] populace, int objectiveIndex) {
        ServiceNSGAII.randomizedQuickSortAgainstObjective(populace, 0, populace.length - 1, objectiveIndex);
    }

    public static double selectMaximumObjectiveValue(final Chromosome[] populace, int objectiveIndex) {

        double result = populace[0].getObjectiveValues().get(objectiveIndex);

        for (Chromosome chromosome : populace)
            if (chromosome.getObjectiveValues().get(objectiveIndex) > result)
                result = chromosome.getObjectiveValues().get(objectiveIndex);

        return result;
    }

    public static double selectMinimumObjectiveValue(final Chromosome[] populace, int objectiveIndex) {

        double result = populace[0].getObjectiveValues().get(objectiveIndex);

        for (Chromosome chromosome : populace)
            if (chromosome.getObjectiveValues().get(objectiveIndex) < result)
                result = chromosome.getObjectiveValues().get(objectiveIndex);

        return result;
    }

    public static void printGenerationInformation(List<Chromosome> solutions) {
        int index = 1;
        for (Chromosome chromosome : solutions) {

            System.out.println("SOLUTION " + index);

            System.out.println(Configuration.objectives.get(0).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(0) + " | "
                    + Configuration.objectives.get(1).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(1));

            System.out.println();

            //student information
            for (int i = 0; i < GUI.students.size(); i++) {
                System.out.println(GUI.students.get(i).getFirstName() + " " + GUI.students.get(i).getLastName()
                        + " is assigned to team " + GUI.teams.get(ServiceNSGAII.studentIsAssigned(chromosome, i)).getName());
            }
            System.out.println();

            //team information
            for (int i = 0; i < GUI.teams.size(); i++) {
                System.out.println("Team " + GUI.teams.get(i).getName() + " - assigned students: ");
                List<Student> assignedStudents = ServiceNSGAII.getStudentsOfTeam(chromosome, i);
                for (Student student : assignedStudents) {
                    System.out.print(student.getLastName() + " | ");
                }
                System.out.println();
                System.out.println();
            }
            index++;
        }
    }

    public static void printGenerationInformation(Set<Chromosome> solutions) {
        int index = 1;
        for (Chromosome chromosome : solutions) {

            System.out.println("SOLUTION " + index);

            System.out.println(Configuration.objectives.get(0).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(0) + " | "
                    + Configuration.objectives.get(1).objectiveFunctionTitle() + " : " + chromosome.getObjectiveValues().get(1));

            System.out.println();

            //student information
            for (int i = 0; i < GUI.students.size(); i++) {
                System.out.println(GUI.students.get(i).getFirstName() + " " + GUI.students.get(i).getLastName()
                        + ": " + GUI.teams.get(ServiceNSGAII.studentIsAssigned(chromosome, i)).getName());
            }
            System.out.println();

            //team information
            for (int i = 0; i < GUI.teams.size(); i++) {
                System.out.println("Team " + GUI.teams.get(i).getName() + " - assigned students: ");
                List<Student> assignedStudents = ServiceNSGAII.getStudentsOfTeam(chromosome, i);
                for (Student student : assignedStudents) {
                    System.out.print(student.getLastName() + " | ");
                }
                System.out.println();
                System.out.println();
            }
            index++;
        }
    }


    //here should be the mapping between each student and the assigned team
    public static int[] decodeGeneticCode(final Allele[] geneticCode) {

        int[] assignment = new int[GUI.students.size()];

        for (int i = 0; i < assignment.length; i++) {
            assignment[i] = -1;
        }

        int index = 0;//current student
        String binaryString = "";

        String assignmentStudent = "";

        int count = GUI.teams.size();

        for (Allele bit : geneticCode) binaryString += bit.getGene() ? "1" : "0";

        for (int i = 0; i < binaryString.length(); i++) {
            assignmentStudent += binaryString.charAt(i);
            count--;
            if (count == 0) {
                for (int j = 0; j < assignmentStudent.length(); j++) {
                    if (assignmentStudent.charAt(j) == '1') {
                        assignment[index] = j;
                    }
                }
                count = GUI.teams.size();
                index++;
                assignmentStudent = "";
            }
        }

        //check
        boolean ok = false;
        for (int i = 0; i < assignment.length; i++) {
            if (assignment[i] != 0) {
                ok = true;
            }
        }
        if (!ok) {
            System.out.println("Error in decode function");
            exit(-1);
        }

        return assignment;
    }

    private static int randomizedPartitionForRank(final List<Chromosome> populace, final int head, final int tail) {

        ServiceNSGAII.swapForRank(populace, head, ThreadLocalRandom.current().nextInt(head, tail + 1));

        return ServiceNSGAII.partitionForRank(populace, head, tail);
    }

    private static void swapForRank(final List<Chromosome> populace, final int firstIndex, final int secondIndex) {

        Chromosome temporary = populace.get(firstIndex);

        populace.set(firstIndex, populace.get(secondIndex));
        populace.set(secondIndex, temporary);
    }

    private static int partitionForRank(final List<Chromosome> populace, final int head, final int tail) {

        int pivot = populace.get(tail).getRank();
        int pivotIndex = head;

        for (int j = head; j < tail; j++) {

            if (populace.get(j).getRank() <= pivot) {

                ServiceNSGAII.swapForRank(populace, pivotIndex, j);
                ++pivotIndex;
            }
        }

        ServiceNSGAII.swapForRank(populace, pivotIndex, tail);

        return pivotIndex;
    }

    private static void randomizedQuickSortForCrowdingDistance(final List<Chromosome> populace, final int head, final int tail) {

        if (head < tail) {

            int pivot = ServiceNSGAII.randomizedPartitionForCrowdingDistance(populace, head, tail);

            ServiceNSGAII.randomizedQuickSortForCrowdingDistance(populace, head, pivot - 1);
            ServiceNSGAII.randomizedQuickSortForCrowdingDistance(populace, pivot + 1, tail);
        }
    }

    private static int randomizedPartitionForCrowdingDistance(final List<Chromosome> populace, final int head, final int tail) {

        ServiceNSGAII.swapForCrowdingDistance(populace, head, ThreadLocalRandom.current().nextInt(head, tail + 1));

        return ServiceNSGAII.partitionForCrowdingDistance(populace, head, tail);
    }

    private static void swapForCrowdingDistance(final List<Chromosome> populace, final int firstIndex, final int secondIndex) {

        Chromosome temporary = populace.get(firstIndex);

        populace.set(firstIndex, populace.get(secondIndex));
        populace.set(secondIndex, temporary);
    }

    private static int partitionForCrowdingDistance(final List<Chromosome> populace, final int head, final int tail) {

        double pivot = populace.get(tail).getCrowdingDistance();
        int pivotIndex = head;

        for (int j = head; j < tail; j++) {

            if (populace.get(j).getCrowdingDistance() >= pivot) {

                ServiceNSGAII.swapForRank(populace, pivotIndex, j);
                ++pivotIndex;
            }
        }

        ServiceNSGAII.swapForRank(populace, pivotIndex, tail);

        return pivotIndex;
    }

    private static void randomizedQuickSortAgainstObjective(final Chromosome[] populace, final int head, final int tail, final int objectiveIndex) {

        if (head < tail) {

            int pivot = ServiceNSGAII.randomizedPartitionAgainstObjective(populace, head, tail, objectiveIndex);

            ServiceNSGAII.randomizedQuickSortAgainstObjective(populace, head, pivot - 1, objectiveIndex);
            ServiceNSGAII.randomizedQuickSortAgainstObjective(populace, pivot + 1, tail, objectiveIndex);
        }
    }

    private static int randomizedPartitionAgainstObjective(final Chromosome[] populace, final int head, final int tail, final int objectiveIndex) {

        ServiceNSGAII.swapAgainstObjective(populace, head, ThreadLocalRandom.current().nextInt(head, tail + 1));

        return ServiceNSGAII.partitionAgainstObjective(populace, head, tail, objectiveIndex);
    }

    private static void swapAgainstObjective(final Chromosome[] populace, final int firstIndex, final int secondIndex) {

        Chromosome temporary = populace[firstIndex];
        populace[firstIndex] = populace[secondIndex];
        populace[secondIndex] = temporary;
    }

    private static int partitionAgainstObjective(final Chromosome[] populace, final int head, final int tail, final int objectiveIndex) {

        double pivot = populace[tail].getObjectiveValues().get(objectiveIndex);
        int pivotIndex = head;

        for (int j = head; j < tail; j++) {

            if (populace[j].getObjectiveValues().get(objectiveIndex) <= pivot) {

                ServiceNSGAII.swapAgainstObjective(populace, pivotIndex, j);
                ++pivotIndex;
            }
        }

        ServiceNSGAII.swapAgainstObjective(populace, pivotIndex, tail);

        return pivotIndex;
    }

    public static Set<Chromosome> computeParetoSolution(Set<Chromosome> solutions) {
        Set<Chromosome> result = new HashSet<Chromosome>();

        for (Chromosome c1 : solutions) {
            boolean dominant = true;
            for (Chromosome c2 : solutions) {
                if (!Synthesis.isGeneticCodeSimilar(c1.getGeneticCode(), c2.getGeneticCode())) {
                    if (!NSGAIIGenetics.isDominant(c1, c2)) {
                        dominant = false;
                    }
                }
            }
            if (dominant) {
                result.add(c1);
            }
        }

        return result;
    }
}
