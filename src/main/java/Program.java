import java.util.List;
import java.util.Random;
import java.util.function.*;

public class Program {

    private static Simulation buildSimulation() {

        final int initialUnits = 10_000;

        final Random random = new Random(324);

        final UtilityFunction citizenUtilityFunction = SimulationFunctionFactory.getLogUtilityFunction(10, 100, 0);

        final Supplier<Node> citizenSupplier = SimulationFunctionFactory.getNoisyNodeSupplier(initialUnits, citizenUtilityFunction, random);

        final Consumer<List<Node>> citizenConnector = citizens -> {};

        final ToIntFunction<Node> calculateProductivity = SimulationFunctionFactory.getLogProductivityFunction(10, (int) (0.025*initialUnits));

        final ToDoubleBiFunction<Node, Integer> calculateEffort = SimulationFunctionFactory.getLogEffortFunction(10, 0.1, 0.00001);

        final IntUnaryOperator calculatePersonalIncomeTax = SimulationFunctionFactory.getFlatPersonalIncomeTaxFunction(0.2);

        final ToIntFunction<Node> calculateLivingCosts = citizen -> (int)(0.1*initialUnits);

        return new SocietySimulation(10, new Society.Builder(50).supplier(citizenSupplier)
                .connector(citizenConnector).productivity(calculateProductivity).effort(calculateEffort)
                .personalIncomeTax(calculatePersonalIncomeTax).livingCosts(calculateLivingCosts)
                .build());
    }

    public static void main(String[] args) {
        Simulation simulation = buildSimulation();
        simulation.run();
    }
}
