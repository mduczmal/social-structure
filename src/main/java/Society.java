import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class Society {
    private List<Node> citizens;
    private final Supplier<Node> supplier;
    private final Consumer<List<Node>> connector;
    private final ToIntFunction<Node> productivity;
    private final ToDoubleBiFunction<Node, Integer> effort;
    private final IntUnaryOperator personalIncomeTax;
    private final ToIntFunction<Node> livingCosts;

    private Society(Builder builder) {
        citizens = builder.citizens;
        supplier = builder.supplier;
        connector = builder.connector;
        productivity = builder.productivity;
        effort = builder.effort;
        personalIncomeTax = builder.personalIncomeTax;
        livingCosts = builder.livingCosts;
    }

    public List<Node> getCitizens() {
        return new ArrayList<>(citizens);
    }

    int getUnitsToProduce(Node citizen) {
        int maxUnits = productivity.applyAsInt(citizen);
        int tax = personalIncomeTax.applyAsInt(maxUnits);
        int taxedUnits = maxUnits-tax;
        return (int) Math.round(effort.applyAsDouble(citizen, taxedUnits)*maxUnits);
    }

    public static class Builder {
        private final int citizensNumber;
        private List<Node> citizens;

        private Supplier<Node> supplier = Node::new;
        private Consumer<List<Node>> connector = citizens -> {};
        private ToIntFunction<Node> productivity = citizen -> 0;
        private ToDoubleBiFunction<Node, Integer> effort = (citizen, income) -> 1;
        private IntUnaryOperator personalIncomeTax = income -> 0;
        private ToIntFunction<Node> livingCosts = citizen -> 0;


        public Builder(int citizensNumber) {
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

        public Society build() {
            citizens = new ArrayList<>();
            for (int i = 0; i < citizensNumber; i++) {
                Node node = supplier.get();
                citizens.add(node);
            }
            connector.accept(citizens);
            return new Society(this);
        }
    }

    public int calculateLivingCosts(Node citizen) {
        return livingCosts.applyAsInt(citizen);
    }
    public int calculatePersonalIncomeTax(int units) {
        return personalIncomeTax.applyAsInt(units);
    }
}
