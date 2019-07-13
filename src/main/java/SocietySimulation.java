import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class SocietySimulation implements Simulation {
    private int rounds;
    private List<Node> citizens;
    private int citizensNumber;
    private ToIntFunction<Node> calculateLivingCosts;
    private ToIntFunction<Node> calculateProductivity;
    private IntUnaryOperator calculatePersonalIncomeTax;
    private ToDoubleBiFunction<Node, Integer> calculateEffort;
    private Consumer<List<Node>> citizenConnector;
    private Supplier<Node> citizenSupplier;

    SocietySimulation(int rounds, int citizensNumber, Supplier<Node> citizenSupplier,
                      Consumer<List<Node>> citizenConnector, ToIntFunction<Node> calculateProductivity,
                      ToDoubleBiFunction<Node, Integer> calculateEffort, IntUnaryOperator calculatePersonalIncomeTax,
                      ToIntFunction<Node> calculateLivingCosts) {
        this.rounds = rounds;
        this.citizensNumber = citizensNumber;
        this.citizenSupplier = citizenSupplier;
        this.citizenConnector = citizenConnector;
        this.calculateLivingCosts = calculateLivingCosts;
        this.calculateEffort = calculateEffort;
        this.calculateProductivity = calculateProductivity;
        this.calculatePersonalIncomeTax = calculatePersonalIncomeTax;
        citizens = new ArrayList<>();
        for (int i = 0; i < this.citizensNumber; i++) {
            Node node = this.citizenSupplier.get();
            citizens.add(node);
            this.citizenConnector.accept(citizens);
        }
    }
    private int getUnitsToProduce(Node citizen) {
        int maxUnits = calculateProductivity.applyAsInt(citizen);
        int tax = calculatePersonalIncomeTax.applyAsInt(maxUnits);
        int taxedUnits = maxUnits-tax;
        return (int) Math.round(calculateEffort.applyAsDouble(citizen, taxedUnits)*maxUnits);
    }

    private void step() {
        for (Node citizen : citizens) {
            int units = getUnitsToProduce(citizen);
            int taxedUnits = units- calculatePersonalIncomeTax.applyAsInt(units);
            int livingCosts = calculateLivingCosts.applyAsInt(citizen);
            citizen.produceUnits(taxedUnits-livingCosts);
        }
    }

    @Override
    public void run() {
        SocialEnvironment env = new DefaultSocialEnvironment();
        for (int i=0; i<rounds; i++) {
            step();
            long utilitySum = 0;
            long unitsSum = 0;
            for (Node citizen : citizens) {
                utilitySum += citizen.computeUtility(env);
                unitsSum += citizen.getUnits();
            }
            System.out.format("Utility: " + "%,d" + " at step: " + "%d%n", utilitySum, i);
            System.out.format("Units: " + "%,d" + " at step: " + "%d%n", unitsSum, i);
        }
    }
}
