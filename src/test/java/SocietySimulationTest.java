import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocietySimulationTest {
    @Test
    void createSocietySimulationTest() {
        SocietySimulation s = new SocietySimulation.Builder(100, 10).build();
        assertEquals(100, s.getRounds());
        assertEquals(10, s.getCitizens().size());
    }
    @Test
    void runSocietySimulationTest() {
        SocietySimulation s = new SocietySimulation.Builder(100, 10).build();
        s.run();
        assertEquals(100, s.getRounds());
        assertEquals(10, s.getCitizens().size());
    }
    @Test
    void stableUnitsTest() {
        SocietySimulation s = new SocietySimulation.Builder(13, 34)
                .supplier(() -> new Node(15)).build();
        s.run();
        assertEquals(15, s.getCitizens().get(18).getUnits());
    }
}