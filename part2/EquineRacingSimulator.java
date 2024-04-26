import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquineRacingSimulator {
    private JFrame mainFrame;
    private CardLayout cardLayoutManager;
    private JPanel cardContainer, configurationPanel, competitionPanel, competitionControlPanel;
    private List<Equine> participants = new ArrayList<>();
    private List<Equestrian> bettors = new ArrayList<>();
    private int competitionDistance = 500;
    private JButton beginCompetitionButton;
    private Thread competitionThread;
    private boolean participantsReady = false;
    private boolean bettorsReady = false;

    public EquineRacingSimulator() throws IOException {
        setupUserInterface();
    }

    /**
     * Creates a styled button with the specified text and action.
     *
     * @param text   the text of the button
     * @param action the action to be performed when the button is clicked
     * @return the created button
     */
    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(150, 64, 32));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setOpaque(true);
        button.setMargin(new Insets(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(e -> {
            action.run();
            verifySetupCompletion();
        });
        return button;
    }

    /**
     * Sets up the user interface of the application.
     *
     * @throws IOException if an I/O error occurs while loading the background image
     */
    private void setupUserInterface() throws IOException {
        mainFrame = new JFrame("Equine Racing Simulator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 460);

        Image backgroundImage = ImageIO.read(getClass().getResource("/background.jpg"));
        Image scaledImage = backgroundImage.getScaledInstance(mainFrame.getWidth(), mainFrame.getHeight(), Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setLayout(new BorderLayout());

        mainFrame.setContentPane(background);

        cardLayoutManager = new CardLayout();
        cardContainer = new JPanel(cardLayoutManager);
        cardContainer.setOpaque(false);

        configurationPanel = createConfigurationPanel();
        configurationPanel.setOpaque(false);
        cardContainer.add(configurationPanel, "Setup");

        competitionPanel = new JPanel();
        competitionPanel.setOpaque(false);
        cardContainer.add(competitionPanel, "Race Track");

        background.add(cardContainer, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    /**
     * Creates the configuration panel for setting up the race.
     *
     * @return the created configuration panel
     */
    private JPanel createConfigurationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to the Equine Racing Simulator");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, constraints);

        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;

        JButton setupParticipantsButton = createButton("Setup Equines", this::setupParticipants);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(setupParticipantsButton, constraints);

        JButton setupBettorsButton = createButton("Setup Equestrians", this::setupBettors);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(setupBettorsButton, constraints);

        JButton customizeCompetitionTrackButton = createButton("Customize Race Track", this::customizeCompetitionTrack);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(customizeCompetitionTrackButton, constraints);

        beginCompetitionButton = createButton("Start Race", this::startCompetition);
        beginCompetitionButton.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(beginCompetitionButton, constraints);

        return panel;
    }

    /**
     * Verifies if the setup is complete and enables the start competition button if all conditions are met.
     */
    private void verifySetupCompletion() {
        if (participantsReady && bettorsReady && competitionDistance > 0) {
            beginCompetitionButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new EquineRacingSimulator();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets up the participants (equines) for the race.
     */
    private void setupParticipants() {
        if (!participantsReady) {
            String numParticipantsStr = JOptionPane.showInputDialog(mainFrame, "Enter the number of equines:");
            if (numParticipantsStr != null && !numParticipantsStr.isEmpty()) {
                try {
                    int numParticipants = Integer.parseInt(numParticipantsStr);
                    if (numParticipants >=2 && numParticipants <=4) {
                        for (int i = 0; i < numParticipants; i++) {
                            Equine newParticipant = getParticipantDetails();
                            while (newParticipant == null) {
                                newParticipant = getParticipantDetails();
                            }
                            if (newParticipant != null) {
                                participants.add(newParticipant);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Input from 2-4");
                        setupParticipants();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number.");
                    setupParticipants();
                }
            }
            participantsReady = true;
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Equines are already configured.", "Setup Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Customizes the competition track.
     */
    private void customizeCompetitionTrack() {
        String trackLengthStr = JOptionPane.showInputDialog(mainFrame, "Enter the length of the race track:");
        if (trackLengthStr != null && !trackLengthStr.isEmpty()) {
            try {
                int length = Integer.parseInt(trackLengthStr);
                if (length >= 300 && length<=510) {
                    competitionDistance = length;
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter from 300-510");
                    customizeCompetitionTrack();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number.");
                customizeCompetitionTrack();
            }
        }
    }

    /**
     * Gets the details of a participant (equine) from user input.
     *
     * @return the created Equine object with the entered details, or null if the input is invalid or cancelled
     */
    private Equine getParticipantDetails() {
        JTextField nameField = new JTextField(10);
        JComboBox<Equine.Species> speciesComboBox = new JComboBox<>(Equine.Species.values());
        JTextField velocityField = new JTextField(10);
        JRadioButton equineIcon1 = new JRadioButton("🐴");
        JRadioButton equineIcon2 = new JRadioButton("🦄");
        JRadioButton equineIcon3 = new JRadioButton("🐎");
        JRadioButton equineIcon4 = new JRadioButton("🏇");

        JCheckBox saddleCheckbox = new JCheckBox("Saddle");
        JCheckBox horseshoeCheckbox = new JCheckBox("Horseshoe");
        JCheckBox bridleCheckbox = new JCheckBox("Bridle");

        JPanel gearPanel = new JPanel(new GridLayout(1, 3));
        gearPanel.add(saddleCheckbox);
        gearPanel.add(horseshoeCheckbox);
        gearPanel.add(bridleCheckbox);

        ButtonGroup iconsGroup = new ButtonGroup();
        iconsGroup.add(equineIcon1);
        iconsGroup.add(equineIcon2);
        iconsGroup.add(equineIcon3);
        iconsGroup.add(equineIcon4);

        JPanel iconsPanel = new JPanel(new GridLayout(1, 4));
        iconsPanel.add(equineIcon1);
        iconsPanel.add(equineIcon2);
        iconsPanel.add(equineIcon3);
        iconsPanel.add(equineIcon4);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Name:"), constraints);

        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Species:"), constraints);

        constraints.gridx = 1;
        panel.add(speciesComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Base Velocity:"), constraints);

        constraints.gridx = 1;
        panel.add(velocityField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(new JLabel("Select Equine Icon:"), constraints);

        constraints.gridx = 1;
        panel.add(iconsPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(new JLabel("Gear:"), constraints);

        constraints.gridx = 1;
        panel.add(gearPanel, constraints);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Enter Equine Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            Equine.Species species = (Equine.Species) speciesComboBox.getSelectedItem();
            String velocity = velocityField.getText();

            String selectedIcon = getSelectedIcon(equineIcon1, equineIcon2, equineIcon3, equineIcon4);
            if(selectedIcon =="" || name==""){
                JOptionPane.showMessageDialog(mainFrame, "Must complete all inputs", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Set<String> gear = getSelectedGear(saddleCheckbox, horseshoeCheckbox, bridleCheckbox);

            double velocityValue;
            try {
                velocityValue = Double.parseDouble(velocity);
                if (velocityValue < 0 || velocityValue > 1) {
                    throw new NumberFormatException("Velocity should be between 0 and 1");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid velocity value. It should be a decimal number between 0 and 1.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return new Equine(name, species, Color.GRAY, velocityValue, gear, selectedIcon);
        }
        return null;
    }

    /**
     * Gets the selected icon from the radio buttons.
     *
     * @param equineIcons the radio buttons representing the equine icons
     * @return the selected icon, or an empty string if no icon is selected
     */
    private String getSelectedIcon(JRadioButton... equineIcons) {
        for (JRadioButton icon : equineIcons) {
            if (icon.isSelected()) {
                return icon.getText();
            }
        }
        return "";
    }

    /**
     * Gets the selected gear from the checkboxes.
     *
     * @param gearCheckboxes the checkboxes representing the gear options
     * @return a set of selected gear
     */
    private Set<String> getSelectedGear(JCheckBox... gearCheckboxes) {
        Set<String> gear = new HashSet<>();
        for (JCheckBox checkbox : gearCheckboxes) {
            if (checkbox.isSelected()) {
                gear.add(checkbox.getText().toLowerCase());
            }
        }
        return gear;
    }

    /**
     * Sets up the bettors (equestrians) for the race.
     */
    private void setupBettors() {
        if (!bettorsReady) {
            String numBettorsStr = JOptionPane.showInputDialog(mainFrame, "Enter the number of equestrians:");
            if (numBettorsStr != null && !numBettorsStr.isEmpty()) {
                try {
                    int numBettors = Integer.parseInt(numBettorsStr);
                    if (numBettors >=2 && numBettors <=5) {
                        for (int i = 0; i < numBettors; i++) {
                            bettors.add(new Equestrian(1000));
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Input must be from 2-5");
                        setupBettors();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number.");
                    setupBettors();
                }
            }
            bettorsReady = true;
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Equestrians are already configured.", "Setup Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates the competition panel for displaying the race track and controls.
     */
    private void createCompetitionPanel() {
        competitionPanel.setLayout(new BorderLayout());
        competitionPanel.setOpaque(false);
        competitionPanel.removeAll();

        JPanel raceTrackPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
                drawRaceTrack(g);
            }
        };

        competitionPanel.add(raceTrackPanel, BorderLayout.CENTER);

        competitionControlPanel = createCompetitionControlPanel();
        competitionPanel.add(competitionControlPanel, BorderLayout.SOUTH);

        competitionPanel.revalidate();
        competitionPanel.repaint();
    }

    /**
     * Draws the race track on the provided Graphics object.
     *
     * @param g the Graphics object to draw on
     */
    private void drawRaceTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);int panelWidth = competitionPanel.getWidth();
        int trackHeight = 50;
        int startY = (competitionPanel.getHeight() - (trackHeight * participants.size())) / 2;
        int spaceBetweenTracks = 10;
    
        Stroke dotted = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{9}, 0);
        g2d.setStroke(dotted);
    
        for (int i = 0; i < participants.size(); i++) {
            int laneY = startY + i * (trackHeight + spaceBetweenTracks);
            g2d.setColor(new Color(0, 0, 0, 0));
            g2d.fillRect(50, laneY, panelWidth - 100, trackHeight);
    
            g2d.setColor(Color.BLACK);
            g2d.drawRect(50, laneY, panelWidth - 100, trackHeight);
    
            Equine participant = participants.get(i);
            int participantX = (int) ((panelWidth - 100) * (participant.getDistanceTraveled() / (double) competitionDistance)) + 50;
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String participantText = participant.getIcon() + " - " + participant.getEquineName();
            if (participant.hasStumbled()) {
                participantText = "❌";
            }
            g2d.drawString(participantText, participantX, laneY + trackHeight / 2);
        }
    
        if (participants.size() > 0) {
            g2d.drawRect(50, startY, panelWidth - 100, participants.size() * (trackHeight + spaceBetweenTracks) - spaceBetweenTracks);
        }
        int preferredHeight = calculatePanelHeight();
        Dimension newSize = new Dimension(competitionPanel.getWidth(), preferredHeight);
        competitionPanel.setPreferredSize(newSize);
        competitionPanel.revalidate();
        g2d.dispose();
    }
    
    /**
     * Creates the competition control panel with buttons for various actions.
     *
     * @return the created competition control panel
     */
    private JPanel createCompetitionControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
    
        JButton viewStatsButton = createControlButton("View Statistics", this::displayStatistics);
        panel.add(viewStatsButton);
    
        JButton viewWagerHistoryButton = createControlButton("View Wager History", this::displayWagerHistory);
        panel.add(viewWagerHistoryButton);
    
        JButton viewBettorsInfoButton = createControlButton("View Equestrians Info", this::displayBettorsInfo);
        panel.add(viewBettorsInfoButton);
    
        JButton placeWagersButton = createControlButton("Place Wagers", this::conductWagers);
        panel.add(placeWagersButton);
    
        JButton raceAgainButton = createControlButton("Race Again", this::startCompetition);
        panel.add(raceAgainButton);
    
        return panel;
    }
    
    /**
     * Calculates the preferred height of the competition panel based on the number of participants.
     *
     * @return the calculated preferred height
     */
    private int calculatePanelHeight() {
        int trackHeight = 50;
        int spaceBetweenTracks = 10;
        int padding = 20;
        int totalHeight = (trackHeight + spaceBetweenTracks) * participants.size() + padding;
        return totalHeight;
    }
    
    /**
     * Starts the competition by creating the competition panel and simulating the race.
     */
    private void startCompetition() {
        createCompetitionPanel();
        cardLayoutManager.show(cardContainer, "Race Track");
    
        for (Equine participant : participants) {
            participant.resetForNewCompetition();
            participant.incrementCompetitions();
        }
        competitionThread = new Thread(() -> {
            boolean competitionFinished = false;
            while (!competitionFinished) {
                for (Equine participant : participants) {
                    if (!participant.hasStumbled() && participant.getDistanceTraveled() < competitionDistance) {
                        if (Math.random() < participant.getBaseVelocity()) {
                            participant.compete(competitionDistance);
                        }
                        if (participant.getDistanceTraveled() >= competitionDistance && !participant.hasStumbled()) {
                            competitionFinished = true;
                            JOptionPane.showMessageDialog(mainFrame, "The winner is " + participant.getEquineName(), "Race Finished", JOptionPane.INFORMATION_MESSAGE);
                            participant.incrementVictories();
                            settleWagers(participant);
                            break;
                        }
                    }
    
                    if (Math.random() < 0.1 * participant.getBaseVelocity() * participant.getBaseVelocity()) {
                        participant.stumble();
                    }
                }
    
                boolean allStumbled = participants.stream().allMatch(Equine::hasStumbled);
                if (allStumbled) {
                    competitionFinished = true;
                    JOptionPane.showMessageDialog(mainFrame, "All Equines have stumbled. The race has ended.", "Race Ended", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
    
                competitionPanel.repaint();
    
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
    
        competitionThread.start();
    }
    
    /**
     * Settles the wagers for each bettor based on the winning participant.
     *
     * @param winner the winning participant
     */
    private void settleWagers(Equine winner) {
        for (Equestrian bettor : bettors) {
            bettor.settleWager(winner);
        }
    }
    
    /**
     * Displays the statistics of each bettor in a dialog.
     */
    private void displayStatistics() {
        JDialog statsDialog = new JDialog(mainFrame, "Competition Statistics", true);
        statsDialog.setLayout(new BoxLayout(statsDialog.getContentPane(), BoxLayout.Y_AXIS));
        statsDialog.setSize(500, 300);
    
        JTabbedPane tabbedPane = new JTabbedPane();
        for (Equestrian bettor : bettors) {
            JPanel bettorStatsPanel = createBettorStatsPanel(bettor);
            tabbedPane.addTab("Equestrian " + (bettors.indexOf(bettor) + 1), bettorStatsPanel);
        }
        statsDialog.add(tabbedPane);
    
        statsDialog.setLocationRelativeTo(mainFrame);
        statsDialog.setVisible(true);
    }
    
    /**
     * Creates a panel displaying the statistics of a bettor.
     *
     * @param bettor the bettor whose statistics are to be displayed
     * @return the created bettor statistics panel
     */
    private JPanel createBettorStatsPanel(Equestrian bettor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        int totalCompetitions = bettor.getTotalCompetitions();
        int totalVictories = bettor.getTotalVictories();
        double winRate = totalCompetitions > 0 ? (double) totalVictories / totalCompetitions * 100 : 0;
        double totalWinnings = bettor.getTotalWinnings();
        double totalLosses = bettor.getTotalLosses();
        double netAmount = totalWinnings - totalLosses;
    
        String[][] data = {
                {"Number of Victories", String.valueOf(totalVictories)},
                {"Win Rate (%)", String.format("%.2f", winRate)},
                {"Total Winnings", String.format("$%.2f", totalWinnings)},
                {"Total Losses", String.format("$%.2f", totalLosses)},
                {"Net Amount", String.format("$%.2f", netAmount)}
        };
    
        String[] columnNames = {"Statistic", "Value"};
    
        JTable bettorStatsTable = new JTable(data, columnNames);
        bettorStatsTable.setEnabled(false);
        bettorStatsTable.setRowHeight(25);
        bettorStatsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        bettorStatsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    
        JScrollPane scrollPane = new JScrollPane(bettorStatsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }
    
    /**
     * Creates a control button with the specified text and action.
     *
     * @param text   the text of the button
     * @param action the action to be performed when the button is clicked
     * @return the created control button
     */
    private JButton createControlButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(150, 64, 32));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setOpaque(true);
        button.setMargin(new Insets(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(200, 40));
        button.addActionListener(e -> action.run());
        return button;
    }
    
    /**
     * Displays the wager history of each bettor in a dialog.
     */
    private void displayWagerHistory() {
        JDialog historyDialog = new JDialog(mainFrame, "Wager History", true);
        String[] columnNames = {"Equestrian", "Equine", "Wager Amount", "Won"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(25);
    
        for (Equestrian bettor : bettors) {
            for (Equestrian.Wager wager : bettor.getWagerHistory()) {
                model.addRow(new Object[]{
                        "Equestrian " + (bettors.indexOf(bettor) + 1),
                        wager.getEquine().getEquineName(),
                        String.format("$%.2f", wager.getWagerAmount()),
                        wager.isWon() ? "Yes" : "No"
                });
            }
        }
    
        JScrollPane scrollPane = new JScrollPane(historyTable);
        historyDialog.add(scrollPane);
        historyDialog.setSize(500, 400);
        historyDialog.setLocationRelativeTo(mainFrame);
        historyDialog.setVisible(true);
    }
    
    /**
     * Displays the information of each bettor in a dialog.
     */
    private void displayBettorsInfo() {
        JDialog infoDialog = new JDialog(mainFrame, "Equestrians Information", true);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        for (Equestrian bettor : bettors) {
            JPanel bettorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bettorPanel.setBorder(BorderFactory.createTitledBorder("Equestrian " + (bettors.indexOf(bettor) + 1)));
            JLabel balanceLabel = new JLabel("Balance: $" + String.format("%.2f", bettor.getBalance()));
            bettorPanel.add(balanceLabel);
            infoPanel.add(bettorPanel);
        }
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        infoDialog.add(scrollPane);
        infoDialog.setSize(300, 200);
        infoDialog.setLocationRelativeTo(mainFrame);
        infoDialog.setVisible(true);
    }
    
    /**
     * Conducts the wagers for each bettor by prompting them to select a participant and enter the wager amount.
     */
    private void conductWagers() {
        for (Equestrian bettor : bettors) {
            JComboBox<Equine> participantComboBox = new JComboBox<>(participants.toArray(new Equine[0]));
            JTextField wagerAmountField = new JTextField("100");
            JPanel wagerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            wagerPanel.add(new JLabel("Select Equine:"));
            wagerPanel.add(participantComboBox);
            wagerPanel.add(new JLabel("Wager Amount:"));
            wagerPanel.add(wagerAmountField);
    
            int result = JOptionPane.showConfirmDialog(mainFrame, wagerPanel, "Place Wager", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                double wagerAmount;
                try {
                    wagerAmount = Double.parseDouble(wagerAmountField.getText());
                    if (wagerAmount <= 0) {
                        throw new NumberFormatException("Wager amount should be a positive number");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid wager amount. It should be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
    
                Equine selectedParticipant = (Equine) participantComboBox.getSelectedItem();
                if (!bettor.placeWager(selectedParticipant, wagerAmount)) {
                    JOptionPane.showMessageDialog(mainFrame, "Insufficient balance to place wager.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}
    