import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocietySimulationTest {
    @Test
    void createSocietySimulationTest() {
        Society society = new Society.Builder(10).build();
        SocietySimulation s = new SocietySimulation(100, society);
        assertEquals(100, s.getRounds());
        assertEquals(10, s.society.getCitizens().size());
    }
    @Test
    void runSocietySimulationTest() {
        Society society = new Society.Builder(10).build();
        SocietySimulation s = new SocietySimulation(100, society);
        s.run();
        assertEquals(100, s.getRounds());
        assertEquals(10, s.society.getCitizens().size());
    }
    @Test
    void stableUnitsTest() {
        Society society = new Society.Builder(34).supplier(() -> new Node(15)).build();
        SocietySimulation s = new SocietySimulation(13, society);
        s.run();
        assertEquals(15, s.society.getCitizens().get(18).getUnits());
    }
    @Test
    void stableProductivityTest() {
        Society society = new Society.Builder(3).supplier(() -> new Node(20)).productivity(citizen -> 1).build();
        SocietySimulation s = new SocietySimulation(7, society);
        s.run();
        assertEquals(27, s.society.getCitizens().get(2).getUnits());
    }
    @Test
    void halfIncomeEffortTest() {
        Society society = new Society.Builder(16).supplier(() -> new Node(6000))
                .productivity(citizen -> 200).effort((citizen, income) -> 0.5).build();
        SocietySimulation s = new SocietySimulation(5, society);
        s.run();
        assertFalse(s.society.getCitizens().isEmpty());
        for (Node citizen : s.society.getCitizens()) {
            assertEquals(6500, citizen.getUnits());
        }
    }
    @Test
    void halfUnitsLivingCostsTest() {
        Society society = new Society.Builder(2)
                .supplier(() -> new Node(16000)).livingCosts(citizen -> (int) (0.5*citizen.getUnits())).build();
        SocietySimulation s = new SocietySimulation(5, society);
        s.run();
        assertEquals(500, s.society.getCitizens().get(1).getUnits());
    }
    @Test
    void runForTest() {
        Society society = new Society.Builder(5).productivity(citizen -> 1).build();
        SocietySimulation s = new SocietySimulation(30, society);
        s.run(12);
        assertEquals(12, s.society.getCitizens().get(3).getUnits());
    }

    @Test
    void multipleRunTest() {
        Society society = new Society.Builder(11)
                .supplier(() -> new Node(1))
                .productivity(Node::getUnits).build();
        SocietySimulation s = new SocietySimulation(7, society);

        s.run(2);
        int afterTwo = s.society.getCitizens().get(10).getUnits();
        int stepTwo = s.getStep();
        s.run(3);
        int afterFive = s.society.getCitizens().get(3).getUnits();
        int stepFive = s.getStep();
        s.run();
        int afterSeven = s.society.getCitizens().get(7).getUnits();
        int stepSeven = s.getStep();

        assertEquals(2, stepTwo);
        assertEquals(4, afterTwo);
        assertEquals(5, stepFive);
        assertEquals(32, afterFive);
        assertEquals(7, stepSeven);
        assertEquals(128, afterSeven);
    }

    /*
    TODO: Add simulation saving
    @Test
    void restartSimulationTest() {
        DatabaseProxy db = mock(DatabaseProxy.class);
        when(db.restore()).thenReturn(new SocietySimulation.Builder(0, 0).build());
        SocietySimulation saved = new SocietySimulation.Builder(23, 35).
                productivity(citizen -> 1).build();

        saved.run(10);
        saved.save();
        SocietySimulation restored = db.restore();
        int stepAfterRestore = restored.getStep();
        restored.run();

        assertEquals(10, stepAfterRestore);
        assertEquals(23, restored.getStep());
        assertEquals(10, saved.getStep());
        assertEquals(10, saved.getCitizens().get(27).getUnits());
        assertEquals(23, restored.getCitizens().get(3).getUnits());
    }*/
}