package gui;

import agent.Heuristic;
import agent.Solution;
import mummymaze.MummyMazeAgent;
import mummymaze.MummyMazeProblem;
import mummymaze.MummyMazeState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.FieldPosition;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import searchmethods.*;


public class MainFrame extends JFrame {
    String defaultState =
            "             \n" +
            " . . . . .|. \n" +
            "     -       \n" +
            " . . . . . H \n" +
            "     -       \n" +
            " . . . .|. . \n" +
            "       -   - \n" +
            " . . . . .|. \n" +
            "   - -       \n" +
            " . . . M . . \n" +
            "         -   \n" +
            " . . . . . . \n" +
            " S           ";

    private MummyMazeAgent agent = new MummyMazeAgent(new MummyMazeState(defaultState));
    private JComboBox comboBoxSearchMethods;
    private JComboBox comboBoxHeuristics;
    private JLabel labelSearchParameter = new JLabel("limit/beam size:");
    private JTextField textFieldSearchParameter = new JTextField("0", 5);
    private GameArea gamePanel = new GameArea();
    private JButton buttonInitialState = new JButton("Read initial state");
    private JButton buttonRunAll = new JButton("Run all levels");
    private JButton buttonRunAllForSelFile = new JButton("Run all search meth, for open file");
    private JButton buttonSolve = new JButton("Solve");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonShowSolution = new JButton("Show solution");
    private JButton buttonReset = new JButton("Reset to initial state");
    private JTextArea textArea;
    File openedFile;

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void jbInit() throws Exception {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("MummyMaze");

        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel panelButtons = new JPanel(new FlowLayout());

        panelButtons.add(buttonRunAll);
        buttonRunAll.addActionListener(new ButtonRunAll_ActionAdapter(this));
        panelButtons.add(buttonInitialState);
        buttonInitialState.addActionListener(new ButtonInitialState_ActionAdapter(this));
        panelButtons.add(buttonSolve);
        buttonSolve.addActionListener(new ButtonSolve_ActionAdapter(this));
        panelButtons.add(buttonStop);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ButtonStop_ActionAdapter(this));
        panelButtons.add(buttonShowSolution);
        buttonShowSolution.setEnabled(false);
        buttonShowSolution.addActionListener(new ButtonShowSolution_ActionAdapter(this));
        panelButtons.add(buttonReset);
        buttonReset.setEnabled(false);
        buttonReset.addActionListener(new ButtonReset_ActionAdapter(this));

        JPanel panelSearchMethods = new JPanel(new FlowLayout());
        panelSearchMethods.add(buttonRunAllForSelFile);

        buttonRunAllForSelFile.addActionListener(new ButtonRunAllForSelFile_ActionAdapter(this));
        comboBoxSearchMethods = new JComboBox(agent.getSearchMethodsArray());
        panelSearchMethods.add(comboBoxSearchMethods);
        comboBoxSearchMethods.addActionListener(new ComboBoxSearchMethods_ActionAdapter(this));
        panelSearchMethods.add(labelSearchParameter);
        labelSearchParameter.setEnabled(false);
        panelSearchMethods.add(textFieldSearchParameter);
        textFieldSearchParameter.setEnabled(false);
        textFieldSearchParameter.setHorizontalAlignment(JTextField.RIGHT);
        textFieldSearchParameter.addKeyListener(new TextFieldSearchParameter_KeyAdapter(this));
        comboBoxHeuristics = new JComboBox(agent.getHeuristicsArray());
        panelSearchMethods.add(comboBoxHeuristics);
        comboBoxHeuristics.setEnabled(false);
        comboBoxHeuristics.addActionListener(new ComboBoxHeuristics_ActionAdapter(this));

