import org.junit.jupiter.api.Test;

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
}
