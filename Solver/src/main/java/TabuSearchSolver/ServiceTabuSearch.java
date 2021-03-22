package TabuSearchSolver;

import start.GUI;
import data.Student;

import java.util.ArrayList;
import java.util.List;


public class ServiceTabuSearch {

    public static int countMacs(int[] assignments, int teamIndex) {
        int result = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            if (assignments[GUI.teams.size() * i + teamIndex] == 1) {
                if (GUI.students.get(i).isHasMac()) {
                    result++;
                }
            }
        }
        return result;
    }


    public static int countIDevice(int[] assignments, int teamIndex) {
        int result = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            if (assignments[GUI.teams.size() * i + teamIndex] == 1) {
                if (GUI.students.get(i).isHasIDevice()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int countFemales(int[] assignments, int teamIndex) {
        int result = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            if (assignments[GUI.teams.size() * i + teamIndex] == 1) {
                if (GUI.students.get(i).isFemale()) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int countStudentsInTeam(int[] assignments, int teamIndex) {
        int result = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            if (assignments[GUI.teams.size() * i + teamIndex] == 1) {
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

    public static boolean constraintMacSatisfied(int[] assignments, int teamIndex) {
        return countMacs(assignments, teamIndex) >= GUI.globalConstraint.getMinMac();
    }

    public static boolean constraintIDevSatisfied(int[] assignments, int teamIndex) {
        return countIDevice(assignments, teamIndex) >= GUI.globalConstraint.getMinIDev();
    }

    public static boolean constraintFemaleSatisfied(int[] assignments, int teamIndex) {
        return countFemales(assignments, teamIndex) >= GUI.globalConstraint.getMinFemales();
    }

    public static boolean constraintTeamSizeSatisfied(int[] assignments, int teamIndex) {
        return countStudentsInTeam(assignments, teamIndex) >= GUI.globalConstraint.getMinSize() && countStudentsInTeam(assignments, teamIndex) <= GUI.globalConstraint.getMaxSize();
    }

    public static boolean constraintExactlyOneTeamForEachStudent(int[] assignments) {

        for (int i = 0; i < GUI.students.size(); i++) {
            boolean assigned = false;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (assignments[i * GUI.teams.size() + t] == 1) {
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

    public static boolean constraintsSatisfiedByTeam(int[] assignments, int teamIndex) {
        return constraintMacSatisfied(assignments, teamIndex) && constraintIDevSatisfied(assignments, teamIndex)
                && constraintFemaleSatisfied(assignments, teamIndex) && constraintTeamSizeSatisfied(assignments, teamIndex);
    }

    public static boolean teamConstraintsSatisfied(int[] assignments) {

        for (int t = 0; t < GUI.teams.size(); t++) {
            if (!constraintsSatisfiedByTeam(assignments, t)) {
                return false;
            }
        }
        return true;
    }

    public static boolean constraintsSatisfied(int[] assignments) {
        return teamConstraintsSatisfied(assignments) && constraintExactlyOneTeamForEachStudent(assignments);
    }

    public static List<Student> getStudentsWithMac(List<Student> students) {
        List<Student> result = new ArrayList<Student>();

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
    public static int studentIsAssigned(int[] assignments, int index) {
        int result = -1;

        for (int i = 0; i < GUI.teams.size(); i++) {
            if (assignments[index * GUI.teams.size() + i] == 1) {
                result = i;
            }
        }
        return result;
    }

    public static List<Integer> getStudentsAssignedToTeam(int[] assignments, int index) {
        List<Integer> students = new ArrayList<Integer>();

        for (int i = 0; i < students.size(); i++) {
            if (assignments[i * GUI.teams.size() + index] == 1) {
                students.add(i);
            }
        }
        return students;
    }

    public static List<Integer> getUnassignedStudents(int[] assignments) {
        List<Integer> students = new ArrayList<Integer>();

        for (int i = 0; i < GUI.students.size(); i++) {
            boolean assigned = false;
            for (int j = 0; j < GUI.teams.size(); j++) {
                if (assignments[GUI.teams.size() * i + j] == 1) {
                    assigned = true;
                }
            }
            if (!assigned) {
                students.add(i);
            }
        }
        return students;
    }

    public static List<Student> getStudentsOfTeam(int[] assignments, int teamIndex) {
        List<Student> result = new ArrayList<Student>();

        for (int i = 0; i < GUI.students.size(); i++) {
            if (assignments[GUI.teams.size() * i + teamIndex] == 1) {
                result.add(GUI.students.get(i));
            }
        }
        return result;
    }

    //returns the assignment solution
    public static int[] calculateFitness(int[] assignments) {

        return decodeSolution(assignments);
    }


    //returns the mapping between each student and the assigned team
    public static int[] decodeSolution(final int[] assignments) {

        int[] finalAssignment = new int[GUI.students.size()];
        int[] copyAssignment = new int[GUI.students.size()];

        int[] oneStudent = new int[GUI.teams.size()];

        for (int i = 0; i < finalAssignment.length; i++) {
            finalAssignment[i] = -1;
            copyAssignment[i] = -1;
        }

        int index = 0;
        int currentStudent = 0;

        int helper = -1;

        for (int i = 0; i < assignments.length; i++) {
            if (index != GUI.teams.size()) {
                oneStudent[index] = assignments[i];
                index++;
            } else {
                index = 0;

                for (int j = 0; j < oneStudent.length; j++) {
                    if (oneStudent[j] == 1) {
                        finalAssignment[currentStudent] = j;
                    }
                }
                oneStudent[index] = assignments[i];
                index++;
                currentStudent++;
            }
        }
        //last student
        for (int j = 0; j < oneStudent.length; j++) {
            if (oneStudent[j] == 1) {
                finalAssignment[currentStudent] = j;
            }
        }

        boolean ok = true;
        int student = -1;

        for (int i = 0; i < finalAssignment.length; i++) {
            if (finalAssignment[i] == -1) {
                ok = false;
                student = i;
            }
        }

        if (!ok) {
            System.out.println("Error in decode function: student " + student + " was not assigned to any team.");
            return copyAssignment;
        }

        return finalAssignment;
    }


    public static int[] swapTeams(int[] assignmentVariables, int firstStudentIndex, int secondStudentIndex) {
        int[] result = new int[GUI.students.size() * GUI.teams.size()];
        System.arraycopy(assignmentVariables, 0, result, 0, result.length);

        int teamStudent1 = studentIsAssigned(assignmentVariables, firstStudentIndex);
        int teamStudent2 = studentIsAssigned(assignmentVariables, secondStudentIndex);

        result[firstStudentIndex * GUI.teams.size() + teamStudent1] = 0;
        result[firstStudentIndex * GUI.teams.size() + teamStudent2] = 1;
        result[secondStudentIndex * GUI.teams.size() + teamStudent2] = 0;
        result[secondStudentIndex * GUI.teams.size() + teamStudent1] = 1;

        return result;
    }

    public static boolean identicAssignments(int[] array1, int[] array2) {
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
}
