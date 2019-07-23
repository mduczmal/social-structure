import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    @Test
    void connectSingleFriendTest() {
        Node node = new Node();
        Node friend = new Node();
        List<Node> connected = new LinkedList<>();
        connected.add(friend);

        node.connect(friend);
        List<Node> friends = node.getFriends();

        assertEquals(connected, friends);
    }

    @Test
    void isConnectedSingleFriendTest() {
        Node node = new Node();
        Node friend = new Node();

        node.connect(friend);

        assertTrue(node.isConnected(friend));
        assertTrue(friend.isConnected(node));
    }

    @Test
    void cliqueIsFullyConnectedTest() {
        List<Node> nodes = new LinkedList<>();
        for(int i = 0; i<5; i++) {
            nodes.add(new Node());
        }
        List<Node> zerosFriends = new LinkedList<>(nodes);
        zerosFriends.remove(0);

        for(int i=1; i<5; i++) {
            Node node = nodes.get(i);
            for (int j=0; j<i; j++) {
                Node friend = nodes.get(j);
                node.connect(friend);
            }
        }

        for (Node node : nodes) {
            for (Node friend : nodes) {
                if (!node.equals(friend)) {
                    assertTrue(node.isConnected(friend));
                }
            }
        }
        assertEquals(zerosFriends, nodes.get(0).getFriends());
    }

    @Test
    void zeroUnitsAfterConstructionTest() {
        Node node = new Node();
        assertEquals(0, node.getUnits());
    }

    @Test
    void produceUnitsTest() {
        Node node = new Node();
        node.produceUnits(5);
        assertEquals(5, node.getUnits());
    }

    @Test
    void constructWithUnitsTest() {
        Node node = new Node(341261645);
        assertEquals(341261645, node.getUnits());

    }

    @Test
    void giveTest() {
        Node rich = new Node(2300);
        Node poor = new Node(20);

        rich.give(poor, 150);

        assertEquals(2150, rich.getUnits());
        assertEquals(170, poor.getUnits());
    }

    @Test
    void takeTest() {
        Node victim = new Node(500);
        Node robber = new Node();

        robber.take(victim, 400);

        assertEquals(400, robber.getUnits());
        assertEquals(100, victim.getUnits());
    }

    @Test
    void giveTooMuchTest() {
        Node poor = new Node(15);
        Node poorer = new Node(2);

        assertThrows(NotEnoughUnitsException.class, () -> poor.give(poorer, 100));
        assertEquals(2, poorer.getUnits());
        assertEquals(15, poor.getUnits());
    }

    @Test
    void takeTooMuchTest() {
        Node victim = new Node(3000);
        Node robber = new Node();

        assertThrows(NotEnoughUnitsException.class, () -> robber.take(victim, 5000));
        assertEquals(3000, victim.getUnits());
        assertEquals(0, robber.getUnits());
    }

    @Test
    void trivialUtilityFunctionTest() {
        Node node = new Node(50);

        assertEquals(50, node.computeUtility());

    }
    @Test
    void simpleUtilityFunctionTest() {
        UtilityFunction utilityFunction = node -> 3*node.getUnits();
        Node node = new Node(27, utilityFunction);

        assertEquals(81, node.computeUtility());
    }

}