        JPanel puzzlePanel = new JPanel(new FlowLayout());
        puzzlePanel.add(gamePanel);
        //textArea = new JTextArea(15, 31);
        textArea = new JTextArea(15, 31);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        puzzlePanel.add(scrollPane);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelButtons, BorderLayout.NORTH);
        mainPanel.add(panelSearchMethods, BorderLayout.CENTER);
        mainPanel.add(puzzlePanel, BorderLayout.SOUTH);

        gamePanel.setState(agent.resetEnvironment());
        buttonSolve.setEnabled(false);
        buttonRunAllForSelFile.setEnabled(false);

        contentPane.add(mainPanel);

        pack();
    }

    public void buttonInitialState_ActionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File("./Niveis"));
        try {
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                openedFile = fc.getSelectedFile();
                gamePanel.setState(agent.readInitialStateFromFile(fc.getSelectedFile()));
                this.setTitle("MummyMaze - Level file: " + fc.getSelectedFile().getName());
                buttonSolve.setEnabled(true);
                buttonShowSolution.setEnabled(false);
                buttonReset.setEnabled(false);
                buttonRunAllForSelFile.setEnabled(true);
                buttonRunAll.setEnabled(true);
                gamePanel.setSolutionCost(0);
            }
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void buttonRunAll(ActionEvent e) {
        File searchReportSummary = new File("searchReportSummary.xls");
        Boolean searchWantsHeuristic = false;
        int amountOfHeuristics = 0;

        File dir = new File("./Niveis");
        File[] files = dir.listFiles();
        buttonStop.setEnabled(true);
        buttonSolve.setEnabled(false);
        buttonShowSolution.setEnabled(false);

        for (File file : files) {
            try {
                utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "Level: "+file.getName().split(".txt")[0]);
                utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\tHeuristic\tCost\tNum exp. nodes\tMax frontier size\tNum gen. states\tTime(ms)");

                gamePanel.setState(agent.readInitialStateFromFile(file));
                gamePanel.setState(agent.resetEnvironment());

                SearchMethod[] searchMethods = agent.getSearchMethodsArray();
                for (SearchMethod searchMethod: searchMethods) {
                    if (searchMethod.toString().equals("Beam search") || searchMethod.toString().equals("Limited depth first search")) {
                        continue;
                    }
                    utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\r\n" + searchMethod.toString());
                    agent.setSearchMethod(searchMethod);
                    gamePanel.setState(agent.resetEnvironment());


                    if (searchMethod.toString().equals("Greedy best first search") || searchMethod.toString().equals("A* search")) {
                        searchWantsHeuristic = true;
                        amountOfHeuristics = agent.getHeuristicsArray().length;
                    } else {
                        searchWantsHeuristic = false;
                    }

                    Heuristic[] heuristics = agent.getHeuristicsArray();
                    do {
                        //#region show message box, only way to interrupt code
                        final JOptionPane optionPane;

                        if (searchWantsHeuristic) {
                            Heuristic currentHeuristic = heuristics[amountOfHeuristics-1];
                            agent.setHeuristic(currentHeuristic);
                            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\r\n\t" + currentHeuristic.toString());
                            textArea.setText("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName()+"\n\n"+agent.getHeuristic().toString());
                            optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName()+"\n\n"+agent.getHeuristic().toString(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                        } else {
                            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t");
                            textArea.setText("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName());
                            optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                        }

                        //final JOptionPane optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

                        final JDialog dialog = new JDialog();
                        dialog.setTitle("Solving progress");
                        dialog.setModal(true);

                        dialog.setContentPane(optionPane);

                        //dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        dialog.pack();
                        //create timer to dispose of dialog after 5 seconds

                        Timer timer = new Timer(1, new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                dialog.dispose();
                            }
                        });
                        timer.setRepeats(false);//the timer should only go off once

                        //start timer to close JDialog as dialog modal we must start the timer before its visible
                        timer.start();

                        dialog.setVisible(true);
                        //#endregion

                        prepareSearchAlgorithm();
                        MummyMazeProblem problem = new MummyMazeProblem((MummyMazeState) agent.getEnvironment().clone());
                        long start = System.nanoTime();
                        agent.solveProblem(problem);
                        long elapsedTime = System.nanoTime() - start;
                        String report = agent.getSearchReport();

                        if (agent.hasSolution()) {
                            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t" +  agent.getSolutionCost());
                        } else {
                            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\tNoSolutionFound");
                        }

                        utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t" + searchMethod.getStatistics().numExpandedNodes);
                        utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t" + searchMethod.getStatistics().maxFrontierSize);
                        utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t" + searchMethod.getStatistics().numGeneratedSates);
                        utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\t" +  TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));

                        if (searchWantsHeuristic) {
                            amountOfHeuristics--;
                        } else {
                            amountOfHeuristics=0;
                        }
                    } while(amountOfHeuristics>0);
                }
            } catch (IOException ex) {
                System.out.println("ex.toString() = " + ex.toString());
            } catch (NoSuchElementException e2) {
                JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
            }
            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\r\n\r\n");
            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "########################\t###########################\t################\t###############\t###############\t##############\t##########");
            utils.FileOperations.appendToTextFile(searchReportSummary.getName(), "\r\n\r\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        buttonSolve.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonShowSolution.setEnabled(false);
        buttonRunAllForSelFile.setEnabled(false);
        comboBoxSearchMethods.setEnabled(false);
        int index = comboBoxSearchMethods.getSelectedIndex();
        agent.setSearchMethod((SearchMethod) comboBoxSearchMethods.getItemAt(index));
        index = comboBoxHeuristics.getSelectedIndex();
        agent.setHeuristic((Heuristic) comboBoxHeuristics.getItemAt(index));

        JOptionPane.showMessageDialog(this, "All search methods have been run.\nSearch report file has been created, '"+ searchReportSummary.getName() + "'.", "Information", JOptionPane.INFORMATION_MESSAGE);
        textArea.setText("");
    }

    public void buttonRunAllForSelFile(ActionEvent e) {
        File independentSearchReport = new File("independentSearchReport.xls");
        Boolean searchWantsHeuristic = false;
        int amountOfHeuristics = 0;

        File dir = new File("./Niveis");
        File[] files = dir.listFiles();
        buttonStop.setEnabled(true);
        buttonSolve.setEnabled(false);
        buttonShowSolution.setEnabled(false);

        try {
            utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "Level: "+openedFile.getName().split(".txt")[0]);
            utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\tHeuristic\tCost\tNum exp. nodes\tMax frontier size\tNum gen. states\tTime(ms)");

            SearchMethod[] searchMethods = agent.getSearchMethodsArray();
            for (SearchMethod searchMethod: searchMethods) {
                if (searchMethod.toString().equals("Beam search") || searchMethod.toString().equals("Limited depth first search")) {
                    continue;
                }
                utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\r\n" + searchMethod.toString());
                agent.setSearchMethod(searchMethod);
                gamePanel.setState(agent.resetEnvironment());


                if (searchMethod.toString().equals("Greedy best first search") || searchMethod.toString().equals("A* search")) {
                    searchWantsHeuristic = true;
                    amountOfHeuristics = agent.getHeuristicsArray().length;
                } else {
                    searchWantsHeuristic = false;
                }

                Heuristic[] heuristics = agent.getHeuristicsArray();
                do {
                    //#region show message box, only way to interrupt code
                    final JOptionPane optionPane;

                    if (searchWantsHeuristic) {
                        Heuristic currentHeuristic = heuristics[amountOfHeuristics-1];
                        agent.setHeuristic(currentHeuristic);
                        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\r\n\t" + currentHeuristic.toString());
                        textArea.setText("Currently solving:\n"+searchMethod.toString()+"\n"+openedFile.getName()+"\n\n"+agent.getHeuristic().toString());
                        optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+openedFile.getName()+"\n\n"+agent.getHeuristic().toString(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                    } else {
                        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t");
                        textArea.setText("Currently solving:\n"+searchMethod.toString()+"\n"+openedFile.getName());
                        optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+openedFile.getName(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                    }

                    //final JOptionPane optionPane = new JOptionPane("Currently solving:\n"+searchMethod.toString()+"\n"+file.getName(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

                    final JDialog dialog = new JDialog();
                    dialog.setTitle("Solving progress");
                    dialog.setModal(true);

                    dialog.setContentPane(optionPane);

                    //dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    dialog.pack();
                    //create timer to dispose of dialog after 5 seconds

                    Timer timer = new Timer(1, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            dialog.dispose();
                        }
                    });
                    timer.setRepeats(false);//the timer should only go off once

                    //start timer to close JDialog as dialog modal we must start the timer before its visible
                    timer.start();

                    dialog.setVisible(true);
                    //#endregion

                    prepareSearchAlgorithm();
                    MummyMazeProblem problem = new MummyMazeProblem((MummyMazeState) agent.getEnvironment().clone());
                    long start = System.nanoTime();
                    agent.solveProblem(problem);
                    long elapsedTime = System.nanoTime() - start;
                    String report = agent.getSearchReport();

                    if (agent.hasSolution()) {
                        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t" +  agent.getSolutionCost());
                    } else {
                        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\tNoSolutionFound");
                    }

                    utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t" + searchMethod.getStatistics().numExpandedNodes);
                    utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t" + searchMethod.getStatistics().maxFrontierSize);
                    utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t" + searchMethod.getStatistics().numGeneratedSates);
                    utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\t" +  TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));

                    if (searchWantsHeuristic) {
                        amountOfHeuristics--;
                    } else {
                        amountOfHeuristics=0;
                    }
                } while(amountOfHeuristics>0);
            }
        } catch (NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\r\n\r\n");
        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "########################\t###########################\t################\t###############\t###############\t##############\t##########");
        utils.FileOperations.appendToTextFile(independentSearchReport.getName(), "\r\n\r\n");

        buttonSolve.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonRunAllForSelFile.setEnabled(false);
        comboBoxSearchMethods.setEnabled(false);
        buttonShowSolution.setEnabled(false);
        int index = comboBoxSearchMethods.getSelectedIndex();
        agent.setSearchMethod((SearchMethod) comboBoxSearchMethods.getItemAt(index));
        index = comboBoxHeuristics.getSelectedIndex();
        agent.setHeuristic((Heuristic) comboBoxHeuristics.getItemAt(index));
        gamePanel.setState(agent.resetEnvironment());
        JOptionPane.showMessageDialog(this, "All search methods have been run.\nSearch report file has been created, '"+ independentSearchReport.getName() + "'.", "Information", JOptionPane.INFORMATION_MESSAGE);
        textArea.setText("");
    }

    public void comboBoxSearchMethods_ActionPerformed(ActionEvent e) {
        int index = comboBoxSearchMethods.getSelectedIndex();
        agent.setSearchMethod((SearchMethod) comboBoxSearchMethods.getItemAt(index));
        gamePanel.setState(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
        comboBoxHeuristics.setEnabled(index > 4); //Informed serch methods
        textFieldSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
        labelSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
    }

    public void comboBoxHeuristics_ActionPerformed(ActionEvent e) {
        int index = comboBoxHeuristics.getSelectedIndex();
        agent.setHeuristic((Heuristic) comboBoxHeuristics.getItemAt(index));
        gamePanel.setState(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
    }

    public void buttonSolve_ActionPerformed(ActionEvent e) {
        gamePanel.setSolutionCost(0);
        SwingWorker worker = new SwingWorker<Solution, Void>() {
            @Override
            public Solution doInBackground() {
                textArea.setText("");
                buttonSolve.setEnabled(false);
                buttonStop.setEnabled(true);
                buttonRunAllForSelFile.setEnabled(false);
                comboBoxSearchMethods.setEnabled(false);
                buttonShowSolution.setEnabled(false);
                try {
                    prepareSearchAlgorithm();
                    MummyMazeProblem problem = new MummyMazeProblem((MummyMazeState) agent.getEnvironment().clone());
                    agent.solveProblem(problem);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return null;
            }

            @Override
            public void done() {
                if (!agent.hasBeenStopped()) {
                    textArea.setText(agent.getSearchReport());
                    if (agent.hasSolution()) {
                        buttonShowSolution.setEnabled(true);
                    }
                }
                buttonSolve.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonRunAllForSelFile.setEnabled(true);
                comboBoxSearchMethods.setEnabled(true);
                buttonShowSolution.setEnabled(true);
            }
        };

        worker.execute();
    }

    public void buttonStop_ActionPerformed(ActionEvent e) {
        agent.stop();
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(true);
    }

    public void buttonShowSolution_ActionPerformed(ActionEvent e) {
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(false);
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                agent.executeSolution();
                buttonReset.setEnabled(true);
                return null;
            }

            @Override
            public void done() {
                buttonShowSolution.setEnabled(true);
                buttonSolve.setEnabled(true);
            }
        };
        worker.execute();
    }

    public void buttonReset_ActionPerformed(ActionEvent e) {
        gamePanel.setState(agent.resetEnvironment());
        buttonShowSolution.setEnabled(true);
        buttonReset.setEnabled(false);
        gamePanel.setSolutionCost(0);
    }

    private void prepareSearchAlgorithm() {
        if (agent.getSearchMethod() instanceof DepthLimitedSearch) {
            DepthLimitedSearch searchMethod = (DepthLimitedSearch) agent.getSearchMethod();
            searchMethod.setLimit(Integer.parseInt(textFieldSearchParameter.getText()));
        } else if (agent.getSearchMethod() instanceof BeamSearch) {
            BeamSearch searchMethod = (BeamSearch) agent.getSearchMethod();
            searchMethod.setBeamSize(Integer.parseInt(textFieldSearchParameter.getText()));
        }
    }
}

class ComboBoxSearchMethods_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxSearchMethods_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxSearchMethods_ActionPerformed(e);
    }
}

class ComboBoxHeuristics_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxHeuristics_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxHeuristics_ActionPerformed(e);
    }
}

class ButtonInitialState_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonInitialState_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonInitialState_ActionPerformed(e);
    }
}

class ButtonRunAll_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonRunAll_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonRunAll(e);
    }
}

class ButtonRunAllForSelFile_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonRunAllForSelFile_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonRunAllForSelFile(e);
    }
}

class ButtonSolve_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonSolve_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonSolve_ActionPerformed(e);
    }
}

class ButtonStop_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonStop_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonStop_ActionPerformed(e);
    }
}

class ButtonShowSolution_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonShowSolution_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonShowSolution_ActionPerformed(e);
    }
}

class ButtonReset_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonReset_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonReset_ActionPerformed(e);
    }
}

class TextFieldSearchParameter_KeyAdapter implements KeyListener {

    private final MainFrame adaptee;

    TextFieldSearchParameter_KeyAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            e.consume();
        }
    }
}
