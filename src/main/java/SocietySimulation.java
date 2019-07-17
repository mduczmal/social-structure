import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class SocietySimulation implements Simulation {

    private int rounds;
    private List<Node> citizens;
    private ToIntFunction<Node> calculateLivingCosts;
    private ToIntFunction<Node> calculateProductivity;
    private IntUnaryOperator calculatePersonalIncomeTax;
    private ToDoubleBiFunction<Node, Integer> calculateEffort;
    private Consumer<List<Node>> citizenConnector;
    private Supplier<Node> citizenSupplier;

    private SocietySimulation(Builder builder) {
        rounds = builder.rounds;
        citizens = builder.citizens;
        citizenSupplier = builder.supplier;
        citizenConnector = builder.connector;
        calculateLivingCosts = builder.livingCosts;
        calculateEffort = builder.effort;
        calculateProductivity = builder.productivity;
        calculatePersonalIncomeTax = builder.personalIncomeTax;
    }

    public static class Builder {
        private final int rounds;
        private final int citizensNumber;
        private List<Node> citizens;

        private Supplier<Node> supplier = Node::new;
        private Consumer<List<Node>> connector = citizens -> {};
        private ToIntFunction<Node> productivity = citizen -> 0;
        private ToDoubleBiFunction<Node, Integer> effort = (citizen, income) -> 1;
        private IntUnaryOperator personalIncomeTax = income -> 0;
        private ToIntFunction<Node> livingCosts = citizen -> 0;


        public Builder(int rounds, int citizensNumber) {
            this.rounds = rounds;
            this.citizensNumber = citizensNumber;
        }
        public Builder supplier(Supplier<Node> supplier) {
            this.supplier = supplier;
            return this;
        }
        public Builder connector(Consumer<List<Node>> connector) {
            this.connector = connector;
            return this;
        }
        public Builder productivity(ToIntFunction<Node> productivity) {
            this.productivity = productivity;
            return this;
        }
        public Builder effort(ToDoubleBiFunction<Node, Integer> effort) {
            this.effort = effort;
            return this;
        }
        public Builder personalIncomeTax(IntUnaryOperator personalIncomeTax) {
            this.personalIncomeTax = personalIncomeTax;
            return this;
        }
        public Builder livingCosts(ToIntFunction<Node> livingCosts) {
            this.livingCosts = livingCosts;
            return this;
        }

        public SocietySimulation build() {
            citizens = new ArrayList<>();
            for (int i = 0; i < citizensNumber; i++) {
                Node node = supplier.get();
                citizens.add(node);
            }
            connector.accept(citizens);
            return new SocietySimulation(this);
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
            int taxedUnits = units - calculatePersonalIncomeTax.applyAsInt(units);
            int livingCosts = calculateLivingCosts.applyAsInt(citizen);
            citizen.produceUnits(taxedUnits-livingCosts);
        }
    }

    public int getRounds() {
        return rounds;
    }

    public List<Node> getCitizens() {
        return new ArrayList<>(citizens);
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
