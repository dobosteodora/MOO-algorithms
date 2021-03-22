package start;

import MOPSOSolver.*;
import NSGAIISolver.NSGAII;
import TabuSearchSolver.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import data.GlobalConstraint;
import data.Student;
import data.Team;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {

    public static List<Student> students = new ArrayList<Student>();
    public static List<Team> teams = new ArrayList<Team>();

    public static GlobalConstraint globalConstraint;

    public static void setGlobalConstraint(int minSize, int maxSize, int minMac, int minIdev, int minFemales) {
        globalConstraint = new GlobalConstraint(minSize, maxSize, minMac, minIdev, minFemales);
    }

    public static void main(String[] args) throws IOException {

        int teamIndex = 0;

        try {

            //parse CSV file with students information
            //https://www.geeksforgeeks.org/reading-csv-file-java-using-opencv/

            FileReader filereader = new FileReader("2_rated.csv");

            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();


            int count = 0;

            for (String[] row : allData) {
                if (count == 54) {
                    count = 0;
                }
                String firstName = row[0];
                String lastName = row[1];
                String email = row[2];
                String attribute_2 = row[3];
                String attribute_3 = row[4];
                String major = row[5];

                int semester = 0;

                try {
                    semester = Integer.parseInt(row[6].trim());
                } catch (NumberFormatException nfe) {
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }

                String language_a1 = row[7];
                String language_a2 = row[8];
                String iOSDev = row[9];
                String appStoreLink = row[10];
                String iOSDevExplained = row[11];
                String introAssessment_INTRO = row[12];
                String introAssessmentTutor_INTRO = row[13];

                String iPad = row[14];
                boolean devices_iPad = iPad.equals("Yes") ? true : false;

                String iPhone = row[15];
                boolean devices_iPhone = iPhone.equals("Yes") ? true : false;

                String watch = row[16];
                boolean devices_Watch = watch.equals("Yes") ? true : false;

                String mac = row[17];
                boolean devices_Mac = mac.equals("Yes") ? true : false;

                String iPadAR = row[18];
                boolean devices_iPadAR = iPadAR.equals("Yes") ? true : false;

                String iPhoneAR = row[19];
                boolean devices_iPhoneAR = iPhoneAR.equals("Yes") ? true : false;

                String expinterWEBFE_expinter_1 = row[20];
                String expinterWEBFE_expinter_2 = row[21];
                String justifyWEBFE = row[22];
                String expinterSSDEV_expinter_1 = row[23];
                String expinter_SSDEV_expinter_2 = row[24];
                String justifySSDEV = row[25];
                String expinterUIUX_expinter_1 = row[26];
                String expinter_UIUX_expinter_2 = row[27];
                String justifyUIUX = row[28];
                String expinterEMBEDED_expinter_1 = row[29];
                String expinterEMBEDED_expinter_2 = row[30];
                String justifyEMBEDED = row[31];
                String expinterVRAR_expinter_1 = row[32];
                String expinterVRAR_expinter_2 = row[33];
                String justifyVRAR = row[34];
                String expinterMLALG_expinter_1 = row[35];
                String expinterMLALG_expinter_2 = row[36];
                String justifyMLALG = row[37];

                String otherSkills = row[38];

                String[] priorities = new String[10];
                priorities[0] = row[39];
                priorities[1] = row[40];
                priorities[2] = row[41];
                priorities[3] = row[42];
                priorities[4] = row[43];
                priorities[5] = row[44];
                priorities[6] = row[45];
                priorities[7] = row[46];
                priorities[8] = row[47];
                priorities[9] = row[48];


                for (int i = 0; i < priorities.length; i++) {
                    boolean newTeam = true;
                    for (int j = 0; j < teams.size(); j++) {
                        if (teams.get(j).getName().equals(priorities[i])) {
                            newTeam = false;
                        }
                    }
                    if (newTeam) {
                        teams.add(new Team(priorities[i], teamIndex));
                        teamIndex++;
                    }
                }

                String comments = row[49];
                String supervisorRating = row[50];
                String commentsTutor = row[51];

                String pinned = row[52];
                boolean isPinned = pinned.equals("FALSE") ? false : true;
                String teamName = row[53];


                students.add(new Student(firstName, lastName, email, attribute_2, attribute_3, major, semester, language_a1,
                        language_a2, iOSDev, appStoreLink, iOSDevExplained, introAssessment_INTRO, introAssessmentTutor_INTRO,
                        devices_iPad, devices_iPhone, devices_Watch, devices_Mac, devices_iPadAR, devices_iPhoneAR, expinterWEBFE_expinter_1,
                        expinterWEBFE_expinter_2, justifyWEBFE, expinterSSDEV_expinter_1, expinter_SSDEV_expinter_2,
                        justifySSDEV, expinterUIUX_expinter_1, expinter_UIUX_expinter_2, justifyUIUX,
                        expinterEMBEDED_expinter_1, expinterEMBEDED_expinter_2, justifyEMBEDED, expinterVRAR_expinter_1, expinterVRAR_expinter_2,
                        justifyVRAR, expinterMLALG_expinter_1, expinterMLALG_expinter_2, justifyMLALG, otherSkills, priorities, comments, supervisorRating,
                        commentsTutor, isPinned, teamName));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int[] minSize = {0};
        final int[] maxSize = {0};
        final int[] minMac = {0};
        final int[] minIDev = {0};
        final int[] minFemaleStudents = {0};

        final JFrame f = new JFrame("Solver");

        JLabel welcome = new JLabel();
        welcome.setText("Enter global constraints: ");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(200, 100);
        f.add(welcome);

        final JLabel assignment = new JLabel();
        assignment.setText("Assignment found!");
        assignment.setHorizontalAlignment(JLabel.CENTER);
        assignment.setSize(200, 200);
        assignment.setBounds(340, 40, 150, 20);
        f.add(assignment);
        assignment.setVisible(false);

        //minSize constraint
        final JLabel labelMinSize = new JLabel();
        labelMinSize.setText("Min size: ");
        labelMinSize.setHorizontalAlignment(JLabel.CENTER);
        labelMinSize.setSize(100, 230);

        minSize[0] = 8;

        SpinnerModel valueMinSize = new SpinnerNumberModel(8, 8, (int) (students.size() / teams.size()), 1);

        final JSpinner spinnerMinSize = new JSpinner(valueMinSize);

        spinnerMinSize.setBounds(200, 100, 50, 30);
        f.add(labelMinSize);
        f.add(spinnerMinSize);

        spinnerMinSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minSize[0] = (Integer) spinnerMinSize.getValue();
            }
        });


        //maxSize constraint
        final JLabel labelMaxSize = new JLabel();
        labelMaxSize.setText("Max size: ");
        labelMaxSize.setHorizontalAlignment(JLabel.CENTER);
        labelMaxSize.setSize(100, 330);

        SpinnerModel valueMaxSize = new SpinnerNumberModel(9, 9, students.size(), 1);

        final JSpinner spinnerMaxSize = new JSpinner(valueMaxSize);

        spinnerMaxSize.setBounds(200, 150, 50, 30);
        f.add(spinnerMaxSize);
        f.add(labelMaxSize);

        spinnerMaxSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                maxSize[0] = (Integer) spinnerMaxSize.getValue();
            }
        });


        //minMac constraint
        final JLabel labelMinMac = new JLabel();
        labelMinMac.setText("Min Mac: ");
        labelMinMac.setHorizontalAlignment(JLabel.CENTER);
        labelMinMac.setSize(100, 430);

        SpinnerModel valueMinMac = new SpinnerNumberModel(0, 0,
                ServiceTabuSearch.getStudentsWithMac(students).size() / teams.size(), 1);

        final JSpinner spinnerMinMac = new JSpinner(valueMinMac);

        spinnerMinMac.setBounds(200, 200, 50, 30);
        f.add(spinnerMinMac);
        f.add(labelMinMac);

        spinnerMinMac.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minMac[0] = (Integer) spinnerMinMac.getValue();
            }
        });


        //minIDevice constraint
        final JLabel labelMinIDev = new JLabel();
        labelMinIDev.setText("Min IDevices: ");
        labelMinIDev.setHorizontalAlignment(JLabel.CENTER);
        labelMinIDev.setSize(130, 530);

        SpinnerModel valueMinIDev = new SpinnerNumberModel(0, 0,
                ServiceTabuSearch.getStudentsWithIDevice(students).size() / teams.size(), 1);

        final JSpinner spinnerMinIDev = new JSpinner(valueMinIDev);

        spinnerMinIDev.setBounds(200, 250, 50, 30);
        f.add(spinnerMinIDev);
        f.add(labelMinIDev);

        spinnerMinIDev.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minIDev[0] = (Integer) spinnerMinIDev.getValue();
            }
        });


        //minFemaleStudents constraint
        final JLabel labelMinFemaleStudents = new JLabel();
        labelMinFemaleStudents.setText("Min female students: ");
        labelMinFemaleStudents.setHorizontalAlignment(JLabel.CENTER);
        labelMinFemaleStudents.setSize(180, 630);

        SpinnerModel valueMinFemaleStudents = new SpinnerNumberModel(0, 0,
                ServiceTabuSearch.getFemaleStudents(students).size() / teams.size(), 1);

        final JSpinner spinnerMinFemaleStudents = new JSpinner(valueMinFemaleStudents);

        spinnerMinFemaleStudents.setBounds(200, 300, 50, 30);
        f.add(spinnerMinFemaleStudents);
        f.add(labelMinFemaleStudents);


        spinnerMinFemaleStudents.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minFemaleStudents[0] = (Integer) spinnerMinFemaleStudents.getValue();
            }
        });


        //display solution for a selected team
        final JLabel teamMessage = new JLabel("Assigned students: ");
        teamMessage.setBounds(350, 80, 150, 20);
        f.add(teamMessage);
        teamMessage.setVisible(false);


        final JLabel teamsLabel = new JLabel("Team: ");
        teamsLabel.setBounds(350, 120, 150, 20);

        String teamNames[] = new String[teams.size()];
        for (int i = 0; i < teamNames.length; i++) {
            teamNames[i] = teams.get(i).getName();
        }
        final JComboBox cb1 = new JComboBox(teamNames);

        cb1.setBounds(430, 120, 150, 20);
        f.add(teamsLabel);
        f.add(cb1);
        teamsLabel.setVisible(false);
        cb1.setVisible(false);


        //display solution for a selected student
        final JLabel studentMessage = new JLabel("Assigned team: ");
        studentMessage.setBounds(350, 200, 150, 20);
        f.add(studentMessage);
        studentMessage.setVisible(false);

        final JLabel studentsLabel = new JLabel("Student: ");
        studentsLabel.setBounds(350, 240, 150, 20);

        String studentNames[] = new String[students.size()];
        for (int i = 0; i < studentNames.length; i++) {
            studentNames[i] = students.get(i).getLastName();
        }
        final JComboBox cb2 = new JComboBox(studentNames);

        cb2.setBounds(430, 240, 150, 20);
        f.add(studentsLabel);
        f.add(cb2);
        studentsLabel.setVisible(false);
        cb2.setVisible(false);


        final String[] result = new String[students.size() + 3];


        //choose solver
        final JLabel solverMessage = new JLabel("Choose a solver: ");
        solverMessage.setBounds(20, 380, 150, 20);
        f.add(solverMessage);
        solverMessage.setVisible(true);


        String solvers[] = new String[3];
        solvers[0] = "TabuSearch Solver";
        solvers[1] = "MOPSO Solver";
        solvers[2] = "NSGA-II Solver";
        final JComboBox cbSolver = new JComboBox(solvers);
        cbSolver.setBounds(140, 380, 180, 30);
        f.add(cbSolver);
        cbSolver.setVisible(true);

        cbSolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(cbSolver.getItemAt(cbSolver.getSelectedIndex()));

                boolean notNSGA = true;
                setGlobalConstraint(minSize[0], maxSize[0], minMac[0], minIDev[0], minFemaleStudents[0]);

                if (cbSolver.getSelectedIndex() == 0) {//Tabu Search Solver
                    System.arraycopy(TabuSearch.generateSolution(), 0, result, 0, result.length);
                } else if (cbSolver.getSelectedIndex() == 1) {//MOPSO Solver
                    System.arraycopy(MOPSO.generateSolution(), 0, result, 0, result.length);
                } else if (cbSolver.getSelectedIndex() == 2) {//NSGA-II Solver

                    //this solver is treated separately because it generates multiple solutions (no predefined number
                    // of solutions) -> see class NSGAII

                    notNSGA = false;

                    final int[] countSolutions = {0};
                    final String[] result = new String[students.size() * 100];

                    //get the assignment solution
                    final String[] resultNSGA = NSGAII.generateSolutions();
                    System.arraycopy(resultNSGA, 0, result, 0, resultNSGA.length);

                    countSolutions[0] = resultNSGA.length / 85;

                    //create csv files with assignment results -> each solution has its own file

                    try {

                        for (int k = 0; k < countSolutions[0]; k++) {

                            CSVWriter writer = new CSVWriter(new FileWriter("allocation" + (k + 1) + ".csv"));

                            String[] header = {"firstname", "lastname", "email", "attribute_2", "attribute_3",
                                    "major", "semester", "language[a1]", "language[a2]", "iOSDev",
                                    "appStoreLink", "iOSDevExplained", "introAssessment[INTRO]",
                                    "introAssessmentTutor[INTRO]", "devices[iPad]", "devices[iPhone]", "devices[Watch]",
                                    "devices[Mac]", "devices[iPadAR]", "devices[iPhoneAR]", "expinterWEBFE[expinter][1]",
                                    "expinterWEBFE[expinter][2]", "justifyWEBFE", "expinterSSDEV[expinter][1]", "expinterSSDEV[expinter][2]",
                                    "justifySSDEV", "expinterUIUX[expinter][1]", "expinterUIUX[expinter][2]",
                                    "justifyUIUX", "expinterEMBED[expinter][1]", "expinterEMBED[expinter][2]", "justifyEMBED",
                                    "expinterVRAR[expinter][1]", "expinterVRAR[expinter][2]", "justifyVRAR",
                                    "expinterMLALG[expinter][1]", "expinterMLALG[expinter][2]", "justifyMLALG", "otherSkills",
                                    "Priorities[1]", "Priorities[2]", "Priorities[3]", "Priorities[4]", "Priorities[5]",
                                    "Priorities[6]", "Priorities[7]", "Priorities[8]", "Priorities[9]", "Priorities[10]",
                                    "Comments", "supervisorRating", "CommentsTutor", "IsPinned", "teamName"};

                            writer.writeNext(header);

                            for (int i = 0; i < students.size(); i++) {
                                String[] piece = new String[54];
                                for (int j = 0; j < piece.length; j++) {
                                    piece[j] = "";
                                }
                                piece[0] += students.get(i).getFirstName();
                                piece[1] += students.get(i).getLastName();
                                piece[2] += students.get(i).getEmail();
                                piece[3] += students.get(i).getAttribute_2();
                                piece[4] += students.get(i).getAttribute_3();
                                piece[5] += students.get(i).getMajor();
                                piece[6] += students.get(i).getSemester();
                                piece[7] += students.get(i).getLanguage_a1();
                                piece[8] += students.get(i).getLanguage_a2();
                                piece[9] += students.get(i).getiOSDev();
                                piece[10] += students.get(i).getAppStoreLink();
                                piece[11] += students.get(i).getiOSDevExplained();
                                piece[12] += students.get(i).getIntroAssessment_INTRO();
                                piece[13] += students.get(i).getIntroAssessmentTutor_INTRO();
                                if (students.get(i).isDevices_iPad()) {
                                    piece[14] += "Yes";
                                } else {
                                    piece[14] += "No";
                                }
                                if (students.get(i).isDevices_iPhone()) {
                                    piece[15] += "Yes";
                                } else {
                                    piece[15] += "No";
                                }

                                if (students.get(i).isDevices_Watch()) {
                                    piece[16] += "Yes";
                                } else {
                                    piece[16] += "No";
                                }

                                if (students.get(i).isDevices_Mac()) {
                                    piece[17] += "Yes";
                                } else {
                                    piece[17] += "No";
                                }
                                if (students.get(i).isDevices_iPadAR()) {
                                    piece[18] += "Yes";
                                } else {
                                    piece[18] += "No";
                                }
                                if (students.get(i).isDevices_iPhoneAR()) {
                                    piece[19] += "Yes";
                                } else {
                                    piece[19] += "No";
                                }
                                piece[20] += students.get(i).getExpinterWEBFE_expinter_1();
                                piece[21] += students.get(i).getExpinterWEBFE_expinter_2();
                                piece[22] += students.get(i).getJustifyWEBFE();
                                piece[23] += students.get(i).getExpinterSSDEV_expinter_1();
                                piece[24] += students.get(i).getExpinter_SSDEV_expinter_2();
                                piece[25] += students.get(i).getJustifySSDEV();
                                piece[26] += students.get(i).getExpinterUIUX_expinter_1();
                                piece[27] += students.get(i).getExpinter_UIUX_expinter_2();
                                piece[28] += students.get(i).getJustifyUIUX();
                                piece[29] += students.get(i).getExpinterEMBEDED_expinter_1();
                                piece[30] += students.get(i).getExpinterEMBEDED_expinter_2();
                                piece[31] += students.get(i).getJustifyEMBEDED();
                                piece[32] += students.get(i).getExpinterVRAR_expinter_1();
                                piece[33] += students.get(i).getExpinterVRAR_expinter_2();
                                piece[34] += students.get(i).getJustifyVRAR();
                                piece[35] += students.get(i).getExpinterMLALG_expinter_1();
                                piece[36] += students.get(i).getExpinterMLALG_expinter_2();
                                piece[37] += students.get(i).getJustifyMLALG();
                                piece[38] += students.get(i).getOtherSkills();
                                piece[39] += students.get(i).getPriorities()[0];
                                piece[40] += students.get(i).getPriorities()[1];
                                piece[41] += students.get(i).getPriorities()[2];
                                piece[42] += students.get(i).getPriorities()[3];
                                piece[43] += students.get(i).getPriorities()[4];
                                piece[44] += students.get(i).getPriorities()[5];
                                piece[45] += students.get(i).getPriorities()[6];
                                piece[46] += students.get(i).getPriorities()[7];
                                piece[47] += students.get(i).getPriorities()[8];
                                piece[48] += students.get(i).getPriorities()[9];
                                piece[49] += students.get(i).getComments();
                                piece[50] += students.get(i).getSupervisorRating();
                                piece[51] += students.get(i).getCommentsTutor();

                                if (students.get(i).isPinned()) {
                                    piece[52] += "FALSE";
                                } else {
                                    piece[52] += "TRUE";
                                }
                                piece[53] += result[students.size() * k + i];

                                writer.writeNext(piece);
                            }
                            writer.close();
                        }
                    } catch (IOException exception) {

                        exception.printStackTrace();
                    }
                    assignment.setVisible(true);
                }


                if (notNSGA) {
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("allocation.csv"));

                        String[] header = {"firstname", "lastname", "email", "attribute_2", "attribute_3",
                                "major", "semester", "language[a1]", "language[a2]", "iOSDev",
                                "appStoreLink", "iOSDevExplained", "introAssessment[INTRO]",
                                "introAssessmentTutor[INTRO]", "devices[iPad]", "devices[iPhone]", "devices[Watch]",
                                "devices[Mac]", "devices[iPadAR]", "devices[iPhoneAR]", "expinterWEBFE[expinter][1]",
                                "expinterWEBFE[expinter][2]", "justifyWEBFE", "expinterSSDEV[expinter][1]", "expinterSSDEV[expinter][2]",
                                "justifySSDEV", "expinterUIUX[expinter][1]", "expinterUIUX[expinter][2]",
                                "justifyUIUX", "expinterEMBED[expinter][1]", "expinterEMBED[expinter][2]", "justifyEMBED",
                                "expinterVRAR[expinter][1]", "expinterVRAR[expinter][2]", "justifyVRAR",
                                "expinterMLALG[expinter][1]", "expinterMLALG[expinter][2]", "justifyMLALG", "otherSkills",
                                "Priorities[1]", "Priorities[2]", "Priorities[3]", "Priorities[4]", "Priorities[5]",
                                "Priorities[6]", "Priorities[7]", "Priorities[8]", "Priorities[9]", "Priorities[10]",
                                "Comments", "supervisorRating", "CommentsTutor", "IsPinned", "teamName"};

                        writer.writeNext(header);

                        for (int i = 0; i < students.size(); i++) {
                            String[] piece = new String[54];
                            for (int j = 0; j < piece.length; j++) {
                                piece[j] = "";
                            }
                            piece[0] += students.get(i).getFirstName();
                            piece[1] += students.get(i).getLastName();
                            piece[2] += students.get(i).getEmail();
                            piece[3] += students.get(i).getAttribute_2();
                            piece[4] += students.get(i).getAttribute_3();
                            piece[5] += students.get(i).getMajor();
                            piece[6] += students.get(i).getSemester();
                            piece[7] += students.get(i).getLanguage_a1();
                            piece[8] += students.get(i).getLanguage_a2();
                            piece[9] += students.get(i).getiOSDev();
                            piece[10] += students.get(i).getAppStoreLink();
                            piece[11] += students.get(i).getiOSDevExplained();
                            piece[12] += students.get(i).getIntroAssessment_INTRO();
                            piece[13] += students.get(i).getIntroAssessmentTutor_INTRO();
                            if (students.get(i).isDevices_iPad()) {
                                piece[14] += "Yes";
                            } else {
                                piece[14] += "No";
                            }
                            if (students.get(i).isDevices_iPhone()) {
                                piece[15] += "Yes";
                            } else {
                                piece[15] += "No";
                            }

                            if (students.get(i).isDevices_Watch()) {
                                piece[16] += "Yes";
                            } else {
                                piece[16] += "No";
                            }

                            if (students.get(i).isDevices_Mac()) {
                                piece[17] += "Yes";
                            } else {
                                piece[17] += "No";
                            }
                            if (students.get(i).isDevices_iPadAR()) {
                                piece[18] += "Yes";
                            } else {
                                piece[18] += "No";
                            }
                            if (students.get(i).isDevices_iPhoneAR()) {
                                piece[19] += "Yes";
                            } else {
                                piece[19] += "No";
                            }
                            piece[20] += students.get(i).getExpinterWEBFE_expinter_1();
                            piece[21] += students.get(i).getExpinterWEBFE_expinter_2();
                            piece[22] += students.get(i).getJustifyWEBFE();
                            piece[23] += students.get(i).getExpinterSSDEV_expinter_1();
                            piece[24] += students.get(i).getExpinter_SSDEV_expinter_2();
                            piece[25] += students.get(i).getJustifySSDEV();
                            piece[26] += students.get(i).getExpinterUIUX_expinter_1();
                            piece[27] += students.get(i).getExpinter_UIUX_expinter_2();
                            piece[28] += students.get(i).getJustifyUIUX();
                            piece[29] += students.get(i).getExpinterEMBEDED_expinter_1();
                            piece[30] += students.get(i).getExpinterEMBEDED_expinter_2();
                            piece[31] += students.get(i).getJustifyEMBEDED();
                            piece[32] += students.get(i).getExpinterVRAR_expinter_1();
                            piece[33] += students.get(i).getExpinterVRAR_expinter_2();
                            piece[34] += students.get(i).getJustifyVRAR();
                            piece[35] += students.get(i).getExpinterMLALG_expinter_1();
                            piece[36] += students.get(i).getExpinterMLALG_expinter_2();
                            piece[37] += students.get(i).getJustifyMLALG();
                            piece[38] += students.get(i).getOtherSkills();
                            piece[39] += students.get(i).getPriorities()[0];
                            piece[40] += students.get(i).getPriorities()[1];
                            piece[41] += students.get(i).getPriorities()[2];
                            piece[42] += students.get(i).getPriorities()[3];
                            piece[43] += students.get(i).getPriorities()[4];
                            piece[44] += students.get(i).getPriorities()[5];
                            piece[45] += students.get(i).getPriorities()[6];
                            piece[46] += students.get(i).getPriorities()[7];
                            piece[47] += students.get(i).getPriorities()[8];
                            piece[48] += students.get(i).getPriorities()[9];
                            piece[49] += students.get(i).getComments();
                            piece[50] += students.get(i).getSupervisorRating();
                            piece[51] += students.get(i).getCommentsTutor();

                            if (students.get(i).isPinned()) {
                                piece[52] += "FALSE";
                            } else {
                                piece[52] += "TRUE";
                            }
                            piece[53] += result[i];

                            writer.writeNext(piece);

                            DecimalFormat df = new DecimalFormat("##.##");
                            df.setRoundingMode(RoundingMode.DOWN);

                            final JLabel priorityObjective = new JLabel("Mean priority: " +
                                    df.format(Float.parseFloat(result[students.size()])));
                            priorityObjective.setBounds(350, 300, 500, 20);
                            f.add(priorityObjective);

                            final JLabel experienceObjective = new JLabel("Mean deviation exp. level: "
                                    + df.format(Float.parseFloat(result[students.size() + 1])));
                            experienceObjective.setBounds(350, 340, 500, 20);
                            f.add(experienceObjective);

                            final JLabel combinedObjective = new JLabel("Combined objective value: " +
                                    df.format(Float.parseFloat(result[students.size() + 2])));
                            combinedObjective.setBounds(350, 380, 500, 20);
                            f.add(combinedObjective);

                            priorityObjective.setVisible(true);
                            experienceObjective.setVisible(true);
                            combinedObjective.setVisible(true);

                        }
                        writer.close();
                    } catch (IOException exception) {

                        exception.printStackTrace();
                    }

                    assignment.setVisible(true);
                    teamMessage.setVisible(true);
                    studentMessage.setVisible(true);
                    teamsLabel.setVisible(true);
                    cb1.setVisible(true);
                    studentsLabel.setVisible(true);

                    cb2.setVisible(true);
                }
            }
        });


        cb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(cb1.getItemAt(cb1.getSelectedIndex()));

                JFrame frame = new JFrame("Team " + teams.get(cb1.getSelectedIndex()).getName());
                frame.setSize(200, 250);

                String information = "";
                int counter = 1;
                for (int i = 0; i < students.size(); i++) {
                    if (result[i].equals(cb1.getItemAt(cb1.getSelectedIndex()))) {
                        information += counter + ". ";
                        counter++;
                        information += students.get(i).getLastName() + "\n";
                    }
                }

                JTextArea area = new JTextArea(information);
                area.setBounds(10, 30, 200, 200);
                frame.add(area);
                frame.setLocationRelativeTo(f);
                frame.setVisible(true);
            }
        });

        cb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(cb2.getItemAt(cb2.getSelectedIndex()));

                JFrame frame = new JFrame("Assigned team ");
                frame.setSize(200, 80);

                JTextArea area = new JTextArea(result[cb2.getSelectedIndex()]);
                area.setBounds(50, 50, 100, 100);
                frame.add(area);
                frame.setLocationRelativeTo(f);
                frame.setVisible(true);
            }
        });

        f.setSize(600, 450);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
    }
}
