import java.util.ArrayList;
import java.util.List;

public class Equestrian {
    private double balance;
    private List<Wager> wagerHistory;
    private int victoriesCount = 0;
    public int totalCompetitions = 0;
    public double totalWinnings = 0;
    public double totalLosses = 0;

    public Equestrian(double initialBalance) {
        this.balance = initialBalance;
        this.wagerHistory = new ArrayList<>();
    }

    public boolean placeWager(Equine equine, double wagerAmount) {
        this.totalCompetitions++;
        if (balance < wagerAmount) {
            return false;
        }
        balance -= wagerAmount;
        wagerHistory.add(new Wager(equine, wagerAmount));
        return true;
    }

    public void incrementVictories() {
        this.victoriesCount++;
    }

    public void incrementCompetitions() {
        this.totalCompetitions++;
    }

    public int getTotalCompetitions() {
        return totalCompetitions;
    }

    public int getTotalVictories() {
        return this.victoriesCount;
    }

    public double getTotalLosses() {
        return totalLosses;
    }

    public double getTotalWinnings() {
        return totalWinnings;
    }

    public void settleWager(Equine winner) {
        if (!wagerHistory.isEmpty()) {
            Wager latestWager = wagerHistory.get(wagerHistory.size() - 1);
            if (latestWager.getEquine().equals(winner)) {
                latestWager.markAsWon();
                double winnings = latestWager.calculatePayout();
                this.victoriesCount++;
                balance += winnings + latestWager.getWagerAmount();
                totalWinnings += winnings;
            } else {
                double losses = latestWager.getWagerAmount();
                latestWager.markAsLost();
                totalLosses += losses;
            }
        }
    }

    public double getBalance() {
        return balance;
    }

    public List<Wager> getWagerHistory() {
        return wagerHistory;
    }

    public class Wager {
        private final Equine equine;
        private final double wagerAmount;
        private boolean won;
        private double winningProbability;

        public Wager(Equine equine, double wagerAmount) {
            this.equine = equine;
            this.wagerAmount = wagerAmount;
            this.won = false;
            this.winningProbability = equine.getWinningProbability();
        }

        public void markAsWon() {
            won = true;
        }

        public void markAsLost() {
            won = false;
        }

        public double calculatePayout() {
            return won ? wagerAmount * winningProbability : 0;
        }

        public Equine getEquine() {
            return equine;
        }

        public double getWagerAmount() {
            return wagerAmount;
        }

        public boolean isWon() {
            return won;
        }

        public double getWinningProbability() {
            return winningProbability;
        }

        public void setWinningProbability(double winningProbability) {
            this.winningProbability = winningProbability;
        }
    }
}