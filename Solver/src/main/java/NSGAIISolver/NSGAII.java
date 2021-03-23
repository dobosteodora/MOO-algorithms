package NSGAIISolver;

import start.GUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;

public class NSGAII {

    public static String[] generateSolutions() {

        Configuration.buildObjectives();

        Allocation allocation = new Allocation();

        Reporter.reportGeneration(1);

        Population parentInitial = Synthesis.syntesizePopulation();//everything is 0

        Population parentWithAllConstraints = Synthesis.syntesizePopulationWithConstraints(parentInitial);

        Population parent = NSGAIIGenetics.preparePopulation(parentWithAllConstraints);

        Population child = Synthesis.synthesizeChild(parent);

        Population combinedPopulation;

        for (int generation = 1; generation <= Configuration.GENERATIONS; generation++) {

            Reporter.reportGeneration(generation + 1);

            combinedPopulation = NSGAIIGenetics.preparePopulation(Synthesis.createCombinedPopulation(parent, child));

            parent = NSGAIIGenetics.getChildFromCombinedPopulation(combinedPopulation);

            child = Synthesis.synthesizeChild(parent);

            allocation.prepareMultipleDataset(child, generation, "gen. " + generation);
        }

        Set<Chromosome> solutions = Allocation.getSolutions();


        System.out.println("PARETO SOLUTIONS:");
        int k = 1;
        Set<Chromosome> pareto = Allocation.computePareto(solutions);

        final String[] solution = new String[pareto.size() * GUI.students.size()];


        for (int i = 0; i < solution.length; i++) {
            solution[i] = "";
        }

        int counter = 0;

        final JFrame nsgaFrame = new JFrame("NSGA-II Solutions");
        nsgaFrame.setSize(350, 100);
        nsgaFrame.setLocationRelativeTo(null);
        nsgaFrame.setVisible(true);

        String objectives[] = new String[pareto.size()];

        int indexSolution = 0;
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        for (Chromosome c : pareto) {

            indexSolution++;
            String display = "";

            System.out.println("Solution " + k);

            k++;
            System.out.println("Mean Priority: " + c.getObjectiveValues().get(0) + " | " +
                    "Mean deviation exp. level: " + c.getObjectiveValues().get(1));

            display += "Solution " + indexSolution + ": Priority: " + df.format(c.getObjectiveValues().get(0))
                    + " | Experience: " + df.format(c.getObjectiveValues().get(1));

            for (int s = 0; s < GUI.students.size(); s++) {
                int team = ServiceNSGAII.studentIsAssigned(c, s);
                solution[counter] += GUI.teams.get(team).getName();
                counter++;
            }
            objectives[indexSolution - 1] = display;
        }

        final JComboBox cbObjectives = new JComboBox(objectives);
        cbObjectives.setBounds(140, 250, 180, 30);
        nsgaFrame.add(cbObjectives);
        cbObjectives.setVisible(true);

        cbObjectives.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Solution " + (cbObjectives.getSelectedIndex() + 1));
                frame.setSize(250, 200);

                final JLabel teamMessage = new JLabel("Assigned students: ");
                teamMessage.setBounds(30, 30, 150, 20);
                frame.add(teamMessage);
                teamMessage.setVisible(true);

                final JLabel teamsLabel = new JLabel("Team: ");
                teamsLabel.setBounds(30, 60, 150, 20);

                String teamNames[] = new String[GUI.teams.size()];
                for (int i = 0; i < teamNames.length; i++) {
                    teamNames[i] = GUI.teams.get(i).getName();
                }
                final JComboBox cb1 = new JComboBox(teamNames);

                cb1.setBounds(70, 60, 150, 20);
                frame.add(teamsLabel);
                frame.add(cb1);
                teamsLabel.setVisible(true);
                cb1.setVisible(true);

                final JLabel studentMessage = new JLabel("Assigned team: ");
                studentMessage.setBounds(30, 100, 150, 20);
                frame.add(studentMessage);
                studentMessage.setVisible(true);

                final JLabel studentsLabel = new JLabel("Student: ");
                studentsLabel.setBounds(30, 130, 150, 20);

                String studentNames[] = new String[GUI.students.size()];
                for (int i = 0; i < studentNames.length; i++) {
                    studentNames[i] = GUI.students.get(i).getLastName();
                }
                final JComboBox cb2 = new JComboBox(studentNames);

                cb2.setBounds(80,130,150,20);
                frame.add(studentsLabel);
                frame.add(cb2);
                studentsLabel.setVisible(true);
                cb2.setVisible(true);
                frame.setLayout(null);
                frame.setLocationRelativeTo(nsgaFrame);
                frame.setVisible(true);

                cb1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        System.out.println(cb1.getSelectedIndex());

                        JFrame frame1 = new JFrame("Team " + GUI.teams.get(cb1.getSelectedIndex()).getName());
                        frame1.setSize(200, 250);

                        String information = "";
                        int index = 1;

                        for (int s = 0; s < GUI.students.size(); s++) {

                            if (solution[cbObjectives.getSelectedIndex() *
                                    GUI.students.size() + s].equals(cb1.getItemAt(cb1.getSelectedIndex()))) {
                                information += index + ". ";
                                index++;
                                information += GUI.students.get(s).getLastName() + "\n";
                            }
                        }

                        JTextArea area = new JTextArea(information);
                        area.setBounds(10, 30, 200, 200);
                        frame1.add(area);
                        frame1.setLocationRelativeTo(nsgaFrame);
                        frame1.setVisible(true);
                    }
                });

                cb2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(cb2.getItemAt(cb2.getSelectedIndex()));

                        JFrame frame2 = new JFrame("Assigned team ");
                        frame2.setSize(200, 80);

                        JTextArea area = new JTextArea(solution[cbObjectives.getSelectedIndex() *
                                GUI.students.size() + cb2.getSelectedIndex()]);
                        area.setBounds(50, 50, 100, 100);
                        frame2.add(area);
                        frame2.setLocationRelativeTo(nsgaFrame);
                        frame2.setVisible(true);
                    }
                });
            }
        });
        return solution;
    }
}

