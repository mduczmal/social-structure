import java.util.List;
import java.util.Random;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntFunction;

class SimulationFunctionFactory {

    static ToIntFunction<Node> getLogProductivityFunction(int base, double multiplier){
        return citizen -> (int) Math.round(multiplier * Math.log(citizen.getUnits())/Math.log(base));
    }

    static IntUnaryOperator getFlatPersonalIncomeTaxFunction(double taxRate) {
        return income -> {
            if (income < 0) return 0;
            return (int) Math.round(taxRate * income);
        };
    }

    static IntUnaryOperator getTieredPersonalIncomeTaxFunction(List<Integer> incomeLevels, List<Double> taxRates) {
        //sorted (ascending) list of incomeLevels and corresponding taxRates
        //taxRates should contain one more entry than incomeLevels - for incomes above all incomeLevels
        if (incomeLevels.size()+1 != taxRates.size()) {
            String errorMessage = "Please provide list of tax rates one element longer" +
                    "than list of income levels (for incomes greater than last level).\n";
            throw new IllegalArgumentException(errorMessage);
        }
        incomeLevels.add(Integer.MAX_VALUE);
        return income -> {
            if (income < 0) return 0;
            for (int i=0; i<incomeLevels.size(); i++) {
                int incomeLevel = incomeLevels.get(i);
                if (income <= incomeLevel) return (int) (income*taxRates.get(i));
            }
            return (int) (income * taxRates.get(taxRates.size()-1));
        };
    }

    static Supplier<Node> getNoisyNodeSupplier(int initialUnits, UtilityFunction citizenUtilityFunction, Random random) {
        return () -> {
            double std = 0.05*initialUnits;
            int noise = (int) Math.round(std*random.nextGaussian());
            return new Node(initialUnits+noise, citizenUtilityFunction);
        };
    }

    static UtilityFunction getLogUtilityFunction(int base, int selfMultiplier, int friendMultiplier) {
        return (Node node, SocialEnvironment environment) -> {
            double sum = 0;
            for (Node friend : node.getFriends()) {
                sum += Math.log(friend.getUnits())/Math.log(base);
            }
            return (int) Math.round(selfMultiplier * Math.log(node.getUnits())/Math.log(base) + friendMultiplier * sum);
        };
    }


    static ToDoubleBiFunction<Node, Integer> getLogEffortFunction(int base, double maxEffort, double noEffort) {
        return (citizen, maxProfit) -> {
            double wealthFraction = ((double) maxProfit)/citizen.getUnits();
            double multiplier = 1/(Math.log(maxEffort)/Math.log(base)-Math.log(noEffort)/Math.log(base));
            double intercept = multiplier*(-Math.log(noEffort)/Math.log(base));
            if (wealthFraction > maxEffort) {
                return 1.0;
            }
            else if (wealthFraction < noEffort) {
                return 0.0;
            }
            else {
                return multiplier * Math.log(wealthFraction)/Math.log(base) + intercept;
            }
        };

    }
}
