import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the track length: ");
        int trackLength = scanner.nextInt();
        scanner.nextLine(); 

        Race race = new Race(trackLength);

        for (int i = 1; i <= 3; i++) {
            System.out.println("Enter details for Horse " + i + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Symbol: ");
            char symbol = scanner.next().charAt(0);
            System.out.print("Confidence (0.0 - 1.0): ");
            double confidence = scanner.nextDouble();
            scanner.nextLine(); 

            Horse horse = new Horse(symbol,name, confidence);
            race.enlistHorse(horse, i);
        }

        System.out.println("Starting the race...");
        race.commenceRace();
    }
}