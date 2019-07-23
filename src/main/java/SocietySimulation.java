public class SocietySimulation implements Simulation {

    //number of completed simulation steps
    private int step;
    private final int rounds;
    final Society society;

    public SocietySimulation(int rounds, Society society) {
        this.rounds = rounds;
        this.society = society;
        step = 0;
    }

    private void step() {
        for (Node citizen : society.getCitizens()) {
            int units = society.getUnitsToProduce(citizen);
            int taxedUnits = units - society.calculatePersonalIncomeTax(units);
            int livingCosts = society.calculateLivingCosts(citizen);
            citizen.produceUnits(taxedUnits-livingCosts);
        }
        step += 1;
    }

    public int getRounds() {
        return rounds;
    }

    public int getStep() {
        return step;
    }

    @Override
    public void run() {
        run(rounds-step);
    }

    void run(int steps) {
        for (int i=0; i<steps; i++) {
            step();
            long utilitySum = 0;
            long unitsSum = 0;
            for (Node citizen : society.getCitizens()) {
                utilitySum += citizen.computeUtility();
                unitsSum += citizen.getUnits();
            }
            System.out.format("Utility: " + "%,d" + " at step: " + "%d%n", utilitySum, step);
            System.out.format("Units: " + "%,d" + " at step: " + "%d%n", unitsSum, step);
        }
    }
}
