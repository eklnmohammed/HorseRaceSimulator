public class HorseTestDriver {
    public static void main(String[] args) {
        new HorseTestDriver().runTests();
    }
    private void runTests() {
        Horse steed = new Horse('S', "Shadowfax", 0.9);
        validate("Shadowfax", steed.getName());
        validate('S', steed.getSymbol());
        validate(0.9, steed.getConfidence());
        validate(false, steed.hasFallen());
        validate(0, steed.getDistanceTravelled());

        steed.moveForward();
        validate(1, steed.getDistanceTravelled());

        steed.fall();
        validate(true, steed.hasFallen());
        validate(0.8, steed.getConfidence());

        steed.fall();
        validate(0.7, steed.getConfidence());

        steed.setConfidence(0.6);
        validate(0.6, steed.getConfidence());

        steed.setConfidence(-0.2);
        validate(0.0, steed.getConfidence());

        steed.setConfidence(1.2);
        validate(1.0, steed.getConfidence());

        steed.goBackToStart();
        validate(0, steed.getDistanceTravelled());

        steed.setSymbol('R');
        validate('R', steed.getSymbol());

        System.out.println("Success!");
    }

    private <T> void validate(T expected, T actual) {
        if (!expected.equals(actual))
            throw new AssertionError("Expected: " + expected + ", but got: " + actual);
    }
}