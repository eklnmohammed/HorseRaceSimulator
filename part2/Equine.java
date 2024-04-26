import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Equine {
    public enum Species { ARABIAN, THOROUGHBRED, CLYDESDALE }

    private String equineName;
    private Species species;
    private Color equineColor;
    private double baseVelocity;
    private double distanceTraveled;
    private boolean stumbled;
    private String icon;
    private Set<String> gear;
    private int victoriesCount = 0;
    private int competitionsCount = 0;

    public Equine(String equineName, Species species, Color equineColor, double baseVelocity, Set<String> gear, String icon) {
        this.equineName = equineName;
        this.species = species;
        this.equineColor = equineColor;
        this.baseVelocity = baseVelocity;
        this.distanceTraveled = 0.0;
        this.stumbled = false;
        this.icon = icon;
        this.gear = new HashSet<>(gear);
    }

    public void incrementVictories() {
        this.victoriesCount++;
    }

    public void incrementCompetitions() {
        this.competitionsCount++;
    }

    @Override
    public String toString() {
        return equineName + " Winning Probability: " + this.getWinningProbability();
    }

    public double getWinningProbability() {
        if (competitionsCount == 0 || victoriesCount == 0) {
            return 1 / (0.01);
        } else {
            return 1 / ((double) victoriesCount / competitionsCount);
        }
    }

    public void addGear(String item) {
        gear.add(item);
    }

    public double getBaseVelocity() {
        return this.baseVelocity;
    }

    public double calculateVelocityWithGear() {
        double velocity = baseVelocity;
        for (String item : gear) {
            switch (item.toLowerCase()) {
                case "saddle":
                    velocity *= 1.3;
                    break;
                case "horseshoes":
                    velocity *= 1.2;
                    break;
                case "bridle":
                    velocity *= 1.1;
                    break;
                default:
                    break;
            }
        }
        return velocity;
    }

    public void compete(double competitionDistance) {
        if (!stumbled) {
            distanceTraveled += competitionDistance / 15 * calculateVelocityWithGear();
            if (distanceTraveled >= competitionDistance) {
                distanceTraveled = competitionDistance;
            }
        }
    }

    public void stumble() {
        stumbled = true;
    }

    public boolean hasStumbled() {
        return stumbled;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEquineName() {
        return equineName;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setEquineName(String equineName) {
        this.equineName = equineName;
    }

    public void setEquineColor(Color equineColor) {
        this.equineColor = equineColor;
    }

    public Color getEquineColor() {
        return equineColor;
    }

    public void resetForNewCompetition() {
        this.distanceTraveled = 0.0;
        this.stumbled = false;
    }

    public Set<String> getGear() {
        return new HashSet<>(gear);
    }

    public void removeGear(String item) {
        gear.remove(item);
    }
}