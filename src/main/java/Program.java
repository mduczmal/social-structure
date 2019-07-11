import java.util.List;
import java.util.Random;
import java.util.function.*;

public class Program {

    private static Simulation simulationBuilder() {
        final int initialUnits = 10_000;
        final Random random = new Random(324);

        UtilityFunction citizenUtilityFunction = (Node node, SocialEnvironment environment) -> {
            int friendSignificance = 0;
            int selfSignificance = 100;
            double sum = 0;
            for (Node friend : node.getFriends()) {
                sum += Math.log10(friend.getUnits());
            }
            return (int) Math.round(selfSignificance * Math.log10(node.getUnits()) + friendSignificance * sum);
        };
        final Function<Node, Integer> calculateLivingCosts = node -> (int)(0.1*initialUnits);

        final Function<Node, Integer> calculateProductivity = node -> {
            int multiplier = (int)(0.025*initialUnits);
            return (int) Math.round(multiplier*Math.log10(node.getUnits()));
        };
        final IntUnaryOperator calculatePersonalIncomeTax = income -> {
            double flatTaxRate = 0.2;
            if (income < 0) return 0;
            return (int) Math.round(flatTaxRate*income);
        };
        final ToDoubleBiFunction<Node, Integer> calculateEffort = (citizen, maxProfit) -> {
            //multiplier are so choosen as to provide function f, where f(0.00001) = 0, f(0.1) = 1
            //and is logarithmic between those values, e.g. f(0.001) = 0.5
            double wealthFraction = ((double) maxProfit)/citizen.getUnits();
            double intercept = 1.25;
            double multiplier = 0.25;
            double maximumEffortFraction = 0.1;
            double noEffortFraction = 0.00001;
            if (wealthFraction > maximumEffortFraction) {
                return 1.0;
            }
            else if (wealthFraction < noEffortFraction) {
                return 0.0;
            }
            else {
                return multiplier*Math.log10(wealthFraction)+intercept;
            }
        };
        Consumer<List<Node>> citizenConnector = citizens -> {};
        final Supplier<Node> citizenSupplier = () -> {
            double std = 0.05*initialUnits;
            int noise = (int) Math.round(std*random.nextGaussian());
            return new Node(initialUnits+noise, citizenUtilityFunction);
        };

        return new OptimalStructure(10, 50, citizenSupplier, citizenConnector,
                calculateProductivity, calculateEffort, calculatePersonalIncomeTax, calculateLivingCosts);
    }

    public static void main(String[] args) {
        Simulation complexStructure = simulationBuilder();
        complexStructure.run();
    }
}
