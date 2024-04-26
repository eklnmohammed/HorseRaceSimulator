import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Race {
    private int trackLength;
    private List<Horse> participants;

    public Race(int distance) {
        trackLength = distance;
        participants = new ArrayList<>();
    }

    public void enlistHorse(Horse theHorse, int laneNumber) {
        if (laneNumber >= 1 && laneNumber <= participants.size() + 1) {
            participants.add(laneNumber - 1, theHorse);
        } else {
            System.out.println("Invalid lane number. Horse not enlisted in the contest.");
        }
    }

    public void commenceRace() {
        boolean raceEnded = false;
    
        for (Horse horse : participants) {
            horse.goBackToStart();
        }
    
        while (!raceEnded) {
            for (Horse horse : participants) {
                moveHorseForward(horse);
            }
    
            displayRaceProgress();
    
            if (allHorsesDown()) {
                System.out.println("All horses have fallen. The contest concludes.");
                raceEnded = true;
            } else if (anyHorseVictorious()) {
                for (Horse horse : participants) {
                    if (horseWins(horse)) {
                        System.out.println("The winning horse is " + horse.getName());
                    }
                }
                raceEnded = true;
            }
    
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {
                // Handle the exception
            }
        }
    }

    private boolean allHorsesDown() {
        for (Horse horse : participants) {
            if (!horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    private boolean anyHorseVictorious() {
        for (Horse horse : participants) {
            if (horseWins(horse)) {
                return true;
            }
        }
        return false;
    }
    private void moveHorseForward(Horse horse) {
        if (!horse.hasFallen()) {
            if (Math.random() < horse.getConfidence()) {
                horse.moveForward();
                adjustHorsePerformance(horse, false);
            }

            if (Math.random() < (0.1 * horse.getConfidence() * horse.getConfidence())) {
                horse.fall();
            }
        }
    }

    private boolean horseWins(Horse horse) {
        return horse.getDistanceTravelled() == trackLength;
    }
    private void displayRaceProgress() {
        StringBuilder raceDisplay = new StringBuilder();

        raceDisplay.append("\033[2J");

        raceDisplay.append("\033[H");
    
        raceDisplay.append(String.format("%0" + (trackLength + 3) + "d", 0).replace("0", "="));
        raceDisplay.append(System.lineSeparator());

        for (Horse horse : participants) {
            raceDisplay.append("|");
            int spacesBefore = horse.getDistanceTravelled();
            int spacesAfter = trackLength - horse.getDistanceTravelled();
    
            for (int i = 0; i < spacesBefore; i++) {
                raceDisplay.append(" ");
            }
    
            if (horse.hasFallen()) {
                raceDisplay.append("❌");
            } else {
                raceDisplay.append(horse.getSymbol());
            }
    
            for (int i = 0; i < spacesAfter; i++) {
                raceDisplay.append(" ");
            }
    
            raceDisplay.append("| ");
    
            if (horse.hasFallen()) {
                raceDisplay.append(horse.getName()).append(" (Horse has stumbled)");
            } else {
                raceDisplay.append(horse.getName()).append(" (Confidence: ")
                        .append(String.format("%.2f", horse.getConfidence())).append(")");
            }
    
            raceDisplay.append(System.lineSeparator());
        }
    
        raceDisplay.append(String.format("%0" + (trackLength + 3) + "d", 0).replace("0", "="));
        raceDisplay.append(System.lineSeparator());

        System.out.print(raceDisplay.toString());
    }

    private void adjustHorsePerformance(Horse horse, boolean wins) {
        double currentPerformance = horse.getConfidence();

        if (wins) {
            horse.setConfidence(Math.min(currentPerformance + 0.1, 1.0));
        } else {
            horse.setConfidence(Math.max(currentPerformance - 0.01, 0.0));
        }
    }
}
